package controllers;

import com.avaje.ebean.Ebean;
import models.Picture;
import models.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Initialize the DB with admin user and default pictures.
     */
    public void init() {
        User admin = new User("admin", "admin@test.at", "secret");
        admin.setActive(true);

        // init user table
        if (User.find.all().isEmpty()) {
            registerUser(admin);
        }

        // init picture table
        if (Picture.find.all().isEmpty()) {
            List<BufferedImage> images = new ArrayList<BufferedImage>();
            for (int i = 1; i <= 13; i++) {
                try {
                    images.add(ImageIO.read(new File("public/images/pictures/" + i + ".jpg")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            BufferedImage img = images.get(0);
            Picture pic = new Picture(48.858249354605626, 2.2944045066833496, "Eiffel tower",
                    "The Eiffel tower is a 324m high iron tower located in Paris/France.", img.getHeight(), img.getWidth(), admin);
            pic.setAccepted(true);
            savePicture(pic);

            img = images.get(1);
            pic = new Picture(48.18463003003112, 16.312079429626465, "Schönbrunn Palace",
                    "The palace is located in Vienna/Austria and is one of the most important " +
                            "architectural and historical monuments in the country.", img.getHeight(), img.getWidth(), admin);
            pic.setAccepted(true);
            savePicture(pic);

            img = images.get(2);
            pic = new Picture(35.360561218306344, 138.72779242694378, "Mount Fuji",
                    "Mount Fuji is a volcano and also the highest mountain of Japan, " +
                            "located in Honshu Island.", img.getHeight(), img.getWidth(), admin);
            pic.setAccepted(true);
            savePicture(pic);

            img = images.get(3);
            pic = new Picture(41.89020210802678, 12.492313385009766, "Colosseum",
                    "The Colosseum is the largest amphitheatre in the world and is located " +
                            "in the centre of the city of Rome/Italy.", img.getHeight(), img.getWidth(), admin);
            pic.setAccepted(true);
            savePicture(pic);

            img = images.get(4);
            pic = new Picture(-22.952011341775687, -43.210596442222595, "Cristo Redentor",
                    "Cristo Redentor is a 30m high statue of Jesus Christ in Reo de Janiero/Brazil.", img.getHeight(), img.getWidth(), admin);
            pic.setAccepted(true);
            savePicture(pic);

            img = images.get(5);
            pic = new Picture(55.7525014854839, 37.62309908866882, "St. Basil's Cathedral",
                    "The Cathedral is a church in Red Square in Moscow/Russia and now used as a museum.", img.getHeight(), img.getWidth(), admin);
            pic.setAccepted(true);
            savePicture(pic);

            img = images.get(6);
            pic = new Picture(43.08277473065676, -79.0739893913269, "Niagara Falls",
                    "The Niagara Falls,  the collective name for three waterfalls, form the highest " +
                            "flow rate of any waterfall in the world and straddle the international " +
                            "border between Canada and the USA.", img.getHeight(), img.getWidth(), admin);
            pic.setAccepted(true);
            savePicture(pic);

            img = images.get(7);
            pic = new Picture(29.975828999267698, 31.130383014678955, "Pyramid of Khafre",
                    "The Pyramid of Khafre is the second-largest of the Egyptian Pyramids of Gizah.", img.getHeight(), img.getWidth(), admin);
            pic.setAccepted(true);
            savePicture(pic);

            img = images.get(8);
            pic = new Picture(48.860578691434604, 2.3375773429870605, "Louvre Museum",
                    "The Louvre Museum, located in Paris/France, is one of the world's largest museums " +
                            "and is famous for Leonardo da Vinci's Mona Lisa.", img.getHeight(), img.getWidth(), admin);
            pic.setAccepted(true);
            savePicture(pic);

            img = images.get(9);
            pic = new Picture(25.197145904278177, 55.2742338180542, "Burj Khalifa",
                    "The Burj Khalifa is with 829.8m the tallest building in the world, located in " +
                            "Dubai/United Arab Emirates.", img.getHeight(), img.getWidth(), admin);
            pic.setAccepted(true);
            savePicture(pic);

            img = images.get(10);
            pic = new Picture(40.68916428323798, -74.04452562332153, "Statue of Liberty",
                    "The Statue of Liberty, located on Liberty Island in New York City/USA, was a " +
                            "gift to the United States from the people of France.", img.getHeight(), img.getWidth(), admin);
            pic.setAccepted(true);
            savePicture(pic);

            img = images.get(11);
            pic = new Picture(38.89756277173564, -77.03654050827026, "White House",
                    "The White House, located in Washington/D.C, is the official residence and principal " +
                            "workplace of the President of the United States.", img.getHeight(), img.getWidth(), admin);
            pic.setAccepted(true);
            savePicture(pic);

            img = images.get(12);
            pic = new Picture(37.81848862108136, -122.47840762138367, "Golden Gate Bridge",
                    "The Golden Gate Bridge is with 1280m the longest suspension brige of the world. " +
                            "The bridge is located between the San Francisco Bay and Marin County " +
                            "(both California/USA).", img.getHeight(), img.getWidth(), admin);
            pic.setAccepted(true);
            savePicture(pic);
        }
    }

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
}