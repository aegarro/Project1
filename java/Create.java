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


    public static Entity createMonster(String id, int resourceLimit,
                                            Point position, int actionPeriod, int animationPeriod,
                                            List<PImage> images)
    {
        return new Monster("Monster", resourceLimit, position, actionPeriod, animationPeriod, images);
    }

    public static Entity createSpace(String id, Point position,
                                        List<PImage> images)
    {
        return new Space("space", position, images);
    }

    public static Entity createBoulder(String id, Point position,
                                     List<PImage> images)
    {
        return new Boulder("boulder", position, images);
    }

    public static Entity createDoor(String id, Point position,
                                          List<PImage> images)
    {
        return new Door("door", position, images);
    }
    public static Entity createBoo(String id,
                                       Point position, int actionPeriod, int animationPeriod,
                                       List<PImage> images)
    {
        return new Boo("boo", position, actionPeriod, animationPeriod, images);
    }
}
