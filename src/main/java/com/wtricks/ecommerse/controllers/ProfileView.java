package com.wtricks.ecommerse.controllers;

import com.wtricks.ecommerse.DBConnection;
import com.wtricks.ecommerse.router.Router;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ProfileView implements Initializable {
    @FXML
    public Label name;

    @FXML
    public Label email;

    @FXML
    public Label pass;

    @FXML
    public Label cart;


    @FXML
    public void goToCartPage() {
        Router.activateRoute("cart");
    }

    @FXML
    public void goToHomePage() {
        Router.activateRoute("default");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection conn = DBConnection.getConnection();

        try {
            Statement stmt = conn.createStatement();
            ResultSet set = stmt.executeQuery("SELECT name, email, pass, count(c.id) FROM users u LEFT JOIN cart c ON u.id = c.user_id WHERE u.id="+ Router.isLoggedIn);
            if (set.next()) {
                name.setText(set.getString(1));
                email.setText(set.getString(2));
                pass.setText(set.getString(3));
                cart.setText(set.getInt(4)+"");
            }
        } catch(Exception e) {
            // TODO: HANDLE ERROR HERE
        }
    }
}
