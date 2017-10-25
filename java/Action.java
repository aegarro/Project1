public interface Action

{
   public Action createAction();
   public void executeAction(EventScheduler scheduler);
}
