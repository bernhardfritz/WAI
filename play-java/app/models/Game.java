package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Game extends Model {

    @Id
    private Long id;

    private Long user1ID;
    private User user1;

    private Long user2ID;
    private User user2;

    private Long winnerID;
    private User winner;

    private boolean finished;

    private int round;

    private Long currentUserID;
    private User currentUser;

    public Game(User user1, User user2) {
        this.user1ID = user1.getId();
        this.user1 = user1;
        this.user2ID = user2.getId();
        this.user2 = user2;
        this.winnerID = null;
        this.winner = null;
        this.finished = false;
        this.round = 1;
        this.currentUserID = user1.getId();
        this.currentUser = user1;
    }

    public static Finder<Long, Game> find = new Finder<Long, Game>(Long.class, Game.class);

    public Long getId() {
        return id;
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
        setUser1ID(user1.getId());
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
        setUser2ID(user2.getId());
    }

    public Long getWinnerID() {
        return winnerID;
    }

    public void setWinnerID(Long winnerID) {
        this.winnerID = winnerID;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
        setWinnerID(winner.getId());
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getRound() {
        return round;
    }

    public void incrementRound() {
        round++;
        if (round > 3) {
            setFinished(true);
        }
    }

    public Long getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(Long currentUserID) {
        this.currentUserID = currentUserID;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUserUser(User currentUser) {
        this.currentUser = currentUser;
        setCurrentUserID(currentUser.getId());
    }
}
