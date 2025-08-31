import java.awt.Point;

public class Drag {

    public void drawDragRect(ImagePanel imgPanel, Point desPoint) {
        if (bbox != null)
            imgPanel.removeLastRect();
            bbox = App.buildRect(startPoint, desPoint);
            imgPanel.drawRect(bbox);
    }

    public void updateObjectBbox(ImagePanel imgPanel, Corner corner, Point point) {
        Point dig = imgPanel.getRealSize(corner.cornerParent.bbox).diagonalCorner(corner.selectedCorner);
        Rectangle newRect = App.buildRect(dig, point);
        corner.cornerParent.bbox = imgPanel.getNormSize(newRect);
        corner.selectedCorner = point;
        imgPanel.repaint();
    }

    public Point startPoint;
    public Rectangle bbox;
}
