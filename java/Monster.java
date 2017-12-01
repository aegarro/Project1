import processing.core.PImage;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Monster extends AbstractMoveable{
    private String id;
    private int actionPeriod;
    private int resourceLimit;
    private List<Point> nextPosList;


    public Monster(String id, int resourceLimit,
                     Point position, int actionPeriod, int animationPeriod,
                     List<PImage> images){
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
            EntityVisitor<Boolean> check_b = new Visitor_Boo();
            if(target.accept(check_b)) {
                world.moveEntity(this, target.position());
                world.removeEntity(target);
                scheduler.unscheduleAllEvents(target);
            }
            return true;
        }
        else
        {
            this.nextPosition(world, target.position());

            if(this.nextPosList == null){
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

    public void executePath(Point pos, WorldModel world, ImageStore imageStore){
        EntityVisitor<Vein> v = new Visitor_Vein();

        Background b = new Background("grass", imageStore.getImageList("grass"));
        world.setBackground(pos, b);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest(this.position(),
                new Visitor_Boo());

        if (fullTarget.isPresent() &&
                this.moveTo(world, fullTarget.get(), scheduler))
        {
            EntityVisitor<Boolean> boo_v = new Visitor_Boo();
            List<Entity> list_boo = VirtualWorld.entity_List(position(), boo_v, world);
            int num_boo =0;
            if(list_boo != null) {
                for (Entity b : list_boo) {
                    num_boo += 1;
                }
            }
            if(num_boo==0) {
                this.transform(world, scheduler, imageStore);
                EntityVisitor<Boolean> this_Monster = new Visitor_Monster();
                List<Entity> list_mon = VirtualWorld.entity_List(position(), boo_v, world);
                if(list_mon != null) {
                    for (Entity b : list_mon) {
                        ((Monster)b).transform(world,scheduler,imageStore);
                    }
                }
            }
        }
        else
        {
            scheduler.scheduleEvent(this, ActionFactory.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
        executePath(this.position(), world, imageStore);
    }


    private void transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        Entity Miner = Create.createMinerNotFull(this.id, this.resourceLimit,
                this.position(), this.actionPeriod, this.getAnimationPeriod(),
                imageStore.getImageList("miner"));

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(Miner);
        ((Schedulable)Miner).scheduleActions(scheduler, world, imageStore);
    }
    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }
}

