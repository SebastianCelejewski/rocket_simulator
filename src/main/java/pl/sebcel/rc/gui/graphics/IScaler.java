package pl.sebcel.rc.gui.graphics;

import java.awt.Point;

public interface IScaler {

    public Point rescale(double x, double y);

    public int rescale(double distance);

    public double getScale();
}