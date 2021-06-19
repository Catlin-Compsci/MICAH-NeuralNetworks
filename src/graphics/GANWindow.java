package graphics;

import core.data.ArrayData;
import core.network_components.network_wrappers.ArrayDataToBufferedImage;
import core.network_components.network_wrappers.GANetwork;
import core.network_components.network_wrappers.Transformer;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GANWindow extends JFrame {

    GANetwork gan;
    ArrayDataToBufferedImage imageBarfer;

    Canvas canvas;

    public GANWindow(GANetwork gan, List<ArrayData> examples, double lRate, ArrayDataToBufferedImage imageBarfer) {
        super();
        setLocationRelativeTo(null);
        setVisible(true);

        this.gan = gan;
        this.imageBarfer = imageBarfer;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        JPanel boi = new JPanel();

        canvas = new Canvas();
//        add(boi);
        add(canvas);

        canvas.createBufferStrategy(2);

        setSize(new Dimension(600,600));
        setResizable(false);

        for (int i = 0; i < 300; i++) {
            ArrayData generated = gan.generate();
            System.out.println(generated);
            canvas.getBufferStrategy().getDrawGraphics().drawImage(imageBarfer.transform(generated),0,0,canvas.getWidth(),canvas.getHeight(),null);
            canvas.getBufferStrategy().show();
            gan.fitEpoch(examples,lRate);
//            try {
////                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            for(int ii = 0; ii<50; ii++) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                generated = gan.generate();
                System.out.println(generated);
                canvas.getBufferStrategy().getDrawGraphics().drawImage(imageBarfer.transform(generated),0,0,canvas.getWidth(),canvas.getHeight(),null);
                canvas.getBufferStrategy().show();
            }
        }

    }
}
