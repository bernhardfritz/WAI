package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import java.sql.Blob;

/**
 * Created by philipphausle on 01.04.15.
 */
    @Entity
    public class Picture extends Model {
        public Blob image;
    }
