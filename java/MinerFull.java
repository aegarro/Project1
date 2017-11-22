import processing.core.PImage;
import java.util.Optional;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MinerFull extends AbstractMoveable{
    private String id;
    private int actionPeriod;
    private int resourceLimit;
    private List<Point> nextPosList;


    public MinerFull(String id, int resourceLimit,
                     Point position, int actionPeriod, int animationPeriod,
                     List<PImage> images){
        super(position, images, 0, animationPeriod);
        this.id = id;
        this.resourceLimit = resourceLimit;
        this.actionPeriod = actionPeriod;
        this.nextPosList = null;
    }

    /*public Point nextPosition(WorldModel world, Point destPos)
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
    }*/

    public void nextPosition(WorldModel world, Point destPos){

        Predicate<Point> canPassThrough = Point ->  !world.isOccupied(Point);  //check if obstacle here;

        this.nextPosList = this.getStrategy().computePath(this.position(), destPos, canPassThrough, PathingStrategy.withinReach, PathingStrategy.CARDINAL_NEIGHBORS);
    }


    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, ActionFactory.createActivityAction(this, world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this, ActionFactory.createAnimationAction(this, 0),
                this.getAnimationPeriod());
    }

    private boolean adjacent(Point p2)
    {
        return (this.position().x == p2.x && Math.abs(this.position().y - p2.y) == 1) ||
                (this.position().y == p2.y && Math.abs(this.position().x - p2.x) == 1);
    }


    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (this.adjacent(target.position()))
        {
            //world.moveEntity(this, target.position());
            return true;
        }
        else
        {
            this.nextPosition(world, target.position());

            if(this.nextPosList.size() == 0){
                return false;
            }

            Point nextPos = this.nextPosList.get(0);

            if (!this.position().equals(nextPos))
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
        Optional<Entity> fullTarget = world.findNearest(this.position(),
                new Visitor_BlackSmith());
        if (fullTarget.isPresent() &&
                ////
                this.moveTo(world, fullTarget.get(), scheduler))
        {
            this.transform(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this, ActionFactory.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }


    private void transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        Entity miner = Create.createMinerNotFull(this.id, this.resourceLimit,
                this.position(), this.actionPeriod, this.getAnimationPeriod(),
                this.getImage());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        ((Schedulable)miner).scheduleActions(scheduler, world, imageStore);
    }
    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }
}
