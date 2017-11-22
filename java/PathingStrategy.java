import java.util.stream.*;
import java.util.*;
import java.util.function.*;

interface PathingStrategy
{
    /*
     * Returns a prefix of a path from the start point to a point within reach
     * of the end point.  This path is only valid ("clear") when returned, but
     * may be invalidated by movement of other entities.
     *
     * The prefix includes neither the start point nor the end point.
     */
    List<Point> computePath(Point start, Point end,
                            Predicate<Point> canPassThrough,
                            BiPredicate<Point, Point> withinReach,
                            Function<Point, Stream<Point>> potentialNeighbors);


    BiPredicate<Point, Point> withinReach = (Point p1, Point p2) -> (Math.abs(p2.x-p1.x)<=1 && p2.y == p1.y) ||
                                                                    (Math.abs(p2.y-p1.y)<=1 && p2.x == p1.x);
    //checks if goal is within reach where P2 is the goal


    static final Function<Point, Stream<Point>> CARDINAL_NEIGHBORS =
            point ->
                    Stream.<Point>builder()
                            .add(new Point(point.x, point.y - 1))
                            .add(new Point(point.x, point.y + 1))
                            .add(new Point(point.x - 1, point.y))
                            .add(new Point(point.x + 1, point.y))
                            .build();
}
