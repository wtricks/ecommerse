package com.wtricks.ecommerse;

import com.wtricks.ecommerse.router.Router;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        /* initialize the router */
        Router.init(stage, "Blank Kart", Config.SCREEN_HEIGHT, Config.SCREEN_WIDTH);

        /* Adding favicon */
        Router.setFavicon("assets/logo.png");

        /* Activating current route */
        Router.activateRoute("default");

        /* Show Application */
        Router.show();
    }

    public static void main(String[] args) {
        launch();
    }
}