package controllers;

import com.avaje.ebean.Expr;
import models.*;
import org.joda.time.LocalDateTime;
import play.libs.Yaml;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
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
            List<Object> users = all.get("users");

            for (Object o : users) {
                registerUser((User) o);
            }
        }

        // init picture table
        if (Picture.find.all().isEmpty()) {
            List<Object> pictures = all.get("pictures");

            for (int i = 1; i <= pictures.size(); i++) {
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

        // init game table
        if (Game.find.all().isEmpty()) {
            List<Object> games = all.get("games");

            for (Object o : games) {
                Game g = (Game) o;
                createGame(getUser(g.getUser1ID()), getUser(g.getUser2ID()));
            }
        }
    }


    /* =========================== Friends functions =========================== */

    /**
     * Save new friendship to the DB.
     * @param user1
     * @param user2
     */
    public void saveFriend(User user1, User user2) {
        List<User> savedFriends = getFriends(user1);
        if (!user1.equals(user2) && !savedFriends.contains(user2)) {
            new Friend(user1, user2).save();
        }
    }

    /**
     * Get all saved friends for a user.
     * @param user
     * @return All saved friends for a user.
     */
    public List<User> getFriends(User user) {
        List<User> friends = new ArrayList<User>();

        if (user != null) {
            for (Friend f : Friend.find.where().ieq("user1id", user.getId().toString()).findList()) {
                if (!friends.contains(f.getUser2ID())) {
                    friends.add(getUser(f.getUser2ID()));
                }
            }

            for (Friend f : Friend.find.where().ieq("user2id", user.getId().toString()).findList()) {
                if (!friends.contains(f.getUser1ID())) {
                    friends.add(getUser(f.getUser1ID()));
                }
            }
        }

        return friends;
    }


    /* =========================== Game functions =========================== */

    /**
     * Create a new game and save it to the DB.
     * @param user1
     * @param user2
     */
    public Game createGame(User user1, User user2) {
        Game game = new Game(user1, user2);
        game.save();

        List<Picture> pictures = new ArrayList<Picture>();
        for (int i = 0; i < 3; i++) {
            Picture picture = null;
            do {
                long id = 1;
                id += Math.random() * getPictureCount();
                picture = getPicture(id);
            } while (pictures.contains(picture));
            pictures.add(picture);

            new Round(game, picture).save();
        }

        return game;
    }

    /**
     * Get all unfinished games of a user.
     * @param user
     * @return All unfinished games of a user.
     */
    public List<Game> getUnfinishedGames(User user) {
        List<Game> games = Game.find.where().ieq("finished", "0").or(Expr.ieq("user1id", user.getId().toString()),
                Expr.ieq("user2id", user.getId().toString())).ieq("current_user_id", user.getId().toString()).findList();

        for (Game g : games) {
            g.setUser1(getUser(g.getUser1ID()));
            g.setUser2(getUser(g.getUser2ID()));
            if (g.getWinner() != null) {
                g.setWinner(getUser(g.getWinnerID()));
            }
            if (g.getCurrentUser() != null) {
                g.setCurrentUserUser(getUser(g.getCurrentUserID()));
            }
        }

        return games;
    }

    public void gameAction(Game game, User user, double distance) {
        Round round = getRounds(game).get(game.getRound() - 1);
        User u = null;
        if (user.equals(game.getUser1())) {
            u = game.getUser1();
        }
        else if (user.equals(game.getUser2())) {
            u = game.getUser2();
        }
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


    /* =========================== Report functions =========================== */

    /**
     * Save a report to the DB.
     * @param report
     */
    public void saveReport(Report report) {
        report.save();
    }

    /**
     * Get all reports from the DB.
     * @return All reports from the DB.
     */
    public List<Report> getAllReports() {
        return Report.find.all();
    }

    /**
     * Get all unhandled reports.
     * @return All unhandled reports.
     */
    public List<Report> getUnhandledReports() {
        return Report.find.where().ieq("handled", "0").findList();
    }

    /**
     * Get report from id.
     * @param id
     * @return The respective report or NULL if there is no report with that id.
     */
    public Report getReport(Long id) {
        return addConnections(Report.find.where().ieq("id", id.toString()).findUnique());
    }

    /**
     * Change the reported picture with the report values and save it to the DB.
     * @param report
     */
    public void acceptReportChanges(Report report) {
        Picture picture = getPicture(report.getOldId());
        picture.setLat(report.getLat());
        picture.setLng(report.getLng());
        picture.setTitle(report.getTitle());
        picture.setDescription(report.getDescription());
        picture.save();

        report.setHandled(true);
        report.save();
    }

    private Report addConnections(Report report) {
        if (report != null) {
            report.setCreateUser(getUser(report.getCreateUserID()));
        }

        return report;
    }


    /* =========================== Round functions =========================== */

    /**
     * Get all rounds for a game.
     * @param game
     * @return All rounds for a game.
     */
    public List<Round> getRounds(Game game) {
        List<Round> rounds = Round.find.where().ieq("game_id", game.getId().toString()).orderBy("id").findList();
        for (Round r : rounds) {
            r.setGame(game);
            r.setPicture(getPicture(r.getPictureID()));
            r.setWinner(getUser(r.getWinnerID()));
        }

        return rounds;
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
        if (id != null) {
            return User.find.where().ieq("id", id.toString()).ieq("active", "1").findUnique();
        }
        return null;
    }

    /**
     * Get user from username.
     * @param username
     * @return The respective user or NULL if there is no active user with that username.
     */
    public User getUser(String username) {
        if (username != null) {
            return User.find.where().ieq("name", username).ieq("active", "1").findUnique();
        }
        return null;
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

    /**
     * Get a list of all users with username like "%searchString%".
     * @param searchString
     * @return A list of all users with username like "%searchString%".
     */
    public List<User> findUser(String searchString) {
        String name = "%" + searchString.toLowerCase() + "%";
        return User.find.where().like("name", name).findList();
    }
}