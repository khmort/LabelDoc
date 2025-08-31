import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class Corner {

    public void update(ImagePanel imgPanel, Point point) {
        Object[] res = imgPanel.corner(point, 7);
        if (res == null) {
            if (onCorner) {
                onCorner = false;
                selectedCorner = null;
                cornerParent = null;
                imgPanel.repaint();
            }
            return;
        }
        cornerParent = (PageObject) res[0];
        selectedCorner = (Point) res[1];
        Graphics g = imgPanel.getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(selectedCorner.x - 7, selectedCorner.y - 7, 14, 14);
        onCorner = true;
    }

    public boolean onCorner;
    public Point selectedCorner;
    public PageObject cornerParent;
}
