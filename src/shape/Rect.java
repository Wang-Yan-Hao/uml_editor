// Rect.java
package shape;
import java.awt.*;
import shape.Shape;


public class Rect extends Shape {
    public Rect(int x, int y, int width, int height) {
        this(x, y, width, height, Color.WHITE, "Rect", 12);
    }

    public Rect(int x, int y, int width, int height, Color backgroundColor) {
        this(x, y, width, height, backgroundColor, "Rect", 12);
    }

    public Rect(int x, int y, int width, int height, Color backgroundColor, String name, int fontSize) {
        super(x, y, width, height, backgroundColor, name, fontSize);
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(backgroundColor);
        g2d.fillRect(x, y, width, height);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, width, height);
        drawCenteredText(g2d, name, getBounds(), fontSize);
    }

    @Override
    public void move(int dx, int dy) {
        super.move(dx, dy);
    }
}