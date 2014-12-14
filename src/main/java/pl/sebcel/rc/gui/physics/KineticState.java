package pl.sebcel.rc.gui.physics;

public class KineticState {

    private double x;
    private double y;
    private double alpha;

    private double vx;
    private double vy;
    private double omega;

    private double ax;
    private double ay;
    private double epsilon;

    private double staticPressure;
    private double dynamicPressureX;
    private double dynamicPressureY;

    public KineticState(double x, double y, double alpha, double vx, double vy, double omega, double ax, double ay, double epsilon) {
	this.x = x;
	this.y = y;
	this.alpha = alpha;

	this.vx = vx;
	this.vy = vy;
	this.omega = omega;

	this.ax = ax;
	this.ay = ay;
	this.epsilon = epsilon;
    }

    public double getX() {
	return x;
    }

    public double getY() {
	return y;
    }

    public double getAlpha() {
	return alpha;
    }

    public double getVx() {
	return vx;
    }

    public double getVy() {
	return vy;
    }

    public double getOmega() {
	return omega;
    }

    public double getAx() {
	return ax;
    }

    public double getAy() {
	return ay;
    }

    public double getEpsilon() {
	return epsilon;
    }

    public double getStaticPressure() {
	return staticPressure;
    }

    public void setStaticPressure(double staticPressure) {
	this.staticPressure = staticPressure;
    }

    public double getDynamicPressureX() {
	return dynamicPressureX;
    }

    public void setDynamicPressureX(double dynamicPressureX) {
	this.dynamicPressureX = dynamicPressureX;
    }

    public double getDynamicPressureY() {
	return dynamicPressureY;
    }

    public void setDynamicPressureY(double dynamicPressureY) {
	this.dynamicPressureY = dynamicPressureY;
    }

}