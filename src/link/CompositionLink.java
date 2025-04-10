// CompositionLink.java
package link;
import link.Link;

import java.awt.image.BufferedImage;

import shape.Shape;
import shape.Rect;
import shape.Oval;


public class CompositionLink extends Link {
    public CompositionLink(Shape startShape, Shape endShape, BufferedImage image) {
        super(startShape, endShape, image);
    }
    // draw() method is now inherited from Link
}