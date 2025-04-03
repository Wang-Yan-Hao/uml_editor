// AssociationLink.java
import java.awt.image.BufferedImage;

public class AssociationLink extends Link {
    public AssociationLink(Shape startShape, Shape endShape, BufferedImage image) {
        super(startShape, endShape, image);
    }
    // draw() method is now inherited from Link
}
