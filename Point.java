public class Point {
    private int x;
    private int y;
    private int hashCode;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        hashCode = calculateHashCode();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        return y == point.y;

    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    public int calculateHashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
