import java.util.List;
import java.util.Random;

import processing.core.PImage;
public class Ore extends AbstractSchedulable{
    private String id;
    private int actionPeriod;
    private static final String BLOB_ID_SUFFIX = " -- blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;
    private static final Random rand = new Random();



    public Ore(String id, Point position, List<PImage> images, int actionPeriod) {
        super(position, images, 0);
        this.id = id;
        this.actionPeriod = actionPeriod;
    }

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Point pos = this.position();  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Entity blob = Create.createOreBlob(this.id + BLOB_ID_SUFFIX,
                pos, this.actionPeriod / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN +
                        rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN),
                imageStore.getImageList(WorldLoader.BLOB_KEY));
                //BLOB_KEY
        world.addEntity(blob);
        ((Schedulable)blob).scheduleActions(scheduler, world, imageStore);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, ActionFactory.createActivityAction(this, world, imageStore), this.actionPeriod);

    }

}

