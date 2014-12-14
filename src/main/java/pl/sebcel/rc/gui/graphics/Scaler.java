package pl.sebcel.rc.gui.graphics;

import java.awt.Rectangle;

public class Scaler implements IScaler {

    private double scaleX;
    private double scaleY;
    private int offsetX;
    private int offsetY;
    private double generalX;
    private double generalY;

    public Scaler(double generalX, double generalY, double scaleX, double scaleY) {
	this.generalX = generalX;
	this.generalY = generalY;
	this.scaleX = scaleX;
	this.scaleY = -scaleY;
    }

    public void setClipBounds(Rectangle clipBounds) {
	offsetX = clipBounds.width / 2;
	offsetY = clipBounds.height / 2;
    }

    @Override
    public int x(double x) {
	double dX = generalX;
	int ret = offsetX + (int) (Math.round((x - dX) * scaleX));
	return ret;
    }

    @Override
    public int y(double y) {
	double dY = generalY;
	int ret = offsetY + (int) (Math.round((y - dY) * scaleY));
	return ret;
    }

    @Override
    public int width(double width) {
	return (int) Math.round(width * scaleX);
    }

    @Override
    public int height(double height) {
	return (int) Math.round(height * scaleY);
    }
}
