package kh.mort;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import kh.mort.canvas.Box;
import kh.mort.canvas.Canvas;
import kh.mort.canvas.ImageObject;
import kh.mort.list.ClassItem;
import kh.mort.list.ClassItemRenderer;
import kh.mort.list.ObjectList;

public class App extends JFrame
{

    Button openImagesBtn;
    Button openWorkspaceBtn;
    Button createWorkspaceBtn;
    Button clearBtn;
    Button saveBtn;
    Button compressBtn;
    Button boxBoosterBtn;
    Button boxGeneratorBtn;
    Button zoomInBtn, zoomOutBtn;

    JList<String> filenamesList = new JList<>();
    JButton searchBtn;
    JTextField searchArea;

    Canvas canvas;

    JList<ClassItem> classesList;
    JButton editBtn;
    JButton removeBtn;
    ObjectList objList;

    public static void main( String[] args )
    {
        App app = new App();
    }

    public App() {
        initComponents();
    }

    private void initComponents()
    {
        setTitle("labelDoc 0.2");

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLUE),
            "File",
            TitledBorder.CENTER,
            TitledBorder.CENTER));

        openImagesBtn = new Button(
            "Open images folder",
            new ImageIcon("labelDoc/src/main/resources/icons8-open-folder-30.png"),
            35,
            new Dimension(70, 80));
        openWorkspaceBtn = new Button(
            "Open workspace",
            new ImageIcon("labelDoc/src/main/resources/icons8-workspace-30.png"),
            35,
            new Dimension(70, 80));
        createWorkspaceBtn = new Button(
            "Create new workspace",
            new ImageIcon("labelDoc/src/main/resources/icons8-create-30.png"),
            35,
            new Dimension(70, 80));
        saveBtn = new Button(
            "Save",
            new ImageIcon("labelDoc/src/main/resources/icons8-save-30.png"),
            35,
            new Dimension(70, 80));
        compressBtn = new Button(
            "Compress workspace", 
            new ImageIcon("labelDoc/src/main/resources/icons8-compress-30.png"),
            35,
            new Dimension(70, 80));

        filePanel.add(openImagesBtn);
        filePanel.add(openWorkspaceBtn);
        filePanel.add(createWorkspaceBtn);
        filePanel.add(saveBtn);
        filePanel.add(compressBtn);

        JPanel boosterPanel = new JPanel(new FlowLayout());
        boosterPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLUE),
            "Booster",
            TitledBorder.CENTER,
            TitledBorder.CENTER));
        
        boxBoosterBtn = new Button(
            "Box booster",
            new ImageIcon("labelDoc/src/main/resources/icons8-rectangle-30.png"),
            35,
            new Dimension(70, 80));
        boxGeneratorBtn = new Button(
            "Box generator",
            new ImageIcon("labelDoc/src/main/resources/icons8-engine-30.png"),
            35,
            new Dimension(70, 80));
        clearBtn = new Button(
            "Clear all boxes!",
            new ImageIcon("labelDoc/src/main/resources/icons8-clear-30.png"),
            35,
            new Dimension(70, 80));
        
        boosterPanel.add(boxBoosterBtn);
        boosterPanel.add(boxGeneratorBtn);
        boosterPanel.add(clearBtn);

        JPanel viewPanel = new JPanel(new FlowLayout());
        viewPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLUE),
            "View",
            TitledBorder.CENTER,
            TitledBorder.CENTER));
        zoomInBtn = new Button(
            "Zoom in!",
            new ImageIcon("labelDoc/src/main/resources/icons8-zoom-in-30.png"),
            35,
            new Dimension(70, 80));
        zoomOutBtn = new Button(
            "Zoom out!",
            new ImageIcon("labelDoc/src/main/resources/icons8-zoom-out-30.png"),
            35,
            new Dimension(70, 80));
        viewPanel.add(zoomInBtn);
        viewPanel.add(zoomOutBtn);

        JPanel toolsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolsPanel.add(filePanel);
        toolsPanel.add(boosterPanel);
        toolsPanel.add(viewPanel);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchArea = new JTextField(10);
        searchArea.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        searchBtn = new JButton(new ImageIcon("labelDoc/src/main/resources/icons8-search-25.png"));
        searchBtn.setPreferredSize(new Dimension(30, 30));
        searchPanel.add(searchArea, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);
        filenamesList = new JList<>();

        JPanel imgsListPanel = new JPanel(new BorderLayout());
        imgsListPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Images"));
        imgsListPanel.add(searchPanel, BorderLayout.NORTH);
        imgsListPanel.add(filenamesList, BorderLayout.CENTER);

        JPanel classesEditPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        editBtn = new JButton(new ImageIcon("labelDoc/src/main/resources/icons8-edit-30.png"));
        editBtn.setPreferredSize(new Dimension(35, 35));
        removeBtn = new JButton(new ImageIcon("labelDoc/src/main/resources/icons8-remove-30.png"));
        removeBtn.setPreferredSize(new Dimension(35, 35));
        classesEditPanel.add(editBtn);
        classesEditPanel.add(removeBtn);
        classesList = new JList<>(new ClassItem[] {
            new ClassItem("Cat", 0, Color.MAGENTA),
            new ClassItem("Dog", 1, Color.CYAN)
        });
        classesList.setCellRenderer(new ClassItemRenderer());

        JPanel classesPanel = new JPanel(new BorderLayout());
        JScrollPane classesListScroll = new JScrollPane(classesList);
        classesList.setVisibleRowCount(10);
        classesList.setFixedCellWidth(160);
        classesListScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Classes"));
        classesPanel.add(classesListScroll, BorderLayout.CENTER);
        classesPanel.add(classesEditPanel, BorderLayout.SOUTH);

        JPanel objectsPanel = new JPanel(new BorderLayout());
        canvas = new Canvas();
        objList = new ObjectList(canvas);
        canvas.list = objList;
        objList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Objects"));
        objList.addItemComponent(new ImageObject(new Box(0.1, 0.08, 0.8, 0.1), 0));
        objectsPanel.add(classesPanel, BorderLayout.NORTH);
        objectsPanel.add(objList, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new BorderLayout());
        canvas.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Canvas"));
        controlPanel.add(imgsListPanel, BorderLayout.WEST);
        controlPanel.add(canvas, BorderLayout.CENTER);
        controlPanel.add(objectsPanel, BorderLayout.EAST);
        
        mainPanel.add(toolsPanel, BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.CENTER);

        this.getContentPane().add(mainPanel);
        this.setSize(500, 500);
        this.setDefaultCloseOperation(3);
        this.setVisible(true);
    }
}
