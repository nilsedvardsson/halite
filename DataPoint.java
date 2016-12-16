public class DataPoint implements Comparable<DataPoint> {

    private int strength;
    private Point point;

    public DataPoint(Point point, int strength) {
        this.point = point;
        this.strength = strength;
    }

    public int getStrength() {
        return strength;
    }

    public Point getPoint() {
        return point;
    }

    @Override
    public int compareTo(DataPoint o) {
        return -Integer.compare(strength, o.getStrength());
    }
}
