public class ActionFactory {

    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;

    /*public ActionFactory(Entity entity, WorldModel world,
                         ImageStore imageStore) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }*/


    public static Action createActivityAction(Entity entity, WorldModel world,
                                              ImageStore imageStore) {
        return new ActivityAction(entity, world, imageStore, 0);
    }
}
