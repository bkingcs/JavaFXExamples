package com.brk.javafxexamples.animation;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * A simple lights extended from a {@link Circle} that adds some helpful attributes
 * and methods for managing a moving ball that can bounce off of walls
 */
public class MovingBall extends Circle {
    public static final double MAX_DURATION_IN_SEC = 1.0;
    public static final double FLOAT_EPSILON = 1.0E-10;

    /** Housekeeping... TODO - delete these */
    private static int idCounter = 0;
    private int id;

    /** Speed in x-coordinate direction per second (in pixels) */
    private DoubleProperty dxPerSec;

    /** Speed in y-coordinate direction per second (in pixels) */
    private DoubleProperty dyPerSec;

    /**
     * Creates a new instance of Circle with a specified position and radius.
     *
     * @param centerX the horizontal position of the center of the circle in pixels
     * @param centerY the vertical position of the center of the circle in pixels
     * @param radius  the radius of the circle in pixels
     */
    public MovingBall(double centerX, double centerY, double radius) {
        super(centerX, centerY, radius);
        this.dxPerSec = new SimpleDoubleProperty(0);
        this.dyPerSec = new SimpleDoubleProperty(0);
        this.id = idCounter++;
    }

    /**
     * @return the change in x per second
     */
    public double getDxPerSec() {
        return dxPerSec.get();
    }

    /**
     * @return the change in x per second as a {@link DoubleProperty}
     */
    public DoubleProperty dxPerSecProperty() {
        return dxPerSec;
    }

    /**
     * Set a new speed in the x-direction
     *
     * @param dxPerSec speed in the x-direction per second (in pixels)
     */
    public void setDxPerSec(double dxPerSec) {
        this.dxPerSec.set(dxPerSec);
    }

    /**
     * @return the change in y per second
     */
    public double getDyPerSec() {
        return dyPerSec.get();
    }

    /**
     * @return the change in y per second as a {@link DoubleProperty}
     */
    public DoubleProperty dyPerSecProperty() {
        return dyPerSec;
    }

    /**
     * Set a new speed in the y-direction
     *
     * @param dyPerSec speed in the x-direction per second (in pixels)
     */
    public void setDyPerSec(double dyPerSec) {
        this.dyPerSec.set(dyPerSec);
    }

    /**
     * Compute the amount of time that the ball will take to hit a wall. It
     * considers both directions. Whichever wall is approaching the fastest
     * will be returned
     *
     * @param bounds {@link Bounds} object dictating the bounds where the ball
     *                             can move
     * @return the time the ball can move before hitting the wall
     */
    public double getTimeToHitAnyWallInSec(Bounds bounds) {
        return Math.min(
                getTimeToHitXWallInSec(bounds),
                getTimeToHitYWallInSec(bounds)
        );
    }

    /**
     * Compute the amount of time that the ball will take to hit a wall in the
     * x-coordinate direction.
     *
     * @param bounds {@link Bounds} object dictating the bounds where the ball
     *                             can move
     * @return the time before the ball hits a wall in the x-coordinate direction
     */
    public double getTimeToHitXWallInSec(Bounds bounds) {
        double timeX;
        if (this.getDxPerSec() > 0)
            timeX = ((bounds.getMaxX() - this.getRadius()) - this.getCenterX()) /
                    this.getDxPerSec();
        else if (this.getDxPerSec() < 0)
            timeX = (this.getCenterX() - (bounds.getMinX() + this.getRadius())) /
                    -this.getDxPerSec();
        else
            timeX = MAX_DURATION_IN_SEC;

        if (Math.abs(timeX) <= FLOAT_EPSILON)
            timeX = 0.0;
        return timeX;
    }

    /**
     * Compute the amount of time that the ball will take to hit a wall in the
     * y-coordinate direction.
     *
     * @param bounds {@link Bounds} object dictating the bounds where the ball
     *                             can move
     * @return the time before the ball hits a wall in the y-coordinate direction
     */
    public double getTimeToHitYWallInSec(Bounds bounds) {
        double timeY;
        if (this.getDyPerSec() > 0)
            timeY = ((bounds.getMaxY() - this.getRadius()) - this.getCenterY()) /
                    this.getDyPerSec();
        else if (this.getDyPerSec() < 0)
            timeY = (this.getCenterY() - (bounds.getMinY() + this.getRadius())) /
                    -this.getDyPerSec();
        else
            timeY = MAX_DURATION_IN_SEC;

        if (Math.abs(timeY) <= FLOAT_EPSILON)
            timeY = 0.0;
        return timeY;
    }

    /**
     * Get the x-coordinate destination at the current speed after {@code timeInSec}
     * seconds
     *
     * @param timeInSec time in second to compute distance traveled
     * @return the x-coordinate after {@code timeInSec} seconds
     */
    public double getDestX(double timeInSec) {
        return this.getCenterX() + this.getDxPerSec() * timeInSec;
    }

    /**
     * Get the y-coordinate destination at the current speed after {@code timeInSec}
     * seconds
     *
     * @param timeInSec time in second to compute distance traveled
     * @return the y-coordinate after {@code timeInSec} seconds
     */
    public double getDestY(double timeInSec) {
        return this.getCenterY() + this.getDyPerSec() * timeInSec;
    }

    @Override
    public String toString() {
        return "MovingBall{" +
                "id=" + id +
                String.format(", center=[%.0f,%.0f]", this.getCenterX(), this.getCenterY()) +
                String.format(", dxy=[%.0f,%.0f]", this.getDxPerSec(), this.getDyPerSec()) +
                "}";
    }

//    private KeyFrame getKeyFrame(Bounds bounds) {
//        double time = 0.0;
//        while (time <= 0.0) {
//            time = this.getTimeToHitAnyWallInSec(bounds);
//            if (time <= 0.0) {
//                System.out.println(this + ": HIT WALL!");
//                if (this.getTimeToHitXWallInSec(bounds) <= 0.0)
//                    this.setDxPerSec(this.getDxPerSec()*-1.0);
//                else
//                    this.setDyPerSec(this.getDyPerSec()*-1.0);
//            }
//        }
//        if (time > MAX_DURATION_IN_SEC)
//            time = MAX_DURATION_IN_SEC;
//        System.out.println(this + "Time to hit wall: " + time);
//        return new KeyFrame(
//                Duration.millis(time*1000.0),
//                new KeyValue(
//                        this.centerXProperty(), this.getDestX(time)
//                ),
//                new KeyValue(
//                        this.centerYProperty(), this.getDestY(time)
//                )
//        );
//    }

}
