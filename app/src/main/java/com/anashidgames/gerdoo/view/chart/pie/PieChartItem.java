package com.anashidgames.gerdoo.view.chart.pie;

/**
 * Created by psycho on 4/30/16.
 */
public class PieChartItem {
    private String title;
    private double value;
    private int color;

    public PieChartItem(String title, double value, int color) {
        this.title = title;
        this.value = Math.abs(value);
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public double getValue() {
        return value;
    }

    public int getColor() {
        return color;
    }
}
