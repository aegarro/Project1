import processing.core.PImage;

import java.util.List;

public class Create {

    public static Entity createOre(String id, Point position, int actionPeriod,
                                List<PImage> images) {
        return new Ore(id, position, images, actionPeriod);
    }

    public static Entity createBlacksmith(String id, Point position,
                                              List<PImage> images)
    {
        return new Blacksmith(id, position, images);
    }

    public static Entity createObstacle(String id, Point position,
                                          List<PImage> images)
    {
        return new Obstacle(id, position, images);
    }
    public static Entity createVein(String id, Point position, int actionPeriod,
                                  List<PImage> images)
    {
        return new Vein(id, position, images, actionPeriod);
    }

    public static Entity createQuake(Point position, List<PImage> images)
    {
        return new Quake("quake", position, images, 1100, 100);
    }

    public static Entity createOreBlob(String id, Point position,
                                         int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new Ore_Blob(id, position, images, actionPeriod, animationPeriod);
    }

    public static Entity createMinerFull(String id, int resourceLimit,
                                         Point position, int actionPeriod, int animationPeriod,
                                         List<PImage> images)
    {
        return new MinerFull(id,resourceLimit, position,
                actionPeriod, animationPeriod, images);
    }

    public static Entity createMinerNotFull(String id, int resourceLimit,
                                            Point position, int actionPeriod, int animationPeriod,
                                            List<PImage> images)
    {
        return new MinerNotFull(id, resourceLimit, position, actionPeriod, animationPeriod, images);
    }





}
