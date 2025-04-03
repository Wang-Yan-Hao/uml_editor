// Oval.java
import java.awt.*;

public class Oval extends Shape {
    public Oval(int x, int y, int width, int height) {
        this(x, y, width, height, Color.WHITE, "Oval", 12);
    }

    public Oval(int x, int y, int width, int height, Color backgroundColor) {
        this(x, y, width, height, backgroundColor, "Oval", 12);
    }

    public Oval(int x, int y, int width, int height, Color backgroundColor, String name, int fontSize) {
        super(x, y, width, height, backgroundColor, name, fontSize);
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(backgroundColor);
        g2d.fillOval(x, y, width, height);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x, y, width, height);
        drawCenteredText(g2d, name, getBounds(), fontSize);
    }

    @Override
    public void move(int dx, int dy) {
        super.move(dx, dy);
    }
}