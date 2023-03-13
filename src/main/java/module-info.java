module com.wtricks.ecommerse {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.wtricks.ecommerse to javafx.fxml;
    exports com.wtricks.ecommerse;
    exports com.wtricks.ecommerse.router;
    opens com.wtricks.ecommerse.router to javafx.fxml;
    exports com.wtricks.ecommerse.controllers;
    opens com.wtricks.ecommerse.controllers to javafx.fxml;
}