package com.wtricks.ecommerse.controllers;

import com.wtricks.ecommerse.DBConnection;
import com.wtricks.ecommerse.Util;
import com.wtricks.ecommerse.router.Router;
import javafx.scene.Scene;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginView {
    @FXML
    public TextField email;

    @FXML
    public PasswordField pass;

    @FXML
    public Label label;

    public void goForgotPage() {
        Router.activateRoute("forgot");
    }

    public void goRegisterPage() {
        Router.activateRoute("register");
    }

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
    public void doLogin() {
        String em = email.getText();
        String ps = pass.getText();

        if (em.equals("")) {
            showAlert("Email is required!", true);
        } else if (ps.equals("")) {
            showAlert("Password is required!", true);
        } else {
            showAlert("Please wait...", false);

            // get database connection
            Connection conn = DBConnection.getConnection();
            try {
                Statement stmt = conn.createStatement();
                ResultSet set = stmt.executeQuery("SELECT id, pass FROM users WHERE email='"+ em +"'");
                if (!set.next()) {
                    showAlert("Email was not found!", true);
                    return;
                }

                // Encrypting password
                String pass = Util.encryptString(ps);

                // Check if password matched or not
                if (!pass.equals(set.getString(2))) {
                    showAlert("Password doesn't match!", true);
                    return;
                } else {
                    int USER_ID = set.getInt(1);
                    Util.AlertType = Alert.AlertType.INFORMATION;
                    Util.showAlert("You are successfully logged in!", e -> {
                        Util.AlertType = Alert.AlertType.WARNING;
                        Router.isLoggedIn = USER_ID;
                        Router.activateRoute("default");
                    });
                }

                stmt.close();
            } catch(Exception e) {
                // TODO: HANDLE ERROR HERE
            }
        }
    }
}
