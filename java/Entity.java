import processing.core.PImage;
import java.util.List;
import java.util.Optional;


public interface Entity {
    public void setPosition(Point position);
    public Point position();
    public PImage getCurrentImage();
    public int getAnimationPeriod();
    public void nextImage();
    public void scheduleActions();
    public void executeActivity();
}
