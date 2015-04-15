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

    private String path;

    private double lat;

    private double lng;

    private String title;

    private String description;

    private boolean accepted;

    private LocalDateTime updloadDate;

    public Picture(double lat, double lng, String title, String description) {
        this.lat = lat;
        this.lng = lng;
        this.title = title;
        this.description = description;
        this.accepted = false;
        this.updloadDate = LocalDateTime.now();
    }

    public static Finder<Long, Picture> find = new Finder<Long, Picture>(Long.class, Picture.class);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
}