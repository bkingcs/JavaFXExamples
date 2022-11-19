module javafxexamples {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.brk.javafxexamples;
    opens com.brk.multiscenetest to javafx.fxml;
    exports com.brk.javafxexamples.animation;
    opens com.brk.javafxexamples.animation to javafx.fxml;
    exports com.brk.javafxexamples.lights;
    opens com.brk.javafxexamples.lights to javafx.fxml;

}
