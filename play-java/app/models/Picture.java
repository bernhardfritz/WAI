package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;

@Entity
public class Picture extends Model {
    private Blob image;
    private double lat;
    private double lng;
    private String description;

    public Picture(byte[] array,double lat,double lng,String description) {
        try {
            this.image = new SerialBlob(array);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.lat = lat;
        this.lng = lng;
        this.description = description;
    }
}
