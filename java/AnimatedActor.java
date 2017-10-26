public interface AnimatedActor extends Animated, Actor{
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
    public int getAnimationPeriod();
    public void nextImage();
}
