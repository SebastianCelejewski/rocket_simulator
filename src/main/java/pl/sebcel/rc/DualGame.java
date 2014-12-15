package pl.sebcel.rc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import pl.sebcel.rc.gui.components.Cloud;
import pl.sebcel.rc.gui.components.Earth;
import pl.sebcel.rc.gui.graphics.DualGameFrame;
import pl.sebcel.rc.gui.graphics.GraphicsEnvironment;
import pl.sebcel.rc.gui.physics.PhysicalEnvironment;

public class DualGame {

    private GraphicsEnvironment graphicsEnvironment;
    private PhysicalEnvironment physicalEnvironment;

    private DualGameFrame gameFrame;
    private Timer timer;
    private int timerDelay;

    public DualGame() {

//	new Measurements();
	graphicsEnvironment = new GraphicsEnvironment();
	physicalEnvironment = new PhysicalEnvironment();
	Earth leftEarth = new Earth();

	graphicsEnvironment.addGraphicsObject(leftEarth);
	physicalEnvironment.addComponents(graphicsEnvironment);

	for (int i = 0; i < 200; i++) {
	    graphicsEnvironment.addGraphicsObject(new Cloud());
	}

	gameFrame = new DualGameFrame(graphicsEnvironment, physicalEnvironment);
	gameFrame.setBounds(50, 100, 1600, 800);

	timerDelay = 100;

	timer = new Timer(timerDelay, new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		tick();
	    }
	});
    }

    public void start() {
	gameFrame.setVisible(true);
	timer.start();
    }

    public void tick() {
	physicalEnvironment.tick(((double) timerDelay) / 1000);
	gameFrame.tick();
    }
}