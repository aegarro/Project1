import processing.core.PImage;
import java.util.Optional;

import java.util.List;

public class MinerNotFull extends AbstractMoveable {
    private String id;
    private int actionPeriod;
    private int resourceLimit;
    private int resourceCount;

    public MinerNotFull(String id, int resourceLimit, Point position, int actionPeriod,
                              int animationPeriod, List<PImage> images){
        super(position, images, 0, animationPeriod);
        this.id = id;
        this.resourceLimit = resourceLimit;
        this.actionPeriod = actionPeriod;
    }


    public Point nextPosition(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.position().x);
        Point newPos = new Point(this.position().x + horiz,
                this.position().y);

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.y - this.position().y);
            newPos = new Point(this.position().x,
                    this.position().y + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = this.position();
            }
        }

        return newPos;
    }


    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, ActionFactory.createActivityAction(this, world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this, ActionFactory.createAnimationAction(this, 0), this.getAnimationPeriod());
    }

    private boolean adjacent(Point p2)
    {
        return (this.position().x == p2.x && Math.abs(this.position().y - p2.y) == 1) ||
                (this.position().y == p2.y && Math.abs(this.position().x - p2.x) == 1);
    }



    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.adjacent(target.position())) {
            this.resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.position());

            if (!this.position().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest(this.position(),
                Ore.class);

        if (!notFullTarget.isPresent() ||
                !this.moveTo(world, notFullTarget.get(), scheduler) ||
                !this.transform(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    ActionFactory.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    private boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit)
        {
            Entity miner = Create.createMinerFull(this.id, this.resourceLimit,
                    this.position(), this.actionPeriod, this.getAnimationPeriod(),
                    this.getImage());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            ((Schedulable)miner).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
}
