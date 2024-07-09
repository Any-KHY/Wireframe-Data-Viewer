/*
Start up code for 159.235 Assignment 2

The Main() class is the "controller". This controls the other components according settings
on the sliders and buttons.

You will not need to make large changes to this - just some minor additions need to get it to respond
appropriately according to the slider and button settings

 */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

public class Main extends JFrame implements ActionListener, ChangeListener {

    // Dimensions of JFrame
    static private final int FRAME_WIDTH = 900;
    static private final int FRAME_HEIGHT = 800;
    static private final int PANEL_HEIGHT = 600;

    // Menu items
    private JMenuItem openItem, quitItem, helpItem;

    // Buttons to change the size of the figure
    private final JButton biggerButton, smallerButton;

    // Sliders for rotation angles
    private final JSlider sliderXY, sliderXZ, sliderYZ;
    static private final int SLIDER_MIN = 0;
    static private final int SLIDER_MAX = 360;
    static private final int SLIDER_INIT = 0;

    // Anti-aliasing buttons
    private final JRadioButton aaOff, aaOn;
    private boolean antiAlias = false;

    public static Wireframe wired = null;

    public static Transform3d getTransform() {
        return transform;
    }

    private static Transform3d transform = null;

    // JPanel to display the image
    private final DisplayPanel displayP;

    // Need this for the meu items and buttons
    public void actionPerformed(ActionEvent event) {

        JComponent source = (JComponent) event.getSource();

        if (source == openItem) {
            JFileChooser chooser = new JFileChooser("./");
            int retVal = chooser.showOpenDialog(this);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File myFile = chooser.getSelectedFile();
                try {
                    wired = WireframeDataIO.read(myFile);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (source == quitItem) {
            System.exit(0);
        } else if (source == helpItem) {
        } else if (source == biggerButton) {
            WireframeDrawer.scalingFactor *= 1.1;
        } else if (source == smallerButton) {
            WireframeDrawer.scalingFactor *= 0.9;
        } else if (source == aaOff) {
            antiAlias = false;
        } else if (source == aaOn) {
            antiAlias = true;
        }
        displayP.repaint();
    }

    // Need this for the sliders
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        double angle;

        if (source.getValueIsAdjusting()) {
            angle = Math.toRadians(source.getValue());
            if (source == sliderXY) {
                transform.setXY(angle);
            }
            if (source == sliderYZ) {
                transform.setYZ(angle);
            }
            if (source == sliderXZ) {
                transform.setXZ(angle);
            }
            displayP.repaint();
        }

    }

    private void makeMenu() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        openItem = new JMenuItem("Open");
        quitItem = new JMenuItem("Quit");
        fileMenu.add(openItem);
        fileMenu.add(quitItem);

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        helpItem = new JMenuItem("Help");
        helpMenu.add(helpItem);
    }

    private static JSlider makeSlider(JPanel panel, String title) {
        JSlider slider = new JSlider(SLIDER_MIN, SLIDER_MAX, SLIDER_INIT);
        slider.setBorder(BorderFactory.createTitledBorder(title));
        slider.setMajorTickSpacing(90);
        slider.setMinorTickSpacing(30);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        panel.add(slider);
        return slider;
    }

    public Main() {
        super("159235 Assignment 2");

        transform = new Transform3d();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);

        // Set up menu bar, menus, and menu items
        makeMenu();
        openItem.addActionListener(this);
        quitItem.addActionListener(this);
        helpItem.addActionListener(this);

        // Get a reference to the JFrames content pane to which
        // JPanels will be added
        Container content = this.getContentPane();
        content.setLayout(null);

        // Will use 3 "control panels"
        JPanel controlP1 = new JPanel();   // for the sliders
        JPanel controlP2 = new JPanel();   // for the rescale buttons
        JPanel controlP3 = new JPanel();   // for the antialiasing toggle
        content.add(controlP1);
        content.add(controlP2);
        content.add(controlP3);

        // Arrange panels on the frame by setting appropriate positions and sizes
        controlP1.setBounds(new Rectangle(0, 0, 620, 100));
        controlP2.setBounds(new Rectangle(620, 0, 250, 50));
        controlP3.setBounds(new Rectangle(620, 50, 250, 50));

        // Set up the sliders
        sliderXY = makeSlider(controlP1, "XY Plane");
        sliderYZ = makeSlider(controlP1, "YZ Plane");
        sliderXZ = makeSlider(controlP1, "XZ Plane");
        sliderXY.addChangeListener(this);
        sliderYZ.addChangeListener(this);
        sliderXZ.addChangeListener(this);

        // Set up the rescale buttons
        controlP2.add(new JLabel("Scaling"));
        biggerButton = new JButton("+");
        smallerButton = new JButton("-");
        controlP2.add(biggerButton);
        controlP2.add(smallerButton);
        biggerButton.addActionListener(this);
        smallerButton.addActionListener(this);

        // Set up anti-aliasing toggle buttons
        controlP3.add(new JLabel("Anti-aliasing"));
        aaOff = new JRadioButton("Off");
        aaOn = new JRadioButton("On");
        aaOff.setSelected(true);
        controlP3.add(aaOff);
        controlP3.add(aaOn);
        ButtonGroup group = new ButtonGroup();
        group.add(aaOff);
        group.add(aaOn);
        aaOff.addActionListener(this);
        aaOn.addActionListener(this);

        // JPanel to display the drawn figure - implemented as inner class below
        displayP = new DisplayPanel();
        displayP.setBounds(new Rectangle(0, 100, FRAME_WIDTH, PANEL_HEIGHT));
        content.add(displayP);

        this.setVisible(true);

    }

    // An inner class to handle the final rendering of the figure
    class DisplayPanel extends JPanel {
        public void paintComponent(Graphics g) {
            // Place the origin at the centre of the panel and have the y-axis pointing up the screen
            if (wired != null) {
                Graphics2D g2 = (Graphics2D) g;
                g2.clearRect(0, 0, FRAME_WIDTH, PANEL_HEIGHT); // Must do this first
                g2.translate(0.5 * FRAME_WIDTH, 0.5 * PANEL_HEIGHT); // move the origin to the centre
                g2.scale(1, -1); // Flip the y-axis
                WireframeDrawer.draw(g2, wired, antiAlias);
            }
        }
    }

    // Program entry point
    public static void main(String[] args) {
        new Main();
    }
}
