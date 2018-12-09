package pl.sebcel.rc;

import javax.swing.Timer;

import pl.sebcel.rc.gui.graphics.DualGameFrame;
import pl.sebcel.rc.gui.graphics.GraphicsEnvironment;
import pl.sebcel.rc.gui.physics.PhysicalEnvironment;

public class Game {

    private final static int TIMER_DELAY = 100;

    private GraphicsEnvironment graphicsEnvironment;
    private PhysicalEnvironment physicalEnvironment;

    private DualGameFrame gameFrame;
    private Timer timer;

    public Game() {

	graphicsEnvironment = new GraphicsEnvironment();
	physicalEnvironment = new PhysicalEnvironment();
	physicalEnvironment.addComponents(graphicsEnvironment);

	gameFrame = new DualGameFrame(graphicsEnvironment, physicalEnvironment);
	gameFrame.setBounds(50, 100, 1600, 800);

	timer = new Timer(TIMER_DELAY, e -> tick());
    }

    public void start() {
	gameFrame.setVisible(true);
	timer.start();
    }

    public void tick() {
	physicalEnvironment.tick(((double) TIMER_DELAY) / 1000);
	gameFrame.tick();
    }
}