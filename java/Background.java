import java.util.List;
import processing.core.PImage;

final class Background
{
   private String id;
   private List<PImage> images;
   private int imageIndex;

   public Background(String id, List<PImage> images)
   {
      this.id = id;
      this.images = images;
   }
   public PImage getCurrentImage() {
      return (images.get(imageIndex));
   }

   /*public static PImage getCurrentImage(Background entity) {
      return entity.images.get(((Background)entity).imageIndex);
   }*/
}
