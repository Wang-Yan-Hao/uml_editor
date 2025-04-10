// Shape.java
package shape;
import java.awt.*;

public abstract class Shape {
    public int x; // 左上角點的 x and y
    public int y;
    public int width;
    public int height;

    public Color backgroundColor;
    public String name;
    public int fontSize;

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

    // 移動就是把 x y 設定
    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    // 看有沒有在圖形裡面
    public boolean contains(int px, int py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    // 邊緣就是正方形 回傳一個正方形
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

    // 在圖形中間寫字
    public void drawCenteredText(Graphics2D g2d, String text, Rectangle bounds, int fontSize) {
        Font font = new Font("Arial", Font.PLAIN, fontSize);
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics(font);
        int textX = bounds.x + (bounds.width - metrics.stringWidth(text)) / 2;
        int textY = bounds.y + ((bounds.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g2d.setColor(Color.BLACK); // Default text color
        g2d.drawString(text, textX, textY);
    }
}