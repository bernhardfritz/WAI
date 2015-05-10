package controllers;

import com.google.common.io.Files;
import models.LatLng;
import models.Picture;
import models.User;
import play.data.DynamicForm;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Application extends Controller {
    private static DBManager dbManager = DBManager.getInstance();

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
            for (User u : dbManager.getAllUsers()) {
                if (u.getName().equals(name) || u.getEmail().equals(email)) {
                    return "Invalid name, email address or password!";
                }
            }
            return null;
        }
    }

    public static Result authenticate() {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        }
        else {
            session().clear();
            session("username", loginForm.get().username);
            return redirect(routes.Application.index());
        }
    }

    public static Result blank() {
        return ok(blank.render("Blank",null,null));
    }

    public static Result bootstrap() {
        return ok(bootstrap.render());
    }

    public static Result gallery() {
        List<Picture> pictures = new ArrayList<Picture>();
        for(int i=1; i<=dbManager.getPictureCount(); i++) {
            pictures.add(dbManager.getPicture(0L+i));
        }
        return ok(gallery.render(pictures));
    }

    public static Result game(Long id) {
        return result(id);
    }

    public static Result game() {
        long id = 1;
        id+=Math.random()*dbManager.getPictureCount();
        return ok(game.render(id));
    }

    public static Result gameAction(Long id) {
        return result(id);
    }

    public static Double getDistance(Picture picture) {
        if (getLat() != null && getLng() != null) {
            double R = 6378137; // Earth’s mean radius in meter
            double lat1 = Math.toRadians(picture.getLat());
            double lng1 = Math.toRadians(picture.getLng());
            double lat2 = Math.toRadians(getLat());
            double lng2 = Math.toRadians(getLng());
            double dLat = lat2 - lat1;
            double dLong = lng2 - lng1;
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(lat1) * Math.cos(lat2) *
                            Math.sin(dLong / 2) * Math.sin(dLong / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double d = R * c;
            return d; // returns the distance in m
        }
        return null;
    }

    public static Double getLat() {
        if(session().get("lat") != null) {
            String session = session().get("lat").toString();
            double lat = session != null ? Double.parseDouble(session) : 0.0;
            return lat;
        }
        return null;
    }

    public static Double getLng() {
        if (session().get("lng") != null) {
            String session = session().get("lng").toString();
            double lng = session != null ? Double.parseDouble(session) : 0.0;
            return lng;
        }
        return null;
    }

    public static LatLng getLatLng() {
        LatLng latlng = null;
        Double lat = getLat();
        Double lng = getLng();
        if(lat==null || lng==null) return null;
        return new LatLng(lat,lng);
    }

    public static Result getLocation() {
        return log("" + getLat() + " " + getLng());
    }

    public static Result gotoRegister() {
        Form<Register> registerForm = Form.form(Register.class);
        return ok(register.render(registerForm));
    }

    //@Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(index.render());
    }

    public static Result log(String text) {
        return ok(log.render(text));
    }

    public static Result login() {
        Form<Login> loginForm = Form.form(Login.class);
        return ok(login.render(loginForm));
    }

    @Security.Authenticated(Secured.class)
    public static Result logout() {
        session().clear();
        return redirect(routes.Application.login());
    }

    public static Result map(long id) {
        if (id == 0L) {
            return ok(map.render(0L,0,0,getLatLng()));
        }
        Picture p = dbManager.getPicture(id);
        return ok(map.render(id, p.getWidth(), p.getHeight(),getLatLng()));
    }

    public static Result new_game_menu() {
        return ok(new_game_menu.render());
    }

    public static Result no_javascript() {return ok(no_javascript.render());}

    public static Result picture(Long id) throws IOException {
        return ok(Files.toByteArray(new File("public/images/pictures/" + id + ".jpg"))).as("image/jpg");
    }

    public static Result play_menu() {
        return ok(play_menu.render());
    }

    public static Result practise() {
        long id = 1;
        id+=Math.random()*dbManager.getPictureCount();
        Picture p = dbManager.getPicture(id);
        return ok(practise.render(id, p.getWidth(), p.getHeight()));
    }

    public static Result practiseAction(Long id) {
        return result(id);
    }

    public static Double prettifyDistance(double distance) {
        return Double.valueOf(String.format("%.1f",distance/1000));
    }

    public static Result register() {
        Form<Register> registerForm = Form.form(Register.class).bindFromRequest();
        if (registerForm.hasErrors()) {
            return badRequest(register.render(registerForm));
        }
        else {
            dbManager.registerUser(new User(registerForm.get().name, registerForm.get().email, registerForm.get().password));
            session().clear();
            session("username", registerForm.get().name);
            return redirect(routes.Application.index());
        }
    }

    public static Result result(long id) {
        Picture picture = dbManager.getPicture(id);
        if (getDistance(picture)!=null) {
            return ok(result.render(picture, prettifyDistance(getDistance(picture))));
        }
        return ok(result.render(picture,null));
    }

    public static Result result_map(long id) {
        Picture picture = dbManager.getPicture(id);
        List<LatLng> latlngs = new ArrayList<>();
        latlngs.add(picture.getLatLng());
        if (getLat() != null && getLng() != null) {
            latlngs.add(new LatLng(getLat(), getLng()));
            session().remove("lat");
            session().remove("lng");
            return ok(result_map.render(latlngs));
        }
        return ok(result_map.render(latlngs));
    }

    public static Result search_friend() {
        return ok(search_friend.render());
    }

    public static Result send_email() {
        return ok(send_email.render());
    }

    public static Result send_email_action() {

        DynamicForm dynamicForm = Form.form().bindFromRequest();
        String email = dynamicForm.get("email");
        String text = dynamicForm.get("text");
        String name = dynamicForm.get("name");
        EmailManager emailManager = new EmailManager();
        boolean sentornot = emailManager.send(email,"Where Am I Invitation (From " + name + ")",text);
        return redirect(routes.Application.send_email_done(sentornot, email));
    }

    public static Result send_email_done(boolean sentornot, String email) {
        return ok(send_email_done.render(sentornot, email));
    }

    public static Result setLocation(double lat, double lng) {
        session("lat", String.valueOf(lat));
        session("lng",String.valueOf(lng));
        return ok();
    }

    public static Result template() {
        return ok(template.render("Template", null, null));
    }

    public static Result template_with_navbar() {
        return ok(template_with_navbar.render("Template with navbar", null, null));
    }

    public static Result thumbnail(Long id) throws IOException {
        return ok(Files.toByteArray(new File("public/images/thumbnails/" + id + ".jpg"))).as("image/jpg");
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
            BufferedImage img = ImageIO.read(file);

            // save picture to db
            Picture pic = new Picture(getLat(),getLng(),title,description, img.getWidth(), img.getHeight(), dbManager.getUser(session().get("username")));
            dbManager.savePicture(pic);

            // save picture to "public/images/pictures/pictureID.jpg"
            //img = PictureManager.getInstance().getScaledImage(img);
            PictureManager.getInstance().saveToFile(img, pic.getId().intValue());
            img=PictureManager.getInstance().createThumbnail(img,100);
            PictureManager.getInstance().saveAsThumbnail(img, pic.getId().intValue());

            //EmailManager.getInstance().send("bernhard.e.fritz@gmail.com","test","dies ist ein testtext");
            //return redirect("picture/" + pic.getId()); // seite refreshen
            return index();
        } else {
            return upload();
        }
    }
}
