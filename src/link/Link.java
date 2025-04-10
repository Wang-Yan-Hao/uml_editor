// Link.java
package link;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import shape.Shape;
import shape.Rect;
import shape.Oval;

public abstract class Link {
    protected Shape startShape;
    protected Shape endShape;
    protected Point2D.Double start;
    protected Point2D.Double end;
    protected BufferedImage image; // To store the link's BufferedImage

    public Link(Shape startShape, Shape endShape, BufferedImage image) {
        this.startShape = startShape;
        this.endShape = endShape;
        this.image = image;
    }

    public Shape getStartShape() {
        return startShape;
    }

    public Shape getEndShape() {
        return endShape;
    }

    public void setStartPoint(Point2D.Double start) {
        this.start = start;
    }

    public void setEndPoint(Point2D.Double end) {
        this.end = end;
    }

    // Helper methods to get the endpoint on the other shape
    public Point getEndPointOnOtherShape() {
        return new Point((int) end.x, (int) end.y);
    }

    public Point getStartPointOnOtherShape() {
        return new Point((int) start.x, (int) start.y);
    }

    public void draw(Graphics2D g2d) {
        if (image == null || start == null || end == null) {
            g2d.setColor(Color.BLACK);
            g2d.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
            return;
        }

        Point2D.Double drawStart = end; // Swap start and end for drawing
        Point2D.Double drawEnd = start;

        double angle = Math.atan2(drawEnd.y - drawStart.y, drawEnd.x - drawStart.x);
        double distance = drawStart.distance(drawEnd);

        double scaleX = distance / image.getWidth();
        double scaleY = 1.0;

        AffineTransform originalTransform = g2d.getTransform();
        try {
            g2d.translate(drawStart.x, drawStart.y);
            g2d.rotate(angle);
            g2d.scale(scaleX, scaleY);

            g2d.drawImage(image, 0, -image.getHeight() / 2, null);

        } finally {
            g2d.setTransform(originalTransform);
        }
    }
}