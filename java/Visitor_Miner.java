public class Visitor_Miner extends AllFalseEntityVisitor{
    public Boolean visit(MinerFull miner) {
        return true;
    }
    public Boolean visit(MinerNotFull miner) {
        return true;
    }

}
