package pl.sebcel.rc.gui.graphics;

import java.awt.Point;
import java.awt.Rectangle;

public class Scaler implements IScaler {

    private double scale;
    private double alpha;
    private int offsetX;
    private int offsetY;
    private double viewportX;
    private double viewportY;

    public Scaler(double viewportX, double viewportY, double scale, double alpha) {
	this.viewportX = viewportX;
	this.viewportY = viewportY;
	this.scale = scale;
	this.alpha = alpha;
    }

    public void setClipBounds(Rectangle clipBounds) {
	offsetX = clipBounds.width / 2;
	offsetY = clipBounds.height / 2;
    }

    @Override
    public Point rescale(double x, double y) {
	double dX = viewportX;
	double dY = viewportY;

	double r = Math.sqrt((x - dX) * (x - dX) + (y - dY) * (y - dY)) * scale;
	double b = Math.atan2(y - dY, x - dX);

	double xx = r * Math.sin(alpha + b);
	double yy = r * Math.cos(alpha + b);

	int retX = offsetX + (int) (xx);
	int retY = offsetY + (int) (yy);
	return new Point(retX, retY);
    }

    @Override
    public int rescale(double distance) {
	return (int) Math.round(distance * scale);
    }

    @Override
    public double getScale() {
	return scale;
    }
}
