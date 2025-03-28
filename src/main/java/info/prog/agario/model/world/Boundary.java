package info.prog.agario.model.world;

public class Boundary {
    private double x, y, width, height;

    /**
     * Constructor of the Boundary class
     * @param x the x coordinate of the boundary
     * @param y the y coordinate of the boundary
     * @param width the width of the boundary
     * @param height the height of the boundary
     */
    public Boundary(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Get the x coordinate of the boundary
     * @return double the x coordinate of the boundary
     */
    public double getX() {
        return x;
    }

    /**
     * Get the y coordinate of the boundary
     * @return double the y coordinate of the boundary
     */
    public double getY() {
        return y;
    }

    /**
     * Get the width of the boundary
     * @return double the width of the boundary
     */
    public double getWidth() {
        return width;
    }

    /**
     * Get the height of the boundary
     * @return double the height of the boundary
     */
    public double getHeight() {
        return height;
    }

    /**
     * Check if the boundary contains a specific point
     * @param px the x coordinate of the point
     * @param py the y coordinate of the point
     * @return boolean if the boundary contains the point
     */
    public boolean contains(double px, double py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    /**
     * Check if the boundary intersects with another boundary
     * @param other the other boundary
     * @return boolean if the boundaries intersect
     */
    public boolean intersects(Boundary other) {
        return !(other.x > x + width ||
                other.x + other.width < x ||
                other.y > y + height ||
                other.y + other.height < y);
    }
}