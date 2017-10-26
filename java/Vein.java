import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;
public class Vein implements Entity, Actor, Schedulable{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;
    private static final String ORE_ID_PREFIX = "ore -- ";
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;
    private static final int ORE_REACH = 1;
    private static final Random rand = new Random();

    public Vein(String id, Point position,
                  List<PImage> images, int actionPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
    }

    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex);
    }

    /*public PImage getCurrentImage(){
        if (this instanceof Background) {
            return ((Background)this).images.get(((Background)this).imageIndex);
        }
        else if (this instanceof Entity) {
            return ((Entity)this).images.get(((Entity)this).imageIndex);
        }
        else {
            throw new UnsupportedOperationException(String.format("getCurrentImage not " +
                    "supported for Vein"));
        }
    }*/

    public int getImageIndex(){
        return this.imageIndex;
    }

    /*public List<PImage> getImages(){
        return this.images;
    }*/

    public Point position(){
        return this.position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    /*public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }*/


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = findOpenAround(world, this.position);

        if (openPt.isPresent())
        {
            Ore ore = createOre(ORE_ID_PREFIX + this.id,
                    openPt.get(), ORE_CORRUPT_MIN +
                            rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(Functions.ORE_KEY));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                Action.createActivityAction(this, world, imageStore),
                this.actionPeriod);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
    }

    public Optional<Point> findOpenAround(WorldModel world, Point pos)
    {
        for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++)
        {
            for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++)
            {
                Point newPt = new Point(pos.x + dx, pos.y + dy);
                if (world.withinBounds(newPt) &&
                        !world.isOccupied(newPt))
                {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
    }
}
