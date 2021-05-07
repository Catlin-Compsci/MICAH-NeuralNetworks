package graphics;

import core.data.ArrayData;
import core.network_components.OneHotOutputTransformer;
import core.network_components.network_classes.LinearNetwork;
import core.network_components.validation_functions.OneHotGreatest;
import magick.ImageMagick;
import magick.MagickImage;
import magick.MagickLoader;
import utils.ImageUtils;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

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


//        iIm = ImageUtils.resize(doodle, 8, 8,Image.SCALE_REPLICATE);
        //smooth decimal
        // Polar black or white
//        iIm = ImageUtils.resize(doodle, 8, 8,Image.SCALE_DEFAULT);

        iIm = ImageUtils.resize(iIm, 8, 8,Image.SCALE_SMOOTH);
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
    }

    private class DrawHandler implements MouseListener, MouseMotionListener, KeyListener {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            p.setColor(eraser ? Color.WHITE : Color.BLACK);
            p.setStroke(penStroke);
            p.drawLine((int) mousePos.getX(), (int) mousePos.getY(), e.getX(), e.getY());
            mousePos = e.getPoint();
            draw();
            classify();
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
        this.nn = digitClassfier;
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
    }
}
