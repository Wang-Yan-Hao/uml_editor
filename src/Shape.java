// Shape.java
import java.awt.*;

public abstract class Shape {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Color backgroundColor;
    protected String name;         // Text label for the shape
    protected int fontSize;      // Font size for the label

    public Shape(int x, int y, int width, int height, Color backgroundColor, String name, int fontSize) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;
        this.name = name;
        this.fontSize = fontSize;
    }

    public abstract void draw(Graphics2D g2d);

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public boolean contains(int px, int py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Getters and Setters
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    // Helper method to draw text centered in the shape
    protected void drawCenteredText(Graphics2D g2d, String text, Rectangle bounds, int fontSize) {
        Font font = new Font("Arial", Font.PLAIN, fontSize);
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics(font);
        int textX = bounds.x + (bounds.width - metrics.stringWidth(text)) / 2;
        int textY = bounds.y + ((bounds.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g2d.setColor(Color.BLACK); // Default text color
        g2d.drawString(text, textX, textY);
    }
}