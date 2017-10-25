import processing.core.PImage;
import java.util.List;
import java.util.Optional;


public interface Entity {
    public void setPosition(Point position);
    //public Point position();
    public Point getPosition();
    public PImage getCurrentImage();
    public List<PImage> getImages();
    public int getImageIndex();
    public int getAnimationPeriod();
    public void nextImage();
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}
