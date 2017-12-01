import java.util.List;

import processing.core.PImage;
public class Boulder extends AbstractEntity {
    private String id;

    public Boulder(String id, Point position,
                 List<PImage> images)
    {
        super(position, images, 0);
        this.id = id;
    }

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }
}