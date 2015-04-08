package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class Picture extends Model {
    @Lob
    private byte[] data;
    private double lat;
    private double lng;
    private String title;
    private String description;

    public Picture(byte[] data, double lat, double lng, String title, String description) {
        this.data = data;
        this.lat = lat;
        this.lng = lng;
        this.title = title;
        this.description = description;
    }
}
