package utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class ImageUtils {

    // Copied from https://stackoverflow.com/questions/9417356/bufferedimage-resize
    public static BufferedImage resize(BufferedImage img, int newW, int newH, int hints) {
        Image tmp = img.getScaledInstance(newW, newH, hints);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    // Copied from http://www.java2s.com/example/java-utility-method/bufferedimage-trim/trim-bufferedimage-image-3fec3.html

    /**
     * Trim excess whitespace from the given image
     *
     * @param image
     * @return
     * @throws Exception
     */
    public static BufferedImage trim(BufferedImage image) throws Exception {

        Rectangle clip = getClipRectangle(image, new int[]{255,255,255,255});
        return crop(image, clip);

    }

    /**
     * @param img
     * @param bgColor
     * @param
     * @return
     */
    public static Rectangle getClipRectangle(BufferedImage img, int[] bgColor) {

        int[] aux = { 255, 255, 255, 255 };
//        int[] bgColor = { 255, 255, 255 , 255};

        Raster raster = img.getRaster();

        /*
         * Retrieve the background color
         */
//        bgColor = raster.getPixel(x, y,bgColor);

        Point tl = new Point(0, 0);
        Point br = new Point(raster.getWidth() - 1, raster.getHeight() - 1);

        /*
         * Find the left border
         */
        boolean gotLef = false;
        for (int c = 0; !gotLef && (c < raster.getWidth()); c++) {
            for (int r = 0; r < raster.getHeight(); r++) {
                int[] pix = raster.getPixel(c, r, aux);
                if (comparePixel(bgColor, pix)) {
                    tl.x = c;
                    gotLef = true;
                    break;
                }
            }
        }

        /*
         * Find the right border
         */
        boolean gotRig = false;
        for (int c = raster.getWidth() - 1; !gotRig && (c >= 0); c--) {
            // Find the right
            for (int r = 0; r < raster.getHeight(); r++) {
                int[] pix = raster.getPixel(c, r, aux);
                if (comparePixel(bgColor, pix)) {
                    br.x = c;
                    gotRig = true;
                    break;
                }
            }
        }

        /*
         * Find the top border
         */
        boolean gotTop = false;
        for (int r = 0; !gotTop && (r < raster.getHeight()); r++) {
            for (int c = tl.x; c < br.x; c++) {
                int[] pix = raster.getPixel(c, r, aux);
                if (comparePixel(bgColor, pix)) {
                    tl.y = r;
                    gotTop = true;
                    break;
                }
            }
        }

        /*
         * Find the bottom border
         */
        boolean gotBot = false;
        for (int r = raster.getHeight() - 1; !gotBot && (r >= 0); r--) {
            for (int c = tl.x; c < br.x; c++) {
                int[] pix = raster.getPixel(c, r, aux);
                if (comparePixel(bgColor, pix)) {
                    br.y = r;
                    gotBot = true;
                    break;
                }
            }
        }

        Rectangle rect = new Rectangle(tl.x, tl.y, Math.abs(br.x - tl.x) + 1, Math.abs(br.y - tl.y) + 1);
        return rect;

    }

    /**
     * @param image
     * @param clip
     * @return
     * @throws Exception
     */
    public static BufferedImage crop(BufferedImage image, Rectangle clip) throws Exception {

        /*
         * Create the return image
         */
        BufferedImage retval = createImage(clip.width, clip.height);
        Graphics2D g2 = retval.createGraphics();

        /*
         * Render the clip region
         */
        g2.drawImage(image.getSubimage(clip.x, clip.y, clip.width, clip.height), 0, 0, null);

        g2.dispose();
        retval.flush();

        return retval;

    }

    /**
     * Compares two pixels and returns true if they
     * are different, otherwise returns false
     *
     * @param p1
     * @param p2
     * @return
     */
    public static boolean comparePixel(int[] p1, int[] p2) {
        return ((p2[0] == p1[0]) && (p2[1] == p1[1]) && (p2[2] == p1[2]) && (p2[3] == p1[3]) ? false : true);
    }

    /**
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage createImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    }
}
