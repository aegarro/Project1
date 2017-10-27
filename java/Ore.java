import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;
public class Ore implements Entity, Actor, Schedulable {
    private String id;
    private Point position;
    private List<PImage> images;
    //private int resourceLimit;
    //private int resourceCount;
    private int actionPeriod;
    //private int animationPeriod;
    private static final String BLOB_ID_SUFFIX = " -- blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;
    private static final Random rand = new Random();



    public Ore(String id, Point position, List<PImage> images, int actionPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        //this.resourceLimit = resourceLimit;
        //this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        //this.animationPeriod = animationPeriod;
    }

    public PImage getCurrentImage() {
        return this.images.get(0);
    }

    /*public PImage getCurrentImage() {
        if (this instanceof Background) {
            return ((Background) this).images.get(((Background)this).imageIndex);
        }
        else if (this instanceof Entity) {
            return ((Entity) this).images.get(((Entity) this).imageIndex);
        }
        else {
            throw new UnsupportedOperationException(String.format("getCurrentImage not " +
                    "supported for Ore"));
        }
    }*/


    public List<PImage> getImages() {
        return this.images;
    }

    public Point position() {
        return this.position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Point pos = this.position;  // store current position before removing

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

