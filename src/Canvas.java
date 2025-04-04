// Canvas.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.LinkedList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener {
    private LinkedList<Shape> shapes = new LinkedList<>(); // 現在 canva 上面有的 shape
    private LinkedList<Link> links = new LinkedList<>(); // To store links

    private LinkedList<Shape> apple = new LinkedList<>();

    private Shape selectedShape; // 之前選過得 shape
    private Shape startLinkShape; // Temporary start shape for drawing links
    private Shape endLinkShape;   // Temporary end shape for drawing links
    private Point dragStart;
    private int dragOffsetX, dragOffsetY;
    private Rectangle currentRect;
    private Ellipse2D.Double currentOval;
    private String currentMode = "select"; // 不同的模式
    private BufferedImage associationImage;
    private BufferedImage generalizationImage;
    private BufferedImage compositionImage;

    public Canvas() {
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        setBackground(Color.WHITE);
        try {
            associationImage = ImageIO.read(getClass().getResource("/images/association-line.png")); // Corrected filename
            generalizationImage = ImageIO.read(getClass().getResource("/images/generalization-line.png")); // Corrected filename
            compositionImage = ImageIO.read(getClass().getResource("/images/composition-line.png")); // Corrected filename
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading link images!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    
    }

    public void combineAppleShapes() {
        if (apple.size() < 2) {
            JOptionPane.showMessageDialog(this, "Select at least two shapes to combine.");
            return;
        }

        GroupShape combinedShape = new GroupShape(new LinkedList<>(apple));
        shapes.removeAll(apple);
        shapes.add(combinedShape);

        apple.clear();
        repaint();
    }

    public void uncombineShape(GroupShape combinedShape) {
        if (combinedShape == null) {
            JOptionPane.showMessageDialog(this, "Select a combined shape to uncombine.");
            return;
        }

        shapes.remove(combinedShape);
        shapes.addAll(combinedShape.getGroupedShapes());

        repaint();
    }

    public String getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(String mode) {
        this.currentMode = mode;
        repaint();
    }

    public Shape getSelectedShape() {
        return selectedShape;
    }

    public void setSelectedShape(Shape shape) {
        this.selectedShape = shape;
        repaint();
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
        repaint();
    }

    public void removeShape(Shape shape) {
        shapes.remove(shape);
        repaint();
    }

    public void replaceShape(Shape oldShape, Shape newShape) {
        shapes.remove(oldShape);
        shapes.add(newShape);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw shapes
        for (Shape shape : shapes) {
            shape.draw(g2d);
        }

        // Draw links
        for (Link link : links) {
            link.draw(g2d);
        }

        for (Shape app : apple) {
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(2));
            Rectangle bounds = app.getBounds();
            g2d.drawRect(bounds.x - 3, bounds.y - 3, bounds.width + 6, bounds.height + 6); // Draw selection outline
            g2d.setStroke(new BasicStroke(1));

            // Draw eight ports around the selected shape
            drawPorts(g2d, bounds);
        }
    }

    private void drawPorts(Graphics2D g2d, Rectangle bounds) {
        int portSize = 6;
        g2d.setColor(Color.BLACK);

        for (Shape app : apple) {

            if (app instanceof Oval) {
                double centerX = bounds.getCenterX();
                double centerY = bounds.getCenterY();
                double halfWidth = bounds.getWidth() / 2;
                double halfHeight = bounds.getHeight() / 2;

                // North
                g2d.fillRect((int) centerX - portSize / 2, bounds.y - portSize / 2, portSize, portSize);
                // South
                g2d.fillRect((int) centerX - portSize / 2, bounds.y + bounds.height - portSize / 2, portSize, portSize);
                // West
                g2d.fillRect(bounds.x - portSize / 2, (int) centerY - portSize / 2, portSize, portSize);
                // East
                g2d.fillRect(bounds.x + bounds.width - portSize / 2, (int) centerY - portSize / 2, portSize, portSize);
            } else if (app instanceof Rect) {
                // Draw eight ports for rectangles
                // Corners
                g2d.fillRect(bounds.x - portSize / 2, bounds.y - portSize / 2, portSize, portSize);             // Top Left
                g2d.fillRect(bounds.x + bounds.width - portSize / 2, bounds.y - portSize / 2, portSize, portSize);       // Top Right
                g2d.fillRect(bounds.x - portSize / 2, bounds.y + bounds.height - portSize / 2, portSize, portSize);    // Bottom Left
                g2d.fillRect(bounds.x + bounds.width - portSize / 2, bounds.y + bounds.height - portSize / 2, portSize, portSize); // Bottom Right

                // Midpoints
                g2d.fillRect(bounds.x + bounds.width / 2 - portSize / 2, bounds.y - portSize / 2, portSize, portSize);             // Top
                g2d.fillRect(bounds.x + bounds.width / 2 - portSize / 2, bounds.y + bounds.height - portSize / 2, portSize, portSize); // Bottom
                g2d.fillRect(bounds.x - portSize / 2, bounds.y + bounds.height / 2 - portSize / 2, portSize, portSize);             // Left
                g2d.fillRect(bounds.x + bounds.width - portSize / 2, bounds.y + bounds.height / 2 - portSize / 2, portSize, portSize); // Right
            }

        }
    }

    private Shape findShapeAt(Point p) {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            if (shapes.get(i).contains(p.x, p.y)) {
                return shapes.get(i);
            }
        }
        return null;
    }

    private void selectShape(Point p) {
        Shape foundShape = findShapeAt(p);
        if (foundShape != null) {
            if (selectedShape != null && selectedShape != foundShape) {
                // Deselect previously selected shape
                selectedShape = null;
            }
            selectedShape = foundShape;
            apple.add(selectedShape);
            // Calculate offset for dragging
            Rectangle bounds = selectedShape.getBounds();
            dragOffsetX = p.x - bounds.x;
            dragOffsetY = p.y - bounds.y;
        } else {
            selectedShape = null;
            apple.clear();
        }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        if (currentMode.equals("select")) {
            selectShape(p);
        } else if (currentMode.equals("rect")) {
            currentRect = new Rectangle(p.x, p.y, 0, 0);
            dragStart = p;
        } else if (currentMode.equals("oval")) {
            currentOval = new Ellipse2D.Double(p.x, p.y, 0, 0);
            dragStart = p;
        } else if (currentMode.equals("association") || currentMode.equals("generalization") || currentMode.equals("composition")) {
            startLinkShape = findShapeAt(p);
            dragStart = p; // Store the initial point for drawing the temporary line
            repaint(); // Redraw to show the temporary line
        }
    }

@Override
public void mouseDragged(MouseEvent e) {
    Point p = e.getPoint();
    if (currentMode.equals("select") && selectedShape != null) {
        // Calculate deltaX and deltaY based on the shape's bounds
        double deltaX = p.x - selectedShape.getBounds().x;
        double deltaY = p.y - selectedShape.getBounds().y;

        // Move the selected shape
        selectedShape.move((int) (deltaX - dragOffsetX), (int) (deltaY - dragOffsetY));

        // Update links connected to this shape
        for (Link link : links) {
            Shape startShape = link.getStartShape();
            if (startShape != null) {
                link.setStartPoint(getClosestPort(startShape, startShape.getBounds(), link.getEndPointOnOtherShape()));
            }
            Shape endShape = link.getEndShape();
            if (endShape != null) {
                link.setEndPoint(getClosestPort(endShape, endShape.getBounds(), link.getStartPointOnOtherShape()));
            }
        }

        dragOffsetX = (int) deltaX;
        dragOffsetY = (int) deltaY;

        repaint();
    } else if (currentMode.equals("rect") && currentRect != null) {
        currentRect.setBounds(Math.min(dragStart.x, p.x), Math.min(dragStart.y, p.y), Math.abs(dragStart.x - p.x), Math.abs(dragStart.y - p.y));
        repaint();
    } else if (currentMode.equals("oval") && currentOval != null) {
        currentOval.setFrame(Math.min(dragStart.x, p.x), Math.min(dragStart.y, p.y), Math.abs(dragStart.x - p.x), Math.abs(dragStart.y - p.y));
        repaint();
    } else if ((currentMode.equals("association") || currentMode.equals("generalization") || currentMode.equals("composition")) && startLinkShape != null) {
        dragStart = p; // Update dragStart to the current mouse position for temporary line
        repaint();
    }
}

    @Override
    public void mouseMoved(MouseEvent e) {
        // Method required by MouseMotionListener
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point p = e.getPoint();
        if (currentMode.equals("rect") && currentRect != null) {
            addShape(new Rect(currentRect.x, currentRect.y, currentRect.width, currentRect.height));
            currentRect = null;
        } else if (currentMode.equals("oval") && currentOval != null) {
            addShape(new Oval((int) currentOval.x, (int) currentOval.y, (int) currentOval.width, (int) currentOval.height));
            currentOval = null;
        } else if ((currentMode.equals("association") || currentMode.equals("generalization") || currentMode.equals("composition")) && startLinkShape != null) {
            endLinkShape = findShapeAt(p);
            if (endLinkShape != null && endLinkShape != startLinkShape) {
                BufferedImage linkImage = null;
                if (currentMode.equals("association")) {
                    linkImage = associationImage;
                } else if (currentMode.equals("generalization")) {
                    linkImage = generalizationImage;
                } else if (currentMode.equals("composition")) {
                    linkImage = compositionImage;
                }

                Link link = null;
                if (currentMode.equals("association")) {
                    link = new AssociationLink(startLinkShape, endLinkShape, linkImage);
                } else if (currentMode.equals("generalization")) {
                    link = new GeneralizationLink(startLinkShape, endLinkShape, linkImage);
                } else if (currentMode.equals("composition")) {
                    link = new CompositionLink(startLinkShape, endLinkShape, linkImage);
                }

                if (link != null) {
                    link.setStartPoint(getClosestPort(startLinkShape, startLinkShape.getBounds(), p));
                    link.setEndPoint(getClosestPort(endLinkShape, endLinkShape.getBounds(), p));
                    links.add(link);
                }
            }
            startLinkShape = null;
            endLinkShape = null;
            repaint();
        }
        dragStart = null;
    }

private Point2D.Double getClosestPort(Shape shape, Rectangle bounds, Point p) {
    double centerX = bounds.getCenterX();
    double centerY = bounds.getCenterY();
    double dx = p.x - centerX;
    double dy = p.y - centerY;

    double angle = Math.atan2(dy, dx);
    double x = 0, y = 0;
    double minDistanceSq = Double.MAX_VALUE;

    if (shape instanceof Oval) {
        // Find closest of the four cardinal direction ports for ovals
        double halfWidth = bounds.getWidth() / 2;
        double halfHeight = bounds.getHeight() / 2;

        // North
        double distSq = Point2D.distanceSq(p.x, p.y, centerX, bounds.getMinY());
        if (distSq < minDistanceSq) { minDistanceSq = distSq; x = centerX; y = bounds.getMinY(); }
        // South
        distSq = Point2D.distanceSq(p.x, p.y, centerX, bounds.getMaxY());
        if (distSq < minDistanceSq) { minDistanceSq = distSq; x = centerX; y = bounds.getMaxY(); }
        // West
        distSq = Point2D.distanceSq(p.x, p.y, bounds.getMinX(), centerY);
        if (distSq < minDistanceSq) { minDistanceSq = distSq; x = bounds.getMinX(); y = centerY; }
        // East
        distSq = Point2D.distanceSq(p.x, p.y, bounds.getMaxX(), centerY);
        if (distSq < minDistanceSq) { minDistanceSq = distSq; x = bounds.getMaxX(); y = centerY; }
    } else {
        // Find closest of the eight ports for rectangles
        // Corners
        double distSq = Point2D.distanceSq(p.x, p.y, bounds.getMinX(), bounds.getMinY());
        if (distSq < minDistanceSq) { minDistanceSq = distSq; x = bounds.getMinX(); y = bounds.getMinY(); }
        distSq = Point2D.distanceSq(p.x, p.y, bounds.getMaxX(), bounds.getMinY());
        if (distSq < minDistanceSq) { minDistanceSq = distSq; x = bounds.getMaxX(); y = bounds.getMinY(); }
        distSq = Point2D.distanceSq(p.x, p.y, bounds.getMinX(), bounds.getMaxY());
        if (distSq < minDistanceSq) { minDistanceSq = distSq; x = bounds.getMinX(); y = bounds.getMaxY(); }
        distSq = Point2D.distanceSq(p.x, p.y, bounds.getMaxX(), bounds.getMaxY());
        if (distSq < minDistanceSq) { minDistanceSq = distSq; x = bounds.getMaxX(); y = bounds.getMaxY(); }

        // Midpoints
        distSq = Point2D.distanceSq(p.x, p.y, centerX, bounds.getMinY());
        if (distSq < minDistanceSq) { minDistanceSq = distSq; x = centerX; y = bounds.getMinY(); }
        distSq = Point2D.distanceSq(p.x, p.y, centerX, bounds.getMaxY());
        if (distSq < minDistanceSq) { minDistanceSq = distSq; x = centerX; y = bounds.getMaxY(); }
        distSq = Point2D.distanceSq(p.x, p.y, bounds.getMinX(), centerY);
        if (distSq < minDistanceSq) { minDistanceSq = distSq; x = bounds.getMinX(); y = centerY; }
        distSq = Point2D.distanceSq(p.x, p.y, bounds.getMaxX(), centerY);
        if (distSq < minDistanceSq) { minDistanceSq = distSq; x = bounds.getMaxX(); y = centerY; }
    }

    return new Point2D.Double(x, y);
}

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }
}