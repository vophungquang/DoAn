package com.example.vophungquang.network.pojo;
/**
 * Created by vophungquang
 */
public class Geometry {
    public Geometry() {
    }

    private Location location;

    public Geometry(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

