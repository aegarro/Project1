import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

final class WorldView
{
   private PApplet screen;
   private WorldModel world;
   private int tileWidth;
   private int tileHeight;
   private Viewport viewport;

   public WorldView(int numRows, int numCols, PApplet screen, WorldModel world,
      int tileWidth, int tileHeight)
   {
      this.screen = screen;
      this.world = world;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
      this.viewport = new Viewport(numRows, numCols);
   }

    public void drawViewport()
    {
        this.drawBackground();
        this.drawEntities();
    }

    private void drawEntities()
    {
        for (Entity entity : this.world.getEntities())
        {
            Point pos = entity.position();

            if (this.viewport.contains(pos))
            {
                Point viewPoint = this.viewport.worldToViewport(pos.x, pos.y);
                this.screen.image(Background.getCurrentImage(entity),
                        viewPoint.x * this.tileWidth, viewPoint.y * this.tileHeight);
            }
        }
    }

    private void drawBackground()
    {
        for (int row = 0; row < this.viewport.getNumRows(); row++)
        {
            for (int col = 0; col < this.viewport.getNumCols(); col++)
            {
                Point worldPoint = this.viewport.viewportToWorld(col, row);
                Optional<PImage> image = this.world.getBackgroundImage(worldPoint);
                if (image.isPresent())
                {
                    this.screen.image(image.get(), col * this.tileWidth,
                            row * this.tileHeight);
                }
            }
        }
    }

    public void shiftView(int colDelta, int rowDelta)
    {
        int newCol = clamp(this.viewport.getCol() + colDelta, 0,
                this.world.getNumCols() - this.viewport.getNumCols());
        int newRow = clamp(this.viewport.getRow() + rowDelta, 0,
                this.world.getNumRows() - this.viewport.getNumRows());

        this.viewport.shift(newCol, newRow);
    }

    private static int clamp(int value, int low, int high)
    {
        return Math.min(high, Math.max(value, low));
    }


}
