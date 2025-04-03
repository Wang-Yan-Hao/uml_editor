// CompositeShape.java
import java.awt.*;
import java.util.ArrayList;

public class CompositeShape extends Shape {
    private ArrayList<Shape> children = new ArrayList<>();

    public CompositeShape(int x, int y) {
        this(x, y, 0, 0, null, "Group", 12); // Default name and fontSize
        updateBounds();
    }

    public CompositeShape(int x, int y, int width, int height, Color backgroundColor) {
        this(x, y, width, height, backgroundColor, "Group", 12);
    }

    public CompositeShape(int x, int y, int width, int height, Color backgroundColor, String name, int fontSize) {
        super(x, y, width, height, backgroundColor, name, fontSize);
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(backgroundColor != null ? backgroundColor : new Color(220, 220, 220, 100)); // Draw background if set, otherwise a translucent gray
        g2d.fillRect(x, y, width, height);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, width, height);
        drawCenteredText(g2d, name, getBounds(), fontSize); // Draw the group name

        for (Shape child : children) {
            child.draw(g2d);
        }
    }

    @Override
    public void move(int dx, int dy) {
        super.move(dx, dy);
        for (Shape child : children) {
            child.move(dx, dy);
        }
        updateBounds();
    }

    @Override
    public boolean contains(int px, int py) {
        for (Shape child : children) {
            if (child.contains(px, py)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void addShape(Shape shape) {
        children.add(shape);
        updateBounds();
    }

    public void removeShape(Shape shape) {
        children.remove(shape);
        updateBounds();
    }

    private void updateBounds() {
        if (children.isEmpty()) {
            this.width = 0;
            this.height = 0;
            return;
        }

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Shape child : children) {
            Rectangle bounds = child.getBounds();
            minX = Math.min(minX, bounds.x);
            minY = Math.min(minY, bounds.y);
            maxX = Math.max(maxX, bounds.x + bounds.width);
            maxY = Math.max(maxY, bounds.y + bounds.height);
        }

        this.x = minX;
        this.y = minY;
        this.width = maxX - minX;
        this.height = maxY - minY;
    }
}