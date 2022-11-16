/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Fall 2022
 *
 * Name: YOUR NAME
 * Date: 11/12/22
 * Time: 9:25 PM
 *
 * Project: MultiSceneTest
 * Package: com.brk.multiscenetest
 * Class: AnimationDemoController
 * Description:
 *
 * ****************************************
 */

package com.brk.multiscenetest.animation;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class AnimationDemoController {
    public static final double MAX_UPDATE_TIME_SEC = 1.0;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane canvas;

    @FXML
    private Label lblStatusBar;

    private MovingBall ball;

    private LinkedList<MovingBall> balls;

    @FXML
    void initialize() {
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'animation-demo.fxml'.";
        assert lblStatusBar != null : "fx:id=\"lblStatusBar\" was not injected: check your FXML file 'animation-demo.fxml'.";
        ball = null;
    }

    public Pane getCanvas() {
        return canvas;
    }

    /*
     * Circle has properties:
     * - center[X,Y]  - position of the center of the circle
     * - layout[X,Y] - coordinates of the translation that is added to this Node's transform for the
     * purpose of
     * layout.
     * - translate[X,Y] = Defines the coordinates of the translation that is added to this Node's transform.
     */
    @FXML
    void onCanvasMouseClicked(MouseEvent event) {
        double startX = event.getX();
        double startY = event.getY();
        lblStatusBar.setText(String.format("Clicked - [%.0f, %.0f]",startX,startY));

        ball = new MovingBall(startX, startY, 10);
        ball.setDxPerSec(200);
        ball.setDyPerSec(1);
        ball.setFill(Color.AZURE);

        canvas.getChildren().add(ball);

        Thread t = new Thread(new MovingBallThread(ball,this));
        t.setDaemon(true);
        t.start();

//        Timeline timeline;
//        timeline = new Timeline(getKeyFrame(ball));
//        timeline.setOnFinished(
//                actionEvent -> {
//                    timeline.getKeyFrames().clear();
//                    timeline.getKeyFrames().add(getKeyFrame(ball));
//                    timeline.play();
//                });
//        timeline.setCycleCount(1);
//        timeline.play();
    }

}

class MovingBallThread implements Runnable {
    private MovingBall ball;
    private Timeline timeline;
    private AnimationDemoController controller;

    public MovingBallThread(MovingBall ball, AnimationDemoController controller) {
        this.ball = ball;
        this.controller = controller;
        this.timeline = null;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (this.timeline == null) {
                    this.timeline = new Timeline(
                            getKeyFrame()
                    );
                    this.timeline.setCycleCount(1);
                    this.timeline.play();
                    //                Platform.runLater(() -> this.timeline.play());
                }
                else {
                    switch (timeline.getStatus()) {
                        case RUNNING -> Thread.sleep(25);
                        case STOPPED -> timeline = null;
                    }
                }

            }
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private KeyFrame getKeyFrame() {
        Bounds bound = controller.getCanvas().getBoundsInLocal();
        double time = 0.0;
        while (time <= 0.0) {
            time = ball.getTimeToHitAnyWallInSec(bound);
            if (time <= 0.0) {
//                System.out.println(ball + ": HIT WALL!");
                if (ball.getTimeToHitXWallInSec(bound) <= 0.0)
                    ball.setDxPerSec(ball.getDxPerSec()*-1.0);
                else
                    ball.setDyPerSec(ball.getDyPerSec()*-1.0);
            }
        }
        if (time > AnimationDemoController.MAX_UPDATE_TIME_SEC)
            time = AnimationDemoController.MAX_UPDATE_TIME_SEC;

//        System.out.println(ball + " --> Time to hit wall: " + time);
        return new KeyFrame(
                Duration.millis(time*1000.0),
                new KeyValue(
                        ball.centerXProperty(), ball.getDestX(time)
                ),
                new KeyValue(
                        ball.centerYProperty(), ball.getDestY(time)
                )
        );
    }

}
