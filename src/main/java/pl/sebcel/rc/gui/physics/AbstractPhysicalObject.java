package pl.sebcel.rc.gui.physics;

public abstract class AbstractPhysicalObject implements PhysicalObject {

    private double x;
    private double y;
    private double vx;
    private double vy;
    private double alpha;
    private double omega;
    private boolean justCreated;

    public void initialize(double x, double y, double vx, double vy, double alpha, double omega) {
	this.x = x;
	this.y = y;
	this.vx = vx;
	this.vy = vy;
	this.alpha = alpha;
	this.omega = omega;
	this.justCreated = true;
    }

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

    public abstract Shape getShape();

    @Override
    public void tick(PhysicalConditions physicalConditions, PhysicalEnvironment physicalEnvironment, double deltaTime) {
	justCreated = false;
    }

    @Override
    public boolean isJustCreated() {
	return justCreated;
    }

    public abstract double getMass();

    public boolean shock(double s) {
	return false;
    }

    protected KineticState calculateKineticState(double mainThrustForce, double horizontalThrustForce, PhysicalConditions physicalConditions, double deltaTime) {
	return this.calculateKineticState(mainThrustForce, horizontalThrustForce, physicalConditions, deltaTime, false);
    }

    protected KineticState calculateKineticState(double mainThrustForce, double horizontalThrustForce, PhysicalConditions physicalConditions, double deltaTime, boolean simplifiedModel) {
	double mass = getMass();
	double h = Math.sqrt(getX() * getX() + getY() * getY()) - Constants.EARTH_RADIUS;

	double momentOfInertia = getMomentOfInertia();
	double airPressure = physicalConditions.getAirDensity(h);

	double[] gravitationForce = physicalConditions.getGravitationForce(getX(), getY(), mass);
	double[] airFriction = physicalConditions.calculateAirFriction(getShape(), getAlpha(), h, getVx(), getVy());
	double horizontalFriction = airFriction[0];
	double verticalFriction = airFriction[1];

	double objectFx = gravitationForce[0] + horizontalFriction + mainThrustForce * Math.sin(-getAlpha());
	double objectFy = gravitationForce[1] + mainThrustForce * Math.cos(-getAlpha()) + verticalFriction;

	double objectM = horizontalThrustForce;

	double objectAx = objectFx / mass;
	double objectAy = objectFy / mass;

	double objectEpsilon = objectM / momentOfInertia;

	double objectVx = getVx() + deltaTime * objectAx;
	double objectVy = getVy() + deltaTime * objectAy;
	double objectOmega = getOmega() + deltaTime * objectEpsilon;

	if (simplifiedModel) {
	    objectOmega = calculateSimplifiedOmega(objectOmega, horizontalThrustForce);
	}

	double objectX = getX() + deltaTime * objectVx;
	double objectY = getY() + deltaTime * objectVy;

	double objectAlpha = getAlpha() + deltaTime * objectOmega;

	KineticState kineticState = new KineticState(objectX, objectY, objectAlpha, objectVx, objectVy, objectOmega, objectAx, objectAy, objectEpsilon);
	kineticState.setStaticPressure(airPressure);
	kineticState.setDynamicPressureX(horizontalFriction);
	kineticState.setDynamicPressureY(verticalFriction);
	return kineticState;
    }

    private double calculateSimplifiedOmega(double objectOmega, double horizontalThrustForce) {
	if (horizontalThrustForce < 0) {
	    return -.5;
	}
	if (horizontalThrustForce > 0) {
	    return .5;
	}
	return 0;
    }

    protected abstract double getMomentOfInertia();
}