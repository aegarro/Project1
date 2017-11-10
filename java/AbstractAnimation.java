import processing.core.PImage;

import java.util.List;

public abstract class AbstractAnimation extends AbstractSchedulable {

    private int animationPeriod;


    protected AbstractAnimation(Point position, List<PImage> images, int imageIndex, int animationPeriod){
            super(position, images, imageIndex);
            this.animationPeriod = animationPeriod;
        }

    public void nextImage()
    {
        this.setImageIndex((this.getImageIndex() + 1) % this.getImage().size());
    }

    public int getAnimationPeriod()
    {
        return this.animationPeriod;
    }

    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

}
