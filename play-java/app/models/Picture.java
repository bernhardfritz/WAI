package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;

@Entity
public class Picture extends Model {
    private Blob image;

    public Picture(byte[] array) {
        try {
            this.image = new SerialBlob(array);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
