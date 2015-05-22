package models;

import org.joda.time.LocalDateTime;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by philipphausle on 10.05.15.
 */
@Entity
public class Report extends Model {

    @Id
    private Long id;

    private Long pictureID;
    private Picture picture;

    private double lat;

    private double lng;

    private String title;

    private String description;

    private String optional;

    private LocalDateTime updloadDate;

    private Long createUserID;
    private User createUser;

    private boolean handled;

    public Report(double lat, double lng, String title, String description, String optional,  User createUser, Picture picture) {
        this.lat = lat;
        this.lng = lng;
        this.title = title;
        this.description = description;
        this.optional=optional;
        this.updloadDate = LocalDateTime.now();
        this.createUser = createUser;
        this.createUserID = createUser.getId();
        this.picture = picture;
        this.pictureID = picture.getId();
        handled = false;
    }

    public static Finder<Long, Report> find = new Finder<Long, Report>(Long.class, Report.class);

    public Long getId() {
            return id;
        }

    public Long getPictureID() {
         return pictureID;
        }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
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

    public void setOptional(String optional){
            this.optional=optional;
        }

    public String getOptional(){
            return this.optional;
    }

    public LocalDateTime getUpdloadDate() {
            return updloadDate;
        }

    public String getPrettyUploadDate() {
        return updloadDate.toString("dd.MM.yyyy");
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
        }

    public boolean isHandled() {
        return handled;
    }

    public void setHandled(boolean handled) {
        this.handled = handled;
    }
}