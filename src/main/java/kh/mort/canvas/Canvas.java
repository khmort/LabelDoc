package kh.mort.canvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;

import kh.mort.list.ClassItem;
import kh.mort.list.ObjectList;

public class Canvas extends JPanel {

    public BufferedImage img;
    public ArrayList<ImageObject> objs;
    public ArrayList<ImageObject> selects;
    public ObjectList list;
    public DefaultListModel<ClassItem> classesModel;
    public double zoom = 1.0;
    public boolean moveMode;
    
    private Point cursorPoint;
    private ImageObject cornerFor;
    private Box imgBox;
    private Point cornerPoint;
    private Point clickedPoint;
    private boolean multipleSelection = false;
    private Point basePoint;
    private boolean isCreating;
    private Point lastDraggedPoint;
    
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

    public Canvas() {
        objs = new ArrayList<>();
        selects = new ArrayList<>();
        initListeners();
    }

    private void initListeners() {

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                cursorPoint = e.getPoint();
                cornerPoint = null;
                cornerFor = null;
                for (ImageObject obj : objs) {
                    Point[] corners = obj.box.getReal(imgBox.getWidth(), imgBox.getHeight()).move(imgBox.x1, imgBox.y1).getCorners();
                    for (Point corner : corners) {
                        if (distance(corner, e.getPoint()) <= 5.0) {
                            cornerPoint = corner;
                            cornerFor = obj;
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

                if (moveMode) {

                    if (lastDraggedPoint == null) {
                        lastDraggedPoint = clickedPoint;
                    }

                    double dx = (lastDraggedPoint.x - e.getX()) / imgBox.getWidth();
                    double dy = (lastDraggedPoint.y - e.getY()) / imgBox.getHeight();

                    for (ImageObject obj : selects) {
                        obj.box.x1 -= dx;
                        obj.box.x2 -= dx;
                        obj.box.y1 -= dy;
                        obj.box.y2 -= dy;
                    }

                    lastDraggedPoint = e.getPoint();

                } else {
                    if (cornerFor == null) {
    
                        isCreating = true;
    
                        selects.clear();
    
                        Box target = Box.createBox(clickedPoint, e.getPoint()).move(-imgBox.x1, -imgBox.y1)
                            .getNorm(imgBox.getWidth(), imgBox.getHeight());
                        cornerFor = new ImageObject(target, Integer.MAX_VALUE);
    
                        basePoint = clickedPoint;
    
                        objs.add(cornerFor);
                        selects.add(cornerFor);
    
                    } else {
    
                        Box isEditing = cornerFor.box.getReal(imgBox.getWidth(), imgBox.getHeight()).move(imgBox.x1, imgBox.y1);
    
                        if (basePoint == null)
                            basePoint = getFarthest(e.getPoint(), isEditing);
                        
                        isEditing = Box.createBox(basePoint, e.getPoint()).move(-imgBox.x1, -imgBox.y1)
                            .getNorm(imgBox.getWidth(), imgBox.getHeight());
                        
                        cornerFor.box = isEditing;
    
                        list.addToSelects(cornerFor);
                        list.updateSelectsUI(false);
                        
                    }
                }

                repaint();
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                requestFocus();

                multipleSelection = list.multipleSelection;
                clickedPoint = e.getPoint();

                ImageObject choosen = null;

                if (cornerFor == null) {
                    for (ImageObject obj : objs) {

                        Rectangle2D rect = obj.box.getReal(imgBox.getWidth(), imgBox.getHeight()).move(imgBox.x1, imgBox.y1)
                            .toRectangle2D();
                        
                        if (rect.contains(e.getPoint())) {
                            choosen = obj;
                            break;
                        }

                    }
                } else {
                    choosen = cornerFor;
                }

                if (choosen == null) {
                    if (!moveMode) {
                        selects.clear();
                        list.clearSelects();
                    }
                } else {
                    if (!multipleSelection) {
                        selects.clear();
                        list.clearSelects();
                    }
                    if (!selects.contains(choosen)) {
                        selects.add(choosen);
                        list.addToSelects(choosen);
                    }
                }

                list.updateSelectsUI(false);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isCreating) {
                    isCreating = false;
                    if (classesModel.isEmpty()) {
                        removeObject(cornerFor);
                        cornerFor = null;
                        cornerPoint = null;
                    } else {
                        String[] options = new String[classesModel.size()];
                        for (int i = 0; i < classesModel.size(); i++) {
                            options[i] = classesModel.get(i).name;
                        }
                        ListSelectionDialog selectionDialog = new ListSelectionDialog(null, "Label chooser", options);
                        selectionDialog.setVisible(true);
                        if (selectionDialog.getSelectedValue() == null) {
                            removeObject(cornerFor);
                            cornerFor = null;
                            cornerPoint = null;
                        } else {
                            cornerFor.clazz = getClazz(selectionDialog.getSelectedValue());
                            list.addItemComponent(cornerFor, selectionDialog.getSelectedValue());
                            list.addToSelects(cornerFor);
                            list.updateSelectsUI(false);
                            list.revalidate();
                        }
                    }
                }
                lastDraggedPoint = null;
                clickedPoint = null;
                basePoint = null;
                Canvas.this.getMouseMotionListeners()[0].mouseMoved(e);
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 17) {
                    multipleSelection = true;
                    list.multipleSelection = true;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 17) {
                    multipleSelection = false;
                    list.multipleSelection = false;
                }
            }
        });

    }

    public void addObject(ImageObject obj) {
        if (!objs.contains(obj)) {
            objs.add(obj);
        }
    }

    public void removeObject(ImageObject obj) {
        objs.remove(obj);
        selects.remove(obj);
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
        if (objs.isEmpty())
            return;
        for (ImageObject obj : objs) {
            Color clsColor = getColor(obj.clazz);
            g.setColor(clsColor == null ? Color.BLUE : clsColor);
            Box box = obj.box.getReal(imgBox.getWidth(), imgBox.getHeight()).move(imgBox.x1, imgBox.y1);
            g.drawString(getClassName(obj.clazz), (int) box.x1, (int) box.y1 - 5);
            g.drawRect((int) box.x1, (int) box.y1, (int) box.getWidth(), (int) box.getHeight());
        }
    }

    public Color getColor(int id) {
        for (int i=0; i<classesModel.size(); i++) {
            ClassItem itm = classesModel.get(i);
            if (itm.id == id) {
                return itm.color;
            }
        }
        return null;
    }

    public int getClazz(String clsName) {
        for (int i=0; i<classesModel.size(); i++) {
            ClassItem itm = classesModel.get(i);
            if (itm.name.equals(clsName)) {
                return itm.id;
            }
        }
        return -1;
    }

    public String getClassName(int id) {
        for (int i=0; i<classesModel.size(); i++) {
            ClassItem itm = classesModel.get(i);
            if (itm.id == id) {
                return itm.name;
            }
        }
        return "";
    }

    public void drawSelectsBox(Graphics2D g) {
        if (selects.isEmpty())
            return;
        g.setColor(new Color(154, 255, 77, 40));
        for (ImageObject obj : selects) {
            Box box = obj.box.getReal(imgBox.getWidth(), imgBox.getHeight()).move(imgBox.x1, imgBox.y1);
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

    @Override
    public Dimension getPreferredSize() {
        Dimension parentDim = getParent().getSize();
        return new Dimension(
            (int) (parentDim.getWidth() * zoom),
            (int) (parentDim.getHeight() * zoom)
        );
    }

}
