package com.wtricks.ecommerse.controllers;

import com.wtricks.ecommerse.DBConnection;
import com.wtricks.ecommerse.Util;
import com.wtricks.ecommerse.router.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ForgotView {
    @FXML
    public Label label;

    @FXML
    public TextField email;

    @FXML
    private void showAlert(String st, boolean type) {
        if (type) {
            label.setTextFill(Color.web("red"));
        } else {
            label.setTextFill(Color.web("black"));
        }
        label.setText(st);
    }
    @FXML
    public void onForgotButtonClick() {
        String em = email.getText();

        if (em.equals("")) {
            showAlert("Email is required!", true);
            return;
        }

        showAlert("Please wait...", false);

        // get database connection
        Connection conn = DBConnection.getConnection();
        try {
            Statement stmt = conn.createStatement();
            ResultSet set = stmt.executeQuery("SELECT id, email FROM users WHERE email='"+ em +"'");
            if (!set.next()) {
                showAlert("Email was not found!", true);
                return;
            }

            // User id
            int USER_ID = set.getInt(1);

            // User email
            String USER_EMAIL = set.getString(2);

            /**
             * TODO: GENERATE OTP OR RESET LINK HERE
             * TODO: SEND IT ON USER EMAIL ADDRESS
             */


            Util.AlertType = Alert.AlertType.INFORMATION;
            Util.showAlert("Password reset link was sent to your registered email address!", e -> {
                Util.AlertType = Alert.AlertType.WARNING;
            });
            stmt.close();
        } catch(Exception e) {
            // TODO: HANDLE ERROR HERE
        }
    }
    public void goLoginPage() {
        Router.activateRoute("login");
    }
}
