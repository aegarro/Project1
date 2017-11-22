import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;
public class Ore_Blob extends AbstractMoveable{
    private String id;
    private int actionPeriod;


    public Ore_Blob(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(position, images, 0, animationPeriod);
        this.id = id;
        this.actionPeriod = actionPeriod;
    }


    public Point nextPosition(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - position().x);
        Point newPos = new Point(position().x + horiz,
                position().y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !((occupant.get()) instanceof Ore)))
        {
            int vert = Integer.signum(destPos.y - position().y);
            newPos = new Point(position().x, position().y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !((occupant.get()) instanceof Ore)))
            {
                newPos = position();
            }
        }

        return newPos;
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> blobTarget = world.findNearest(
                this.position(), new Visitor_Vein());
        long nextPeriod = this.actionPeriod;

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().position();

            if (this.moveTo(world, blobTarget.get(), scheduler))
            {
                Entity quake = (Quake)(Create.createQuake(tgtPos,
                        imageStore.getImageList(WorldLoader.QUAKE_KEY)));

                world.addEntity(quake);
                nextPeriod += this.actionPeriod;
                ((Quake)quake).scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                ActionFactory.createActivityAction(this, world, imageStore),
                nextPeriod);
    }


    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
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

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this, ActionFactory.createActivityAction(this, world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this, ActionFactory.createAnimationAction(this, 0), this.getAnimationPeriod());

    }
    private boolean adjacent(Point p2)
    {
        return (this.position().x == p2.x && Math.abs(this.position().y - p2.y) == 1) ||
                (this.position().y == p2.y && Math.abs(this.position().x - p2.x) == 1);
    }

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }


}
