package pl.sebcel.rc.gui.graphics;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import pl.sebcel.rc.gui.graphics.ControlsDisplay.Status;
import pl.sebcel.rc.gui.physics.PhysicalEnvironment;

public class DualGameFrame extends JFrame implements KeyListener {

    private static final long serialVersionUID = 1L;

    private ControlsInterface leftControlsInterface;
    private ControlsInterface rightControlsInterface;

    private ControlsDisplay leftControlsDisplay;
    private ControlsDisplay rightControlsDisplay;

    private GameCanvas leftCanvas;
    private GameCanvas rightCanvas;

    private GraphicsEnvironment graphicsEnvironment;

    public DualGameFrame(GraphicsEnvironment graphicsEnvironment, PhysicalEnvironment physicalEnvironment) {
	this.graphicsEnvironment = graphicsEnvironment;

	this.leftControlsDisplay = new ControlsDisplay(0);
	this.rightControlsDisplay = new ControlsDisplay(1);

	this.leftControlsInterface = new ControlsInterface(0);
	this.rightControlsInterface = new ControlsInterface(1);

	this.setLayout(new GridLayout(1, 2));

	leftCanvas = new GameCanvas(graphicsEnvironment, leftControlsDisplay);
	rightCanvas = new GameCanvas(graphicsEnvironment, rightControlsDisplay);

	physicalEnvironment.addFocusListener(leftCanvas);
	physicalEnvironment.addFocusListener(rightCanvas);

	JPanel leftPanel = new JPanel();
	leftPanel.setLayout(new BorderLayout());
	leftPanel.setBorder(BorderFactory.createRaisedBevelBorder());
	leftPanel.add(leftCanvas, BorderLayout.CENTER);

	JPanel rightPanel = new JPanel();
	rightPanel.setLayout(new BorderLayout());
	rightPanel.setBorder(BorderFactory.createRaisedBevelBorder());
	rightPanel.add(rightCanvas, BorderLayout.CENTER);

	this.add(leftPanel);
	this.add(rightPanel);

	this.addWindowListener(new WindowAdapter() {

	    @Override
	    public void windowClosing(WindowEvent e) {
		System.exit(255);
	    }
	});

	this.setTitle("Rocket simulator");

	this.leftCanvas.setFocusable(true);
	this.rightCanvas.setFocusable(true);
	this.setFocusable(true);
	this.addKeyListener(this);
	this.leftCanvas.addKeyListener(this);
	this.rightCanvas.addKeyListener(this);
    }

    public void tick() {
	leftCanvas.repaint();
	rightCanvas.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
	if (e.getKeyCode() == KeyEvent.VK_F1) {
	    leftControlsDisplay.setDisplayStatus(Status.KINETIC_STATE_SURFACE_RELATIVE);
	}
	if (e.getKeyCode() == KeyEvent.VK_F2) {
	    leftControlsDisplay.setDisplayStatus(Status.KINETIC_STATE_SPACE_RELATIVE);
	}
	if (e.getKeyCode() == KeyEvent.VK_F3) {
	    leftControlsDisplay.setDisplayStatus(Status.INTERNAL_STATE);
	}
	if (e.getKeyCode() == KeyEvent.VK_F4) {
	    leftControlsDisplay.setDisplayStatus(Status.ENVIRONMENT_CONDITIONS);
	}

	if (e.getKeyCode() == KeyEvent.VK_Q) {
	    graphicsEnvironment.zoomIn();
	}

	if (e.getKeyCode() == KeyEvent.VK_W) {
	    graphicsEnvironment.zoomOut();
	}

	if (e.getKeyCode() == KeyEvent.VK_F6) {
	    leftControlsInterface.setToggleSimplifiedModel();
	}

	if (e.getKeyCode() == 65) {
	    leftControlsInterface.setMainThrust(1);
	}
	if (e.getKeyCode() == 66) {
	    leftControlsInterface.toggleSpecialAction();
	}
	if (e.getKeyCode() == 90) {
	    leftControlsInterface.setLeftThrust(1);
	}
	if (e.getKeyCode() == 88) {
	    leftControlsInterface.setRightThrust(1);
	}
	if (e.getKeyCode() == KeyEvent.VK_F5) {
	    leftControlsInterface.reset();
	}
	if (e.getKeyCode() == 83) {
	    leftControlsInterface.killRotation();
	}

	if (e.getKeyCode() == KeyEvent.VK_F9) {
	    rightControlsDisplay.setDisplayStatus(Status.KINETIC_STATE_SURFACE_RELATIVE);
	}
	if (e.getKeyCode() == KeyEvent.VK_F10) {
	    rightControlsDisplay.setDisplayStatus(Status.KINETIC_STATE_SPACE_RELATIVE);
	}
	if (e.getKeyCode() == KeyEvent.VK_F11) {
	    rightControlsDisplay.setDisplayStatus(Status.INTERNAL_STATE);
	}
	if (e.getKeyCode() == KeyEvent.VK_F12) {
	    rightControlsDisplay.setDisplayStatus(Status.ENVIRONMENT_CONDITIONS);
	}

	if (e.getKeyCode() == 38) {
	    rightControlsInterface.setMainThrust(1);
	}
	if (e.getKeyCode() == 40) {
	    rightControlsInterface.toggleSpecialAction();
	}
	if (e.getKeyCode() == 37) {
	    rightControlsInterface.setLeftThrust(1);
	}
	if (e.getKeyCode() == 39) {
	    rightControlsInterface.setRightThrust(1);
	}
	if (e.getKeyCode() == KeyEvent.VK_F8) {
	    rightControlsInterface.reset();
	}
	if (e.getKeyCode() == 12) {
	    rightControlsInterface.killRotation();
	}

	if (e.getKeyCode() == KeyEvent.VK_F7) {
	    rightControlsInterface.setToggleSimplifiedModel();
	}
	if (e.getKeyCode() != KeyEvent.VK_F4) {
	    e.consume();
	}
    }

    @Override
    public void keyReleased(KeyEvent e) {
	if (e.getKeyCode() == 65) {
	    leftControlsInterface.setMainThrust(0);
	}
	if (e.getKeyCode() == 90) {
	    leftControlsInterface.setLeftThrust(0);
	}
	if (e.getKeyCode() == 88) {
	    leftControlsInterface.setRightThrust(0);
	}

	if (e.getKeyCode() == 38) {
	    rightControlsInterface.setMainThrust(0);
	}

	if (e.getKeyCode() == 37) {
	    rightControlsInterface.setLeftThrust(0);
	}
	if (e.getKeyCode() == 39) {
	    rightControlsInterface.setRightThrust(0);
	}
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }
}