package controllers;

import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;


public class Application extends Controller {
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
            //new Picture(Files.toByteArray(f),getLat(),getLng(),title,description).save(); //save picture to db
            BufferedImage img = ImageIO.read(file);
            img = PictureManager.getInstance().getScaledImage(img);
            PictureManager.getInstance().saveToFile(img,1);
            EmailManager.getInstance().send("bernhard.e.fritz@gmail.com","test","dies ist ein testtext");
            return redirect("picture/1"); // seite refreshen
        } else {
            return badRequest("No File or wrong type");
        }
    }

    public static Result bootstrap() {
        return ok(bootstrap.render());
    }

    public static Result login() {
        return ok(login.render());
    }

    public static Result register() {
        return ok(register.render());
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

    public static Result game() {
        return ok(game.render());
    }

    public static Result picture(int id) {
        return ok(picture.render(id));
    }
}
