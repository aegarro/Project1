public interface EntityVisitor<R> {
    R visit(Ore ore);
    R visit(Vein vein);
    R visit(Quake quake);
    R visit(Ore_Blob ore_blob);
    R visit(MinerFull miner);
    R visit(MinerNotFull miner);
    R visit(Blacksmith blacksmith);
    R visit(Obstacle obstacle);
    R visit(Monster monster);
    R visit(Boo boo);
    R visit(Space space);
    R visit(Door door);
    R visit(Boulder door);





}
