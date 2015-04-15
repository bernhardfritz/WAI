package controllers;

import models.Picture;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;


public class Application extends Controller {

    public static class Login {
        public String username;
        public String password;

        public String validate() {
            if (User.authenticate(username, password) == null) {
                return "Invalid user or password!";
            }
            return null;
        }
    }

    public static class Register {
        public String name;
        public String email;
        public String password;
        public String passwordConfirm;

        public String validate() {
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() ||
                    !password.equals(passwordConfirm)) {
                return "Invalid name, email address or password!";
            }
            for (User u : DBManager.getInstance().getAllUsers()) {
                if (u.getName().equals(name) || u.getEmail().equals(email)) {
                    return "Invalid name, email address or password!";
                }
            }
            return null;
        }
    }

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result map() {
        return ok(map.render());
    }

    public static double getLat() {
        String session = session().get("lat").toString();
        double lat = session!=null?Double.parseDouble(session):0.0;
        return lat;
    }

    public static double getLng() {
        String session = session().get("lng").toString();
        double lng = session!=null?Double.parseDouble(session):0.0;
        return lng;
    }

    public static Result getLocation() {
        return log("" + getLat() + " " + getLng());
    }

    public static Result log(String text) {
        return ok(log.render(text));
    }

    public static Result setLocation(double lng, double lat) {
        session("lat", "" + lat);
        session("lng",""+lng);
        return ok();
    }

    public static Result upload() {
        return ok(upload.render());
    }

    public static Result uploadAction() throws IOException {
        MultipartFormData body = request().body().asMultipartFormData();
        Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
        String[] titleParams = asFormUrlEncoded.get("title");
        String title = titleParams!=null?titleParams[0]:"N/A";
        String[] descriptionParams = asFormUrlEncoded.get("description");
        String description = descriptionParams!=null?descriptionParams[0]:"N/A";
        FilePart picture = body.getFile("picture");
        if (picture != null && picture.getContentType().contains("image")){
            File file = picture.getFile();

            // save picture to db
            Picture pic = new Picture(getLat(),getLng(),title,description);
            DBManager.getInstance().savePicture(pic);
            String path = "public/images/" + pic.getId() + ".jpg";
            pic.setPath(path);
            DBManager.getInstance().savePicture(pic);

            // save picture to "public/images/pictureID.jpg"
            BufferedImage img = ImageIO.read(file);
            img = PictureManager.getInstance().getScaledImage(img);
            PictureManager.getInstance().saveToFile(img, pic.getId().intValue());
            //EmailManager.getInstance().send("bernhard.e.fritz@gmail.com","test","dies ist ein testtext");
            return redirect("picture/" + pic.getId()); // seite refreshen
        } else {
            return badRequest("No File or wrong type");
        }
    }

    public static Result bootstrap() {
        return ok(bootstrap.render());
    }

    public static Result login() {
        DBManager.getInstance();
        Form<Login> loginForm = Form.form(Login.class);
        return ok(login.render(loginForm));
    }

    public static Result authenticate() {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        }
        else {
            session().clear();
            session("username", loginForm.get().username);
            return redirect(routes.Application.game());
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result logout() {
        session().clear();
        return redirect(routes.Application.login());
    }

    public static Result gotoRegister() {
        Form<Register> registerForm = Form.form(Register.class);
        return ok(register.render(registerForm));
    }

    public static Result register() {
        Form<Register> registerForm = Form.form(Register.class).bindFromRequest();
        if (registerForm.hasErrors()) {
            return badRequest(register.render(registerForm));
        }
        else {
            DBManager.getInstance().registerUser(new User(registerForm.get().name, registerForm.get().email, registerForm.get().password));
            session().clear();
            session("username", registerForm.get().name);
            return redirect(routes.Application.game());
        }
    }

    public static Result template() {
        return ok(template.render("Template",null,null));
    }

    public static Result template_wo_navbar() {
        return ok(template_wo_navbar.render("Template", null, null));
    }

    public static Result blank() {
        return ok(blank.render("Blank",null,null));
    }

    @Security.Authenticated(Secured.class)
    public static Result game() {
        return ok(game.render());
    }

    public static Result gameAction() {
        return showDistance();
    }

    public static Result picture(Long id) {
        return ok(picture.render(id));
    }

    public static double getDistance(Picture picture) {
        double lat = getLat();
        double lng = getLng();
        double R = 6378137; // Earth’s mean radius in meter
        double dLat = Math.toRadians(lat - picture.getLat());
        double dLong = Math.toRadians(lng - picture.getLng());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(picture.getLat())) * Math.cos(Math.toRadians(lat)) *
                        Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d; // returns the distance in m
    }

    public static Result showDistance() {
        return log(""+Math.round(getDistance(DBManager.getInstance().getPicture(1L)))/1000.0+" km");
    }
}
