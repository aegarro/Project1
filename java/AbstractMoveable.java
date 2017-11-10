import processing.core.PImage;

import java.util.List;

public abstract class AbstractMoveable extends AbstractAnimation {

        protected AbstractMoveable(Point position, List<PImage> images, int imageIndex, int animationPeriod)
        {
            super(position, images, imageIndex, animationPeriod);
        }

    public abstract Point nextPosition(WorldModel world, Point destPos);
    public abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

}

