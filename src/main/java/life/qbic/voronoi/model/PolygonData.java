package life.qbic.voronoi.model;

import kn.uni.voronoitreemap.j2d.PolygonSimple;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class PolygonData {

    private String name;
    private int level;
    private PolygonSimple polygon;
    private Color color;
    private List<Double> ratios;

    public PolygonData (String name, int level, PolygonSimple polygon) {
        this.name = name;
        this.level = level;
        this.polygon = polygon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public PolygonSimple getPolygon() {
        return polygon;
    }

    public Color getColor() {
        if (color != null)
            return color;

        color = new Color(155, 155, 155);

        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<Double> getRatios() {
        if (ratios != null)
            return ratios;
        return null;
    }

    public void setRatios(List<Double> ratios) {
        this.ratios = new ArrayList<>(ratios);
    }

}
