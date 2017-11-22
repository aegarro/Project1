import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Location{

    private int distStart;
    private int h_Dist;
    private int totalD;
    private Point currentP;
    private Point priorP;


    public Location(Point currentP, int distStart, int h_Dist, int totalD, Point priorP){

        this.currentP = currentP;
        this.distStart = distStart;
        this.h_Dist = h_Dist;
        this.totalD = totalD;
        this.priorP = priorP;
    }

    public int getDist() {
        return this.distStart;
    }

    public void setG(int g){
        this.distStart = g;
    }

    public void setF(){
        this.totalD = this.distStart + this.h_Dist;
    }

    public int get_H_dist(){
        return this.h_Dist;
    }
    public int get_total_d(){
        return this.totalD;
    }

    public Point get_prior(){
        return this.priorP;
    }

    public Point get_currentP(){
        return this.currentP;
    }

    public static Location createLoc(Point pos, Point desPos, int distStart, Point prior){
        int h = Math.abs(pos.x-desPos.x) + Math.abs(pos.y - desPos.y);
        int f = distStart + h;
        return new Location(pos, distStart, h, f, prior);
    }

}

