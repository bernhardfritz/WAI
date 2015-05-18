package models;


import controllers.DBManager;
import controllers.HashManager;
import org.joda.time.LocalDateTime;
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

    private boolean active;

    private LocalDateTime registerDate;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = HashManager.getInstance().codeString(password);
        active = true;
    }

    public static Finder<Long, User> find = new Finder<Long, User>(Long.class, User.class);

    public Long getId() { return id; }

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public static User authenticate(String username, String password) {
        return DBManager.getInstance().getUser(username, password);
    }

    @Override
    public boolean equals(Object o) {
        User that = (User) o;
        return (this.id.equals(that.id) && this.name.equals(that.name) && this.email.equals(that.email)
                && this.password.equals(that.password));
    }

    @Override
    public int hashCode() {
        return id.hashCode() * name.hashCode() * email.hashCode() * password.hashCode();
    }
}