package pl.sebcel.rc.gui.physics;

import java.util.ArrayList;
import java.util.List;

import pl.sebcel.rc.events.RocketSettingsChangedEvent;
import pl.sebcel.rc.gui.components.Cloud;
import pl.sebcel.rc.gui.components.Earth;
import pl.sebcel.rc.gui.components.EngineDust;
import pl.sebcel.rc.gui.components.EngineSpark;
import pl.sebcel.rc.gui.components.Rocket;
import pl.sebcel.rc.gui.components.Tower;
import pl.sebcel.rc.gui.graphics.GraphicsEnvironment;
import pl.sebcel.rc.gui.graphics.IFocusListener;
import pl.sebcel.rc.infrastructure.events.EventBus;
import pl.sebcel.rc.infrastructure.events.EventBusFactory;
import pl.sebcel.rc.infrastructure.events.EventListener;

public class PhysicalEnvironment implements EventListener<RocketSettingsChangedEvent> {

    private List<IFocusListener> focusListeners = new ArrayList<IFocusListener>();

    private Earth earth = new Earth();
    private List<Rocket> rockets = new ArrayList<Rocket>();
    private List<Tower> towers = new ArrayList<Tower>();
    private EngineSpark[] sparks = new EngineSpark[400];
    private EngineDust[] dusts = new EngineDust[200];
    private List<Cloud> clouds = new ArrayList<Cloud>();

    private int sparkPointer = 0;
    private int dustPointer = 0;

    private EventBus eventBus;

    private PhysicalConditions physicalConditions;

    public PhysicalEnvironment() {

	physicalConditions = new PhysicalConditions();

	eventBus = EventBusFactory.getInstance();

	eventBus.subscribeToEvent(RocketSettingsChangedEvent.class, this);

	Rocket rocket1 = new Rocket(0);
	Rocket rocket2 = new Rocket(1);
	rockets.add(rocket1);
	rockets.add(rocket2);

	Tower leftTower = new Tower();
	Tower rightTower = new Tower();

	leftTower.initialize(0, Constants.EARTH_RADIUS, 0, 0, 0, 0);
	rightTower.initialize(100, Constants.EARTH_RADIUS, 0, 0, 0, 0);
	towers.add(leftTower);
	towers.add(rightTower);

	towers.add(new Tower());

	for (int i = 0; i < sparks.length; i++) {
	    sparks[i] = new EngineSpark();
	}
	for (int i = 0; i < dusts.length; i++) {
	    dusts[i] = new EngineDust();
	}

	for (int i = 0; i < 200; i++) {
	    clouds.add(new Cloud());
	}

	reset();
    }

    public void tick(double deltaTime) {
	handleEngine(deltaTime);
	handleSparks(deltaTime);
	handleDusts(deltaTime);
    }

    private void handleEngine(double deltaTime) {

	for (int r = 0; r < rockets.size(); r++) {
	    Rocket rocket = rockets.get(r);
	    rocket.tick(physicalConditions, this, deltaTime);
	    double rocketX = rocket.getX();
	    double rocketY = rocket.getY();
	    double rocketVx = rocket.getVx();
	    double rocketVy = rocket.getVy();
	    double rocketAlpha = rocket.getAlpha();

	    int nozzleWidth = rocket.getNozzleWidth();
	    int numberOfMainThrustSparks = rocket.getNumberOfMainThrustSparks();
	    for (int i = 0; i < numberOfMainThrustSparks; i++) {
		double relativeVx = 40 - (Math.random() * 80);
		double relativeVy = -(Math.random() * 400) - 50;

		double vx = -relativeVx * Math.cos(-rocketAlpha) + relativeVy * Math.sin(-rocketAlpha);
		double vy = relativeVx * Math.sin(-rocketAlpha) + relativeVy * Math.cos(-rocketAlpha);

		vx += rocketVx;
		vy += rocketVy;

		double sparkRelativeR = 1 + Math.random() * nozzleWidth / 2;
		double sparkRelativePhi = Math.random() * Math.PI;
		double sparkRelativeX = sparkRelativeR * Math.cos(sparkRelativePhi);
		double sparkRelativeY = -5 - sparkRelativeR * Math.sin(sparkRelativePhi);

		double sparkX = -sparkRelativeX * Math.cos(-rocketAlpha) + sparkRelativeY * Math.sin(-rocketAlpha);
		double sparkY = sparkRelativeX * Math.sin(-rocketAlpha) + sparkRelativeY * Math.cos(-rocketAlpha);

		sparkX += rocketX;
		sparkY += rocketY;

		if (i % 10 == 0) {
		    if (++dustPointer >= dusts.length) {
			dustPointer = 0;
		    }
		    dusts[dustPointer].initialize(sparkX, sparkY, vx / 3, vy / 3);
		} else {
		    if (++sparkPointer >= sparks.length) {
			sparkPointer = 0;
		    }
		    sparks[sparkPointer].initialize(sparkX, sparkY, vx, vy);
		}
	    }

	    int numberOfLeftThrustSparks = rocket.getNumberOfLeftThrustSparks();
	    for (int i = 0; i < numberOfLeftThrustSparks; i++) {

		double relativeVx = -(Math.random() * 100) - 50;
		double relativeVy = -20 + (Math.random() * 40);

		double vx = relativeVx * Math.cos(-rocketAlpha) + relativeVy * Math.sin(-rocketAlpha);
		double vy = -relativeVx * Math.sin(-rocketAlpha) - relativeVy * Math.cos(-rocketAlpha);

		vx += rocketVx;
		vy += rocketVy;

		double sparkRelativeX = -7;
		double sparkRelativeY = rocket.getHorizontalNozzlesPosition();

		double sparkX = sparkRelativeX * Math.cos(-rocketAlpha) + sparkRelativeY * Math.sin(-rocketAlpha);
		double sparkY = -sparkRelativeX * Math.sin(-rocketAlpha) + sparkRelativeY * Math.cos(-rocketAlpha);

		sparkX += rocketX;
		sparkY += rocketY;

		if (i % 10 == 0) {
		    if (++dustPointer >= dusts.length) {
			dustPointer = 0;
		    }
		    dusts[dustPointer].initialize(sparkX, sparkY, vx / 3, vy / 3);
		} else {
		    if (++sparkPointer >= sparks.length) {
			sparkPointer = 0;
		    }
		    sparks[sparkPointer].initialize(sparkX, sparkY, vx, vy);
		}
	    }

	    int numberOfRightThrustSparks = rocket.getNumberOfRightThrustSparks();
	    for (int i = 0; i < numberOfRightThrustSparks; i++) {
		double relativeVx = (Math.random() * 100) + 50;
		double relativeVy = -20 + (Math.random() * 40);

		double vx = relativeVx * Math.cos(-rocketAlpha) + relativeVy * Math.sin(-rocketAlpha);
		double vy = -relativeVx * Math.sin(-rocketAlpha) - relativeVy * Math.cos(-rocketAlpha);

		vx += rocketVx;
		vy += rocketVy;

		double sparkRelativeX = +7;
		double sparkRelativeY = rocket.getHorizontalNozzlesPosition();

		double sparkX = sparkRelativeX * Math.cos(-rocketAlpha) + sparkRelativeY * Math.sin(-rocketAlpha);
		double sparkY = -sparkRelativeX * Math.sin(-rocketAlpha) + sparkRelativeY * Math.cos(-rocketAlpha);

		sparkX += rocketX;
		sparkY += rocketY;

		if (i % 10 == 0) {
		    if (++dustPointer >= dusts.length) {
			dustPointer = 0;
		    }
		    dusts[dustPointer].initialize(sparkX, sparkY, vx / 3, vy / 3);
		} else {
		    if (++sparkPointer >= sparks.length) {
			sparkPointer = 0;
		    }
		    sparks[sparkPointer].initialize(sparkX, sparkY, vx, vy);
		}
	    }

	    focusListeners.get(r).setLocation(rocket.getViewX(), rocket.getViewY());
	}
    }

    private void handleSparks(double deltaTime) {
	for (int i = 0; i < sparks.length; i++) {
	    boolean justCreated = sparks[i].isJustCreated();
	    sparks[i].tick(physicalConditions, this, deltaTime);

	    if (justCreated) {
		continue;
	    }
	    sparks[i].setVx(0.9 * sparks[i].getVx());
	    sparks[i].setVy(0.9 * sparks[i].getVy());

	    sparks[i].setX(sparks[i].getX() + deltaTime * sparks[i].getVx());
	    sparks[i].setY(sparks[i].getY() + deltaTime * sparks[i].getVy());

	    if (sparks[i].getY() <= Constants.EARTH_RADIUS) {
		sparks[i].setY(Constants.EARTH_RADIUS);
		double newVx = (sparks[i].getVx() / Math.abs(sparks[i].getVx())) * Math.abs(sparks[i].getVy());
		double newVy = Math.abs(sparks[i].getVx());

		sparks[i].setVx(newVx);
		sparks[i].setVy(newVy);
	    }
	}
    }

    private void handleDusts(double deltaTime) {
	for (int i = 0; i < dusts.length; i++) {
	    boolean justCreated = sparks[i].isJustCreated();

	    dusts[i].tick(physicalConditions, this, deltaTime);

	    if (justCreated) {
		continue;
	    }

	    dusts[i].setVx(0.9 * dusts[i].getVx());
	    dusts[i].setVy(0.9 * dusts[i].getVy());

	    dusts[i].setX(dusts[i].getX() + deltaTime * dusts[i].getVx());
	    dusts[i].setY(dusts[i].getY() + deltaTime * dusts[i].getVy());

	    if (dusts[i].getY() <= Constants.EARTH_RADIUS) {
		dusts[i].setY(Constants.EARTH_RADIUS);

		double newVx = (dusts[i].getVx() / Math.abs(dusts[i].getVx())) * Math.abs(dusts[i].getVy());
		double newVy = Math.abs(dusts[i].getVx());

		dusts[i].setVx(newVx);
		dusts[i].setVy(newVy);
	    }

	    if (dusts[i].getVx() < 1 && dusts[i].getVy() < 1) {
		dusts[i].setVx(dusts[i].getVx() + .1 - Math.random() * .2);
		dusts[i].setVy(dusts[i].getVy() + .1 - Math.random() * .2);
	    }
	}
    }

    public void addComponents(GraphicsEnvironment graphicsEnvironment) {
	graphicsEnvironment.addGraphicsObject(earth);
	
	for (Rocket rocket : rockets) {
	    graphicsEnvironment.addGraphicsObject(rocket);
	}
	for (int i = 0; i < sparks.length; i++) {
	    graphicsEnvironment.addGraphicsObject(sparks[i]);
	}
	for (int i = 0; i < dusts.length; i++) {
	    graphicsEnvironment.addGraphicsObject(dusts[i]);
	}
	for (int i = 0; i < towers.size(); i++) {
	    graphicsEnvironment.addGraphicsObject(towers.get(i));
	}
	for (int i = 0; i < clouds.size(); i++) {
	    graphicsEnvironment.addGraphicsObject(clouds.get(i));
	}
    }

    public void reset() {
	for (int i = 0; i < rockets.size(); i++) {
	    rockets.get(i).initialize(100 * i, Constants.EARTH_RADIUS, 0, 0, 0, 0);
	}
	for (EngineSpark spark : sparks) {
	    spark.initialize(0, 0, 0, 0);
	}
	for (EngineDust dust : dusts) {
	    dust.reset();
	}
    }

    public void addFocusListener(IFocusListener focusListener) {
	focusListeners.add(focusListener);
    }

    @Override
    public void eventFired(RocketSettingsChangedEvent event) {
	rockets.get(event.getRocketId()).setSettings(event.getSettings());
    }

    public KineticState adjustKineticState(KineticState kineticState, Shape shape) {
	double objectX = kineticState.getX();
	double objectY = kineticState.getY();
	double objectAlpha = kineticState.getAlpha();

	double objectVx = kineticState.getVx();
	double objectVy = kineticState.getVy();
	double objectOmega = kineticState.getOmega();

	double objectAx = kineticState.getAx();
	double objectAy = kineticState.getAy();
	double objectEpsilon = kineticState.getEpsilon();

	double objectLowestPoint = calculateLowestPoint(objectX, objectY, objectAlpha, shape);
	double objectLowestH = objectLowestPoint - Constants.EARTH_RADIUS;

	if (objectLowestH < 5) {
	    double[] newXy = setH(objectX, objectY, 5);
	    objectX = newXy[0];
	    objectY = newXy[1];
	    objectVy = 0;
	    objectAy = 0;
	    objectVx = 0.8 * objectVx;
	}

	for (int t = 0; t < towers.size(); t++) {
	    if (Math.abs(objectX - towers.get(t).getX()) < 30 && objectLowestH < 30) {
		double[] newXy = setH(objectX, objectY, 30);
		objectX = newXy[0];
		objectY = newXy[1];
		objectVy = 0;
		objectAy = 0;
		objectVx = 0.8 * objectVx;
	    }
	}

	KineticState newKineticState = new KineticState(objectX, objectY, objectAlpha, objectVx, objectVy, objectOmega, objectAx, objectAy, objectEpsilon);
	newKineticState.setStaticPressure(kineticState.getStaticPressure());
	newKineticState.setDynamicPressureX(kineticState.getDynamicPressureX());
	newKineticState.setDynamicPressureY(kineticState.getDynamicPressureY());
	return newKineticState;
    }

    private double[] setH(double x, double y, double h) {
	double rAlpha = Math.atan2(y, x);
	double r = h + Constants.EARTH_RADIUS;
	double[] newXy = new double[2];
	newXy[0] = r * Math.cos(rAlpha);
	newXy[1] = r * Math.sin(rAlpha);
	return newXy;
    }

    private double calculateLowestPoint(double rocketX, double rocketY, double rocketAlpha, Shape shape) {
	Double lowestR = null;
	for (Point p : shape.getPoints()) {
	    double x = getX(rocketX, rocketY, p.getX(), p.getY(), rocketAlpha);
	    double y = getY(rocketX, rocketY, p.getX(), p.getY(), rocketAlpha);
	    double r = Math.sqrt(x * x + y * y);
	    if (lowestR == null || lowestR > r) {
		lowestR = r;
	    }
	}
	return lowestR;
    }

    private double getX(double refX, double refY, double x, double y, double alpha) {
	double newX = refX + (x) * Math.cos(-alpha) + (y) * Math.sin(-alpha);
	return newX;
    }

    private double getY(double refX, double refY, double x, double y, double alpha) {
	double newY = refY + (x) * Math.sin(-alpha) + (y) * Math.cos(-alpha);
	return newY;
    }
}