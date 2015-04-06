package controllers;

import com.google.common.io.Files;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.*;

import java.io.File;
import java.io.IOException;


public class Application extends Controller {
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result map() {
        return ok(map.render());
    }

    public static Result location(double lat, double lng) {
        System.out.println("lat: " + lat + " " + "lng: " + lng);
        return ok(location.render(lat,lng));
    }

    public static Result upload() {
        MultipartFormData body = request().body().asMultipartFormData();
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
            //new Picture(blob).save(); //save blob to db
            return ok(blob).as("image/jpeg"); // display blob
        } else {
            return badRequest("No File or wrong type");
        }
    }

    public static Result bootstrap() {
        return ok(bootstrap.render());
    }
}
