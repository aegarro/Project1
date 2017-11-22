import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class A_star implements PathingStrategy{
    public List<Point> computePath(Point start, Point end,
                            Predicate<Point> canPassThrough,
                            BiPredicate<Point, Point> withinReach,
                            Function<Point, Stream<Point>> potentialNeighbors) {

        List<Location> OpenList = new ArrayList<>();
        List<Location> other_OpenList = new ArrayList<>();

        List<Location> ClosedList = new ArrayList<>();
        List<Point> result = null;

        Boolean already_found = false;
        Point prior = start;
        Location firstP = new Location(start, 0, 0, 0, null);
        Location currentP = firstP;
        OpenList.add(firstP);
        int g = 1;

        while (!currentP.get_currentP().equals(end)) {
            List<Point> around =
                    potentialNeighbors.apply(currentP.get_currentP()) //get Neighbors surrounding current point
                            .filter(canPassThrough)
                            .collect(Collectors.toList()); //make Stream into List
            //filter which points can go to next

            other_OpenList = new ArrayList<>();

            for(Location l : OpenList){
                other_OpenList.add(l);
            }

            for (Point p : around) {
                if (p != null) {
                    if (withinReach.test(p, end)) {       //check if next to the goal
                        result = new ArrayList<>();
                        if (start.equals(prior)){
                            result.add(end);
                            return result;
                        }
                        result = new ArrayList<>();
                        Location newP = Location.createLoc(p, end, g, prior);
                        result.add(p);
                        //copy closed list
                        List<Location> other_ClosedList = new ArrayList<>();
                        for (Location l : ClosedList) {
                            other_ClosedList.add(l);
                        }

                        while (prior != null) {
                            for (Location l : other_ClosedList) {
                                if (l.get_currentP().equals(prior)) {
                                    result.add(l.get_currentP());
                                    prior = l.get_prior();
                                    //ClosedList.remove(l);
                                }
                            }
                        }
                        Collections.reverse(result);
                        result.add(end);
                        result.remove(0);
                        if (result.isEmpty()){
                            result = null;
                        }

                        return result;
                    }
                    else {
                        // check if it is in the open List
                        // also check if in closed list
                        Location current_l = Location.createLoc(p, end, g, prior);

                        for (Location other_L : other_OpenList) {
                            if (other_L != null) {
                                if (other_L.get_currentP().equals(p)) {
                                    already_found = true;
                                    if (other_L.getDist() > current_l.getDist()) {
                                        other_L.setG(current_l.getDist());  //change to better G
                                        other_L.setF(); //update F
                                    }
                                }
                            }
                        }

                        for (Location other_L2 : ClosedList) {
                            if (other_L2.get_currentP().equals(p)) {
                                already_found = true;
                            }
                        }

                        if (already_found == false) {
                            OpenList.add(current_l);
                        }
                        already_found = false;
                    }
                }
            }

            //find next position to put on close list
            Location lowest_f = null;
            for (Location find_next : OpenList) {
                if (find_next != null) {
                    if (lowest_f == null) {
                        lowest_f = find_next;
                    }
                    else {
                        if (lowest_f.get_total_d() > find_next.get_total_d()) {
                            lowest_f = find_next;
                        }
                    }
                }
            }
            prior = lowest_f.get_currentP();
            ClosedList.add(lowest_f);
            OpenList.remove(lowest_f);
            g = lowest_f.getDist() + 1;
            currentP = lowest_f;
        }
        return result;
    }
}
