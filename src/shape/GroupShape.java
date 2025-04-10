package shape;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import shape.Shape;

public class GroupShape extends Shape {
	private LinkedList<Shape> groupedShapes;

	public GroupShape(LinkedList<Shape> shapes) {
		super(0, 0, 0, 0, Color.WHITE, "Group", 12);
		this.groupedShapes = shapes;
		calculateBounds();
	}

	private void calculateBounds() {
		if (groupedShapes.isEmpty()) {
			return;
		}

		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double maxY = Double.MIN_VALUE;

		for (Shape shape : groupedShapes) {
			Rectangle2D bounds = shape.getBounds();
			minX = Math.min(minX, bounds.getMinX());
			minY = Math.min(minY, bounds.getMinY());
			maxX = Math.max(maxX, bounds.getMaxX());
			maxY = Math.max(maxY, bounds.getMaxY());
		}

		this.x = (int) minX;
		this.y = (int) minY;
		this.width = (int) (maxX - minX);
		this.height = (int) (maxY - minY);
	}

	@Override
	public void draw(Graphics2D g2d) {
		for (Shape shape : groupedShapes) {
			shape.draw(g2d);
		}
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public LinkedList<Shape> getGroupedShapes() {
		return groupedShapes;
	}

	@Override
	public boolean contains(int x, int y) {
		for (Shape shape : groupedShapes) {
			if (shape.contains(x, y)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void move(int dx, int dy) {
		// Update the GroupShape's position
		this.x += dx;
		this.y += dy;

		// Update the position of the shapes within the group
		for (Shape shape : groupedShapes) {
			shape.move(dx, dy);
		}

		// Recalculate the bounds of the group
		calculateBounds();
	}
}