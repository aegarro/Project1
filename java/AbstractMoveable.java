import processing.core.PImage;

import java.nio.file.Path;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class AbstractMoveable extends AbstractAnimation{
    private PathingStrategy strategy;

        protected AbstractMoveable(Point position, List<PImage> images, int imageIndex, int animationPeriod)
        {
            super(position, images, imageIndex, animationPeriod);
            this.strategy = new A_star();
        }

    //public abstract Point nextPosition(WorldModel world, Point destPos);
    public abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);
    public PathingStrategy getStrategy() {
        return this.strategy;
    }

// pass a new SingleStepPathingStrategy
}

