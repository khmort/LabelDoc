package kh.mort.canvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import kh.mort.Dictionary;

public class Canvas extends JPanel {

    public BufferedImage img;
    public Dictionary<Box, Integer> boxes;
    
    private Point cursorPoint;
    private List<Box> selects;
    private Box cornerFor;
    private Box imgBox;
    private Point cornerPoint;
    private Point clickedPoint;
    private boolean multipleSelection = false;
    private Point basePoint;
    
    public static final double distance(Point p1, Point p2) {
        double w = p1.x - p2.x;
        double h = p1.y - p2.y;
        return Math.sqrt(w * w + h * h);
    }
    
    private static final Point getFarthest(Point p, Box box) {
        double maxDist = -1;
        Point bestPoint = null;
        for (Point c : box.getCorners()) {
            double dist = distance(c, p);
            if (dist > maxDist) {
                bestPoint = c;
                maxDist = distance(c, p);
            }
        }
        return bestPoint;
    }
    
    public static final <T> void setListItem(List<T> list, T oldItem, T newItem) {
        int index = list.indexOf(oldItem);
        if (index != -1) {
            list.set(index, newItem);
        }
    }

    public Canvas() {
        initListeners();
        boxes = new Dictionary<>();
        selects = new ArrayList<>();
    }

    private void initListeners() {

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                cursorPoint = e.getPoint();
                cornerPoint = null;
                cornerFor = null;
                for (Box box : boxes.getKeys()) {
                    Point[] corners = box.getReal(imgBox.getWidth(), imgBox.getHeight()).move(imgBox.x1, imgBox.y1).getCorners();
                    for (Point corner : corners) {
                        if (distance(corner, e.getPoint()) <= 5.0) {
                            cornerPoint = corner;
                            cornerFor = box;
                            break;
                        }
                    }
                    if (cornerPoint != null)
                        break;
                }
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                
                if (cornerFor == null) {

                    selects.clear();

                    Box target = Box.createBox(clickedPoint, e.getPoint()).move(-imgBox.x1, -imgBox.y1)
                        .getNorm(imgBox.getWidth(), imgBox.getHeight());
                    
                    cornerFor = target;
                    basePoint = clickedPoint;

                    boxes.add(cornerFor, -1);
                    selects.add(cornerFor);

                } else {

                    Box isEditing = cornerFor.getReal(imgBox.getWidth(), imgBox.getHeight()).move(imgBox.x1, imgBox.y1);

                    if (basePoint == null)
                        basePoint = getFarthest(e.getPoint(), isEditing);
                    
                    isEditing = Box.createBox(basePoint, e.getPoint()).move(-imgBox.x1, -imgBox.y1)
                        .getNorm(imgBox.getWidth(), imgBox.getHeight());
                    
                    
                    boxes.add(isEditing, (int) boxes.getValueByKey(cornerFor));
                    boxes.removeByKey(cornerFor);
                    setListItem(selects, cornerFor, isEditing);

                    cornerFor = isEditing;
                    cornerPoint = e.getPoint();
                }
                repaint();
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                clickedPoint = e.getPoint();
                Box choosen = null;

                if (cornerFor == null) {
                    for (Box box : boxes.getKeys()) {

                        Rectangle2D rect = box.getReal(imgBox.getWidth(), imgBox.getHeight()).move(imgBox.x1, imgBox.y1)
                            .toRectangle2D();
                        
                        if (rect.contains(e.getPoint())) {
                            choosen = box;
                            break;
                        }

                    }
                } else {
                    choosen = cornerFor;
                }

                if (choosen == null) {
                    selects.clear();
                } else {
                    if (!multipleSelection) {
                        selects.clear();
                    }
                    if (!selects.contains(choosen)) {
                        selects.add(choosen);
                    }
                }

                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                clickedPoint = null;
                basePoint = null;
                Canvas.this.getMouseMotionListeners()[0].mouseMoved(e);
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (img == null) {
            imgBox = new Box(0, 0, 500, 700).fit(getWidth(), getHeight(), false);
        } else {
            imgBox = new Box(0, 0, img.getWidth(), img.getHeight());
            imgBox = imgBox.fit(getWidth(), getHeight(), false);
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2.0f));

        drawBackground(g2);
        drawGuide(g2);
        drawSelectsBox(g2);
        drawBoxes(g2);
        drawCorner(g2);
    }

    public void drawBackground(Graphics2D g) {
        if (img != null) {
            g.drawImage(img,
                (int) imgBox.x1, (int) imgBox.y1,
                (int) imgBox.getWidth(),
                (int) imgBox.getHeight(), null);
        }
    }


    public void drawBoxes(Graphics2D g) {
        if (boxes.isEmpty())
            return;
        g.setColor(Color.BLUE);
        for (Box box : boxes.getKeys()) {
            box = box.getReal(imgBox.getWidth(), imgBox.getHeight()).move(imgBox.x1, imgBox.y1);
            g.drawRect((int) box.x1, (int) box.y1, (int) box.getWidth(), (int) box.getHeight());
        }
    }

    public void drawSelectsBox(Graphics2D g) {
        if (selects.isEmpty())
            return;
        g.setColor(new Color(0, 100, 100, 20));
        for (Box box : selects) {
            box = box.getReal(imgBox.getWidth(), imgBox.getHeight()).move(imgBox.x1, imgBox.y1);
            g.fillRect((int) box.x1, (int) box.y1, (int) box.getWidth(), (int) box.getHeight());
        }
    }

    public void drawCorner(Graphics2D g) {
        if (cornerPoint != null) {
            g.setColor(Color.BLACK);
            g.drawOval(cornerPoint.x - 5, cornerPoint.y - 5, 10, 10);
            g.setColor(new Color(150, 200, 30, 100));
            g.fillOval(cornerPoint.x - 5, cornerPoint.y - 5, 10, 10);
        }
    }

    public void drawGuide(Graphics2D g) {
        if (cursorPoint != null) {
            g.setColor(new Color(100, 100, 100, 180));
            g.drawLine(cursorPoint.x, 0, cursorPoint.x, getHeight());
            g.drawLine(0, cursorPoint.y, getWidth(), cursorPoint.y);
        }
    }

}
