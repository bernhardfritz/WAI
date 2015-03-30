package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

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
}
