package pl.sebcel.rc.gui.components;

import pl.sebcel.rc.events.RocketStateChangedEvent;
import pl.sebcel.rc.gui.RocketControlSettings;
import pl.sebcel.rc.gui.dto.RocketStatus;
import pl.sebcel.rc.gui.graphics.GraphicalObject;
import pl.sebcel.rc.gui.graphics.ScalableGraphics;
import pl.sebcel.rc.gui.physics.AbstractPhysicalObject;
import pl.sebcel.rc.gui.physics.Constants;
import pl.sebcel.rc.gui.physics.KineticState;
import pl.sebcel.rc.gui.physics.PhysicalConditions;
import pl.sebcel.rc.gui.physics.PhysicalEnvironment;
import pl.sebcel.rc.gui.physics.Shape;
import pl.sebcel.rc.infrastructure.events.EventBus;
import pl.sebcel.rc.infrastructure.events.EventBusFactory;

public class Rocket extends AbstractPhysicalObject implements GraphicalObject {

    private static final double FIRST_STAGE_LENGTH = 105;
    private static final double SECOND_STAGE_LENGTH = 45;
    private static final double THIRD_STAGE_LENGTH = 20;
    private static final double LANDER_LENGTH = 15;

    private RocketState lifeCycle = RocketState.INITIAL;

    private RocketFirstStage firstStage;
    private RocketSecondStage secondStage;
    private RocketThirdStage thirdStage;
    private SpaceshipLander lander;
    private DragParachute dragParachute;
    private DrougeParachute drougeParachute;
    private Shape shape;

    private RocketControlSettings settings = new RocketControlSettings();

    private EventBus eventBus;

    private int rocketId;

    public Rocket(int rocketId) {
	this.rocketId = rocketId;

	eventBus = EventBusFactory.getInstance();

	firstStage = new RocketFirstStage(rocketId);
	secondStage = new RocketSecondStage();
	thirdStage = new RocketThirdStage(rocketId);
	lander = new SpaceshipLander();

	dragParachute = new DragParachute();
	drougeParachute = new DrougeParachute();

	calculateShape();

	reset();
    }

    private void calculateShape() {
	Shape shape = new Shape();
	if (isFirstStageAttached()) {
	    shape.append(firstStage.getShape());
	}
	if (isSecondStageAttached()) {
	    shape.append(secondStage.getShape());
	}
	if (isThirdStageAttached()) {
	    shape.append(thirdStage.getShape());
	}
	shape.append(lander.getShape());
	if (drougeParachute.isDeployed()) {
	    shape = drougeParachute.getShape();
	}
	if (dragParachute.isDeployed()) {
	    shape = dragParachute.getShape();
	}
	this.shape = shape;
    }

    private void reset() {
	firstStage.setFuel(2200000);
	secondStage.setFuel(600000);
	thirdStage.setFuel(100000);
	lander.setFuel(0);
	lifeCycle = RocketState.INITIAL;
	setX(100 * rocketId);
	setY(Constants.EARTH_RADIUS);
	setVx(0);
	setVy(0);
	setAlpha(0);
	setOmega(0);
	settings = new RocketControlSettings();
	firstStage.reset();
	secondStage.reset();
	thirdStage.reset();
	lander.reset();
	dragParachute.reset();
	drougeParachute.reset();
	calculateShape();
    }

    public void setSettings(RocketControlSettings settings) {
	this.settings = settings;
	if (lifeCycle == RocketState.INITIAL) {
	    firstStage.setThrottle(settings.getMainThrust());
	} else {
	    firstStage.setThrottle(0);
	}
	if (lifeCycle == RocketState.FIRST_STAGE_SEPARATED) {
	    secondStage.setThrottle(settings.getMainThrust());
	} else {
	    secondStage.setThrottle(0);
	}
	if (lifeCycle == RocketState.SECOND_STAGE_SEPARATED) {
	    thirdStage.setThrottle(settings.getMainThrust());
	} else {
	    thirdStage.setThrottle(0);
	}
	lander.setThrottle(0);
    }

    @Override
    public void initialize(double x, double y, double vx, double vy, double alpha, double omega) {
	super.initialize(x, y, vx, vy, alpha, omega);
	setX(x);
	setY(y);
    }

    public void separateFirstStage() {
	double vx = firstStage.getVx();
	double vy = firstStage.getVy();
	double alpha = getAlpha();

	double dx = -15 * Math.sin(-alpha);
	double dy = -15 * Math.cos(-alpha);
	if (lifeCycle == RocketState.INITIAL) {
	    lifeCycle = RocketState.FIRST_STAGE_SEPARATED;
	    firstStage.setVx(vx + dx);
	    firstStage.setVy(vy + dy);
	    firstStage.detach();
	    secondStage.ignite();
	    calculateShape();
	}
    }

    public void separateSecondStage() {
	double vx = secondStage.getVx();
	double vy = secondStage.getVy();
	double alpha = getAlpha();

	double dx = -15 * Math.sin(-alpha);
	double dy = -15 * Math.cos(-alpha);

	if (lifeCycle == RocketState.FIRST_STAGE_SEPARATED) {
	    lifeCycle = RocketState.SECOND_STAGE_SEPARATED;
	    secondStage.setVx(vx + dx);
	    secondStage.setVy(vy + dy);
	    secondStage.detach();
	    thirdStage.ignite();
	    calculateShape();
	}
    }

    public void separateThirdStage() {
	double vx = secondStage.getVx();
	double vy = secondStage.getVy();
	double alpha = getAlpha();

	double dx = -15 * Math.sin(-alpha);
	double dy = -15 * Math.cos(-alpha);

	if (lifeCycle == RocketState.SECOND_STAGE_SEPARATED) {
	    lifeCycle = RocketState.THIRD_STAGE_SEPARATED;
	    thirdStage.setVx(vx + dx);
	    thirdStage.setVy(vy + dy);
	    thirdStage.detach();
	    lander.ignite();
	    calculateShape();
	}
    }

    public boolean shock(double deltaV) {
	if (Math.abs(deltaV) > 10) {
	    if (lifeCycle == RocketState.INITIAL) {
		firstStage.shock(deltaV);
		secondStage.shock(deltaV);
		thirdStage.shock(deltaV);
		firstStage.detach();
		secondStage.detach();
		thirdStage.detach();
		lander.detach();
		lander.shock(deltaV);
		lifeCycle = RocketState.FIRST_STAGE_SEPARATED;
		return true;
	    }
	    if (lifeCycle == RocketState.FIRST_STAGE_SEPARATED) {
		secondStage.shock(deltaV);
		thirdStage.shock(deltaV);
		lander.shock(deltaV);
		secondStage.detach();
		thirdStage.detach();
		lander.detach();
		lifeCycle = RocketState.FIRST_STAGE_SEPARATED;
		return true;
	    }
	    if (lifeCycle == RocketState.SECOND_STAGE_SEPARATED) {
		thirdStage.shock(deltaV);
		lander.shock(deltaV);
		thirdStage.detach();
		lander.detach();
		lifeCycle = RocketState.FIRST_STAGE_SEPARATED;
	    }
	    if (lifeCycle == RocketState.THIRD_STAGE_SEPARATED) {
		lander.shock(deltaV);
		lander.detach();
	    }
	    return true;
	} else {
	    return false;
	}
    }

    public void deployDrougeParachute() {
	drougeParachute.deploy();
	calculateShape();
    }

    public void releaseDrougeParachute() {
	drougeParachute.release();
	calculateShape();
    }

    public void deployDragParachute() {
	dragParachute.deploy();
	calculateShape();
    }

    public void releaseDragParachute() {
	dragParachute.release();
	calculateShape();
    }

    @Override
    public void paint(ScalableGraphics g) {
	firstStage.paint(g);
	secondStage.paint(g);
	thirdStage.paint(g);
	lander.paint(g);
	drougeParachute.paint(g);
	dragParachute.paint(g);
    }

    public double getFuel() {
	if (lifeCycle == RocketState.INITIAL) {
	    return firstStage.getFuel();
	}
	if (lifeCycle == RocketState.FIRST_STAGE_SEPARATED) {
	    return secondStage.getFuel();
	}
	if (lifeCycle == RocketState.SECOND_STAGE_SEPARATED) {
	    return thirdStage.getFuel();
	}
	if (lifeCycle == RocketState.THIRD_STAGE_SEPARATED) {
	    return 0;
	}
	throw new RuntimeException("Invalid life cycle: " + lifeCycle);
    }

    @Override
    public double getMass() {
	double mass = 0;
	if (isFirstStageAttached()) {
	    mass += firstStage.getMass();
	}
	if (isSecondStageAttached()) {
	    mass += secondStage.getMass();
	}
	if (isThirdStageAttached()) {
	    mass += thirdStage.getMass();
	}
	mass += lander.getMass();

	return mass;
    }

    @Override
    public double getMomentOfInertia() {
	double momentOfInertia = 0;
	if (isFirstStageAttached()) {
	    momentOfInertia += firstStage.getMomentOfInertia();
	}
	if (isSecondStageAttached()) {
	    momentOfInertia += secondStage.getMomentOfInertia();
	}
	if (isThirdStageAttached()) {
	    momentOfInertia += thirdStage.getMomentOfInertia();
	}
	momentOfInertia += lander.getMomentOfInertia();

	return momentOfInertia;
    }

    @Override
    public void setX(double x) {

	double alpha = getAlpha();
	double sin = -Math.sin(alpha);
	super.setX(x);

	if (lifeCycle == RocketState.INITIAL) {
	    firstStage.setX(x);
	    secondStage.setX(x + FIRST_STAGE_LENGTH * sin);
	    thirdStage.setX(x + (FIRST_STAGE_LENGTH + SECOND_STAGE_LENGTH) * sin);
	    lander.setX(x + (FIRST_STAGE_LENGTH + SECOND_STAGE_LENGTH + THIRD_STAGE_LENGTH) * sin);
	    if (drougeParachute.isDeployed()) {
		drougeParachute.setX(x + (FIRST_STAGE_LENGTH + SECOND_STAGE_LENGTH + THIRD_STAGE_LENGTH + LANDER_LENGTH) * sin);
	    }
	    if (dragParachute.isDeployed()) {
		dragParachute.setX(x + (FIRST_STAGE_LENGTH + SECOND_STAGE_LENGTH + THIRD_STAGE_LENGTH + LANDER_LENGTH) * sin);
	    }
	}
	if (lifeCycle == RocketState.FIRST_STAGE_SEPARATED) {
	    secondStage.setX(x);
	    thirdStage.setX(x + (SECOND_STAGE_LENGTH) * sin);
	    lander.setX(x + (SECOND_STAGE_LENGTH + THIRD_STAGE_LENGTH) * sin);
	    if (drougeParachute.isDeployed()) {
		drougeParachute.setX(x + (SECOND_STAGE_LENGTH + THIRD_STAGE_LENGTH + LANDER_LENGTH) * sin);
	    }
	    if (dragParachute.isDeployed()) {
		dragParachute.setX(x + (SECOND_STAGE_LENGTH + THIRD_STAGE_LENGTH + LANDER_LENGTH) * sin);
	    }
	}
	if (lifeCycle == RocketState.SECOND_STAGE_SEPARATED) {
	    thirdStage.setX(x);
	    lander.setX(x + THIRD_STAGE_LENGTH * sin);
	    if (drougeParachute.isDeployed()) {
		drougeParachute.setX(x + (THIRD_STAGE_LENGTH + LANDER_LENGTH) * sin);
	    }
	    if (dragParachute.isDeployed()) {
		dragParachute.setX(x + (THIRD_STAGE_LENGTH + LANDER_LENGTH) * sin);
	    }
	}

	if (lifeCycle == RocketState.THIRD_STAGE_SEPARATED) {
	    lander.setX(x);
	    if (drougeParachute.isDeployed()) {
		drougeParachute.setX(x + (LANDER_LENGTH) * sin);
	    }
	    if (dragParachute.isDeployed()) {
		dragParachute.setX(x + (LANDER_LENGTH) * sin);
	    }
	}
    }

    @Override
    public void setY(double y) {
	super.setY(y);

	double alpha = getAlpha();
	double cos = Math.cos(alpha);

	if (lifeCycle == RocketState.INITIAL) {
	    firstStage.setY(y);
	    secondStage.setY(y + FIRST_STAGE_LENGTH * cos);
	    thirdStage.setY(y + (FIRST_STAGE_LENGTH + SECOND_STAGE_LENGTH) * cos);
	    lander.setY(y + (FIRST_STAGE_LENGTH + SECOND_STAGE_LENGTH + THIRD_STAGE_LENGTH) * cos);

	    if (drougeParachute.isDeployed()) {
		drougeParachute.setY(y + (FIRST_STAGE_LENGTH + SECOND_STAGE_LENGTH + THIRD_STAGE_LENGTH + LANDER_LENGTH) * cos);
	    }
	    if (dragParachute.isDeployed()) {
		dragParachute.setY(y + (FIRST_STAGE_LENGTH + SECOND_STAGE_LENGTH + THIRD_STAGE_LENGTH + LANDER_LENGTH) * cos);
	    }
	}
	if (lifeCycle == RocketState.FIRST_STAGE_SEPARATED) {
	    secondStage.setY(y);
	    thirdStage.setY(y + SECOND_STAGE_LENGTH * cos);
	    lander.setY(y + (SECOND_STAGE_LENGTH + THIRD_STAGE_LENGTH) * cos);
	    if (drougeParachute.isDeployed()) {
		drougeParachute.setY(y + (SECOND_STAGE_LENGTH + THIRD_STAGE_LENGTH + LANDER_LENGTH) * cos);
	    }
	    if (dragParachute.isDeployed()) {
		dragParachute.setY(y + (SECOND_STAGE_LENGTH + THIRD_STAGE_LENGTH + LANDER_LENGTH) * cos);
	    }
	}
	if (lifeCycle == RocketState.SECOND_STAGE_SEPARATED) {
	    thirdStage.setY(y);
	    lander.setY(y + THIRD_STAGE_LENGTH * cos);
	    if (dragParachute.isDeployed()) {
		dragParachute.setY(y + (THIRD_STAGE_LENGTH + LANDER_LENGTH) * cos);
	    }
	}
	if (lifeCycle == RocketState.THIRD_STAGE_SEPARATED) {
	    lander.setY(y);
	    if (drougeParachute.isDeployed()) {
		drougeParachute.setY(y + (LANDER_LENGTH) * cos);
	    }
	    if (dragParachute.isDeployed()) {
		dragParachute.setY(y + (LANDER_LENGTH) * cos);
	    }
	}
    }

    @Override
    public void setVx(double vx) {
	super.setVx(vx);
	if (isFirstStageAttached()) {
	    firstStage.setVx(vx);
	}
	if (isSecondStageAttached()) {
	    secondStage.setVx(vx);
	}
	if (isThirdStageAttached()) {
	    thirdStage.setVx(vx);
	}
	lander.setVx(vx);
	if (!drougeParachute.isReleased()) {
	    drougeParachute.setVx(vx);
	}
	if (!dragParachute.isReleased()) {
	    dragParachute.setVx(vx);
	}
    }

    @Override
    public void setVy(double vy) {
	super.setVy(vy);
	if (isFirstStageAttached()) {
	    firstStage.setVy(vy);
	}
	if (isSecondStageAttached()) {
	    secondStage.setVy(vy);
	}
	if (isThirdStageAttached()) {
	    thirdStage.setVy(vy);
	}
	lander.setVy(vy);
	if (drougeParachute.isDeployed()) {
	    drougeParachute.setVy(vy);
	}
	if (dragParachute.isDeployed()) {
	    dragParachute.setVy(vy);
	}
    }

    @Override
    public double getX() {
	if (lifeCycle == RocketState.INITIAL) {
	    return firstStage.getX();
	}
	if (lifeCycle == RocketState.FIRST_STAGE_SEPARATED) {
	    return secondStage.getX();
	}
	if (lifeCycle == RocketState.SECOND_STAGE_SEPARATED) {
	    return thirdStage.getX();
	}
	if (lifeCycle == RocketState.THIRD_STAGE_SEPARATED) {
	    return lander.getX();
	}
	return super.getX();
    }

    @Override
    public double getY() {
	if (lifeCycle == RocketState.INITIAL) {
	    return firstStage.getY();
	}
	if (lifeCycle == RocketState.FIRST_STAGE_SEPARATED) {
	    return secondStage.getY();
	}
	if (lifeCycle == RocketState.SECOND_STAGE_SEPARATED) {
	    return thirdStage.getY();
	}
	if (lifeCycle == RocketState.THIRD_STAGE_SEPARATED) {
	    return lander.getY();
	}
	return super.getY();
    }

    private double getMainThrustForce(RocketControlSettings rocketControlsSettings, PhysicalConditions physicalConditions) {
	double totalForce = 0;

	if (lifeCycle == RocketState.INITIAL) {
	    return firstStage.getThrustForce(rocketControlsSettings.getMainThrust(), physicalConditions);
	}
	if (lifeCycle == RocketState.FIRST_STAGE_SEPARATED) {
	    return secondStage.getThrustForce(rocketControlsSettings.getMainThrust(), physicalConditions);
	}
	if (lifeCycle == RocketState.SECOND_STAGE_SEPARATED) {
	    return thirdStage.getThrustForce(rocketControlsSettings.getMainThrust(), physicalConditions);
	}
	if (lifeCycle == RocketState.THIRD_STAGE_SEPARATED) {
	    return lander.getThrustForce(0);
	}
	return totalForce;
    }

    public double getLeftRightThrustForce(RocketControlSettings rocketControlsSettings, PhysicalConditions physicalConditions) {
	double horizontalThrustSettings = -rocketControlsSettings.getLeftThrust() + rocketControlsSettings.getRightThrust();

	if (lifeCycle == RocketState.INITIAL) {
	    return firstStage.getHorizontalThrustForce(horizontalThrustSettings, physicalConditions);
	}
	if (lifeCycle == RocketState.FIRST_STAGE_SEPARATED) {
	    return secondStage.getHorizontalThrustForce(horizontalThrustSettings, physicalConditions);
	}
	if (lifeCycle == RocketState.SECOND_STAGE_SEPARATED) {
	    return thirdStage.getHorizontalThrustForce(horizontalThrustSettings, physicalConditions);
	}
	if (lifeCycle == RocketState.THIRD_STAGE_SEPARATED) {
	    return lander.getHorizontalThrustForce(horizontalThrustSettings, physicalConditions);
	}
	throw new RuntimeException("Invalid life cycle: " + lifeCycle);
    }

    public RocketFirstStage getFirstStage() {
	return firstStage;
    }

    public RocketSecondStage getSecondStage() {
	return secondStage;
    }

    public RocketThirdStage getThirdStage() {
	return thirdStage;
    }

    public double getViewX() {
	return lander.getX();
    }

    public double getViewY() {
	return lander.getY();
    }

    public int getNozzleWidth() {
	if (lifeCycle == RocketState.INITIAL) {
	    return 8;
	}
	if (lifeCycle == RocketState.FIRST_STAGE_SEPARATED) {
	    return 6;
	}
	if (lifeCycle == RocketState.SECOND_STAGE_SEPARATED) {
	    return 2;
	}
	if (lifeCycle == RocketState.THIRD_STAGE_SEPARATED) {
	    return 0;
	}
	throw new RuntimeException("Invalid life cycle: " + lifeCycle);
    }

    @Override
    public void tick(PhysicalConditions physicalConditions, PhysicalEnvironment physicalEnvironment, double deltaTime) {
	super.tick(physicalConditions, physicalEnvironment, deltaTime);

	if (settings.isReset()) {
	    reset();
	}

	if (settings.isLifeCycleAction()) {
	    handleLifeCycleAction();
	}

	double mainThrustForce = getMainThrustForce(settings, physicalConditions);
	double horizontalThrustForce = getLeftRightThrustForce(settings, physicalConditions);

	if (getFuel() <= 0) {
	    mainThrustForce = 0;
	}

	KineticState kineticStateBeforeCollisions = calculateKineticState(mainThrustForce, horizontalThrustForce, physicalConditions, deltaTime, settings.isSimplifiedModel());
	KineticState kineticStateAfterCollisions = physicalEnvironment.adjustKineticState(kineticStateBeforeCollisions, getShape());

	double deltaV = Math.abs(kineticStateBeforeCollisions.getVy() - kineticStateAfterCollisions.getVy());

	setAlpha(kineticStateAfterCollisions.getAlpha());
	setOmega(kineticStateAfterCollisions.getOmega());
	setX(kineticStateAfterCollisions.getX());
	setY(kineticStateAfterCollisions.getY());
	setVx(kineticStateAfterCollisions.getVx());
	setVy(kineticStateAfterCollisions.getVy());

	if (settings.isKillRotation()) {
	    setOmega(getOmega() * 0.99);
	}

	if (lifeCycle == RocketState.THIRD_STAGE_SEPARATED) {
	    if (Math.abs(kineticStateAfterCollisions.getOmega()) > .1) {
		setOmega(Math.signum(kineticStateAfterCollisions.getOmega()) * .1);
	    }
	}

	if (lifeCycle == RocketState.THIRD_STAGE_SEPARATED) {
	    double beta = Math.atan2(getVy(), getVx());
	    double deltaAlpha = beta - getAlpha() + Math.PI / 2;
	    if (drougeParachute.isDeployed()) {
		setAlpha(getAlpha() + deltaAlpha * 0.1 * deltaTime);
	    }
	    if (dragParachute.isDeployed()) {
		setAlpha(getAlpha() + deltaAlpha * 0.5 * deltaTime);
	    }
	}

	if (deltaV > 10) {
	    shock(deltaV);
	}

	firstStage.tick(physicalConditions, physicalEnvironment, deltaTime);
	secondStage.tick(physicalConditions, physicalEnvironment, deltaTime);
	thirdStage.tick(physicalConditions, physicalEnvironment, deltaTime);
	lander.tick(physicalConditions, physicalEnvironment, deltaTime);
	drougeParachute.tick(physicalConditions, physicalEnvironment, deltaTime);
	dragParachute.tick(physicalConditions, physicalEnvironment, deltaTime);

	RocketStatus rocketState = new RocketStatus();
	rocketState.setKineticState(kineticStateAfterCollisions);
	rocketState.setFirstStageFuel(getFirstStage().getFuel());
	rocketState.setSecondStageFuel(getSecondStage().getFuel());
	rocketState.setThirdStageFuel(getThirdStage().getFuel());

	rocketState.setRocketState(lifeCycle);
	rocketState.setDrougeParachuteState(drougeParachute.getState());
	rocketState.setDragParachuteState(dragParachute.getState());
	rocketState.setKillRotation(settings.isKillRotation());
	rocketState.setSimplifiedModel(settings.isSimplifiedModel());
	RocketStateChangedEvent rocketStatusChangedEvent = new RocketStateChangedEvent(rocketId, rocketState);
	eventBus.fireEvent(rocketStatusChangedEvent);
    }

    private void handleLifeCycleAction() {
	if (lifeCycle == RocketState.INITIAL) {
	    separateFirstStage();
	    settings.setLifeCycleAction(false);
	    return;
	}

	if (lifeCycle == RocketState.FIRST_STAGE_SEPARATED) {
	    separateSecondStage();
	    settings.setLifeCycleAction(false);
	    return;
	}

	if (lifeCycle == RocketState.SECOND_STAGE_SEPARATED) {
	    separateThirdStage();
	    settings.setLifeCycleAction(false);
	    return;
	}

	if (lifeCycle == RocketState.THIRD_STAGE_SEPARATED) {
	    if (drougeParachute.getState() == ParachuteState.READY) {
		deployDrougeParachute();
		settings.setLifeCycleAction(false);
		return;
	    }

	    if (drougeParachute.getState() == ParachuteState.DEPLOYED) {
		releaseDrougeParachute();
		settings.setLifeCycleAction(false);
		return;
	    }

	    if (dragParachute.getState() == ParachuteState.READY) {
		deployDragParachute();
		settings.setLifeCycleAction(false);
		return;
	    }

	    if (dragParachute.getState() == ParachuteState.DEPLOYED) {
		releaseDragParachute();
		settings.setLifeCycleAction(false);
		return;
	    }
	}
    }

    @Override
    public Shape getShape() {
	return shape;
    }

    public int getHorizontalNozzlesPosition() {
	if (lifeCycle == RocketState.INITIAL) {
	    return 165;
	}
	if (lifeCycle == RocketState.FIRST_STAGE_SEPARATED) {
	    return 60;
	}
	if (lifeCycle == RocketState.SECOND_STAGE_SEPARATED) {
	    return 25;
	}
	if (lifeCycle == RocketState.THIRD_STAGE_SEPARATED) {
	    return 15;
	}
	throw new RuntimeException("Invalid life cycle: " + lifeCycle);
    }

    public int getNumberOfMainThrustSparks() {
	if (getFuel() <= 0) {
	    return 0;
	}
	if (lifeCycle == RocketState.INITIAL) {
	    return (int) (30 * settings.getMainThrust());
	}
	if (lifeCycle == RocketState.FIRST_STAGE_SEPARATED) {
	    return (int) (30 * settings.getMainThrust());
	}
	if (lifeCycle == RocketState.SECOND_STAGE_SEPARATED) {
	    return (int) (6 * settings.getMainThrust());
	}
	if (lifeCycle == RocketState.THIRD_STAGE_SEPARATED) {
	    return 0;
	}

	return 0;
    }

    public int getNumberOfLeftThrustSparks() {
	if (getFuel() <= 0) {
	    return 0;
	}

	return (int) (10 * settings.getLeftThrust());
    }

    public int getNumberOfRightThrustSparks() {
	if (getFuel() <= 0) {
	    return 0;
	}

	return (int) (10 * settings.getRightThrust());
    }

    @Override
    public void setAlpha(double alpha) {
	super.setAlpha(alpha);
	if (isFirstStageAttached()) {
	    firstStage.setAlpha(alpha);
	}
	if (isSecondStageAttached()) {
	    secondStage.setAlpha(alpha);
	}
	if (isThirdStageAttached()) {
	    thirdStage.setAlpha(alpha);
	}
	lander.setAlpha(alpha);
    }

    @Override
    public void setOmega(double omega) {
	super.setOmega(omega);
	if (isFirstStageAttached()) {
	    firstStage.setOmega(omega);
	}
	if (isSecondStageAttached()) {
	    secondStage.setOmega(omega);
	}
	if (isThirdStageAttached()) {
	    thirdStage.setOmega(omega);
	}
	lander.setOmega(omega);
    }

    private boolean isFirstStageAttached() {
	return lifeCycle == RocketState.INITIAL;
    }

    private boolean isSecondStageAttached() {
	return lifeCycle == RocketState.INITIAL || lifeCycle == RocketState.FIRST_STAGE_SEPARATED;
    }

    private boolean isThirdStageAttached() {
	return lifeCycle == RocketState.INITIAL || lifeCycle == RocketState.FIRST_STAGE_SEPARATED || lifeCycle == RocketState.SECOND_STAGE_SEPARATED;
    }
}