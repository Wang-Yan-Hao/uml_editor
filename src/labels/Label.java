import java.awt.*;

public class Label {
    private String text;
    private String shape;
    private Color color;
    private int fontSize;

    public Label(String text, String shape, Color color, int fontSize) {
        this.text = text;
        this.shape = shape;
        this.color = color;
        this.fontSize = fontSize;
    }

    // Getters and Setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
}