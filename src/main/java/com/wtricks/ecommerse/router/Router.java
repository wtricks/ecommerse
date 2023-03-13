package com.wtricks.ecommerse.router;

import com.wtricks.ecommerse.Config;
import com.wtricks.ecommerse.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Router {
    private static Map<String, Route> routes = new HashMap<>();
    private static Stage stage;

    private static int ID = 0;

    public static int isLoggedIn =-1;

    private static int HEIGHT;
    private static int WIDTH;

    public static void init(Stage s, String title) {
        init(s, title, Config.SCREEN_HEIGHT, Config.SCREEN_WIDTH);
    }

    public static void init(Stage s) {
        init(s, "", Config.SCREEN_HEIGHT, Config.SCREEN_WIDTH);
    }

    public static void init(Stage s, String title, int height, int width) {
        stage = s; // set stage object
        HEIGHT = height;
        WIDTH = width;
        stage.setTitle(title);
        loadRoutes();
    }

    public static void show() {
        stage.show();
    }

    private static void loadRoutes() {
        for(int i = RouteMap.route.length - 1; i > 0; i -= 2)
            addRoute(RouteMap.route[i-1], RouteMap.route[i]);
    }

    public static void setFavicon(String loc) {
        stage.getIcons().add(new Image(Main.class.getResourceAsStream(loc)));
    }

    public static void addRoute(String name, String view) {
        if (routes.containsKey(name)) {
            System.out.println("Route <"+ name +" /> is already registered.");
            return;
        }

        routes.put(name, new Route(ID++, name, view, null));
    }

    public static void removeRoute(String name) {
        if (!routes.containsKey(name)) {
            System.out.println("Route <"+ name +" /> is not registered.");
            return;
        }

        routes.remove(name);
    }

    public static void activateRoute(String name) {
        Route route = routes.getOrDefault(name, null);
        if (route == null) {
            System.out.println("Route <" + name + " /> can not be activated.");
            return;
        }

        Scene view = null;
        // check if it already loaded or not.
        if (route.scene == null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/" +route.view + "-view.fxml"));
//                route.scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
                view = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
            } catch(IOException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
                System.out.println("Error occurred while fetching <\""+ route.view +"\" /> markup.");
            }
        }

        // set scene
//        stage.setScene(route.scene);
        stage.setScene(view);
    }
}
