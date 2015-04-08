package controllers;

import com.google.common.io.Files;
import models.Picture;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.*;

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
        return ok(getLocation.render(getLat(),getLng()));
    }

    public static Result setLocation(double lng, double lat) {
        session("lat", "" + lat);
        session("lng",""+lng);
        return ok();
    }

    public static Result upload() {
        return ok(upload.render());
    }

    public static Result uploadAction() {
        MultipartFormData body = request().body().asMultipartFormData();
        Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
        String[] descriptionParams = asFormUrlEncoded.get("description");
        String description = descriptionParams!=null?descriptionParams[0]:"N/A";
        FilePart picture = body.getFile("picture");
        if (picture != null && picture.getContentType().contains("image")){
            //String fileName = picture.getFilename();
            //String contentType = picture.getContentType();
            File file = picture.getFile();
            byte[] blob=null;
            try {
                blob = Files.toByteArray(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //new Picture(blob,getLat(),getLng(),description).save(); //save picture to db TODO: fix
            return ok(blob).as("image/jpeg"); // display blob
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
}
