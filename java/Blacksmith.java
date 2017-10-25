import java.util.List;
import java.util.Optional;
import java.util.Random;
import processing.core.PImage;

public class Blacksmith {

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    private static final Random rand = new Random();



    public Blacksmith(String id, Point position,
                  List<PImage> images, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public int getImageIndex(){
        return this.imageIndex;
    }

    public List<PImage> getImages(){
        return this.images;
    }

    public PImage getCurrentImage(){
        if (this instanceof Background) {
            return ((Background) this).images.get(((Background) this).imageIndex);
        }
        else if (this instanceof Entity) {
            return ((Entity) this).images.get(((Entity) this).imageIndex);
        }
        else {
            throw new UnsupportedOperationException(String.format("getCurrentImage not " +
                    "supportedfor Blacksmith"));
        }
    }

    public Point getPosition(){
        return this.position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }


    public  void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public int getAnimationPeriod() {
        throw new UnsupportedOperationException(
                String.format("getAnimationPeriod not supported for Blacksmith"));
        }


    public static Blacksmith createBlacksmith(String id, Point position,
                                          List<PImage> images)
    {
        return new Blacksmith(id, position, images, 0, 0, 0, 0);
    }

    public String executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        throw new UnsupportedOperationException(
                String.format("executeActivityAction not supported for Blacksmith"));
    }
}
