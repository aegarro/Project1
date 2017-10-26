import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;
public class Ore_Blob  implements Entity, Schedulable, AnimatedActor{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;
    private int animationPeriod;

    /*private static final String BLOB_ID_SUFFIX = " -- blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;*/

    public Ore_Blob(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex);
    }

    /*public PImage getCurrentImage(){
        if (this instanceof Background) {
            return ((Background)this).images.get(((Background)this).imageIndex);
        }
        else if (this instanceof Entity) {
            return ((Entity)this).images.get(((Entity) this).imageIndex);
        }
        else {
            throw new UnsupportedOperationException(String.format("getCurrentImage not " +
                    "supported for Ore_Blob"));
        }
    }*/


    public Point position(){
        return this.position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }


    public Point nextPosition(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - position.x);
        Point newPos = new Point(position.x + horiz,
                position.y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get().kind == EntityKind.ORE)))
        {
            int vert = Integer.signum(destPos.y - position.y);
            newPos = new Point(position.x, position.y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get().kind == EntityKind.ORE)))
            {
                newPos = position;
            }
        }

        return newPos;
    }


    public  void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public int getAnimationPeriod()

    {
        return this.animationPeriod;
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> blobTarget = world.findNearest(
                this.position, EntityKind.VEIN);
        long nextPeriod = this.actionPeriod;

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.position();

            if (this.moveTo(world, blobTarget.get(), scheduler))
            {
                Quake quake = createQuake(tgtPos,
                        imageStore.getImageList(Functions.QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.actionPeriod;
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                Action.createActivityAction(this, world, imageStore),
                nextPeriod);
    }


    private boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (this.adjacent(target.position()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = this.nextPosition(world,target.position());

            if (!this.position.equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), this.getAnimationPeriod());

    }
    private boolean adjacent(Point p2)
    {
        return (this.position.x == p2.x && Math.abs(this.position.y - p2.y) == 1) ||
                (this.position.y == p2.y && Math.abs(this.position.x - p2.x) == 1);
    }


}
