package com.example.vophungquang.network.pojo;
/**
 * Created by vophungquang
 */
public class Route {
    private String copyrights;
    private OverviewPolyline overview_polyline;
    private String summary;

    public Route() {
    }

    public String getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    public OverviewPolyline getOverviewPolyline() {
        return overview_polyline;
    }

    public void setOverviewPolyline(OverviewPolyline overview_polyline) {
        this.overview_polyline = overview_polyline;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}

