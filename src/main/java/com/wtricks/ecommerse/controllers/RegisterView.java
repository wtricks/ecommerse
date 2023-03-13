package com.wtricks.ecommerse.controllers;

import com.wtricks.ecommerse.DBConnection;
import com.wtricks.ecommerse.Util;
import com.wtricks.ecommerse.router.Router;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class RegisterView {
    @FXML
    public TextField name;

    @FXML
    public TextField email;

    @FXML
    public PasswordField pass;

    @FXML
    public PasswordField cpass;

    @FXML
    public Label label;

    public void goLoginPage() {
        Router.activateRoute("login");
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
    public void doRegister() {
        String nm = name.getText();
        String em = email.getText();
        String ps = pass.getText();
        String cp = cpass.getText();

        if (nm.equals("")) {
            showAlert("Name is required!", true);
        } else if (em.equals("")) {
            showAlert("Email is required!", true);
        } else if (ps.equals("")) {
            showAlert("Password is required!", true);
        } else if (!ps.equals(cp)) {
            showAlert("Confirm password doesn't match!", true);
        } else {
            showAlert("Please wait...", false);

            // get database connection
            Connection conn = DBConnection.getConnection();
            try {
                Statement stmt = conn.createStatement();
                ResultSet set = stmt.executeQuery("SELECT id FROM users WHERE email='"+ em +"'");
                if (set.next()) {
                    showAlert("Email is already taken!", true);
                    return;
                }

                // Encrypting password
                String pass = Util.encryptString(ps);

                int insertId = stmt.executeUpdate("INSERT INTO users(email, name , pass, last_login, registered) VALUES('"+ em +"', '"+nm+"', '"+pass+"', current_timestamp, current_timestamp)");
                if (insertId > 0) {
                    Util.AlertType = Alert.AlertType.INFORMATION;
                    Util.showAlert("Your account is created is successfully", e -> {
                        Util.AlertType = Alert.AlertType.WARNING;
                        Router.activateRoute("login");
                    });
                }
                else {
                    showAlert("Something went wrong, Please try again later", true);
                }

                // close statement.
                stmt.close();
            } catch(Exception e) {
                // TODO: HANDLE ERROR HERE
            }
        }
    }
}
