package pl.sebcel.rc.gui.physics;

public class PhysicalConditions {

    public static final double EARTH_RADIUS = 6384000;

    public double getAirDensity(double height) {
	double airDensity = 1.21 * Math.exp(-height / 8000);
	if (Double.isNaN(airDensity)) {
	    airDensity = 0;
	}
	return airDensity;
    }

    public double[] getGravitationForce(double x, double y, double mass) {
	double r = Math.sqrt(x * x + y * y);
	double alpha = Math.atan2(y, x);
	double Fg = Constants.EARTH_MASS * Constants.GRAVITATIONAL_CONSTANT * mass / (r * r);

	double Fgx = -Fg * Math.cos(alpha);
	double Fgy = -Fg * Math.sin(alpha);

	return new double[] { Fgx, Fgy };
    }

    public double getCentrifugalForce(double mass, double vx, double y) {
	return mass * vx * vx / (y + EARTH_RADIUS);
    }

    public double[] calculateAirFriction(Shape shape, double alpha, double y, double vx, double vy) {
	double beta = Math.atan2(vy, vx);
	double airDensity = getAirDensity(y);
	double v = Math.sqrt(vx * vx + vy * vy);
	if (v == 0) {
	    return new double[] { 0, 0 };
	}

	double totalFx = 0;
	double totalFy = 0;

	for (Edge e : shape.getEdges()) {
	    double relativeAngle = e.getAngle() + alpha - beta;
	    while (relativeAngle < 0) {
		relativeAngle += Math.PI * 2;
	    }
	    while (relativeAngle > 2 * Math.PI) {
		relativeAngle -= Math.PI * 2;
	    }
	    if (relativeAngle >= 0 && relativeAngle <= Math.PI) {
		continue;
	    }

	    double Fx = -0.5 * airDensity * e.getLength() * v * v * Math.sin(alpha + e.getAngle()) * Math.sin(alpha - beta + e.getAngle());
	    double Fy = 0.5 * airDensity * e.getLength() * v * v * Math.cos(alpha + e.getAngle()) * Math.sin(alpha - beta + e.getAngle());

	    totalFx += Fx;
	    totalFy += Fy;
	}
	return new double[] { totalFx / 10, totalFy / 10 };
    }
}