package controllers;

import com.avaje.ebean.Ebean;
import models.Picture;
import models.User;
import org.joda.time.LocalDateTime;
import play.libs.Yaml;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBManager {
    private DBManager() {
        init();
    }

    private static class SingletonHelper {
        private static final DBManager INSTANCE = new DBManager();
    }

    public static DBManager getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private void init() {
        Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data.yml");

        // init user table
        if (User.find.all().isEmpty()) {
            registerUser((User) all.get("users").get(0));
        }

        // init picture table
        if (Picture.find.all().isEmpty()) {
            List<Object> pictures = all.get("pictures");

            for (int i = 1; i <= 13; i++) {
                try {
                    BufferedImage img = ImageIO.read(new File("public/images/pictures/" + i + ".jpg"));
                    Picture pic = (Picture) pictures.get(i-1);
                    pic.setHeight(img.getHeight());
                    pic.setWidth(img.getWidth());
                    pic.setUpdloadDate(LocalDateTime.now());
                    savePicture(pic);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }


    /* =========================== User functions =========================== */

    /**
     * Register new user to the DB.
     * @param user
     */
    public void registerUser(User user) {
        user.save();
    }

    /**
     * Authenticate user with username and password.
     * @param username
     * @param password
     * @return The respective user or NULL if the authentication failed.
     */
    public User getUser(String username, String password) {
        return User.find.where().ieq("name", username).ieq("password", HashManager.getInstance().codeString(password)).ieq("active", "1").findUnique();
    }

    /**
     * Get user from id.
     * @param id
     * @return The respective user or NULL if there is no active user with that id.
     */
    public User getUser(Long id) {
        return User.find.where().ieq("id", id.toString()).ieq("active", "1").findUnique();
    }

    /**
     * Get user from username.
     * @param username
     * @return The respective user or NULL if there is no active user with that username.
     */
    public User getUser(String username) {
        return User.find.where().ieq("name", username).ieq("active", "1").findUnique();
    }

    /**
     * Get a list of all registered and active users.
     * @return A list of all registered and active users.
     */
    public List<User> getAllUsers() {
        return User.find.where().ieq("active", "1").findList();
    }

    public void changeUserPassword(User user, String password) {
        user.setPassword(password);
        user.save();
    }


    /* =========================== Picture functions =========================== */

    /**
     * Save a picture to the DB.
     * @param picture
     */
    public void savePicture(Picture picture) {
        picture.save();
    }

    /**
     * Get a picture from id.
     * @param id
     * @return The respective picture or NULL, if there is no accepted picture with that id.
     */
    public Picture getPicture(Long id) {
        Picture p = Picture.find.where().ieq("id", id.toString()).ieq("accepted", "1").findUnique();
        if (p != null) {
            p.setCreateUser(getUser(p.getCreateUserID()));
        }
        return p;
    }

    /**
     * Get the number of all accepted pictures in the DB.
     * @return The number of all accepted pictures in the DB.
     */
    public int getPictureCount() {
        return Picture.find.where().ieq("accepted", "1").findList().size();
    }

    /**
     * Get all pictures from the DB.
     * @return All pictures from the DB.
     */
    public List<Picture> getAllPictures() {
        return Picture.find.all();
    }

    /**
     * Get all accepted pictures from the DB.
     * @return All accepted pictures from the DB.
     */
    public List<Picture> getActivePictures() {
        return Picture.find.where().ieq("accepted", "1").findList();
    }

    /**
     * Accept the given picture and save the changes to the DB.
     * @param picture
     */
    public void acceptPicture(Picture picture) {
        picture.setAccepted(true);
        savePicture(picture);
    }
}