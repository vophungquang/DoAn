package com.example.vophungquang.network.pojo;
/**
 * Created by vophungquang
 */
public class Result {
    private Geometry geometry;

    public Result() {
    }

    public Result(Geometry geometry) {
        this.geometry = geometry;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
