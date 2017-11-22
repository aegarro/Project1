import processing.core.PImage;
import java.util.Optional;

import java.util.List;
import java.util.function.Predicate;

public class MinerNotFull extends AbstractMoveable {
    private String id;
    private int actionPeriod;
    private int resourceLimit;
    private int resourceCount;
    private List<Point> nextPosList;

    public MinerNotFull(String id, int resourceLimit, Point position, int actionPeriod,
                              int animationPeriod, List<PImage> images){
        super(position, images, 0, animationPeriod);
        this.id = id;
        this.resourceLimit = resourceLimit;
        this.actionPeriod = actionPeriod;
        this.nextPosList = null;
    }

    public void nextPosition(WorldModel world, Point destPos){

        Predicate<Point> canPassThrough = Point ->  !world.isOccupied(Point);  //check if obstacle here;
        this.nextPosList = this.getStrategy().computePath(this.position(), destPos, canPassThrough, PathingStrategy.withinReach, PathingStrategy.CARDINAL_NEIGHBORS);
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
            EntityVisitor<Boolean> check_O = new Visitor_Ore();

            if(target.accept(check_O)) {
                world.moveEntity(this, target.position());
            }
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }

        else {
            nextPosition(world, target.position());

            if (this.nextPosList== null){
                return false;
            }

            Point nextPos = this.nextPosList.get(0);
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
                new Visitor_Ore());

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

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }
}
