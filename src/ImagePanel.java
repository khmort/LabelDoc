import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;

public class ImagePanel extends JPanel {

    public ImagePanel() {
        objects = new ArrayList<>();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension parentDim = parent.getSize();
        return new Dimension((int) (parentDim.width * zoom), (int) (parentDim.height * zoom));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setStroke(new BasicStroke(1.5f));

        g2.setColor(Color.lightGray);
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (image == null) return;

        double ratioW = (double) getWidth() / image.getWidth();
        double ratioH = (double) getHeight() / image.getHeight();

        double realW = Math.min(ratioW, ratioH) * image.getWidth(),
               realH = Math.min(ratioW, ratioH) * image.getHeight();
        
        imgWidth = realW;
        imgHeight = realH;
        
        double startX = (getWidth() - realW) / 2.0,
               startY = (getHeight() - realH) / 2.0;
        
        imgX = startX;
        imgY = startY;

        g2.drawImage(image, (int) startX, (int) startY, (int) realW, (int) realH, null);

        if (objects != null) {
            for (PageObject po: objects) {
                int[] rgb = labelToColor(po.labelText);
                g2.setColor(new Color(rgb[0], rgb[1], rgb[2]));
                Rectangle real = po.bbox.getReal(imgWidth, imgHeight);
                g2.drawRect(
                    (int) (real.x1 + imgX),
                    (int) (real.y1 + imgY),
                    (int) (real.getWidth()),
                    (int) (real.getHeight())
                );
                if (po.labelText != null)
                    g2.drawString(po.labelText, (int) (real.x1 + imgX), (int) (real.y1 + imgY - 5));
            }
        }

        if (highlight != null) {
            Rectangle real = highlight.bbox.getReal(imgWidth, imgHeight);
            g2.setColor(new Color(0, 255, 0, 50));
            g2.fillRect(
                (int) (real.x1 + imgX),
                (int) (real.y1 + imgY),
                (int) (real.getWidth()),
                (int) (real.getHeight())
            );
        }

        if (selected != null && selected != highlight) {
            
            Rectangle real = selected.bbox.getReal(imgWidth, imgHeight);
            g2.setColor(new Color(0, 255, 0, 50));
            g2.fillRect(
                (int) (real.x1 + imgX),
                (int) (real.y1 + imgY),
                (int) (real.getWidth()),
                (int) (real.getHeight())
            );
        }

    }

    public static int[] labelToColor(String label) {
        if (label == null || label.length() == 0)
            return new int[] {0, 0, 255};
        int labelId = 0;
        for (char c: label.toCharArray()) {
            labelId += (int) c;
        }
        int r = 5, g = 23, b = 97;
        return new int[] {
            (r * labelId) % 255,
            (g * labelId) % 255,
            (b * labelId) % 255
        };
    }

    public void setImage(String path) throws IOException {
        setImage(ImageIO.read(new File(path)));
    }

    public void setImage(BufferedImage img) {
        image = img;
    }

    public void setObjects(ArrayList<PageObject> objs) {
        this.objects = objs;
    }

    public void zoomIn() {
        zoom += 0.35;
    }

    public void zoomOut() {
        zoom = Math.max(zoom - 0.35, 0.95);
    }

    public Rectangle getNormSize(Rectangle rect) {
        return rect.move(-imgX, -imgY).getNorm(imgWidth, imgHeight);
    }

    public void addRect(Rectangle rect) {
        objects.add(new PageObject(getNormSize(rect)));
    }

    public void drawRect(Rectangle rect) {
        addRect(rect);
        repaint();
    }

    public void removeLastRect() {
        objects.remove(objects.size() - 1);
    }

    public Object[] corner(Point p, double alpha) {
        for (PageObject po: objects) {
            Point[] corners = po.bbox.getReal(imgWidth, imgHeight).move(imgX, imgY).getCorners();
            for (Point c: corners) {
                Rectangle targetSpace = new Rectangle(c.x - alpha, c.y - alpha, c.x + alpha, c.y + alpha);
                if (targetSpace.isInside(p)) {
                    return new Object[] {po, c};
                }
            }
        }
        return null;
    }

    public Rectangle getRealSize(Rectangle rect) {
        return rect.getReal(imgWidth, imgHeight).move(imgX, imgY);
    }

    public PageObject getObject(Point p) {
        for (PageObject po: objects) {
            if (getRealSize(po.bbox).isInside(p))
                return po;
        }
        return null;
    }

    public PageObject getLastObject() {
        return objects.get(objects.size() - 1);
    }

    public void highlight(PageObject obj) {
        highlight = obj;
    }

    public void dehighlight() {
        highlight = null;
    }

    public void select(PageObject obj, JList<PageObject> list) {
        if (obj == null) {
            list.clearSelection();
            selected = null;
            return;
        }
        ListModel<PageObject> model = list.getModel();
        int index = -1;
        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i).equals(obj)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            list.setSelectedIndex(index);
        }
    }

    double imgWidth, imgHeight, imgX, imgY, zoom=0.95;
    BufferedImage image;
    ArrayList<PageObject> objects;
    JComponent parent;
    PageObject highlight, selected;
}
