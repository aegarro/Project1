import processing.core.PImage;

import java.util.List;

public abstract class AbstractSchedulable extends AbstractEntity implements Schedulable{


    protected AbstractSchedulable(Point position, List<PImage> images, int imageIndex){
        super(position, images, imageIndex);
    }

    public abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

}
