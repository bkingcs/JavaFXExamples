module multiscenetest {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.brk.multiscenetest;
    opens com.brk.multiscenetest to javafx.fxml;
    exports com.brk.multiscenetest.animation;
    opens com.brk.multiscenetest.animation to javafx.fxml;
    exports com.brk.multiscenetest.lights;
    opens com.brk.multiscenetest.lights to javafx.fxml;

}