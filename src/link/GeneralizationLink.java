// GeneralizationLink.java
package link;
import link.Link;

import java.awt.image.BufferedImage;

import shape.Shape;
import shape.Rect;
import shape.Oval;

public class GeneralizationLink extends Link {
    public GeneralizationLink(Shape startShape, Shape endShape, BufferedImage image) {
        super(startShape, endShape, image);
    }
    // draw() method is now inherited from Link
}