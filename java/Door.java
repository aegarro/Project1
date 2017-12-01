import java.util.List;

import processing.core.PImage;
public class Door extends AbstractEntity {
    private String id;

    public Door(String id, Point position,
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
