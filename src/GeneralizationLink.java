// GeneralizationLink.java
import java.awt.image.BufferedImage;

public class GeneralizationLink extends Link {
    public GeneralizationLink(Shape startShape, Shape endShape, BufferedImage image) {
        super(startShape, endShape, image);
    }
    // draw() method is now inherited from Link
}