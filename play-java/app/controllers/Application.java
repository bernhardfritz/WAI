package controllers;

import com.google.common.io.Files;
import models.*;
import org.apache.commons.lang3.RandomStringUtils;
import java.util.Random;
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
            name = name.trim();
            email = email.trim();
            password = password.trim();
            passwordConfirm = passwordConfirm.trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() ||
                    !password.equals(passwordConfirm)) {
                return "Invalid name, email address or password!";
            }
            for (User u : dbManager.getAllActiveUsers()) {
                if (u.getName().equals(name) || u.getEmail().equals(email)) {
                    return "Invalid name, email address or password!";
                }
            }
            return null;
        }
    }

    public static Result account(int i){    // i to display if changes where successful (i=0 display nothing, i=1 display successful, i=2 display not successful
        return ok(account.render(0, dbManager.getActiveUser(session().get("username"))));
    }

    public static Result admin() {
        return ok(admin.render());
    }

    public static Result approveReport(Long id) {
        dbManager.acceptReportChanges(dbManager.getReport(id));
        return redirect(routes.Application.reports(1));
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

    public static Result changeEmailPassw(int i) {  // i=0 change email, i=1 change password
        HashManager hashManager = HashManager.getInstance();
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        int changedOrNot = 0;
        if (i==0){
            String oldemail = dynamicForm.get("oldemail");
            String newemail1 = dynamicForm.get("newemail1");
            String newemail2 = dynamicForm.get("newemail2");
            if(newemail1.equals(newemail2) && hashManager.codeString(oldemail).equals(dbManager.getActiveUser(session().get("username")).getEmail())){
                //change email
                changedOrNot=1;
            }
            else{
                changedOrNot=2;
            }
            //System.out.println("email " + oldemail +  newemail1 + newemail2);
        }
        if(i==1){
            String oldpassword = dynamicForm.get("oldpassword");
            String newpassword1 = dynamicForm.get("newpassword1");
            String newpassword2  = dynamicForm.get("newpassword2");
            if(newpassword1.equals(newpassword2) && hashManager.codeString(oldpassword).equals(dbManager.getActiveUser(session().get("username")).getPassword())){
                dbManager.changeUserPassword(dbManager.getActiveUser(session().get("username")), newpassword1);
                changedOrNot=3;
            }
            else{
                changedOrNot=4;
            }
            //System.out.println("passw " + oldpassword +  newpassword1 + newpassword2 + " " + dbManager.getActiveUser(session().get("username")).getPassword());
        }
        return ok(account.render(changedOrNot, dbManager.getActiveUser(session().get("username"))));
    }

    public static Result clearLatLng(){
        session().remove("lat");
        session().remove("lng");
        return ok();
    }

    public static Result disapproveReport(Long id) {
        Report report = dbManager.getReport(id);
        report.setHandled(true);
        report.save();
        return redirect(routes.Application.reports(1));
    }

    public static Result forgotPassword(int sentornot,String email){
        return ok(forgotPassword.render(sentornot, email));
    }

    public static Result forgotPasswordSend(){
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        String email = dynamicForm.get("email");
        EmailManager emailManager = new EmailManager();
        String text1 = emailManager.read("resetPassword1");
        String text2 = emailManager.read("resetPassword2");
        RandomStringUtils randString = new RandomStringUtils();
        Random rand = new Random();
        int length = rand.nextInt(6)+10;
        String tempPassword = randString.randomAlphanumeric(length);
        boolean sentornot = emailManager.send(1,email,"Where am I, Reset your password",text1+tempPassword+text2);
        int i = (sentornot) ? 1 : 2;
        return ok(forgotPassword.render(i,email));
    }

    public static Result gallery(Integer currentpage) {
        Long start = (currentpage-1)*10+1L;
        Long end = start+9L;
        Integer maxpage = dbManager.getPictureCount()/10+1;
        return ok(gallery.render(currentpage, maxpage, dbManager.getPictureRange(start,end)));
    }

    public static Result gallery_edit() {
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        String id = dynamicForm.get("id");
        String accepted = dynamicForm.get("accepted");
        String title = dynamicForm.get("title");
        String latitude = dynamicForm.get("latitude");
        String longitude = dynamicForm.get("longitude");
        String description = dynamicForm.get("description");
        System.out.println(id+"\n"+accepted+"\n"+title+"\n"+latitude+"\n"+longitude+"\n"+description);
        dbManager.editPicture(dbManager.getPicture(Long.parseLong(id)), Double.parseDouble(latitude),
                Double.parseDouble(longitude), title, description, accepted!=null);
        return redirect(routes.Application.gallery(1));
    }

    public static Result gallery_delete(Long id) {
        System.out.println(id);
        dbManager.unacceptPicture(dbManager.getAcceptedPicture(id));
        return redirect(routes.Application.gallery(1));
    }

    public static Result game(Long id) {
        return result(id);
    }

    public static Result game() {
        return ok(game.render(dbManager.getRandomAcceptedPicture().getId()));
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
        Picture p = dbManager.getAcceptedPicture(id);
        return ok(map.render(id, p.getWidth(), p.getHeight(), getLatLng()));
    }


    public static Result no_javascript() {return ok(no_javascript.render());}

    public static Result picture(Long id) throws IOException {
        return ok(Files.toByteArray(new File("public/images/pictures/" + id + ".jpg"))).as("image/jpg");
    }

    @Security.Authenticated(Secured.class)
    public static Result play_menu() {
        return ok(play_menu.render(dbManager.getReadyUnfinishedGames(dbManager.getActiveUser(session().get("username"))),dbManager.getUnreadyUnfinishedGames((dbManager.getActiveUser(session().get("username"))))));
    }

    public static Result practise() {
        return game();
        /*long id = 1;
        id+=Math.random()*dbManager.getAcceptedPictureCount();
        Picture p = dbManager.getAcceptedPicture(id);
        return ok(practise.render(id, p.getWidth(), p.getHeight()));*/
    }

    public static Result practiseAction(Long id) {
        return result(id);
    }

    public static String prettifyDistance(double distance) {
        return String.format("%.1f", distance / 1000.0);
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
    @Security.Authenticated(Secured.class)
    public static Result report(Long id) {
        Picture picture = dbManager.getAcceptedPicture(id);
        double lat=picture.getLat();
        double lng=picture.getLng();
        session("lat", String.valueOf(lat));
        session("lng", String.valueOf(lng));
        return ok(report.render(picture));
    }

    public static Result reportAction() {
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        String title = dynamicForm.get("title");
        String description = dynamicForm.get("description");
        String optional = dynamicForm.get("optional");
        Double lat=Double.parseDouble(dynamicForm.get("latitude"));
        Double lng=Double.parseDouble(dynamicForm.get("longitude"));
        String username=dynamicForm.get("username");
        User user = dbManager.getUser(username);
        Long oldID=Long.parseLong(dynamicForm.get("old_id"));
        session().remove("lat");
        session().remove("lng");
        Report report=new Report(lat,lng,title,description,optional,user,oldID); //Do Something with thr Object
        dbManager.saveReport(report);
        return redirect(routes.Application.index());
    }

    public static Result reports(Integer currentpage) {
        Long start = (currentpage - 1) * 10 + 1L;
        Long end = start + 9L;
        Integer maxpage = dbManager.getUnhandledReportCount() / 10 + 1;
        return ok(reports.render(currentpage, maxpage, dbManager.getUnhandledReportRange(start, end)));
    }

    public static Result result(long id) {
        Picture picture = dbManager.getAcceptedPicture(id);
        if (getDistance(picture)!=null) {
            return ok(result.render(picture, "You were off by "+prettifyDistance(getDistance(picture))+" km"));
        }
        return ok(result.render(picture, "You didn't place a marker."));
    }

    public static Result result_map(long id) {
        Picture picture = dbManager.getAcceptedPicture(id);
        List<LatLng> latlngs = new ArrayList<LatLng>();
        latlngs.add(picture.getLatLng());
        if (getLat() != null && getLng() != null) {
            latlngs.add(new LatLng(getLat(), getLng()));
            session().remove("lat");
            session().remove("lng");
            return ok(result_map.render(latlngs));
        }
        return ok(result_map.render(latlngs));
    }

    public static Result search_user(String str) {
        if (str.equals("")){
            List<User> list = new ArrayList<User>();
            return ok(search_user.render("", list));
        }
        DBManager dbman = DBManager.getInstance();
        return ok(search_user.render("", dbman.findUser(str)));
    }

    public static Result send_email() {
        return ok(send_email.render());
    }

    public static Result send_email_action() {
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        String email = dynamicForm.get("email");
        EmailManager emailManager = new EmailManager();
        String text1 = emailManager.read("inviteEmail");
        boolean sentornot = emailManager.send(1,email,"Where Am I Invitation (From " + dbManager.getActiveUser(session().get("username")).getName() + ")",text1);
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

    public static Result setUser(String str) {
        StringBuilder strbuild = new StringBuilder();
        for (User user:dbManager.findUser(str)){
            strbuild.append(user.getName()).append(" ");
        }
        return ok(strbuild.toString());
    }


    public static Result template() {
        return ok(template.render("Template", null, null));
    }

    public static Result template_navbar() {
        return ok(template_navbar.render("Template", null));
    }

    public static Result template_with_navbar() {
        return ok(template_with_navbar.render("Template with navbar", null, null));
    }

    public static Result thumbnail(Long id) throws IOException {
        return ok(Files.toByteArray(new File("public/images/thumbnails/" + id + ".jpg"))).as("image/jpg");
    }

    public static Result toggleUser(Long id) {
        dbManager.toggleUser(id);
        System.out.println(id);
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
            BufferedImage img = ImageIO.read(file);

            // save picture to db
            Picture pic = new Picture(getLat(),getLng(),title,description, img.getWidth(), img.getHeight(), dbManager.getActiveUser(session().get("username")));
            dbManager.savePicture(pic);

            // save picture to "public/images/pictures/pictureID.jpg"
            //img = PictureManager.getInstance().getScaledImage(img);
            PictureManager.getInstance().saveToFile(img, pic.getId().intValue());
            img=PictureManager.getInstance().createThumbnail(img,256);
            PictureManager.getInstance().saveAsThumbnail(img, pic.getId().intValue());

            //EmailManager.getInstance().send("bernhard.e.fritz@gmail.com","test","dies ist ein testtext");
            //return redirect("picture/" + pic.getId()); // seite refreshen
            return index();
        } else {
            return upload();
        }
    }

    public static Result users(Integer currentpage) {
        Long start = (currentpage-1)*10+1L;
        Long end = start+9L;
        Integer maxpage = dbManager.getUserCount()/10+1;
        return ok(users.render(currentpage, maxpage, dbManager.getUserRange(start, end)));
    }
}
