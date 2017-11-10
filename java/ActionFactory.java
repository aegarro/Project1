public class ActionFactory {



    public static Action createActivityAction(AbstractSchedulable entity, WorldModel world,
                                              ImageStore imageStore) {
        return new ActivityAction(entity, world, imageStore);
    }

    public static Action createAnimationAction(AbstractAnimation entity, int repeatCount) {
        return new AnimationAction(entity, repeatCount);
    }
}
