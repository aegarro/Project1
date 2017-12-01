final class Point {
   public int x;
   public int y;

   public Point(int x, int y) {
      this.x = x;
      this.y = y;
   }

   public String toString() {
      return "(" + x + "," + y + ")";
   }

   public boolean equals(Object other) {
      return other instanceof Point &&
              ((Point) other).x == this.x &&
              ((Point) other).y == this.y;
   }

   public int hashCode() {
      int result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      return result;
   }

   public static Point check_bounds(Point p, int x, int y) {
      if (p.x + x < 0 || p.x +x > 19) {
         x = 0;
      }
      if (p.y + y< 0 || p.y + y> 14) {
         y = 0;
      }
      return new Point(x, y);
      }

}
