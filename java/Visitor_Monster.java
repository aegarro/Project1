public class Visitor_Monster extends AllFalseEntityVisitor {
    public Boolean visit(Monster m) {
        return true;
    }
}