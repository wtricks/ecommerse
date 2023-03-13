package com.wtricks.ecommerse.router;

import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class Route {
    public int id;

    public String name;

    public Scene scene;

    public String view;

    // public List<Scene> children;

    // public Scene parent;

    public Route(int id, String name, String view, Scene parent) {
        this.name = name;
        this.id = id;
        this.view = view;
        // this.parent = parent;
//        this.children = new ArrayList<>();
    }
}
