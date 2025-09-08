package kh.mort.canvas;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class Box {

    double x1, x2, y1, y2;

    public Box(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public static Box createBox(Point p1, Point p2) {
        return new Box(
            Math.min(p1.x, p2.x),
            Math.min(p1.y, p2.y),
            Math.max(p1.x, p2.x),
            Math.max(p1.y, p2.y)
        );
    }

    public Box(Rectangle2D rect) {
        this(rect.getMinX(), rect.getMinY(), rect.getMaxX(), rect.getMaxY());
    }

    public Box(Rectangle rect) {
        this(rect.getMinX(), rect.getMinY(), rect.getMaxX(), rect.getMaxY());
    }

    private Box() {}

    public Rectangle2D toRectangle2D() {
        return new Rectangle2D.Double(x1, y1, getWidth(), getHeight());
    }

    public Rectangle toRectangle() {
        return new Rectangle((int) x1, (int) y1, (int) (x2-x1), (int) (y2-y1));
    }

    public Box getNorm(double containerWidth, double containerHeight) {
        return new Box(x1 / containerWidth, y1 / containerHeight, x2 / containerWidth, y2 / containerHeight);
    }

    public Box getReal(double containerWidth, double containerHeight) {
        return new Box(containerWidth * x1, containerHeight * y1, containerWidth * x2, containerHeight * y2);
    }

    public Box cxcywh() {
        return new Box((x1 + x2) / 2.0, (y1 + y2) / 2.0, x2 - x1, y2 - y1);
    }

    public Box xyxy() {
        return new Box(x1 - x2 / 2.0, x1 + x2 / 2.0, y1 - y2 / 2.0, y1 + y2 / 2.0);
    }

    public double getWidth(boolean cxcywh) {
        return cxcywh ? x2 : x2 - x1;
    }

    public double getWidth() {
        return getWidth(false);
    }

    public double getHeight(boolean cxcywh) {
        return cxcywh ? y2 : y2 - y1;
    }

    public double getHeight() {
        return getHeight(false);
    }

    public Box fit(double containerWidth, double containerHeight, boolean cxcywh) {
        Box result = new Box();
        double width = getWidth(cxcywh), height = getHeight(cxcywh);
        double wratio = containerWidth / width, hratio = containerHeight / height;
        if (wratio < hratio) {
            width *= wratio;
            height *= wratio;
            result.x1 = 0;
            result.y1 = (containerHeight - height) / 2.0;
            result.x2 = containerWidth;
            result.y2 = result.y1 + height;
        } else {
            width *= hratio;
            height *= hratio;
            result.x1 = (containerWidth - width) / 2.0;
            result.y1 = 0;
            result.x2 = result.x1 + width;
            result.y2 = containerHeight;
        }
        return result;
    }

    public Box copy() {
        return new Box(x1, y1, x2, y2);
    }

    public Point[] getCorners() {
        return new Point[] {
            new Point((int) x1, (int) y1),
            new Point((int) x2, (int) y1),
            new Point((int) x2, (int) y2),
            new Point((int) x1, (int) y2)
        };
    }

    public Box move(double dx, double dy) {
        return new Box(
            x1 + dx,
            y1 + dy,
            x2 + dx,
            y2 + dy
        );
    }

    @Override
    public String toString() {
        return "[x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != Box.class) return false;
        Box other = (Box) obj;
        return other.x1 == x1 &&
                other.x2 == x2 &&
                other.y1 == y1 &&
                other.y2 == y2;
    }
}