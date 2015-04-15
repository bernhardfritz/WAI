package controllers;

import com.avaje.ebean.Ebean;
import models.Picture;
import models.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    public void init() {
        if (User.find.all().isEmpty()) {
            registerUser(new User("admin", "admin@test.at", "secret"));
        }
    }

    public void registerUser(User user) {
        user.save();
    }

    public User getUser(String username, String password) {
        return User.find.where().ieq("name", username).ieq("password", HashManager.getInstance().codeString(password)).findUnique();
    }

    public List<User> getAllUsers() {
        return User.find.all();
    }

    public void savePicture(Picture picture) {
        picture.save();
    }

    public Picture getPicture(Long id) {
        return Picture.find.byId(id);
    }
}