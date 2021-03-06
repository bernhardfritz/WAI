package models;

import org.joda.time.LocalDateTime;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.util.Calendar;

@Entity
public class Picture extends Model {

    @Id
    private Long id;

    private double lat;

    private double lng;

    private String title;

    private String description;

    private int height;

    private int width;

    private boolean accepted;

    private LocalDateTime updloadDate;

    // Nur die createUserID wird in der DB gespeichert, weil Sqlite keine ALTER TABLE Stmts. für die ManyToOne Beziehung unterstützt
    private Long createUserID;
    private User createUser;

    public Picture(double lat, double lng, String title, String description, int height, int width,  User createUser) {
        this.lat = lat;
        this.lng = lng;
        this.title = title;
        this.description = description;
        this.height = height;
        this.width = width;
        this.accepted = false;
        this.updloadDate = LocalDateTime.now();
        this.createUser = createUser;
        this.createUserID = createUser.getId();
    }

    public static Finder<Long, Picture> find = new Finder<Long, Picture>(Long.class, Picture.class);

    public Long getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public LatLng getLatLng() {
        return new LatLng(lat,lng);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public LocalDateTime getUpdloadDate() {
        return updloadDate;
    }

    public void setUpdloadDate(LocalDateTime updloadDate) {
        this.updloadDate = updloadDate;
    }

    public Long getCreateUserID() {
        return createUserID;
    }

    public void setCreateUserID(Long createUserID) {
        this.createUserID = createUserID;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
        setCreateUserID(createUser.getId());
    }

    @Override
    public boolean equals(Object o) {
        Picture that = (Picture) o;
        return (this.id.equals(that.id) && this.lat == that.lat && this.lng == that.lng
                && this.title.equals(that.title) && this.description.equals(that.description));
    }

    @Override
    public int hashCode() {
        return (int) (id.hashCode() * lat * lng * title.hashCode() * description.hashCode());
    }
}