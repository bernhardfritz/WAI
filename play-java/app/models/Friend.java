package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Friend extends Model {

    @Id
    private Long id;

    private Long user1ID;
    private User user1;

    private Long user2ID;
    private User user2;

    public Friend(User user1, User user2) {
        this.user1ID = user1.getId();
        this.user1 = user1;
        this.user2ID = user2.getId();
        this.user2 = user2;
    }

    public static Finder<Long, Friend> find = new Finder<Long, Friend>(Long.class, Friend.class);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser1ID() {
        return user1ID;
    }

    public void setUser1ID(Long user1ID) {
        this.user1ID = user1ID;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public Long getUser2ID() {
        return user2ID;
    }

    public void setUser2ID(Long user2ID) {
        this.user2ID = user2ID;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }
}