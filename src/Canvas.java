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
    private LinkedList<Shape> shapes = new LinkedList<>();
    private LinkedList<Link> links = new LinkedList<>(); // To store links
    private Shape selectedShape;
    private Shape startLinkShape; // Temporary start shape for drawing links
    private Shape endLinkShape;   // Temporary end shape for drawing links
    private Point dragStart;
    private int dragOffsetX, dragOffsetY;
    private Rectangle currentRect;
    private Ellipse2D.Double currentOval;
    private String currentMode = "select"; // Default mode
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

        if ((currentMode.equals("association") || currentMode.equals("generalization") || currentMode.equals("composition")) && startLinkShape != null) {
            g2d.setColor(Color.BLACK);
            g2d.drawLine((int) startLinkShape.getBounds().getCenterX(), (int) startLinkShape.getBounds().getCenterY(), (int) dragStart.x, (int) dragStart.y);


        }

        if (currentRect != null) {
            g2d.setColor(Color.BLACK);
            g2d.drawRect(currentRect.x, currentRect.y, currentRect.width, currentRect.height);
        }

        if (currentOval != null) {
            g2d.setColor(Color.BLACK);
            g2d.drawOval((int) currentOval.x, (int) currentOval.y, (int) currentOval.width, (int) currentOval.height);
        }

        if (selectedShape != null) {
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(2));
            Rectangle bounds = selectedShape.getBounds();
            g2d.drawRect(bounds.x - 3, bounds.y - 3, bounds.width + 6, bounds.height + 6); // Draw selection outline
            g2d.setStroke(new BasicStroke(1));

            // Draw eight ports around the selected shape
            drawPorts(g2d, bounds);
        }
    }

private void drawPorts(Graphics2D g2d, Rectangle bounds) {
    int portSize = 6;
    g2d.setColor(Color.BLACK);

    if (selectedShape instanceof Oval) {
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
    } else if (selectedShape instanceof Rect) {
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
            // Calculate offset for dragging
            Rectangle bounds = selectedShape.getBounds();
            dragOffsetX = p.x - bounds.x;
            dragOffsetY = p.y - bounds.y;
        } else {
            selectedShape = null;
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
        // Move the selected shape
        double deltaX = p.x - (selectedShape instanceof Rect ? ((Rect) selectedShape).x : ((Oval) selectedShape).x);
        double deltaY = p.y - (selectedShape instanceof Rect ? ((Rect) selectedShape).y : ((Oval) selectedShape).y);

        if (selectedShape instanceof Rect) {
            Rect rect = (Rect) selectedShape;
            rect.x += deltaX - dragOffsetX;
            rect.y += deltaY - dragOffsetY;
        } else if (selectedShape instanceof Oval) {
            Oval oval = (Oval) selectedShape;
            oval.x += deltaX - dragOffsetX;
            oval.y += deltaY - dragOffsetY;
        }

        Rectangle selectedBounds = selectedShape.getBounds();

        // Update links connected to this shape
        for (Link link : links) {
            if (link.getStartShape() == selectedShape) {
                link.setStartPoint(getClosestPort(selectedBounds, link.getEndPointOnOtherShape()));
            }
            if (link.getEndShape() == selectedShape) {
                link.setEndPoint(getClosestPort(selectedBounds, link.getStartPointOnOtherShape()));
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
                    link.setStartPoint(getClosestPort(startLinkShape.getBounds(), p));
                    link.setEndPoint(getClosestPort(endLinkShape.getBounds(), p));
                    links.add(link);
                }
            }
            startLinkShape = null;
            endLinkShape = null;
            repaint();
        }
        dragStart = null;
    }
    
private Point2D.Double getClosestPort(Rectangle bounds, Point p) {
    double centerX = bounds.getCenterX();
    double centerY = bounds.getCenterY();
    double dx = p.x - centerX;
    double dy = p.y - centerY;

    double angle = Math.atan2(dy, dx);
    double x = 0, y = 0;
    double minDistanceSq = Double.MAX_VALUE;

    if (selectedShape instanceof Oval) {
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