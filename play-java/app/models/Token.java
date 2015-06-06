package models;

import org.joda.time.LocalDateTime;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Token extends Model {

    @Id
    private Long id;

    private String email;

    private LocalDateTime validUntil;

    private String tokenString;

    public Token(String email, String tokenString) {
        this.email = email;
        this.validUntil = LocalDateTime.now().plusDays(1);
        this.tokenString = tokenString;
    }

    public static Finder<Long, Token> find = new Finder<Long, Token>(Long.class, Token.class);

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public String getTokenString() {
        return tokenString;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public void setTokenString(String tokenString) {
        this.tokenString = tokenString;
    }

    public void resetValidUntil() {
        this.validUntil = LocalDateTime.now().plusDays(1);
    }

    public String getPrettyValidUntil() {
        return validUntil.toString("dd.MM.yyyy H:m:s");
    }

    public boolean isValid() {
        return LocalDateTime.now().isBefore(validUntil);
    }
}