public class Visitor_MonsterTarget extends AllFalseEntityVisitor {
    public Boolean visit(Boo boo) {
        return true;
    }
    public Boolean visit(Door door) {
        return true;
    }
    public Boolean visit(Boulder boulder) { return true;}
}