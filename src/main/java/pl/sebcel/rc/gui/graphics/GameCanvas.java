package pl.sebcel.rc.gui.graphics;

import java.awt.Graphics;

import javax.swing.JComponent;

public class GameCanvas extends JComponent implements IFocusListener {

    private static final long serialVersionUID = 1L;

    private double viewportX;
    private double viewportY;
    private double viewportAlpha;
    private double scale;

    private GraphicsEnvironment graphicsEnvironment;
    private ControlsDisplay controlPanel;

    public GameCanvas(GraphicsEnvironment graphicsEnvironment, ControlsDisplay controlPanel) {
	this.graphicsEnvironment = graphicsEnvironment;
	this.controlPanel = controlPanel;
	this.scale = 1.0;
	this.viewportAlpha = Math.PI / 2;
    }

    @Override
    public void paint(Graphics g) {
	graphicsEnvironment.paint(g, viewportX, viewportY, viewportAlpha, scale);
	controlPanel.paint(g);
    }

    @Override
    public void setLocation(double x, double y) {
	this.viewportX = x;
	this.viewportY = y;
    }

    public void zoomIn() {
	scale = scale / Math.sqrt(10);
    }

    public void zoomOut() {
	scale = scale * Math.sqrt(10);
    }

    public void rotateLeft() {
	viewportAlpha = viewportAlpha - Math.PI / 96;
    }

    public void rotateRight() {
	viewportAlpha = viewportAlpha + Math.PI / 96;
    }

    public void rotateToRelativeVertical() {
	viewportAlpha = Math.atan2(viewportY, viewportX);
	viewportAlpha = Math.PI - viewportAlpha;
    }
}