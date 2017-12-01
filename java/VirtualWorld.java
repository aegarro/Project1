import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import processing.core.*;

public final class VirtualWorld
   extends PApplet
{
   private static final int TIMER_ACTION_PERIOD = 100;
   private static Point tile_tracker = new Point(0,0);
   //20 15 down

   private static int M_ID_tracker = 1;
   private static int S_ID_tracker = 1;
   private static int B_ID_tracker = 1;
   private static int clicks = 0;
   private static int num_mon = 0;


   private static final int VIEW_WIDTH = 640;
   private static final int VIEW_HEIGHT = 480;
   private static final int TILE_WIDTH = 32;
   private static final int TILE_HEIGHT = 32;
   private static final int WORLD_WIDTH_SCALE = 2;
   private static final int WORLD_HEIGHT_SCALE = 2;

   private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   private static final String IMAGE_LIST_FILE_NAME = "imagelist";
   private static final String DEFAULT_IMAGE_NAME = "background_default";
   private static final int DEFAULT_IMAGE_COLOR = 0x808080;

   private static final String LOAD_FILE_NAME = "gaia.sav";

   private static final String FAST_FLAG = "-fast";
   private static final String FASTER_FLAG = "-faster";
   private static final String FASTEST_FLAG = "-fastest";
   private static final double FAST_SCALE = 0.5;
   private static final double FASTER_SCALE = 0.25;
   private static final double FASTEST_SCALE = 0.10;

   private static double timeScale = 1.0;

   private ImageStore imageStore;
   private WorldModel world;
   private WorldView view;
   private EventScheduler scheduler;

   private long next_time;


   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      this.imageStore = new ImageStore(
         createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
         createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
         TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME, imageStore);

      scheduleActions(world, scheduler, imageStore);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
   }

   public void draw()
   {
      long time = System.currentTimeMillis();
      if (time >= next_time)
      {
         this.scheduler.updateOnTime(time);
         next_time = time + TIMER_ACTION_PERIOD;
      }

      view.drawViewport();
   }

   public void keyPressed()
   {
      if (key == CODED)
      {
         int dx = 0;
         int dy = 0;

         switch (keyCode)
         {
            case UP:
               dy = -1;
               break;
            case DOWN:
               dy = 1;
               break;
            case LEFT:
               dx = -1;
               break;
            case RIGHT:
               dx = 1;
               break;
         }
         tile_tracker.x += Point.check_bounds(tile_tracker,dx,dy).x;
         tile_tracker.y += Point.check_bounds(tile_tracker,dx,dy).y;
         view.shiftView(dx, dy);
      }
   }

   public void mouseClicked() {
      Point tile = new Point(mouseX / 32 + tile_tracker.x, tile_tracker.y + mouseY / 32);

      String M_ID = "monster" + M_ID_tracker;
      String S_ID = "space" + S_ID_tracker;

      EntityVisitor<Boolean> boo_v = new Visitor_Boo();
      List<Entity> list_boo = entity_List(tile, boo_v, world);
      int num_boo =0;
      if(list_boo != null) {
         for (Entity b : list_boo) {
            num_boo += 1;
         }
      }

      if (num_boo == 0) {
         String B_ID = "boo" + B_ID_tracker;
         Entity boo = Create.createBoo(M_ID, tile, 992, 100, imageStore.getImageList("boo"));
         ((Schedulable) boo).scheduleActions(scheduler, world, this.imageStore);
         world.addEntity(boo);
      /*Entity monster = Create.createMonster(M_ID, 3, tile, 992, 100, imageStore.getImageList("monster"));
      world.tryAddEntity(monster);
      //not check 4 sides for
      ((Schedulable)monster).scheduleActions(scheduler, world, this.imageStore);
      M_ID_tracker +=1;
      S_ID_tracker +=1;*/

         List<Point> around = new LinkedList<>();
         around.add(tile);
         around.add(new Point(tile.x + 1, tile.y));
         around.add(new Point(tile.x - 1, tile.y));
         around.add(new Point(tile.x, tile.y + 1));
         around.add(new Point(tile.x, tile.y - 1));
         around.add(new Point(tile.x + 1, tile.y + 1));
         around.add(new Point(tile.x + 1, tile.y - 1));
         around.add(new Point(tile.x - 1, tile.y - 1));
         around.add(new Point(tile.x - 1, tile.y + 1));

         Background b = new Background("space", imageStore.getImageList("space"));
         int check = 0;
         for (Point i : around) {
            if (check == 0 & !world.isOccupied(i)) {
               Entity door = Create.createDoor("door", i, imageStore.getImageList("door"));
               world.addEntity(door);
               check = 1;
            }
            world.setBackground(i, b);
         }
         clicks = 1;


         //EntityVisitor<Boolean> kind = new Visitor_BlackSmith();
         EntityVisitor<Boolean> miner = new Visitor_Miner();
         //EntityVisitor<Boolean> monstr = new Visitor_Monster();
         //num_mon =0;
         //List<Entity> list_mons = check_Entity(tile, monstr, world);

      /*if(list_mons != null){
         for (Entity m : list_mons) {
            num_mon += 1;
         }
      }
      if (num_mon < 3) {*/
         List<Entity> list_M = check_Entity(tile, miner, world);
         if (list_M != null) {
            for (Entity m : list_M) {
               if (m != null) {
                  Point entity_p = m.position();
                  world.removeEntity(m);
                  scheduler.unscheduleAllEvents(m);
                  Entity mon = Create.createMonster(M_ID, 4, entity_p, 992, 100, imageStore.getImageList("monster"));
                  world.tryAddEntity(mon);
                  ((Schedulable) mon).scheduleActions(scheduler, world, this.imageStore);
               }
            }
         }
      }
   }


      /*List<Entity> list_B = check_Entity(tile, kind, world);
      if (list_B != null){
         for(Entity black_S : list_B) {
            Point entity_p = black_S.position();
            world.removeEntity(black_S);
            scheduler.unscheduleAllEvents(black_S);
            Entity door = Create.createDoor(S_ID, entity_p, imageStore.getImageList("door"));
            world.tryAddEntity(door);
         }
      }*/

      /*EntityVisitor<Boolean> obs = new Visitor_Obstacle();
      List<Entity> list_O = check_Entity(tile, kind, world);
      if (list_B != null){
         for(Entity ob : list_O) {
            Point entity_p = ob.position();
            world.removeEntity(ob);
            scheduler.unscheduleAllEvents(ob);
            Entity space_ob = Create.createSpace_ob(S_ID, entity_p, imageStore.getImageList("space"));
            world.tryAddEntity(space_ob);
         }
      }*/


   public ImageStore get_imageStore(){
      return this.imageStore;
   }

   public List<Entity> check_Entity(Point pos, EntityVisitor<Boolean> kind, WorldModel world)
      {

         List<Entity> ofType = new LinkedList<>();
         for (Entity entity : world.getEntities())
         {
            if (entity.accept(kind))
            //if(kind.visit(entity))
            {
               ofType.add(entity);
            }
         }
         return closestEntity(ofType, pos);
   }

   public static List<Entity> entity_List(Point pos, EntityVisitor<Boolean> kind, WorldModel world) {

      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : world.getEntities()) {
         if (entity.accept(kind))
         //if(kind.visit(entity))
         {
            ofType.add(entity);
         }
      }
      return ofType;
   }

   /*private List<Entity> nearestEntity(List<Entity> entities, Point pos)
   {
      ArrayList<Entity> result = new ArrayList<>();
      if (entities.isEmpty())
      {
         return null;
      }
      else
      {
         for (Entity other : entities)
         {
            if(Math.abs(other.position().x - pos.x) < 2 & Math.abs(other.position().y - pos.y) <2){
               result.add(other);

            }
         }

         return result;
      }
   }*/
   private List<Entity> closestEntity(List<Entity> entities,
                                          Point pos)
   {
      if (entities.isEmpty())
      {
         return null;
      }
      else
      {
         Entity one;
         int one_d;
         Entity two = null;
         int two_d=20;
         Entity three = null;
         int three_d=20;

         int size = 1;

         List<Entity> nearestEntity = new LinkedList<>();
         Entity nearest = entities.get(0);
         entities.remove(0);
         one = nearest;
         int nearestDistance = world.distanceSquared(nearest.position(),pos);
         one_d = nearestDistance;

         for (Entity other : entities) {
            size += 1;
            int otherDistance = world.distanceSquared(other.position(), pos);

            if (otherDistance < one_d) {
               if (size >= 3) {
                  three_d = two_d;
               }
               two_d = one_d;
               one_d = otherDistance;
               three = two;
               two = one;
               one = other;
            } else if (otherDistance < two_d) {
               if (size >= 3) {
                  three_d = two_d;
               }
               two_d = otherDistance;
               three = two;
               two = other;
            } else if (otherDistance < three_d) {
               three = other;
               three_d = otherDistance;
            }
         }
         nearestEntity.add(one);
         nearestEntity.add(two);
         nearestEntity.add(three);
         return nearestEntity;
      }
   }


   private static Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
         imageStore.getImageList(DEFAULT_IMAGE_NAME));
      //DEFAULT_IMAGE_NAME
   }

   private static PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   /*private static void loadImages(String filename, ImageStore imageStore,
      PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         ImageLoader.loadImages(in, imageStore, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }*/

   private static void loadWorld(WorldModel world, String filename,
      ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         WorldLoader.load(in, world, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void loadGrass(WorldModel world, String filename,
                                 ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         WorldLoader.load_Grass(in, world, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   private static void scheduleActions(WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {
      for (Entity entity : world.getEntities())
      {
         EntityVisitor <Boolean> check_schedulable = new Visitor_Schedulable();
         if (entity.accept(check_schedulable)){

            ((Schedulable)entity).scheduleActions(scheduler, world, imageStore);
         }
      }
   }

   private static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }



   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }

   public static void loadImages(String filename, ImageStore imageStore,
                                 PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         ImageLoader.loadImages(in, imageStore, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }
}
