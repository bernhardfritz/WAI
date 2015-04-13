package controllers;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by philipphausle on 13.04.15.
 */
public class Resize {

    private static class SingletonHelper {
        private static final Resize INSTANCE = new Resize();
    }

    public static Resize getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public BufferedImage scale(BufferedImage img){

            int newW = (360 * img.getWidth()) / img.getHeight();
            Image tmp = img.getScaledInstance(newW, 360, Image.SCALE_SMOOTH);
            BufferedImage dimg = new BufferedImage(newW, 360, BufferedImage.TYPE_INT_ARGB);

            if (dimg.getWidth()>1080){
                int newH = (1080 * img.getHeight()) / img.getWidth();
                tmp = img.getScaledInstance(1080, newH, Image.SCALE_SMOOTH);
                dimg = new BufferedImage(1080, newH, BufferedImage.TYPE_INT_ARGB);
            }


            Graphics2D g2d = dimg.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();

        return dimg;
    }

}
