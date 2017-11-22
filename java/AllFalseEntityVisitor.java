public class AllFalseEntityVisitor implements EntityVisitor {
    public Boolean visit(Ore ore) {
        return false;
    }

    public Boolean visit(Quake quake) {
        return false;
    }

    public Boolean visit(Vein vein) {
        return false;
    }

    public Boolean visit(MinerFull miner) {
        return false;
    }

    public Boolean visit(Obstacle obstacle) {
        return false;
    }

    public Boolean visit(Ore_Blob ore_blob) {
        return false;
    }

    public Boolean visit(MinerNotFull miner) {
        return false;
    }

    public Boolean visit(Blacksmith blacksmith) {
        return false;
    }

}
