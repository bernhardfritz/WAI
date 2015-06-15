package controllers;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PictureManager {

    private PictureManager(){
    }

    private static class SingletonHelper {
        private static final PictureManager INSTANCE = new PictureManager();
    }

    public static PictureManager getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public BufferedImage getScaledImage(BufferedImage img) {

        int newW = (360 * img.getWidth()) / img.getHeight();
        Image tmp = img.getScaledInstance(newW, 360, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, 360, BufferedImage.TYPE_INT_RGB);

        if (dimg.getWidth()>1080){
            int newH = (1080 * img.getHeight()) / img.getWidth();
            tmp = img.getScaledInstance(1080, newH, Image.SCALE_SMOOTH);
            dimg = new BufferedImage(1080, newH, BufferedImage.TYPE_INT_RGB);
        }

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public void saveToFile(BufferedImage img, int id) {
        try {
            ImageIO.write(img, "jpg", new File("public/images/pictures/" + id + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAsThumbnail(BufferedImage img, int id) {
        try {
            ImageIO.write(img, "jpg", new File("public/images/thumbnails/" + id + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public static BufferedImage createThumbnail(BufferedImage img, int size) { //creates a thumbnail of a picture by cutting off, the longer sides and then skaling the picture
        if (img.getWidth() < size && img.getHeight() < size) {
            return img;
        } else {

            BufferedImage square = null;

            if ((img.getWidth() - img.getHeight()) == 0) {
                square =img;
            }

            if ((img.getWidth() - img.getHeight()) > 0) {
                int difference = img.getWidth() - img.getHeight();
                square = img.getSubimage((difference / 2), 0, img.getWidth() - difference, img.getHeight());

            }
            if ((img.getWidth() - img.getHeight()) < 0) {
                int difference = img.getHeight() - img.getWidth();
                square = img.getSubimage(0, (difference / 2), img.getWidth(), img.getHeight() - difference);
            }

            Image scaled_square = square.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            BufferedImage result = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);


            Graphics2D g2d = result.createGraphics();
            g2d.drawImage(scaled_square, 0, 0, null);
            g2d.dispose();

            return result;
        }
    }

}
