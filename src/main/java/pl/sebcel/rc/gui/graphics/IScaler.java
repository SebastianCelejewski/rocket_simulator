package pl.sebcel.rc.gui.graphics;

import java.awt.Point;

public interface IScaler {

    public Point p(double x, double y);

    public int d(double distance);

    public double getScale();
}