import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;
public class Quake extends AbstractAnimation{
    private String id;
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
    private static final String QUAKE_ID = "quake";
    private int actionPeriod = 1100;
    //private static final int QUAKE_ACTION_PERIOD = 1100;
    //private static final int QUAKE_ANIMATION_PERIOD = 100;


    public Quake(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(position, images, 0, animationPeriod);
        this.id = id;
        this.actionPeriod = 1100;
    }



    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }


    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this, ActionFactory.createActivityAction(this, world, imageStore), this.actionPeriod);
        scheduler.scheduleEvent(this, ActionFactory.createAnimationAction(this, QUAKE_ANIMATION_REPEAT_COUNT),
                        this.getAnimationPeriod());

    }




}
