import java.awt.Point;

public class PageObject {

    public PageObject(Rectangle bbox, String labelText, int labelId) {
        this.bbox = bbox;
        this.labelText = labelText;
        this.labelId = labelId;
    }

    public PageObject(int x1, int y1, int x2, int y2) {
        this(new Rectangle(x1, y1, x2, y2), null, -1);
    }

    public PageObject(Rectangle bbox) {
        this(bbox, null, -1);
    }

    @Override
    public String toString() {
        return "`" + labelText + "` ID=" + labelId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || ! (obj instanceof PageObject)) return false;
        return ((PageObject) obj).bbox.equals(bbox);
    }

    public Rectangle bbox;
    public int labelId;
    public String labelText;
}


class Rectangle {

    public Rectangle(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public Rectangle getNorm(double width, double height) {
        return new Rectangle(x1 / width, y1 / height, x2 / width, y2 / height);
    }

    public Rectangle getReal(double width, double height) {
        return new Rectangle(x1 * width, y1 * height, x2 * width, y2 * height);
    }

    public double getWidth() {
        return x2 - x1;
    }

    public double getHeight() {
        return y2 - y1;
    }

    public Rectangle move(double dx, double dy) {
        return new Rectangle(dx + x1, dy + y1, dx + x2, dy + y2);
    }

    public boolean isInside(Point p) {
        return (x1 <= p.x && p.x <= x2) && (y1 <= p.y && p.y <= y2);
    }

    public Point[] getCorners() {
        return new Point[] {
            new Point((int) x1, (int) y1),
            new Point((int) x2, (int) y1),
            new Point((int) x2, (int) y2),
            new Point((int) x1, (int) y2)
        };
    }

    public Point diagonalCorner(Point corner) {
        Point[] corners = getCorners();
        double maxDis = -1;
        Point choose = null;
        for (int i=0; i<corners.length; i++) {
            double dis = distance(corner, corners[i]);
            if (dis > maxDis) {
                choose = corners[i];
                maxDis = dis;
            }
        }
        return choose;
    }

    public static double distance(Point p1, Point p2) {
        return Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2);
    }

    @Override
    public String toString() {
        return "[" + x1 + ", " + y1 + ", " + x2 + ", " + y2 + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || ! (obj instanceof Rectangle)) return false;
        Rectangle rec = (Rectangle) obj;
        return rec.x1 == x1 && rec.x2 == x2 && rec.y1 == y1 && rec.y2 == y2;
    }
    
    public double x1, x2, y1, y2;
}