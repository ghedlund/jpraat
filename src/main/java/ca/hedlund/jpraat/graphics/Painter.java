package ca.hedlund.jpraat.graphics;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public interface Painter<T> {

	/**
	 * Paint the given object inside the bounds using the provided java 2D
	 * context.
	 * 
	 * @param obj
	 * @param g2d
	 * @param bounds
	 */
	public void paintInside(T obj, Graphics2D g2d, Rectangle2D bounds);
	
}
