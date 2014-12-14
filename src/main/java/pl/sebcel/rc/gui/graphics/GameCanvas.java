package pl.sebcel.rc.gui.graphics;

import java.awt.Graphics;

import javax.swing.JComponent;

public class GameCanvas extends JComponent implements IFocusListener {

    private static final long serialVersionUID = 1L;

    private double x;
    private double y;
    private double alpha;
    private double vx;
    private double vy;
    private double scale;

    private GraphicsEnvironment graphicsEnvironment;
    private ControlsDisplay controlPanel;

    public GameCanvas(GraphicsEnvironment graphicsEnvironment, ControlsDisplay controlPanel) {
	this.graphicsEnvironment = graphicsEnvironment;
	this.controlPanel = controlPanel;
	this.scale = 1.0;
	this.alpha = Math.PI / 2;
    }

    @Override
    public void paint(Graphics g) {
	graphicsEnvironment.paint(g, x, y, alpha, scale);
	controlPanel.paint(g);
    }

    @Override
    public void setLocation(double x, double y) {
	this.vx = x - this.x;
	this.vy = y - this.y;
	this.x += this.vx / 10;
	this.y += this.vy / 10;
	this.x = x;
	this.y = y;
    }

    public void zoomIn() {
	scale = scale / Math.sqrt(10);
    }

    public void zoomOut() {
	scale = scale * Math.sqrt(10);
    }

    public void rotateLeft() {
	alpha = alpha - Math.PI / 96;
    }

    public void rotateRight() {
	alpha = alpha + Math.PI / 96;
    }

    public void rotateToRelativeVertical() {
	alpha = Math.atan2(y, x);
	alpha = Math.PI - alpha;
    }
}