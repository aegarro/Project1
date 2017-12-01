import processing.core.PImage;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Boo extends AbstractMoveable{
    private String id;
    private int actionPeriod;
    private List<Point> nextPosList;
    private ImageStore image;


    public Boo(String id, Point position, int actionPeriod, int animationPeriod,
                   List<PImage> images){
        super(position, images, 0, animationPeriod);
        this.id = id;
        this.actionPeriod = actionPeriod;
        this.nextPosList = null;
        this.image = null;
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
            Entity occupant2 = world.getEntityOccupant(target.position());

            EntityVisitor<Boolean> bs = new Visitor_BlackSmith();

            EntityVisitor<Boolean> ore = new Visitor_Ore();

            if (occupant2.accept(ore)){
                Point ore_Pos = occupant2.position();
                scheduler.unscheduleAllEvents(occupant2);
                world.removeEntity(occupant2);
                Entity b = Create.createBoulder("boulder", ore_Pos, image.getImageList("boulder"));
                world.addEntity(b);
            }
            else if(occupant2.accept(bs)){
                Point bs_Pos = occupant2.position();
                scheduler.unscheduleAllEvents(occupant2);
                world.removeEntity(occupant2);
                Entity door = Create.createDoor("door",bs_Pos,image.getImageList("door"));
                world.addEntity(door);
                List<Point> around_door = find_around(bs_Pos);
                for(Point p: around_door){
                    if (!world.isOccupied(p)&& world.withinBounds(p)){
                        Entity boo = Create.createBoo("boo",  p, 992, 100, image.getImageList("boo"));
                        world.addEntity(boo);
                        ((Schedulable) boo).scheduleActions(scheduler, world, this.image);
                        break;
                    }
                }
            }
            ((Schedulable)this).scheduleActions(scheduler, world, this.image);

            return true;
        }
        else
        {
            this.nextPosition(world, target.position());

            if(this.nextPosList == null){
                return false;
            }

            Point nextPos = this.nextPosList.get(0);

            //if (!this.position().equals(nextPos))
            //{
                Entity e = world.getEntityOccupant(nextPos);


                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    Entity occupant2 = world.getEntityOccupant(nextPos);

                    EntityVisitor<Boolean> bs = new Visitor_BlackSmith();

                    EntityVisitor<Boolean> ore = new Visitor_Ore();

                    if (occupant2.accept(ore)){
                        Point ore_Pos = occupant2.position();
                        scheduler.unscheduleAllEvents(occupant.get());
                        world.removeEntity(occupant2);
                        Entity b = Create.createBoulder("boulder", ore_Pos, image.getImageList("boulder"));
                        world.addEntity(b);
                    }
                    else if(occupant2.accept(bs)){
                        Point bs_Pos = occupant2.position();
                        scheduler.unscheduleAllEvents(occupant.get());
                        world.removeEntity(occupant2);
                        Entity door = Create.createDoor("door",bs_Pos,image.getImageList("door"));
                        world.addEntity(door);
                        List<Point> around_door = find_around(bs_Pos);
                        for(Point p: around_door){
                            if (!world.isOccupied(p) && world.withinBounds(p)){
                                Entity boo = Create.createBoo("boo", p, 992, 100, image.getImageList("boo"));
                                world.addEntity(boo);
                                ((Schedulable) boo).scheduleActions(scheduler, world, this.image);
                                break;
                            }
                        }
                    }
                    scheduler.unscheduleAllEvents(occupant.get());
                    scheduler.scheduleEvent(this,
                            ActionFactory.createActivityAction(this, world, image),
                            this.actionPeriod);
                    return false;
                }
                world.moveEntity(this, nextPos);
                //setPosition(nextPos);
            }
            return false;
        //}
    }

    public List<Point> find_around(Point point) {
        List<Point> result = new ArrayList<>();
        result.add(new Point(point.x, point.y - 1));
        result.add(new Point(point.x, point.y + 1));
        result.add(new Point(point.x - 1, point.y));
        result.add(new Point(point.x + 1, point.y));
        return result;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        this.image = imageStore;
        Optional<Entity> fullTarget = world.findNearest(this.position(),
                new Visitor_BooTarget());
        if (fullTarget.isPresent() &&
                this.moveTo(world, fullTarget.get(), scheduler))

        {

            //this.transform(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this, ActionFactory.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
        Background b = new Background("space", imageStore.getImageList("space"));
        world.setBackground(this.position(), b);
    }


    /*private void transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        Entity Boo = Create.createBoo(this.id, this.resourceLimit,
                this.position(), this.actionPeriod, this.getAnimationPeriod(),
                this.getImage());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(Boo);
        ((Schedulable)Boo).scheduleActions(scheduler, world, imageStore);
    }*/
    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }
}

