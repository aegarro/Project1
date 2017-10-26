import processing.core.PImage;

import java.util.List;

public class MinerFull implements Entity, Schedulable, AnimatedActor{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;
    private int animationPeriod;
    private int resourceLimit;

    public MinerFull(String id, int resourceLimit,
                     Point position, int actionPeriod, int animationPeriod,
                     List<PImage> images){
        this.id = id;
        this.resourceLimit = resourceLimit;
        this.position = position;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.images = images;
    }

    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex);
    }

    public Point position(){
        return this.position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getAnimationPeriod() {
        return this.animationPeriod;
    }

    public  void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }


    public void executeMinerFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(this.position,
                EntityKind.BLACKSMITH);

        if (fullTarget.isPresent() &&
                this.moveToFull(world, fullTarget.get(), scheduler)) {
            this.transformFull(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0),
                this.getAnimationPeriod());
    }

    private boolean adjacent(Point p2)
    {
        return (this.position.x == p2.x && Math.abs(this.position.y - p2.y) == 1) ||
                (this.position.y == p2.y && Math.abs(this.position.x - p2.x) == 1);
    }


    private boolean moveToFull(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (this.adjacent(target.position))
        {
            return true;
        }
        else
        {
            Point nextPos = this.nextPositionMiner(world, target.position);

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
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest(this.position,
                EntityKind.BLACKSMITH);

        if (fullTarget.isPresent() &&
                this.moveToFull(world, fullTarget.get(), scheduler))
        {
            this.transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }

}
