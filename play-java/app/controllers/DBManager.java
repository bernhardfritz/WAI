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
                User u = (User) o;
                ((User) o).setRegisterDate(LocalDateTime.now());
                registerUser(u);
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
                createGame(getActiveUser(g.getUser1ID()), getActiveUser(g.getUser2ID()));
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
                    friends.add(getActiveUser(f.getUser2ID()));
                }
            }

            for (Friend f : Friend.find.where().ieq("user2id", user.getId().toString()).findList()) {
                if (!friends.contains(f.getUser1ID())) {
                    friends.add(getActiveUser(f.getUser1ID()));
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
        if (user1.equals(user2)) {
            return null;
        }

        Game game = new Game(user1, user2);
        if (getReadyUnfinishedGames(user1).contains(game) || getUnreadyUnfinishedGames(user1).contains(game)) {
            return null;
        }
        game.save();

        List<Picture> pictures = new ArrayList<Picture>();
        for (int i = 0; i < 3; i++) {
            Picture picture = null;
            do {
                picture = getRandomAcceptedPicture();
            } while (pictures.contains(picture));
            pictures.add(picture);

            new Round(game, picture).save();
        }

        return game;
    }

    /**
     * Get all unfinished games of a user where the user has to play.
     * @param user
     * @return All unfinished games of a user where the user has to play.
     */
    public List<Game> getReadyUnfinishedGames(User user) {
        List<Game> games = Game.find.where().ieq("finished", "0").or(Expr.ieq("user1id", user.getId().toString()),
                Expr.ieq("user2id", user.getId().toString())).ieq("current_user_id", user.getId().toString()).findList();

        for (Game g : games) {
            g = addConnections(g);

            checkGameConditions(g);
        }

        games = Game.find.where().ieq("finished", "0").or(Expr.ieq("user1id", user.getId().toString()),
                Expr.ieq("user2id", user.getId().toString())).ieq("current_user_id", user.getId().toString()).findList();

        for (Game g : games) {
            g = addConnections(g);
        }

        return games;
    }

    /**
     * Get all unfinished games of a user where the user has not to play.
     * @param user
     * @return All unfinished games of a user where the user has not to play.
     */
    public List<Game> getUnreadyUnfinishedGames(User user) {
        List<Game> games = Game.find.where().ieq("finished", "0").or(Expr.ieq("user1id", user.getId().toString()),
                Expr.ieq("user2id", user.getId().toString())).not(Expr.ieq("current_user_id", user.getId().toString())).findList();

        for (Game g : games) {
            g = addConnections(g);

            checkGameConditions(g);
        }

        games = Game.find.where().ieq("finished", "0").or(Expr.ieq("user1id", user.getId().toString()),
                Expr.ieq("user2id", user.getId().toString())).not(Expr.ieq("current_user_id", user.getId().toString())).findList();

        for (Game g : games) {
            g = addConnections(g);
        }

        return games;
    }

    /**
     * Get all finished games of a user.
     * @param user
     * @return All finished games of a user.
     */
    public List<Game> getFinishedGames(User user) {
        List<Game> games =  Game.find.where().ieq("finished", "1").or(Expr.ieq("user1id", user.getId().toString()),
                Expr.ieq("user2id", user.getId().toString())).orderBy().desc("id").findList();

        for (Game g : games) {
            g = addConnections(g);
        }

        return games;
    }

    /**
     * Get maximal maxResultCount finished games of a user.
     * @param user
     * @param maxResultCount
     * @return Maximal maxResultCount finished games of a user.
     */
    public List<Game> getFinishedGames(User user, int maxResultCount) {
        List<Game> games = getFinishedGames(user);

        if (games.size() > maxResultCount) {
            games = games.subList(0, maxResultCount);
        }

        return games;
    }

    /**
     * Initialize the game values when a user start a round.
     * @param game
     * @param user
     */
    public void onGameStart(Game game, User user) {
        Round round = getRounds(game).get(game.getRound() - 1);

        round.setEvaluateBegin(LocalDateTime.now().plusSeconds(60));

        if (round.getUser1Distance() == null && round.getUser2Distance() == null) {
            round.setSecondPlayerBegin(LocalDateTime.now().plusSeconds(60));
        }

        if (user.equals(game.getUser1())) {
            round.setUser1Distance(-1.0);
        }
        else if (user.equals(game.getUser2())) {
            round.setUser2Distance(-1.0);
        }

        round.save();
    }

    /**
     * Check the game conditions and do necessary changes when the play_menu page is loaded.
     * @param game
     */
    public void checkGameConditions(Game game) {
        Round round = getRounds(game).get(game.getRound() - 1);
        if (round.getEvaluateBegin() == null || (round.getEvaluateBegin() != null && round.getEvaluateBegin().isAfter(LocalDateTime.now()))) {
            return;
        }

        round = roundWinnerCalculation(round, game);

        if (((game.getRound() % 2 == 1 && game.getCurrentUser().equals(game.getUser1())) ||
                (game.getRound() % 2 == 0 && game.getCurrentUser().equals(game.getUser2()))) &&
                round.getSecondPlayerBegin().isBefore(LocalDateTime.now())) {
            game.setCurrentUser(game.getOtherUser(game.getCurrentUser()));
        }

        if (round.isFinished()) {
            game.incrementRound();
        }

        round.save();

        game = gameWinnerCalculation(game);
        game.save();
    }

    /**
     * Do necessary calculations when the submit button in a game is clicked.
     * @param game
     * @param user
     * @param distance
     */
    public void gameAction(Game game, User user, double distance) {
        if (!game.getCurrentUser().equals(user)) {
            return;
        }

        Round round = getRounds(game).get(game.getRound() - 1);

        if (user.equals(game.getUser1())) {
            round.setUser1Distance(distance);
        }
        else if (user.equals(game.getUser2())) {
            round.setUser2Distance(distance);
        }

        round = roundWinnerCalculation(round, game);

        if (round.isFinished()) {
            game.incrementRound();
        }
        else {
            game.setCurrentUser(game.getOtherUser(user));
        }

        if (round.getSecondPlayerBegin().isAfter(LocalDateTime.now())){
            round.setSecondPlayerBegin(LocalDateTime.now());
        }
        round.setEvaluateBegin(LocalDateTime.now());

        round.save();

        game = gameWinnerCalculation(game);
        game.save();
    }

    /**
     * Check if a round is finished and set the winner of the round.
     * @param round
     * @param game
     * @return The changed round object.
     */
    private Round roundWinnerCalculation(Round round, Game game) {
        if (round.getUser1Distance() != null && round.getUser2Distance() != null) {
            if ((round.getUser1Distance() != -1 && round.getUser1Distance() < round.getUser2Distance()) || round.getUser2Distance() == -1.0) {
                round.setWinner(game.getUser1());
            }
            else if ((round.getUser2Distance() != -1 && round.getUser2Distance() < round.getUser1Distance()) || round.getUser1Distance() == -1.0) {
                round.setWinner(game.getUser2());
            }
            else {
                round.setWinner(game.getUser1());
            }

            round.setFinished(true);
        }

        return round;
    }

    /**
     * Check if a game is finished and set the winner of the game.
     * @param game
     * @return The changed game object.
     */
    private Game gameWinnerCalculation(Game game) {
        if (game.isFinished() && getWonRounds(game, game.getUser1()) >= 2) {
            game.setWinner(game.getUser1());
        }
        else if (game.isFinished() && getWonRounds(game, game.getUser2()) >= 2) {
            game.setWinner(game.getUser2());
        }

        return game;
    }

    /**
     * Get the number of won rounds of a game for a user.
     * @param game
     * @param user
     * @return The number of won rounds of a game for a user.
     */
    public int getWonRounds(Game game, User user) {
        return Round.find.where().ieq("game_id", game.getId().toString()).ieq("winner_id", user.getId().toString()).findList().size();
    }

    /**
     * Get a game from id.
     * @param id
     * @return The respective game or NULL if there is no game with that id.
     */
    public Game getGame(Long id) {
        Game game = Game.find.where().ieq("id", id.toString()).findUnique();
        if (game != null) {
            game.setUser1(getActiveUser(game.getUser1ID()));
            game.setUser2(getActiveUser(game.getUser2ID()));
            if (game.getWinnerID() != null) {
                game.setWinner(getActiveUser(game.getWinnerID()));
            }
            if (game.getCurrentUserID() != null) {
                game.setCurrentUser(getActiveUser(game.getCurrentUserID()));
            }
        }

        return game;
    }

    /**
     * Get the current round of a game.
     * @param game
     * @return The current round of a game.
     */
    public Round getCurrentRound(Game game) {
        return getRounds(game).get(game.getRound() - 1);
    }

    /**
     * Add missing object connections to a game.
     * @param game
     * @return The game with full object connectivity.
     */
    private Game addConnections(Game game) {
        game.setUser1(getActiveUser(game.getUser1ID()));
        game.setUser2(getActiveUser(game.getUser2ID()));
        if (game.getWinnerID() != null) {
            game.setWinner(getActiveUser(game.getWinnerID()));
        }
        if (game.getCurrentUserID() != null) {
            game.setCurrentUser(getActiveUser(game.getCurrentUserID()));
        }

        return game;
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
     * Get a accepted picture from id.
     * @param id
     * @return The respective picture or NULL if there is no accepted picture with that id.
     */
    public Picture getAcceptedPicture(Long id) {
        return addConnections(Picture.find.where().ieq("id", id.toString()).ieq("accepted", "1").findUnique());
    }

    /**
     * Get a picture from id.
     * @param id
     * @return The respective picture or NULL, if there is no picture with that id.
     */
    public Picture getPicture(Long id) {
        return addConnections(Picture.find.where().ieq("id", id.toString()).findUnique());
    }

    /**
     * Get the number of all accepted pictures in the DB.
     * @return The number of all accepted pictures in the DB.
     */
    public int getAcceptedPictureCount() {
        return Picture.find.where().ieq("accepted", "1").findList().size();
    }

    /**
     * Get the number of all pictures in the DB.
     * @return The number of all pictures in the DB.
     */
    public int getPictureCount() {
        return Picture.find.all().size();
    }

    /**
     * Get all pictures from the DB.
     * @return All pictures from the DB.
     */
    public List<Picture> getAllPictures() {
        List<Picture> pictures = Picture.find.all();
        for (Picture p : pictures) {
            p = addConnections(p);
        }

        return pictures;
    }

    /**
     * Get all pictures between start and end id.
     * @return All pictures between start and end id.
     */
    public List<Picture> getPictureRange(Long start, Long end) {
        List<Picture> pictures = Picture.find.where().between("id", start.toString(), end.toString()).findList();
        for(Picture p : pictures) {
            p = addConnections(p);
        }

        return pictures;
    }

    /**
     * Get all accepted pictures from the DB.
     * @return All accepted pictures from the DB.
     */
    public List<Picture> getActivePictures() {
        List<Picture> pictures = Picture.find.where().ieq("accepted", "1").findList();
        for (Picture p : pictures) {
            p = addConnections(p);
        }

        return pictures;
    }

    /**
     * Accept the given picture and save the changes to the DB.
     * @param picture
     */
    public void acceptPicture(Picture picture) {
        if (picture != null) {
            picture.setAccepted(true);
            savePicture(picture);
        }
    }

    /**
     * Unaccept the given picture and save the changes to the DB.
     * @param picture
     */
    public void unacceptPicture(Picture picture) {
        if (picture != null) {
            picture.setAccepted(false);
            savePicture(picture);
        }
    }

    /**
     * Edit a picture with the given values.
     * @param picture
     * @param lat
     * @param lng
     * @param title
     * @param description
     */
    public void editPicture(Picture picture, double lat, double lng, String title, String description, boolean accepted) {
        if (picture != null) {
            picture.setLat(lat);
            picture.setLng(lng);
            picture.setTitle(title);
            picture.setDescription(description);
            picture.setAccepted(accepted);

            savePicture(picture);
        }
    }

    /**
     * Add missing object connections to a picture.
     * @param picture
     * @return The picture with full object connectivity.
     */
    private Picture addConnections(Picture picture) {
        if (picture != null) {
            if (picture.getCreateUserID() != null) {
                picture.setCreateUser(getActiveUser(picture.getCreateUserID()));
            }
        }

        return picture;
    }

    /**
     * Get a random accepted picture from the DB.
     * @return A random accepted picture from the DB.
     */
    public Picture getRandomAcceptedPicture() {
        Picture picture = null;
        while (picture == null) {
            long id = 1;
            id += Math.random() * getPictureCount();
            picture = getAcceptedPicture(id);
        }

        return picture;
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
        List<Report> reports = Report.find.all();
        for (Report r : reports) {
            r = addConnections(r);
        }

        return reports;
    }

    /**
     * Get all unhandled reports.
     * @return All unhandled reports.
     */
    public List<Report> getUnhandledReports() {
        List<Report> reports =  Report.find.where().ieq("handled", "0").findList();
        for (Report r : reports) {
            r = addConnections(r);
        }

        return reports;
    }

    /**
     * Get all unhandled reports between start and end id.
     * @param start
     * @param end
     * @return All unhandled reports between start and end id.
     */
    public List<Report> getUnhandledReportRange(Long start, Long end) {
        List<Report> reports = Report.find.where().ieq("handled", "0").between("id", start.toString(), end.toString()).findList();
        for (Report r : reports) {
            r = addConnections(r);
        }
        return reports;
    }

    /**
     * Get the number of unhandled reports in the DB.
     * @return The number of unhandled reports in the DB.
     */
    public int getUnhandledReportCount() {
        return Report.find.all().size();
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
        Picture picture = report.getPicture();
        picture.setLat(report.getLat());
        picture.setLng(report.getLng());
        picture.setTitle(report.getTitle());
        picture.setDescription(report.getDescription());
        savePicture(picture);

        report.setHandled(true);
        saveReport(report);
    }

    /**
     * Add missing object connections to a report.
     * @param report
     * @return The report with full object connectivity.
     */
    private Report addConnections(Report report) {
        if (report != null) {
            report.setPicture(getAcceptedPicture(report.getPictureID()));
            if (report.getCreateUserID() != null) {
                report.setCreateUser(getActiveUser(report.getCreateUserID()));
            }
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
            r.setPicture(getAcceptedPicture(r.getPictureID()));
            if (r.getWinnerID() != null) {
                r.setWinner(getActiveUser(r.getWinnerID()));
            }
        }

        return rounds;
    }

    /**
     * Get a list of rounds from a picture.
     * @param picture
     * @return A list of rounds from a picture.
     */
    public List<Round> getRounds(Picture picture) {
        List<Round> rounds = Round.find.where().ieq("picture_id", picture.getId().toString()).findList();
        for (Round r : rounds) {
            r.setGame(getGame(r.getGameID()));
            r.setPicture(picture);
            if (r.getWinnerID() != null) {
                r.setWinner(getActiveUser(r.getWinnerID()));
            }
        }

        return rounds;
    }

    /**
     * Get a list of all saved rounds.
     * @return A list of all saved rounds.
     */
    public List<Round> getAllRounds() {
        return Round.find.all();
    }

    /**
     * Get the average saved distance for a given picture.
     * @param picture
     * @return The average saved distance for a given picture.
     */
    public Double getAverageDistance(Picture picture) {
        Double sum = 0.0;
        int count = 1;

        for (Round r : getRounds(picture)) {
            if (r.getUser1Distance() != null && r.getUser1Distance() >= 0) {
                sum += r.getUser1Distance();
                count++;
            }
            if (r.getUser2Distance() != null && r.getUser2Distance() >= 0) {
                sum += r.getUser2Distance();
                count++;
            }
        }

        count = Math.max(1, count-1);

        return sum / ((double) count);
    }


    /* =========================== Token functions =========================== */

    /**
     * Create a new token or edit an existing one and save it to the DB.
     * @param email
     * @param tokenString
     */
    public void saveToken(String email, String tokenString) {
        if (email != null && tokenString != null) {
            User user = getActiveUserFromEmail(email);
            if (user != null) {
                Token token = getTokenFromEmail(email);
                if (token == null) {
                    token = new Token(email, tokenString);
                }
                else {
                    token.setTokenString(tokenString);
                    token.resetValidUntil();
                }
                token.save();
            }
        }
    }

    /**
     * Get a token from the user email address.
     * @param email
     * @return The respective token or NULL if there is no token with that user email address.
     */
    public Token getTokenFromEmail(String email) {
        if (email != null) {
            return Token.find.where().ieq("email", email).findUnique();
        }
        return null;
    }

    /**
     * Get a token from the token string.
     * @param tokenString
     * @return The respective token or NULL if there is no token with that string.
     */
    public Token getTokenFromString(String tokenString) {
        if (tokenString != null) {
            return Token.find.where().ieq("token_string", tokenString).findUnique();
        }
        return null;
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
    public User getActiveUser(String username, String password) {
        return User.find.where().ieq("name", username).ieq("password", HashManager.getInstance().codeString(password)).ieq("active", "1").findUnique();
    }

    /**
     * Get active user from id.
     * @param id
     * @return The respective user or NULL if there is no active user with that id.
     */
    public User getActiveUser(Long id) {
        if (id != null) {
            return User.find.where().ieq("id", id.toString()).ieq("active", "1").findUnique();
        }
        return null;
    }

    /**
     * Get user from id.
     * @param id
     * @return The respective user or NULL if there is no user with that id.
     */
    public User getUser(Long id) {
        if (id != null) {
            return User.find.where().ieq("id", id.toString()).findUnique();
        }
        return null;
    }

    /**
     * Get active user from username.
     * @param username
     * @return The respective user or NULL if there is no active user with that username.
     */
    public User getActiveUser(String username) {
        if (username != null) {
            return User.find.where().ieq("name", username).ieq("active", "1").findUnique();
        }
        return null;
    }

    /**
     * Get user from username.
     * @param username
     * @return The respective user or NULL if there is no user with that username.
     */
    public User getUser(String username) {
        if (username != null) {
            return User.find.where().ieq("name", username).findUnique();
        }
        return null;
    }

    /**
     * Get user from email address.
     * @param email
     * @return The respective user or NULL if there is no user with that email address.
     */
    public User getActiveUserFromEmail(String email) {
        if (email != null) {
            return User.find.where().ieq("email", email).findUnique();
        }
        return null;
    }

    /**
     * Get a list of all registered and active users.
     * @return A list of all registered and active users.
     */
    public List<User> getAllActiveUsers() {
        return User.find.where().ieq("active", "1").findList();
    }

    /**
     * Get a list of all registered users.
     * @return A list of all registered users.
     */
    public List<User> getAllUsers() {
        return User.find.all();
    }

    /**
     * Get all users between start and end id.
     * @return All users between start and end id.
     */
    public List<User> getUserRange(Long start, Long end) {
        List<User> users = User.find.where().between("id", start.toString(), end.toString()).findList();
        return users;
    }

    /**
     * Get the number of all users in the DB.
     * @return The number of all users in the DB.
     */
    public int getUserCount() {
        return User.find.all().size();
    }

    /**
     * Toggle the active value of a user.
     * @param id
     */
    public void toggleUser(Long id) {
        User user = getUser(id);
        user.setActive(!user.isActive());
        user.save();
    }

    /**
     * Set a new password for a user.
     * @param user
     * @param password
     */
    public void changeUserPassword(User user, String password) {
        if (user != null) {
            user.setPassword(password);
            user.save();
        }
    }

    /**
     * Get a list of all users with username like "%searchString%".
     * @param searchString
     * @return A list of all users with username like "%searchString%".
     */
    public List<User> findUser(String searchString) {
        String name = searchString.toLowerCase() + "%";
        return User.find.where().like("name", name).findList();
    }

    /**
     * Check if a user email is registered in the DB.
     * @param email
     * @return True if the given email is registered or false otherwise.
     */
    public boolean isEmailTaken(String email) {
        return getActiveUserFromEmail(email) != null;
    }
}