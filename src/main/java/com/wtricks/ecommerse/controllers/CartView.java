package com.wtricks.ecommerse.controllers;

import com.wtricks.ecommerse.DBConnection;
import com.wtricks.ecommerse.Util;
import com.wtricks.ecommerse.router.Router;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;


public class CartView implements Initializable {
    @FXML
    public Button button;

    @FXML
    public Label label;

    @FXML
    public GridPane grid;

    private int total = 0;

    /**
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // LOAD INITIAL DATA
        try {
            addData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void goToHomePage() {
        Router.activateRoute("default");
    }

    @FXML
    public void goToProfilePage() {
        Router.activateRoute("profile");
    }

    @FXML
    public void checkout() {
        if (total == 0) {
            Router.activateRoute("default");
            return;
        }

        // get connection
        Connection conn = DBConnection.getConnection();

        try {
            Statement stmt = conn.createStatement();
            int deleteID = stmt.executeUpdate("DELETE FROM cart WHERE user_id="+ Router.isLoggedIn);

            if (deleteID > 0) {
                Util.AlertType = Alert.AlertType.INFORMATION;
                Util.showAlert("Order was placed of $" + total, e -> {
                    Util.AlertType = Alert.AlertType.WARNING;
                });
                grid.getChildren().clear();
                label.setText("Your cart is empty");
                total = 0;
                button.setText("Add Items");
            } else {
                Util.showAlert("Something went wrong, please try again later!", e -> {});
            }
        } catch(Exception e) {
            // TODO: HANDLE ERROR HERE
        }
    }

    private void removeItemFromCart(int id) {
        // get connection
        Connection conn = DBConnection.getConnection();

        try {
            Statement stmt = conn.createStatement();
            int deleteID = stmt.executeUpdate("DELETE FROM cart WHERE id="+id);
            if (deleteID > 0) {
                Util.AlertType = Alert.AlertType.INFORMATION;
                Util.showAlert("Product was removed from your cart!", e -> {
                    Util.AlertType = Alert.AlertType.WARNING;
                    Router.activateRoute("cart");
                });
            } else {
                Util.showAlert("Something went wrong, please try again later!", e -> {});
            }
        } catch(Exception e) {
            // TODO: Handle errors here
        }
    }

    private void addData() throws SQLException {
        ResultSet set = getCartData(Router.isLoggedIn);
        grid.getChildren().clear();

        if (set == null) {
            label.setText("Your cart is empty");
            return;
        }

        // Add products
        int row = 0;
        int count = 0;
        label.setText("");
        while(set.next()) {
            int id = set.getInt(1);
            int price = Integer.valueOf(set.getString(2));
            total += price;
            VBox card = Util.createCard(
                    set.getString(3),
                    set.getString(2),
                    set.getString(4),
                    "Remove of $",
                    new EventHandler<ActionEvent>() {
                        private int P_ID = set.getInt(1);

                        @Override
                        public void handle(ActionEvent actionEvent) {
                            removeItemFromCart(P_ID);
                        }
                    });

            grid.addRow(row, card);
            if (++count == 2) {
                count = 0;
                row++;
            }
        }

        button.setText("Checkout ($"+total+")");
        set.close();
    }

    private ResultSet getCartData(int user_id) {
        // get database connection.
        Connection conn = DBConnection.getConnection();

        try {
            Statement stmt = conn.createStatement();

            ResultSet result = stmt.executeQuery("SELECT c.id, price, title, image FROM cart c LEFT JOIN products p ON p.id=c.product_id AND c.user_id="+user_id);
            return result;
        } catch(Exception e) {
            // TODO: HANDLE ERROR HERE
        }

        return null;
    }
}
