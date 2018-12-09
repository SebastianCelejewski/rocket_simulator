package pl.sebcel.rc.gui.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

public class GraphicsEnvironment extends JComponent {

    private static final long serialVersionUID = 1L;
    private List<GraphicalObject> objects = new ArrayList<GraphicalObject>();

    public void addGraphicsObject(GraphicalObject object) {
	objects.add(object);
    }

    public void paint(Graphics g, double viewportX, double viewportY, double alpha, double scale) {
	int blue = 255 - (int) ((double) viewportY / 250);
	if (blue < 0) {
	    blue = 0;
	}
	if (blue > 255) {
	    blue = 255;
	}

	Color backgroundColor = new Color(0, 0, blue);
	g.setColor(backgroundColor);
	g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);

	Scaler s = new Scaler(viewportX, viewportY, scale, alpha);
	s.setClipBounds(g.getClipBounds());

	ScalableGraphics sg = new ScalableGraphics(g, s);

	for (GraphicalObject object : objects) {
	    object.paint(sg);
	}
    }

}