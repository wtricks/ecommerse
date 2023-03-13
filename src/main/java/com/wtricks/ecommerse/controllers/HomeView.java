package com.wtricks.ecommerse.controllers;

import com.wtricks.ecommerse.DBConnection;
import com.wtricks.ecommerse.Util;
import com.wtricks.ecommerse.router.Router;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class HomeView implements Initializable {
    @FXML
    public GridPane grid;

    @FXML
    public TextField text;

    @FXML
    public Label label;

    @FXML
    protected void goToAccountPage() {
        if (Router.isLoggedIn != -1) {
            Router.activateRoute("profile");
            return;
        }

        Util.showAlert("You need to login first!", e -> {
            Router.activateRoute("login");
        });
    }

    @FXML
    protected void goToCartPage() {
        if (Router.isLoggedIn != -1) {
            Router.activateRoute("cart");
            return;
        }

        Util.showAlert("For viewing cart, You need to login first!", e -> {});
    }

    @FXML
    public void SearchData() {
        try {
            AddData(text.getText());
        } catch (SQLException err) {
            throw new RuntimeException(err);
        }
    }

    private void AddToCart(int id, String name) {
        Connection conn = DBConnection.getConnection();

        try {
            Statement stmt = conn.createStatement();
            ResultSet set = stmt.executeQuery("SELECT title FROM cart c LEFT JOIN products p ON c.product_id = p.id WHERE c.user_id="+ Router.isLoggedIn +" AND p.id="+ id);
            if (set.next()) {
                Util.showAlert("'"+ set.getString(1) +"' is already added in your card!", e -> {});
                return;
            }

            int insertId = stmt.executeUpdate("INSERT INTO cart(user_id, product_id, time) VALUES("+ Router.isLoggedIn +","+ id +", current_timestamp)");
            if (insertId > 0) {
                Util.AlertType = Alert.AlertType.INFORMATION;
                Util.showAlert("'"+ name +"' is added in your card!", e -> {
                    Util.AlertType = Alert.AlertType.WARNING;
                });
            }
            else Util.showAlert("Something went wrong!", e -> {});
            set.close();
        } catch (Exception e) {
            // TODO: Handle error here
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void AddData(String query) throws SQLException {
        ResultSet set = getResultSet(query, 12, 0);
        grid.getChildren().clear(); // remove all children first

        if (set == null) {
            label.setText("No product is available");
            return;
        }

        // Add products
        int row = 0;
        int count = 0;
        label.setText("");
        while(set.next()) {
            VBox card = Util.createCard(
                    set.getString(2),
                    set.getString(3),
                    set.getString(4),
                    "Buy in $",
                    new EventHandler<ActionEvent>() {
                        private int P_ID = set.getInt(1);
                        private String name = set.getString(2);

                        @Override
                        public void handle(ActionEvent actionEvent) {
                            if (Router.isLoggedIn != -1) {
                                AddToCart(P_ID, name);
                                return;
                            }

                            Util.showAlert("For adding product to cart, You need to login first!", e -> {});
                        }
                    });

            grid.addRow(row, card);
            if (++count == 2) {
                count = 0;
                row++;
            }
        }
        set.close();
    }

    public ResultSet getResultSet(String q, int limit, int offset) {
        // get database connection
        Connection conn = DBConnection.getConnection();
        String query = "SELECT id, title, price, image FROM products ORDER BY id DESC LIMIT "+ offset +"," + limit;
        if (!q.equals("")) {
            query = "SELECT id, title, price, image FROM products WHERE title LIKE '%"+ q +"%' ORDER BY id DESC LIMIT "+ offset +"," + limit;
        }

        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(query);
        } catch (Exception e) {
            System.out.println("Error occurred while fetching data.");
        }

        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // load data
        try {
            AddData("");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}