public class Visitor_Schedulable extends AllFalseEntityVisitor {

    public Boolean visit(Ore ore) {
        return true;
    }

    public Boolean visit(Quake quake) {
        return true;
    }

    public Boolean visit(Vein vein) {
        return true;
    }

    public Boolean visit(MinerFull miner) {
        return true;
    }

    public Boolean visit(Ore_Blob ore_blob) {
        return true;
    }

    public Boolean visit(MinerNotFull miner) {
        return true;
    }
}
