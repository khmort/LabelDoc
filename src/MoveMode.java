import java.awt.Point;

public class MoveMode {
    
    public void update(ImagePanel imgPanel, Point p) {
        PageObject target = imgPanel.getObject(p);
        if (target == null) {
            if (onObject) {
                imgPanel.repaint();
                onObject = false;
                selectedObject = null;
                imgPanel.dehighlight();
            }
            return;
        }
        onObject = true;
        selectedObject = target;
        imgPanel.highlight(selectedObject);
        imgPanel.repaint();
    }

    public void setObjectBbox(ImagePanel imgPanel, Drag drag, Point p) {
        Rectangle real = imgPanel.getRealSize(selectedObject.bbox);
        real = real.move(p.x - drag.startPoint.x, p.y - drag.startPoint.y);
        drag.startPoint = p;
        selectedObject.bbox = imgPanel.getNormSize(real);
        imgPanel.repaint();
    }

    boolean onObject = false;
    boolean isActive = false;
    PageObject selectedObject;
}
