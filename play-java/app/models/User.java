package models;


import controllers.DBManager;
import controllers.HashManager;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class User extends Model {

    @Id
    private Long id;

    private String name;

    private String email;

    private String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = HashManager.getInstance().codeString(password);
    }

    public static Finder<Long, User> find = new Finder<Long, User>(Long.class, User.class);

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = HashManager.getInstance().codeString(password);
    }

    public static User authenticate(String username, String password) {
        return DBManager.getInstance().getUser(username, password);
    }
}