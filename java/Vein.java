import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;
public class Vein extends AbstractSchedulable{
    private String id;
    private int actionPeriod;
    private static final String ORE_ID_PREFIX = "ore -- ";
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;
    private static final int ORE_REACH = 1;
    private static final Random rand = new Random();

    public Vein(String id, Point position,
                  List<PImage> images, int actionPeriod)
    {
        super(position, images, 0);
        this.id = id;
        this.actionPeriod = actionPeriod;
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = findOpenAround(world, this.position());

        if (openPt.isPresent())
        {
            Entity ore = Create.createOre(ORE_ID_PREFIX + this.id,
                    openPt.get(), ORE_CORRUPT_MIN +
                            rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(WorldLoader.ORE_KEY));
            //ORE_KEY
            world.addEntity(ore);
            ((Schedulable)ore).scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                ActionFactory.createActivityAction(this, world, imageStore),
                this.actionPeriod);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this, ActionFactory.createActivityAction(this, world, imageStore), this.actionPeriod);
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

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }

}
