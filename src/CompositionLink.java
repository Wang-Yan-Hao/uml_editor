// CompositionLink.java
import java.awt.image.BufferedImage;

public class CompositionLink extends Link {
    public CompositionLink(Shape startShape, Shape endShape, BufferedImage image) {
        super(startShape, endShape, image);
    }
    // draw() method is now inherited from Link
}