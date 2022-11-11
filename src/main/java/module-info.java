module multiscenetest {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.brk.multiscenetest to javafx.fxml;
    exports com.brk.multiscenetest;
}