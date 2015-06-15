package models;

import org.joda.time.LocalDateTime;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Round extends Model {

    @Id
    private Long id;

    private Long gameID;
    private Game game;

    private Long pictureID;
    private Picture picture;

    private Double user1Distance;

    private Double user2Distance;

    private Long winnerID;
    private User winner;

    private boolean finished;

    private LocalDateTime secondPlayerBegin;

    private LocalDateTime evaluateBegin;

    private double user1Lat;
    private double user1Lng;

    private double user2Lat;
    private double user2Lng;

    private LatLng user1LatLng;
    private LatLng user2LatLng;

    public Round(Game game, Picture picture) {
        this.gameID = game.getId();
        this.game = game;
        this.pictureID = picture.getId();
        this.picture = picture;
        this.user1Distance = null;
        this.user2Distance = null;
        this.winnerID = null;
        this.winner = null;
        this.finished = false;
        this.secondPlayerBegin = null;
        this.evaluateBegin = null;
        this.user1Lat = 0.0;
        this.user1Lng = 0.0;
        this.user2Lat = 0.0;
        this.user2Lng = 0.0;
        this.user1LatLng = null;
        this.user2LatLng = null;
    }

    public static Finder<Long, Round> find = new Finder<Long, Round>(Long.class, Round.class);

    public Long getId() {
        return id;
    }

    public Long getGameID() {
        return gameID;
    }

    public void setGameID(Long gameID) {
        this.gameID = gameID;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        setGameID(game.getId());
    }

    public Long getPictureID() {
        return pictureID;
    }

    public void setPictureID(Long pictureID) {
        this.pictureID = pictureID;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
        setPictureID(picture.getId());
    }

    public Double getUser1Distance() {
        return user1Distance;
    }

    public void setUser1Distance(Double user1Distance) {
        this.user1Distance = user1Distance;
    }

    public Double getUser2Distance() {
        return user2Distance;
    }

    public void setUser2Distance(Double user2Distance) {
        this.user2Distance = user2Distance;
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

    public LocalDateTime getSecondPlayerBegin() {
        return secondPlayerBegin;
    }

    public void setSecondPlayerBegin(LocalDateTime secondPlayerBegin) {
        this.secondPlayerBegin = secondPlayerBegin;
    }

    public LocalDateTime getEvaluateBegin() {
        return evaluateBegin;
    }

    public void setEvaluateBegin(LocalDateTime evaluateBegin) {
        this.evaluateBegin = evaluateBegin;
    }

    public Double getPrettyUser1Distance() {
        return prettifyDistance(user1Distance);
    }

    public Double getPrettyUser2Distance() {
        return prettifyDistance(user2Distance);
    }

    private Double prettifyDistance(Double distance) {
        if (distance == null) {
            return null;
        }
        
        return ((long) (distance / 100.0)) / 10.0;
    }

    public double getUser1Lat() {
        return user1Lat;
    }

    public void setUser1Lat(double user1Lat) {
        this.user1Lat = user1Lat;
    }

    public double getUser1Lng() {
        return user1Lng;
    }

    public void setUser1Lng(double user1Lng) {
        this.user1Lng = user1Lng;
    }

    public double getUser2Lat() {
        return user2Lat;
    }

    public void setUser2Lat(double user2Lat) {
        this.user2Lat = user2Lat;
    }

    public double getUser2Lng() {
        return user2Lng;
    }

    public void setUser2Lng(double user2Lng) {
        this.user2Lng = user2Lng;
    }

    public LatLng getUser1LatLng() {
        return user1LatLng;
    }

    public void setUser1LatLng(LatLng user1LatLng) {
        this.user1LatLng = user1LatLng;
        if (user1LatLng != null) {
            user1Lat = user1LatLng.getLat();
            user1Lng = user1LatLng.getLng();
        }
    }

    public LatLng getUser2LatLng() {
        return user2LatLng;
    }

    public void setUser2LatLng(LatLng user2LatLng) {
        this.user2LatLng = user2LatLng;
        if (user2LatLng != null) {
            user2Lat = user2LatLng.getLat();
            user2Lng = user2LatLng.getLng();
        }
    }
}
