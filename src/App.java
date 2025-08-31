import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;

public class App extends JFrame {

    public App() {
        this.initComponents();
        this.initListeners();
    }

    private void initComponents() {

        filePanel = new JPanel();
        filePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        openImagesBtn = new Button("Open", new ImageIcon("icon/open_folder.png"), 100);
        openImagesBtn.setMargin(new Insets(0, 0, 0, 0));
        chooseWorkspaceBtn = new Button("Workspace", new ImageIcon("icon/workspace.png"), 100);
        chooseWorkspaceBtn.setMargin(new Insets(0, 0, 0, 0));
        saveBtn = new Button("Save", new ImageIcon("icon/save.png"), 100);
        saveBtn.setMargin(new Insets(0, 0, 0, 0));
        // yoloOrganizerBtn = new Button("Extract corresponding images", new ImageIcon("icon/photo.png"), 100);
        // yoloOrganizerBtn.setMargin(new Insets(0, 0, 0, 0));
        nextBtn = new JButton(">");
        previousBtn = new JButton("<");
        // commitBtn = new Button("Commit", new ImageIcon("icon/commit.png"), 100);
        // commitBtn.setMargin(new Insets(0, 0, 0, 0));

        filePanel.add(openImagesBtn);
        filePanel.add(chooseWorkspaceBtn);
        filePanel.add(saveBtn);
        JSeparator sep1 = new JSeparator(SwingConstants.VERTICAL);
        sep1.setPreferredSize(new Dimension(2, 20));
        filePanel.add(sep1);
        // filePanel.add(yoloOrganizerBtn);
        // filePanel.add(commitBtn);

        Dimension btnsMaxSize = new Dimension(30, 30);
        zoomInBtn = new JButton(new ImageIcon("icon/zoom_in.png"));
        zoomInBtn.setPreferredSize(btnsMaxSize);
        zoomOutBtn = new JButton(new ImageIcon("icon/zoom_out.png"));
        zoomOutBtn.setPreferredSize(btnsMaxSize);
        moveBtn = new JToggleButton(new ImageIcon("icon/move.png"));
        moveBtn.setPreferredSize(btnsMaxSize);
        autoResizeBtn = new JToggleButton(new ImageIcon("icon/bot.png"));
        autoResizeBtn.setPreferredSize(btnsMaxSize);

        viewPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        viewPanel.add(zoomInBtn);
        viewPanel.add(zoomOutBtn);
        viewPanel.add(moveBtn);
        viewPanel.add(autoResizeBtn);
        
        upPanel = new JPanel();
        upPanel.setLayout(new BoxLayout(upPanel, BoxLayout.Y_AXIS));
        upPanel.add(filePanel);
        upPanel.add(viewPanel);

        npPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        npPanel.add(previousBtn);
        npPanel.add(nextBtn);


        imgPanel = new ImagePanel();
        imgPanel.setFocusable(true);
        JScrollPane imgScroll = new JScrollPane(imgPanel);
        imgPanel.parent = imgScroll;

        logArea = new JTextArea();
        logArea.setRows(5);
        logArea.setBackground(Color.darkGray);
        logArea.setForeground(Color.CYAN);
        logArea.setWrapStyleWord(true);
        logArea.setLineWrap(true);
        JScrollPane logAreaScroll = new JScrollPane(logArea);
        logAreaScroll.setBorder(BorderFactory.createTitledBorder("Log"));

        centralPanel = new JPanel(new BorderLayout());
        centralPanel.add(npPanel, BorderLayout.NORTH);
        centralPanel.add(imgScroll, BorderLayout.CENTER);
        centralPanel.add(logAreaScroll, BorderLayout.SOUTH);
        centralPanel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        objLabel = new JLabel("Labels:");
        labelsArea = new JTextArea();
        labelsArea.setRows(5);
        JScrollPane labelScroll = new JScrollPane(labelsArea);
        objLabelPanel = new JPanel(new BorderLayout());
        objLabelPanel.add(objLabel, BorderLayout.NORTH);
        objLabelPanel.add(labelScroll, BorderLayout.CENTER);
        objLabelPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        objObj = new JLabel("Objects:");
        model = new DefaultListModel<>();
        imgObjectsList = new JList<>(model);
        JScrollPane objsListScroll = new JScrollPane(imgObjectsList);
        objsListScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        objObjPanel = new JPanel(new BorderLayout());
        objObjPanel.add(objObj, BorderLayout.NORTH);
        objObjPanel.add(objsListScroll, BorderLayout.CENTER);
        objObjPanel.setPreferredSize(new Dimension(200, -1));
        

        objPanel = new JPanel(new BorderLayout());
        objPanel.add(objLabelPanel, BorderLayout.NORTH);
        objPanel.add(objObjPanel, BorderLayout.CENTER);
        
        imgsListModel = new DefaultListModel<>();
        imgsList = new JList<File>();
        imgsList.setModel(imgsListModel);
        imgsListModel.addElement(new File("icon/welcome.jpg"));
        JScrollPane imgListScroll = new JScrollPane(imgsList);
        imgListScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        imgListScroll.setPreferredSize(new Dimension(165, -1));

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(imgListScroll, BorderLayout.WEST);
        mainPanel.add(centralPanel, BorderLayout.CENTER);
        mainPanel.add(objPanel, BorderLayout.EAST);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        appPanel = new JPanel(new BorderLayout());
        appPanel.add(upPanel, BorderLayout.NORTH);
        appPanel.add(mainPanel, BorderLayout.CENTER);
        appPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        this.getContentPane().add(appPanel);
        this.setSize(1000, 700);
        this.setTitle("LabelDoc");
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
    }

    private void initListeners() {

        imgPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                drag.startPoint = event.getPoint();
                imgPanel.requestFocus();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                if (drag.bbox == null) {
                    imgPanel.select(imgPanel.getObject(e.getPoint()), imgObjectsList);
                    imgPanel.repaint();
                    return;
                }

                Dictionary<Integer, String> idToLabel = extractLabels();
                if (idToLabel.size() == 0) {
                    JOptionPane.showMessageDialog(App.this, "Please create at least one label!");
                    imgPanel.removeLastRect();
                    drag.bbox = null;
                    drag.startPoint = null;
                    imgPanel.repaint();
                    return;
                }

                ListSelectionDialog lsd = new ListSelectionDialog(App.this, "Choose a label!", idToLabel.getValues().toArray(String[]::new));
                lsd.setVisible(true);
                String result = lsd.getSelectedValue();
                if (result == null) {
                    imgPanel.removeLastRect();
                } else {
                    PageObject lastObject = imgPanel.getLastObject();
                    lastObject.labelText = result;
                    lastObject.labelId = (int) idToLabel.getKeyByValue(result);

                    if (autoResizeBtn.isSelected()) {
                        ProcessBuilder pb = new ProcessBuilder(
                            "python3", "-u", "src/image.py",
                            makeValid(lastObject.bbox.x1) + "",
                            makeValid(lastObject.bbox.y1) + "",
                            makeValid(lastObject.bbox.x2) + "",
                            makeValid(lastObject.bbox.y2) + "",
                            "5", "5",
                            ((File) imgsList.getSelectedValue()).getAbsolutePath());
                        pb.directory(new File("."));
                        Process process = null;
                        try {process = pb.start();} catch (IOException e1) {}
                        String occupied = null;
                        if (process != null) {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            try {occupied = reader.readLine();} catch (IOException e1) {}
                        }
                        if (occupied != null) {
                            String[] sbbox = occupied.split(",\s*");
                            sbbox[0] = sbbox[0].replace("[", "");
                            sbbox[3] = sbbox[3].replace("]", "");
                            lastObject.bbox = new Rectangle(
                                makeValid(Double.parseDouble(sbbox[0])),
                                makeValid(Double.parseDouble(sbbox[1])),
                                makeValid(Double.parseDouble(sbbox[2])),
                                makeValid(Double.parseDouble(sbbox[3])));
                        }
                    }

                    model.addElement(lastObject);
                    logArea.append("+ You added an object:\n");
                    logArea.append(
                        "> Label: " + lastObject.labelText +
                        " - ID: " + lastObject.labelId +
                        " - Normilized bbox: [" + (Math.round(lastObject.bbox.x1 * 1000.0) / 1000.0) + "  "
                        + (Math.round(lastObject.bbox.y1 * 1000.0) / 1000.0) + "  "
                        + (Math.round(lastObject.bbox.x2 * 1000.0) / 1000.0) + "  "
                        + (Math.round(lastObject.bbox.y2 * 1000.0) / 1000.0) + "]\n");
                }
                imgObjectsList.setSelectedIndex(imgPanel.objects.size() - 1);
                imgPanel.repaint();
                drag.bbox = null;
                drag.startPoint = null;
            }
        });

        imgPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (moveMode.isActive) {
                    if (moveMode.onObject)
                        moveMode.setObjectBbox(imgPanel, drag, e.getPoint());
                } else if (corner.onCorner) {
                    drag.updateObjectBbox(imgPanel, corner, e.getPoint());
                } else {
                    drag.drawDragRect(imgPanel, e.getPoint());
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (moveMode.isActive) {
                    moveMode.update(imgPanel, e.getPoint());
                } else {
                    corner.update(imgPanel, e.getPoint());
                }
            }
        });

        moveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (moveBtn.isSelected()) {
                    moveMode.isActive = true;
                } else {
                    moveMode.isActive = false;
                }
            }
        });

        zoomInBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                imgPanel.zoomIn();
                imgPanel.revalidate();
                logArea.append("Zoom-in: " + (Math.round(imgPanel.zoom * 10.0) / 10.0) + "x\n");
            }
        });

        zoomOutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                imgPanel.zoomOut();
                imgPanel.revalidate();
                logArea.append("Zoom-out: " + (Math.round(imgPanel.zoom * 10.0) / 10.0) + "x\n");
            }
        });

        imgObjectsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    imgPanel.selected = (PageObject) imgObjectsList.getSelectedValue();
                    imgPanel.repaint();
                }
            }
            
        });

        openImagesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {

                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setDialogTitle("Choose images folder");
                int result = chooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    imgsListModel.clear();
                    File selectedFolder = chooser.getSelectedFile();
                    logArea.append("You selected `" + selectedFolder + "` as images folder.\n");
                    logArea.append("The files that aded to list:\n> ");
                    for (File child: selectedFolder.listFiles()) {
                        if (!child.isFile())
                            continue;
                        String ext = getFileExtension(child);
                        if (ext.equals("png") || ext.equals("jpg")) {
                            imgsListModel.addElement(child);
                            logArea.append(child.getName() + ", ");
                        }
                    }
                    if (imgsListModel.size() > 0) {
                        String logText = logArea.getText();
                        logArea.setText(logText.substring(0, logText.length() - 2) + "\n");
                    } else {
                        logArea.append("Nothing :)\n");
                    }
                }
            }
            
        });

        imgPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 127) {
                    imgPanel.selected = null;
                    PageObject target = (PageObject) imgObjectsList.getSelectedValue();
                    imgPanel.objects.remove(target);
                    model.removeElement(target);
                }
            }
        });

        imgsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {

                if (arg0.getValueIsAdjusting())
                    return;

                File selectedImg = (File) imgsList.getSelectedValue();

                if (selectedImg  == null)
                    return;
                
                File labelFile = new File(workspace, getFileNameWithoutExtention(selectedImg) + ".txt");
                if (labelFile.exists()) {
                    logArea.append("** You worked on " + selectedImg.getName() + " before :)\n");
                }

                if (currentPage != null) {
                    if (root.getKeys().contains(currentPage)) {
                        root.setValue(imgPanel.objects, currentPage);
                    } else {
                        root.add(currentPage, imgPanel.objects);
                    }
                }

                currentPage = selectedImg;

                ArrayList<PageObject> objs = (ArrayList<PageObject>) root.getValueByKey(currentPage);
                objs = (objs == null) ? new ArrayList<>() : objs;
                try {imgPanel.setImage(currentPage.getAbsolutePath());} catch (IOException e) {}
                imgPanel.objects = objs;
                imgPanel.highlight = null;
                imgPanel.selected = null;
                imgPanel.repaint();

                model.clear();
                for (PageObject po: objs) {
                    model.addElement(po);
                }
                
            }
        });

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {

                if (currentPage != null) {
                    if (root.getKeys().contains(currentPage)) {
                        root.setValue(imgPanel.objects, currentPage);
                    } else {
                        root.add(currentPage, imgPanel.objects);
                    }
                }

                for (int i=0; i<root.size(); i++) {
                    File imgFile = root.getKey(i);
                    ArrayList<PageObject> objects = root.getValue(i);
                    if (objects.size() == 0)
                        continue;
                    String fileNameWE = getFileNameWithoutExtention(imgFile);
                    String[] lines = new String[objects.size()];
                    int lineCounter = 0;
                    logArea.append(fileNameWE + ".txt is being saved ...\n");
                    for (PageObject po: objects) {
                        lines[lineCounter] = po.labelId + " " +
                        ((po.bbox.x1 + po.bbox.x2) / 2.0) + " " +
                        ((po.bbox.y1 + po.bbox.y2) / 2.0) + " " +
                        po.bbox.getWidth() + " " +
                        po.bbox.getHeight();
                        lineCounter++;
                    }
                    try {
                        FileWriter writer = new FileWriter(new File(workspace, fileNameWE + ".txt"));
                        writer.write(String.join("\n", lines));
                        writer.close();
                        logArea.append("+ Saved successfully :)\n");
                    } catch (IOException e) {
                        logArea.append("- Could not save! There is a problem :(\n");
                    }
                }

                try {
                    FileWriter fw = new FileWriter(new File(workspace, "classes.txt"));
                    fw.write(labelsArea.getText());
                    fw.close();
                    logArea.append("+ classes.txt save successfully :)\n");
                } catch (IOException e) {
                    logArea.append("- Could not save classes.txt :(\n");
                }
            }
        });

        chooseWorkspaceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setDialogTitle("Choose worksapce folder");
                int result = chooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    workspace = chooser.getSelectedFile();
                    logArea.append("You selected `" + workspace + "` as WORKSPACE.\n");
                }
                try {
                    String labelText = Files.readString(new File(workspace, "classes.txt").toPath());
                    labelsArea.setText(labelText);
                    logArea.append("classes.txt load successfully :)\n");
                } catch (IOException e) {
                    logArea.append("classes.txt could not load :(\n");
                }
            }
        });
    }

    public static void main(String[] args) throws Exception {
        App app = new App();
    }

    public static Rectangle buildRect(Point p1, Point p2) {
        return new Rectangle(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.max(p1.x, p2.x), Math.max(p1.y, p2.y));
    }

    public static double makeValid(double x) {
        if (x < 0.0)
            return 0.0001;
        if (x > 1.0)
            return 0.9999;
        return x;
    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastDotIndex = name.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < name.length() - 1) {
            return name.substring(lastDotIndex + 1).toLowerCase();
        } else
            return "";
    }

    public static String getFileNameWithoutExtention(File file) {
        String name = file.getName();
        int lastDotIndex = name.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < name.length() - 1) {
            return name.substring(0, lastDotIndex).toLowerCase();
        } else
            return name;
    }

    public Dictionary<Integer, String> extractLabels() {
        String text = labelsArea.getText();
        Dictionary<Integer, String> idToLabel = new Dictionary<>();
        for (String line: text.split("\n")) {
            int eqIndex = line.indexOf("=");
            try {
                int id = Integer.parseInt(line.substring(0, eqIndex).trim());
                String label = line.substring(eqIndex + 1, line.length()).trim();
                idToLabel.add(id, label);
            } catch (Exception e) {
                continue;
            }
        }
        idToLabel.sortByKeys(Comparator.naturalOrder());
        return idToLabel;
    }

    JButton openImagesBtn,
            chooseWorkspaceBtn,
            saveBtn,
            yoloOrganizerBtn,
            nextBtn,
            previousBtn,
            commitBtn,
            zoomInBtn,
            zoomOutBtn;

    JList imgsList, imgObjectsList;

    JTextArea logArea, labelsArea;

    JPanel filePanel,
            mainPanel,
            npPanel,
            objPanel,
            appPanel,
            centralPanel,
            objLabelPanel, 
            objObjPanel,
            viewPanel,
            upPanel;
    
    JToggleButton moveBtn, autoResizeBtn;
    
    ImagePanel imgPanel;

    JLabel objLabel, objObj;

    DefaultListModel<PageObject> model;
    DefaultListModel<File> imgsListModel;   

    Drag drag = new Drag();
    Corner corner = new Corner();
    MoveMode moveMode = new MoveMode();

    Dictionary<File, ArrayList<PageObject>> root = new Dictionary<>();
    File currentPage, workspace;
}
