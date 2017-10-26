public class ActivityAction implements Action{
    private Actor entity;
    private WorldModel world;
    private ImageStore imageStore;
    //private int repeatCount;

    public ActivityAction(Actor entity, WorldModel world,
                    ImageStore imageStore){
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        //this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler){
        entity.executeActivity(world, imageStore, scheduler);
    }


    /*public static Activity createAction(Entity entity, WorldModel world,
                                              ImageStore imageStore)
    {
        return new Activity(entity, world, imageStore, 0);
    }*/

}
