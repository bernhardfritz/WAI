package controllers;

import com.google.common.io.Files;

import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.index;
import views.html.location;
import views.html.map;
import views.html.upload;

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

    public static Result up() {
        return ok(upload.render());
    }

    public static Result upload() {
        MultipartFormData body = request().body().asMultipartFormData();
        FilePart picture = body.getFile("picture");
        if (picture != null) {
            String fileName = picture.getFilename();
            String contentType = picture.getContentType();
            File file = picture.getFile();
            byte[] blob=null;
            try {
                blob = Files.toByteArray(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // do something with blob
            return ok("File uploaded");
        } else {
            flash("error", "Missing file");
            return index();
        }
    }
        public static Result createImage(byte[] byteArray) {
            return ok(byteArray).as("image/jpg");
        }
}
