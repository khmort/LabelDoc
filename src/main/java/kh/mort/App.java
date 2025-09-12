package kh.mort;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
    Button clearBtn;
    Button saveBtn;
    Button compressBtn;
    Button boxBoosterBtn;
    Button boxGeneratorBtn;
    Button zoomInBtn, zoomOutBtn;
    Button moveModeBtn;
    Button clearMemoryBtn;

    JList<String> filenamesList = new JList<>();
    DefaultListModel<String> filenamesListModel;
    JButton searchBtn;
    JTextField searchArea;

    Canvas canvas;

    JList<ClassItem> classesList;
    DefaultListModel<ClassItem> classesListModel;
    JButton addClassBtn;
    JButton removeClassBtn;
    ObjectList objList;

    ClassCreatorDialog creatorDialog;
    JFileChooser fileChooser;

    File imgsFolder;
    File workspace;
    
    String workingOn;
    HashMap<String, ArrayList<ImageObject>> root;

    public static void main( String[] args )
    {
        App app = new App();
    }

    public App() {
        root = new HashMap<>();
        initComponents();
        initListeners();
    }

    private void initComponents()
    {
        setTitle("labelDoc 0.2");

        creatorDialog = new ClassCreatorDialog(this, "", true);
        creatorDialog.setSize(500, 500);
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLUE),
            "File",
            TitledBorder.CENTER,
            TitledBorder.CENTER));

        openImagesBtn = new Button(
            "Open images folder",
            new ImageIcon("src/main/resources/icons8-open-folder-30.png"),
            35,
            new Dimension(70, 80), false);
        openWorkspaceBtn = new Button(
            "Open workspace",
            new ImageIcon("src/main/resources/icons8-workspace-30.png"),
            35,
            new Dimension(70, 80), false);
        saveBtn = new Button(
            "Save",
            new ImageIcon("src/main/resources/icons8-save-30.png"),
            35,
            new Dimension(70, 80), false);
        saveBtn.btn.setEnabled(false);
        clearMemoryBtn = new Button(
            "Clear memory!",
            new ImageIcon("src/main/resources/icons8-memory-slot-30.png"),
            35,
            new Dimension(70, 80),
            false);

        filePanel.add(openImagesBtn);
        filePanel.add(openWorkspaceBtn);
        filePanel.add(saveBtn);
        filePanel.add(clearMemoryBtn);

        JPanel boosterPanel = new JPanel(new FlowLayout());
        boosterPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLUE),
            "Booster",
            TitledBorder.CENTER,
            TitledBorder.CENTER));
        
        boxBoosterBtn = new Button(
            "Box booster",
            new ImageIcon("src/main/resources/icons8-rectangle-30.png"),
            35,
            new Dimension(70, 80), true);
        boxGeneratorBtn = new Button(
            "Box generator",
            new ImageIcon("src/main/resources/icons8-engine-30.png"),
            35,
            new Dimension(70, 80), false);
        
        boosterPanel.add(boxBoosterBtn);
        boosterPanel.add(boxGeneratorBtn);

        JPanel editPanel = new JPanel();
        editPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLUE),
            "Edit",
            TitledBorder.CENTER,
            TitledBorder.CENTER));
        
        clearBtn = new Button(
            "Clear all boxes!",
            new ImageIcon("src/main/resources/icons8-clear-30.png"),
            35,
            new Dimension(70, 80), false);
        moveModeBtn = new Button(
            "Move mode",
            new ImageIcon("src/main/resources/icons8-move-30.png"),
            35,
            new Dimension(70, 80), true);
        
        editPanel.add(clearBtn);
        editPanel.add(moveModeBtn);

        JPanel viewPanel = new JPanel(new FlowLayout());
        viewPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLUE),
            "View",
            TitledBorder.CENTER,
            TitledBorder.CENTER));
        zoomInBtn = new Button(
            "Zoom in!",
            new ImageIcon("src/main/resources/icons8-zoom-in-30.png"),
            35,
            new Dimension(70, 80), false);
        zoomOutBtn = new Button(
            "Zoom out!",
            new ImageIcon("src/main/resources/icons8-zoom-out-30.png"),
            35,
            new Dimension(70, 80), false);
        viewPanel.add(zoomInBtn);
        viewPanel.add(zoomOutBtn);

        JPanel toolsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolsPanel.add(filePanel);
        toolsPanel.add(boosterPanel);
        toolsPanel.add(editPanel);
        toolsPanel.add(viewPanel);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchArea = new JTextField(10);
        searchArea.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        searchBtn = new JButton(new ImageIcon("src/main/resources/icons8-search-25.png"));
        searchBtn.setPreferredSize(new Dimension(30, 30));
        searchPanel.add(searchArea, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);
        filenamesListModel = new DefaultListModel<>();
        filenamesList = new JList<>(filenamesListModel);
        filenamesList.setFixedCellWidth(200);
        JScrollPane filenamesListScroll = new JScrollPane(filenamesList);
        filenamesListScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        

        JPanel imgsListPanel = new JPanel(new BorderLayout(0, 10));
        imgsListPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Images"));
        imgsListPanel.add(searchPanel, BorderLayout.NORTH);
        imgsListPanel.add(filenamesListScroll, BorderLayout.CENTER);

        JPanel classesEditPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addClassBtn = new JButton(new ImageIcon("src/main/resources/icons8-create-30.png"));
        addClassBtn.setPreferredSize(new Dimension(35, 35));
        removeClassBtn = new JButton(new ImageIcon("src/main/resources/icons8-remove-30.png"));
        removeClassBtn.setPreferredSize(new Dimension(35, 35));
        removeClassBtn.setEnabled(false);
        classesEditPanel.add(addClassBtn);
        classesEditPanel.add(removeClassBtn);
        classesListModel = new DefaultListModel<>();
        classesList = new JList<>(classesListModel);
        classesList.setCellRenderer(new ClassItemRenderer());

        JPanel classesPanel = new JPanel(new BorderLayout());
        JScrollPane classesListScroll = new JScrollPane(classesList);
        classesListScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        classesList.setVisibleRowCount(10);
        classesList.setFixedCellWidth(200);
        classesListScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Classes"));
        classesPanel.add(classesListScroll, BorderLayout.CENTER);
        classesPanel.add(classesEditPanel, BorderLayout.SOUTH);

        JPanel objectsPanel = new JPanel(new BorderLayout());
        canvas = new Canvas();
        objList = new ObjectList(canvas);
        JScrollPane objsListScroll = new JScrollPane(objList);
        canvas.list = objList;
        canvas.classesModel = classesListModel;
        objsListScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Objects"));
        objectsPanel.add(classesPanel, BorderLayout.NORTH);
        objectsPanel.add(objsListScroll, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(imgsListPanel, BorderLayout.WEST);
        JScrollPane canvasScroll = new JScrollPane(canvas);
        canvasScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Canvas"));
        controlPanel.add(canvasScroll, BorderLayout.CENTER);
        controlPanel.add(objectsPanel, BorderLayout.EAST);
        
        mainPanel.add(toolsPanel, BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.CENTER);

        this.getContentPane().add(mainPanel);
        this.setSize(500, 500);
        this.setDefaultCloseOperation(3);
        this.setVisible(true);
    }

    private void initListeners() {
        addClassBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                creatorDialog.setVisible(true);
                if (creatorDialog.name != null) {
                    classesListModel.addElement(
                        new ClassItem(creatorDialog.name, creatorDialog.id, creatorDialog.color));
                }
            }
        });

        removeClassBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                List<ClassItem> classes = classesList.getSelectedValuesList();
                for (ClassItem item : classes) {
                    classesListModel.removeElement(item);
                }
            }
        });

        classesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (classesList.getSelectedValuesList().size() > 0) {
                    removeClassBtn.setEnabled(true);
                } else {
                    removeClassBtn.setEnabled(false);
                }
            }
        });

        openImagesBtn.btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int status = fileChooser.showOpenDialog(App.this);
                if (status == 0) {
                    imgsFolder = fileChooser.getSelectedFile();
                    for (File file : imgsFolder.listFiles()) {
                        String fname = file.getName();
                        if (file.isFile() && (fname.endsWith(".png") || fname.endsWith(".jpg") || fname.endsWith(".jpeg"))) {
                            filenamesListModel.addElement(fname);
                        }
                    }
                }
            }
        });

        filenamesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {

                if (arg0.getValueIsAdjusting())
                    return;

                if (workingOn != null) {
                    ArrayList<ImageObject> objs = new ArrayList<>(canvas.objs);
                    objs.addAll(objList.hiddens.stream().map(item -> item.obj).toList());
                    if (!objs.isEmpty()) {
                        root.put(workingOn, objs);
                    }
                }

                String selected = filenamesList.getSelectedValue();

                workingOn = selected;
                if (selected == null) 
                    return;

                File imgFile = new File(imgsFolder, selected);

                try {
                    canvas.img = ImageIO.read(imgFile);
                } catch (IOException e) {}

                ArrayList<ImageObject> targetObjs = root.get(selected);

                objList.layoutPanel.removeAll();
                objList.selects.clear();
                objList.hiddens.clear();
                if (targetObjs != null) {
                    canvas.objs = targetObjs;
                    for (ImageObject obj : canvas.objs) {
                        objList.addItemComponent(obj, canvas.getClassName(obj.clazz));
                    }
                } else {
                    canvas.objs.clear();
                }
                canvas.selects.clear();
                
                objList.revalidate();
                canvas.repaint();
            }
        });

        zoomInBtn.btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                canvas.zoom += 0.2;
                canvas.revalidate();
            }
        });

        zoomOutBtn.btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                canvas.zoom = Math.max(0.9, canvas.zoom - 0.2);
                canvas.revalidate();
            }
        });

        moveModeBtn.btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (moveModeBtn.btn.isSelected()) {
                    canvas.moveMode = true;
                    if (canvas.selects.isEmpty() && !canvas.objs.isEmpty()) {
                        objList.addToSelects(canvas.objs.get(0));
                        objList.updateSelectsUI(true);
                    }
                } else {
                    canvas.moveMode = false;
                }
            }
        });

        clearBtn.btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                canvas.selects.clear();
                canvas.objs.clear();
                objList.selects.clear();
                objList.hiddens.clear();
                objList.layoutPanel.removeAll();
                root.remove(filenamesList.getSelectedValue());
                canvas.repaint();
                objList.revalidate();
            }
        });

        clearMemoryBtn.btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                root.clear();
            }
        });

        saveBtn.btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                /*--------------------------------*
                 | Save objects for current image |
                 *--------------------------------*/
                // visibles + hiddens
                ArrayList<ImageObject> currentImageObject = new ArrayList<>(canvas.objs);
                currentImageObject.addAll(objList.hiddens.stream().map(x -> x.obj).toList());
                // put to `ROOT` hashmap
                root.put(filenamesList.getSelectedValue(), currentImageObject);

                
                LogDialog log = new LogDialog(App.this);
                log.setVisible(true);
                log.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                new Thread(() -> {
                    for (String filename : root.keySet()) {
                        File resultFile = new File(workspace, filename.replaceAll("\\.[a-zA-Z]+$", ".txt"));
                        ArrayList<ImageObject> objs = root.get(filename);
                        if (objs.size() == 0) continue;
                        String[] lines = new String[objs.size()];
                        int counter = 0;
                        for (ImageObject obj : objs) {
                            Box cxcywh = obj.box.cxcywh();
                            lines[counter] = obj.clazz + " " + cxcywh.x1 + " " + cxcywh.y1 + " " + cxcywh.x2 + " " + cxcywh.y2;
                            counter++;
                        }
                        try {
                            FileWriter fw = new FileWriter(resultFile);
                            fw.write(String.join("\n", lines));
                            fw.close();
                            log.addLine(resultFile.getName() + " [DONE]");
                        } catch (IOException e) {
                            log.addLine(resultFile.getName() + " [FAILED]");
                        }
                    }
                    log.finish();
                }).start();
            }
        });

        openWorkspaceBtn.btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int status = fileChooser.showOpenDialog(App.this);
                if (status == 0) {
                    workspace = fileChooser.getSelectedFile();
                    App.this.setTitle("labelDoc 0.2 - " + workspace.getAbsolutePath());
                }
                if (workspace == null) {
                    saveBtn.btn.setEnabled(false);
                } else {
                    saveBtn.btn.setEnabled(true);
                }
            }
        });

    }

}
