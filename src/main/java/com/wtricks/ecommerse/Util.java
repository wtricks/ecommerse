package com.wtricks.ecommerse;

import com.wtricks.ecommerse.router.Router;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
    public static Alert.AlertType AlertType = Alert.AlertType.WARNING;
    public static void showAlert(String text, EventHandler<DialogEvent> e) {
        Alert al = new Alert(AlertType);
        al.setTitle("Alert Box");
        al.setContentText(text);
        al.setOnHiding(e);
        al.showAndWait();
    }

    public static String encryptString(String string) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(string.getBytes(StandardCharsets.UTF_8));

        // Convert to Hex String
        BigInteger num = new BigInteger(1, hash);
        StringBuilder sb = new StringBuilder(num.toString());
        while(sb.length() < 32) {
            sb.insert(0, '0');
        }

        return sb.toString();
    }

    public static VBox createCard(String title, String price, String image, String btn, EventHandler<ActionEvent> event) {
        VBox vb = new VBox();
        vb.setStyle("-fx-border-color: #000; -fx-border-radius: 10px; -fx-background-color: #fff");
        vb.setPrefSize(200.0, 125.0);

        // image view
        ImageView iv = new ImageView(new Image(Main.class.getResourceAsStream("assets/"+ image +".png")));
        iv.setStyle("-fx-border-radius: 10px");
        iv.setFitHeight(100.0);
        iv.setFitWidth(120.0);
        iv.setPickOnBounds(true);
        iv.setTranslateX(6.0);
        iv.setTranslateY(8.0);

        // label
        Label lb = new Label(title);
        lb.setFont(new Font("System Bold", 12.0));
        lb.setWrapText(true);

        // Button
        Button bt = new Button(btn + price);
        bt.setStyle("-fx-border-radius: 10px");
        bt.setDefaultButton(true);
        bt.setMnemonicParsing(false);
        bt.setPrefWidth(120.0);
        bt.setFocusTraversable(false);

        // add listener
        bt.setOnAction(event);

        // adding children
        vb.getChildren().addAll(iv, lb, bt);
        vb.setMargin(lb, new Insets(10.0, 0, 0, 6.0));
        vb.setMargin(bt, new Insets(0, 0, 0, 6.0));
        return vb;
    }
}
