package kh.mort.list;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import kh.mort.canvas.Canvas;
import kh.mort.canvas.ImageObject;


public class ObjectList extends JPanel {

    public Canvas canvas;
    public ArrayList<ObjectListItem> hiddens;
    public ArrayList<ObjectListItem> selects;
    public JPanel layoutPanel;
    public boolean multipleSelection = false;

    public ObjectList(Canvas canv)
    {
        canvas = canv;
        hiddens = new ArrayList<>();
        selects = new ArrayList<>();
        initComponents();
    }

    private void initComponents()
    {
        layoutPanel = new JPanel();
        layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
        this.setLayout(new BorderLayout());
        this.add(layoutPanel, BorderLayout.NORTH);
        this.setBackground(Color.WHITE);
    }

    public void addItemComponent(ImageObject obj, String clsName)
    {
        ObjectListItem item = new ObjectListItem(this, obj, clsName, 23, Color.WHITE);
        layoutPanel.add(item);
    }

    public ObjectListItem getItemComponent(int idx)
    {
        return (ObjectListItem) layoutPanel.getComponent(idx);
    }

    public int indexOf(ObjectListItem item)
    {
        int idx = 0;
        for (Component c : layoutPanel.getComponents()) {
            if (c.getClass() == ObjectListItem.class && c == item) {
                return idx;
            }
            idx++;
        }
        return -1;
    }

    public void del(ObjectListItem itm)
    {
        canvas.removeObject(itm.obj);
        layoutPanel.remove(itm);
        selects.remove(itm);
        hiddens.remove(itm);
    }

    public void del(ImageObject obj)
    {
        for (Component c : layoutPanel.getComponents()) {
            if (c.getClass() == ObjectListItem.class) {
                ObjectListItem itm = (ObjectListItem) c;
                if (itm.obj.equals(obj)) {
                    del(itm);
                    break;
                }
            }
        }
    }

    public void hide(ObjectListItem itm)
    {
        canvas.removeObject(itm.obj);
        if (!hiddens.contains(itm)) {
            hiddens.add(itm);
        }
        canvas.repaint();
    }

    public void expose(ObjectListItem itm)
    {
        hiddens.remove(itm);
        if (!canvas.objs.contains(itm.obj)) {
            canvas.addObject(itm.obj);
        }
        canvas.repaint();
    }

    public void exposeAll()
    {
        for (ObjectListItem itm : hiddens) {
            expose(itm);
        }
    }

    public int getItemCount()
    {
        return layoutPanel.getComponentCount();
    }

    public void addToSelects(ObjectListItem item)
    {
        if (multipleSelection) {
            if (!selects.contains(item)) {
                selects.add(item);
            }
        } else {
            selects.clear();
            selects.add(item);
        }
    }

    public void addToSelects(ImageObject obj)
    {
        for (Component c : layoutPanel.getComponents()) {
            if (c.getClass() == ObjectListItem.class) {
                ObjectListItem itm = (ObjectListItem) c;
                if (itm.obj.equals(obj)) {
                    addToSelects(itm);
                    break;
                }
            }
        }
    }

    public void clearSelects() {
        selects.clear();
    }

    public void removeFromSelects(ObjectListItem item)
    {
        if (!multipleSelection) {
            selects.remove(item);
        }
    }

    public void updateSelectsUI(boolean updateCanvasSelects)
    {
        for (Component c : layoutPanel.getComponents()) {
            if (c.getClass() == ObjectListItem.class) {
                ObjectListItem item = (ObjectListItem) c;
                if (selects.contains(c)) {
                    item.setItemColor(Color.BLUE, Color.WHITE);
                } else {
                    item.setItemColor(Color.WHITE, Color.BLACK);
                }
            }
        }
        if (updateCanvasSelects) {
            canvas.selects = new ArrayList<>(
                selects.stream().filter(x -> !hiddens.contains(x)).map(x -> x.obj).toList()
            );
            canvas.repaint();
        }
    }
}
