package graphics;

import com.formdev.flatlaf.FlatLightLaf;
import core.data.ArrayData;
import core.network_components.network_classes.LinearNetwork;
import core.network_components.validation_functions.OneHotGreatest;
import utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class DoodleDigitsWindow extends JFrame {

    public void classify() {
        try {
            // Preprocess -- bounding box
            BufferedImage trimmed = ImageUtils.trim(doodle);
            boolean fw = trimmed.getWidth() > trimmed.getHeight();
            iIm = new BufferedImage(fw ? trimmed.getWidth() : trimmed.getHeight(),fw ? trimmed.getWidth() : trimmed.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
//            iIm = new BufferedImage(300,300,BufferedImage.TYPE_INT_RGB);
            iIm.createGraphics();
            iIm.getGraphics().setColor(Color.WHITE);
            iIm.getGraphics().fillRect(0, 0, iIm.getWidth(), iIm.getHeight());
            iIm.getGraphics().drawImage(trimmed,
                    fw ? 0 : (iIm.getWidth() - trimmed.getWidth())/2,
                    fw ? (iIm.getHeight() - trimmed.getHeight())/2 : 0,
                    trimmed.getWidth(),
                    trimmed.getHeight(), null);
//            iIm.getGraphics().drawImage(trimmed,0,0,300,300,null);
        } catch (Exception e) { e.printStackTrace(); }


//        iIm = ImageUtils.resize(iIm, 8, 8,Image.SCALE_REPLICATE);
        //smooth decimal
        // Polar black or white
//        iIm = ImageUtils.resize(iIm, 8, 8,Image.SCALE_DEFAULT);

//        iIm = ImageUtils.resize(iIm, 8, 8,Image.SCALE_SMOOTH);
        iIm = ImageUtils.resize(iIm, 8, 8,Image.SCALE_AREA_AVERAGING);
        Graphics2D pen = (Graphics2D) scaledImageRenderer.getDrawGraphics();
        pen.drawImage(iIm,15, 0,100,100,null);
//        pen.setColor(Color.BLACK);
//        pen.fillRect(0,0,100,100);
        scaledImageRenderer.show();

        ArrayData inputData = new ArrayData();
        for (int y = 0; y < iIm.getHeight(); y++) {
            for (int x = 0; x < iIm.getWidth(); x++) {
                inputData.add((double) (255 - new Color(iIm.getRGB(x, y)).getGreen()) * (double) 16 / (double) 256);
            }
        }
        ArrayData prediction = nn.predict(inputData);
        System.out.print(prediction + "\r");
        double[] largest = OneHotGreatest.largestIndexValue(prediction);
         number.setText(largest[1]>0.5d ? (int)largest[0] + "" : "?");
    }


    LinearNetwork nn;
    Canvas canvas;
    JPanel infoPanel;
    JPanel iSpyPanel;
    JPanel numberPanel;
    JTextArea iSpy;
    JLabel number;

    BufferStrategy renderer;
    BufferedImage doodle;
    Graphics2D p;

    Canvas scaledImageCanvas;
    BufferStrategy scaledImageRenderer;
    JPanel scaledImagePanel;
    BufferedImage iIm;

    final int pw = 40;
    int penWidth = pw;
    Stroke penStroke = new BasicStroke(penWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    boolean eraser = false;
    Point mousePos;

    private void makeStroke() {
        penWidth = eraser ? (int) (pw * ((double) 35 / (double) 20)) : pw;
        penStroke = new BasicStroke(penWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }

    public void draw() {
        Graphics2D pen = (Graphics2D) renderer.getDrawGraphics();
        pen.setColor(Color.WHITE);
        pen.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        pen.drawImage(doodle, 0, 0, null);
        pen.setColor(eraser ? Color.WHITE : Color.BLACK);
        if (mousePos != null) pen.fillOval(mousePos.x - penWidth / 2, mousePos.y - penWidth / 2, penWidth, penWidth);
        renderer.show();
    }

    public void drawingStuff() {
        doodle = new BufferedImage(300, 300, BufferedImage.TYPE_BYTE_GRAY);
        p = doodle.createGraphics();
        p.setColor(Color.WHITE);
        p.fillRect(0, 0, doodle.getWidth(), doodle.getHeight());
        DrawHandler inputHandler = new DrawHandler();
        canvas.addMouseListener(inputHandler);
        canvas.addMouseMotionListener(inputHandler);
        canvas.addKeyListener(inputHandler);
        canvas.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                draw();
            }
        });
        scaledImageCanvas.createBufferStrategy(2);
        scaledImageRenderer = scaledImageCanvas.getBufferStrategy();
        MouseListener ll = new InfoPanelMouseListener();
        infoPanel.addMouseListener(ll);
        scaledImagePanel.addMouseListener(ll);
        scaledImageCanvas.addMouseListener(ll);
        numberPanel.addMouseListener(ll);
        scaledImagePanel.setVisible(true);
    }

    private class InfoPanelMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            scaledImagePanel.setVisible(!scaledImagePanel.isVisible());
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private class DrawHandler implements MouseListener, MouseMotionListener, KeyListener {

        private long lastClicked = 0;
        private static final int doubleClickMillis = 250;

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        public void mouseDoubleClicked(MouseEvent e) {
            p.setColor(Color.WHITE);
            p.fillRect(0, 0, doodle.getWidth(), doodle.getHeight());
            draw();
            classify();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            p.setColor(eraser ? Color.WHITE : Color.BLACK);
            p.setStroke(penStroke);
            p.drawLine((int) mousePos.getX(), (int) mousePos.getY(), e.getX(), e.getY());
            mousePos = e.getPoint();
            draw();
            classify();


            if(System.currentTimeMillis() - lastClicked <= doubleClickMillis) {
                mouseDoubleClicked(e);
            }
            lastClicked = System.currentTimeMillis();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            p.setColor(eraser ? Color.WHITE : Color.BLACK);
            p.setStroke(penStroke);
            p.drawLine((int) mousePos.getX(), (int) mousePos.getY(), e.getX(), e.getY());
            mousePos = e.getPoint();
            draw();
            classify();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mousePos = e.getPoint();
            draw();
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == ' ') {
                eraser = !eraser;
                makeStroke();
                draw();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    public DoodleDigitsWindow(LinearNetwork digitClassfier) {
        super("Doodle Digits");
        FlatLightLaf.install();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) { e.printStackTrace(); }

        this.nn = digitClassfier;
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("data/appstuff/DDLW.png").getImage());
//        setIconImage(new ImageIcon("data/appstuff/DoodleDigitsIconRainbow.png").getImage());

        canvas = new Canvas();
        scaledImageCanvas = new Canvas();
        scaledImagePanel = new JPanel();
        infoPanel = new JPanel();
        iSpyPanel = new JPanel();
        numberPanel = new JPanel();
//        iSpy = new JTextArea("Pred:");
        number = new JLabel("0");
        number.setForeground(new Color(1, 31, 21));
        scaledImagePanel.setForeground(new Color(1, 31, 21));
//        number.setForeground(Color.GRAY);
        number.setFont(new Font("Roboto", Font.PLAIN, 70));
//        JPanel instP = new JPanel();
//        instP.setForeground(Color.WHITE);
//        instP.setBackground(Color.WHITE);
////        instP.setBackground(new Color(213, 213, 213));
//        instP.setLayout(new BoxLayout(instP,BoxLayout.Y_AXIS));
//        instP.setLayout(new GridBagLayout());
//        instP.setLayout(new GridBagLayout());
//        JTextArea inst = new JTextArea("Double-click to clear | Space to toggle eraser\nClick info panel to toggle input show\nClick this message to close");
//        LinkedList<JLabel> messages = new LinkedList<>();
//        messages.add(new JLabel("Double-click to clear | Space to toggle eraser"));
//        messages.add(new JLabel("Click info panel to toggle input show"));
//        messages.add(new JLabel("Click this message to dismiss it"));
//        messages.forEach(m->{
//            JPanel pp = new JPanel();
//            m.setPreferredSize(new Dimension(250,20));
//            pp.setMaximumSize(new Dimension(10000,20));
//            m.setMaximumSize(new Dimension(10000,20));
//            m.setForeground(new Color(130, 130, 130));
//            m.setBackground(new Color(213, 213, 213));
//            pp.add(m);
//            instP.add(pp);
//        });
//        messages.get(1).setBackground(new Color(130, 130, 130));
//        messages.get(1).setForeground(Color.WHITE);
////        messages.get(1).setForeground(new Color(243, 243, 243));
//        messages.get(1).getParent().setBackground(new Color(190, 190, 190));
//        instP.setMaximumSize(new Dimension(10000,20*3));
//
//        instP.setSize(instP.getHeight(),60);
//        instP.add(inst);
//        inst.setForeground(Color.WHITE);
//        inst.setForeground(new Color(130, 130, 130));
//        inst.setBackground(new Color(213, 213, 213));

        getContentPane().setBackground(Color.WHITE);
        setForeground(Color.WHITE);
        canvas.setBackground(Color.WHITE);
        iSpyPanel.setBackground(Color.WHITE);
//        numberPanel.setForeground(Color.LIGHT_GRAY);
        numberPanel.setBackground(Color.WHITE);
//        numberPanel.setBackground(new Color(236, 239, 248));
        infoPanel.setBackground(Color.WHITE);
        scaledImageCanvas.setBackground(Color.WHITE);
        scaledImagePanel.setBackground(Color.WHITE);
//        infoPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        numberPanel.setBorder(BorderFactory.createTitledBorder("Determined Digit"));
        scaledImagePanel.setBorder(BorderFactory.createTitledBorder("Input Image"));
//        infoPanel.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));

        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS));
        getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        numberPanel.setLayout(new GridBagLayout());

        scaledImagePanel.add(scaledImageCanvas);

        numberPanel.add(number);
//        iSpyPanel.add(iSpy);
//        infoPanel.add(iSpyPanel);
        infoPanel.add(scaledImagePanel);
        infoPanel.add(numberPanel);
//        add(instP);
        add(canvas);
        add(infoPanel);

        iSpyPanel.setSize(new Dimension(150, 150));
        iSpyPanel.setPreferredSize(new Dimension(150, 150));
        numberPanel.setSize(new Dimension(140, 140));
        numberPanel.setPreferredSize(new Dimension(140, 140));
        numberPanel.setMaximumSize(new Dimension(140, 140));
        canvas.setSize(new Dimension(300, 300));
        canvas.setPreferredSize(new Dimension(300, 300));
        scaledImageCanvas.setSize(new Dimension(130, 110));
        scaledImageCanvas.setPreferredSize(new Dimension(130, 110));
        scaledImagePanel.setSize(new Dimension(140, 140));
        scaledImagePanel.setPreferredSize(new Dimension(140, 140));
        scaledImagePanel.setMaximumSize(new Dimension(140, 140));

        setResizable(false);
        setSize(new Dimension(300, 485));

        canvas.createBufferStrategy(2);
        renderer = canvas.getBufferStrategy();

        drawingStuff();

        classify();

        JOptionPane.showMessageDialog(canvas,"[✎] Drag pen and draw numbers!\n[✌] Double-click canvas to clear\n[⎵] Click space to toggle eraser mode\n\n[▤] Input image is automatically resized\n[\uD83D\uDC49] Click below image to hide/show input image preview\n\n- Written by Micah Powch -","Info",JOptionPane.INFORMATION_MESSAGE);
    }
}
