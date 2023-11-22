module modelcheckctl.view {
    requires javafx.controls;
    requires javafx.fxml;


    opens modelCheckCTL.view to javafx.fxml;
    exports modelCheckCTL.view;
}