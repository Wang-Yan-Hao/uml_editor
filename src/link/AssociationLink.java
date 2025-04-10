// AssociationLink.java
package link;
import link.Link;
import java.awt.image.BufferedImage;

import shape.Shape;

public class AssociationLink extends Link {
    public AssociationLink(Shape startShape, Shape endShape, BufferedImage image) {
        super(startShape, endShape, image);
    }
    // draw() method is now inherited from Link
}
