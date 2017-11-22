import processing.core.PImage;

import java.util.List;

public abstract class AbstractEntity implements Entity{
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    protected AbstractEntity(Point position, List<PImage> images, int imageIndex){
        this.position = position;
        this.images = images;
        this.imageIndex = imageIndex;
    }

    public Point position(){
        return this.position;
    }

    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex);
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public List<PImage> getImage() {
        return this.images;
    }

    public int getImageIndex() {
        return this.imageIndex;
    }

    public void setImageIndex(int i){
        this.imageIndex = i;
    }

    public abstract <R> R accept(EntityVisitor<R> visitor);

}
