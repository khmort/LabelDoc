package kh.mort.list;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import kh.mort.Dictionary;
import kh.mort.canvas.Box;
import kh.mort.canvas.Canvas;


public class ObjectList extends JPanel {

    protected Canvas canvas;
    protected Dictionary<Box, Integer> hiddens;
    protected JPanel layoutPanel;
    protected ArrayList<ObjectListItem> selects;
    public boolean multipleSelection = false;

    public ObjectList(Canvas canv)
    {
        canvas = canv;
        hiddens = new Dictionary<>();
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

    public void addItemComponent(Box box, Integer cls)
    {
        ObjectListItem item = new ObjectListItem(this, box, cls, "None", 18, Color.WHITE);
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

    public void del(Box box, int cls)
    {
        canvas.boxes.remove(box, cls);
        for (Component c : layoutPanel.getComponents()) {
            if (c.getClass() == ObjectListItem.class) {
                ObjectListItem item = (ObjectListItem) c;
                if (item.object.equals(box) && item.cls == cls) {
                    layoutPanel.remove(item);
                    break;
                }
            }
        }
    }

    public void hide(Box box, int cls)
    {
        int targetIdx = canvas.boxes.indexOf(box, cls);
        if (targetIdx != -1) {
            canvas.boxes.remove(targetIdx);
            hiddens.add(box, cls);
        }
    }

    public void expose(Box box, int cls)
    {
        int targetIdx = hiddens.indexOf(box, cls);
        if (targetIdx != -1) {
            hiddens.remove(targetIdx);
            if (!canvas.boxes.contains(box, cls)) {
                canvas.boxes.add(box, cls);
            }
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

    public void removeFromSelects(ObjectListItem item)
    {
        if (!multipleSelection) {
            selects.remove(item);
        }
    }

    public void updateSelectsUI()
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
    }
}
