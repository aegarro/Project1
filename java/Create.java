import processing.core.PImage;

import java.util.List;

public class Create {

    public Create();

    public static Ore createOre(String id, Point position, int actionPeriod,
                                List<PImage> images) {
        return new Ore(id, position, images, actionPeriod);
    }

    public static Blacksmith createBlacksmith(String id, Point position,
                                              List<PImage> images)
    {
        return new Blacksmith(id, position, images);
    }

    public static Obstacle createObstacle(String id, Point position,
                                          List<PImage> images)
    {
        return new Obstacle(id, position, images,
                0, 0, 0, 0);
    }
    public static Vein createVein(String id, Point position, int actionPeriod,
                                  List<PImage> images)
    {
        return new Vein(id, position, images, actionPeriod);
    }

    public static Entity createQuake(Point position, List<PImage> images)
    {
        return new Quake("quake", position, images, 1100, 100);
    }

    public static Ore_Blob createOreBlob(String id, Point position,
                                         int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new Ore_Blob(id, position, images, actionPeriod, animationPeriod);
    }





}
