package pl.sebcel.rc.gui.dto;

import pl.sebcel.rc.gui.components.ParachuteState;
import pl.sebcel.rc.gui.components.RocketState;
import pl.sebcel.rc.gui.physics.Constants;
import pl.sebcel.rc.gui.physics.KineticState;

public class RocketStatus {

    private double x;
    private double y;
    private double alpha;

    private double vx;
    private double vy;
    private double omega;

    private double relativeAlpha;
    private double h;
    private double l;
    private double vh;
    private double vv;
    private double ah;
    private double av;

    private double ax;
    private double ay;
    private double epsilon;

    private double firstStageFuel;
    private double secondStageFuel;
    private double thirdStageFuel;
    private boolean killRotation;
    private boolean simplifiedModel;

    private double staticPressure;
    private double dynamicPressureX;
    private double dynamicPressureY;

    private RocketState rocketState;
    private ParachuteState drougeParachuteState;
    private ParachuteState dragParachuteState;

    public double getX() {
	return x;
    }

    public void setX(double x) {
	this.x = x;
    }

    public double getY() {
	return y;
    }

    public void setY(double y) {
	this.y = y;
    }

    public double getVx() {
	return vx;
    }

    public void setVx(double vx) {
	this.vx = vx;
    }

    public double getVy() {
	return vy;
    }

    public void setVy(double vy) {
	this.vy = vy;
    }

    public double getAx() {
	return ax;
    }

    public void setAx(double ax) {
	this.ax = ax;
    }

    public double getAy() {
	return ay;
    }

    public void setAy(double ay) {
	this.ay = ay;
    }

    public double getFirstStageFuel() {
	return firstStageFuel;
    }

    public void setFirstStageFuel(double firstStageFuel) {
	this.firstStageFuel = firstStageFuel;
    }

    public double getSecondStageFuel() {
	return secondStageFuel;
    }

    public void setSecondStageFuel(double secondStageFuel) {
	this.secondStageFuel = secondStageFuel;
    }

    public double getThirdStageFuel() {
	return thirdStageFuel;
    }

    public void setThirdStageFuel(double thirdStageFuel) {
	this.thirdStageFuel = thirdStageFuel;
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

    public double getAlpha() {
	return alpha;
    }

    public void setAlpha(double alpha) {
	this.alpha = alpha;
    }

    public double getOmega() {
	return omega;
    }

    public void setOmega(double omega) {
	this.omega = omega;
    }

    public double getEpsilon() {
	return epsilon;
    }

    public void setEpsilon(double epsilon) {
	this.epsilon = epsilon;
    }

    public RocketState getRocketLifeCycle() {
	return rocketState;
    }

    public void setRocketState(RocketState rocketState) {
	this.rocketState = rocketState;
    }

    public ParachuteState getDrougeParachuteState() {
	return drougeParachuteState;
    }

    public void setDrougeParachuteState(ParachuteState drougeParachuteState) {
	this.drougeParachuteState = drougeParachuteState;
    }

    public ParachuteState getDragParachuteState() {
	return dragParachuteState;
    }

    public void setDragParachuteState(ParachuteState dragParachuteState) {
	this.dragParachuteState = dragParachuteState;
    }

    public boolean isKillRotation() {
	return killRotation;
    }

    public void setKillRotation(boolean killRotation) {
	this.killRotation = killRotation;
    }

    public void setKineticState(KineticState kineticState) {
	setX(kineticState.getX());
	setY(kineticState.getY());
	setAlpha(kineticState.getAlpha());
	setVx(kineticState.getVx());
	setVy(kineticState.getVy());
	setOmega(kineticState.getOmega());
	setAx(kineticState.getAx());
	setAy(kineticState.getAy());
	setEpsilon(kineticState.getEpsilon());
	setStaticPressure(kineticState.getStaticPressure());
	setDynamicPressureX(kineticState.getDynamicPressureX());
	setDynamicPressureY(kineticState.getDynamicPressureY());

	double r = Math.sqrt(x * x + y * y);
	double alfaR = Math.atan2(y, x);
	double alfaV = Math.atan2(vy, vx);
	double alfaA = Math.atan2(ay, ax);
	double v = Math.sqrt(vx * vx + vy * vy);
	double a = Math.sqrt(ax * ax + ay * ay);
	double ar = v * v / r;

	this.relativeAlpha = alpha - alfaR + Math.PI / 2;

	this.h = r - Constants.EARTH_RADIUS;
	this.l = Constants.EARTH_RADIUS * (-alfaR + Math.PI / 2);

	this.vh = v * Math.sin(alfaR - alfaV);
	this.vv = v * Math.cos(alfaR - alfaV);

	this.ah = a * Math.sin(alfaR - alfaA);
	this.av = a * Math.cos(alfaR - alfaA) + ar;
    }

    public double getH() {
	return h;
    }

    public double getL() {
	return l;
    }

    public double getVh() {
	return vh;
    }

    public double getVv() {
	return vv;
    }

    public double getAh() {
	return ah;
    }

    public double getAv() {
	return av;
    }

    public double getRelativeAlpha() {
	return relativeAlpha;
    }

    public boolean isSimplifiedModel() {
	return simplifiedModel;
    }

    public void setSimplifiedModel(boolean simplifiedModel) {
	this.simplifiedModel = simplifiedModel;
    }
}