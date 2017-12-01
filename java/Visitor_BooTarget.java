public class Visitor_BooTarget extends AllFalseEntityVisitor {
    public Boolean visit(Ore ore) {
        return true;
    }
    public Boolean visit(Blacksmith b) {
        return true;
    }

}
