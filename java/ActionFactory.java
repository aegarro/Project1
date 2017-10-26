public class ActionFactory {

    //private Entity entity;
    //private WorldModel world;
    //private ImageStore imageStore;

    /*public ActionFactory(Entity entity, WorldModel world,
                         ImageStore imageStore) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }*/


    public static Action createActivityAction(Actor entity, WorldModel world,
                                              ImageStore imageStore) {
        return new ActivityAction(entity, world, imageStore);
    }

    public static Action createAnimationAction(Animated entity, int repeatCount) {
        return new AnimationAction(entity, null, null, repeatCount);
    }
}
