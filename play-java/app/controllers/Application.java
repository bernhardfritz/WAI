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

    /**
     * @param i to display if changes were successful (i=0 display, i=1 display successful, i=2 display not successful)
     * @return the account page
     */
    public static Result account(int i){    // i to display if changes where successful (i=0 display nothing, i=1 display successful, i=2 display not successful
        return ok(account.render(0, getCurrentUser()));
    }

    /**
     * adds a freind to a user
     * @return
     */
    public static Result addFriend(String user){
        dbManager.saveFriend(getCurrentUser(),dbManager.getUser(user));
        return redirect(routes.Application.search_user(""));
    }

    /**
     * creates a game vs a user
     * @return
     */
    public static Result addGame(String user){
        dbManager.createGame(getCurrentUser(), dbManager.getUser(user));
        return redirect(routes.Application.search_user(""));
    }

    public static Result addGameMenu(String user){
        dbManager.createGame(getCurrentUser(), dbManager.getUser(user));
        return redirect(routes.Application.play_menu());
    }

    public static Result addRandomGameMenu() {
        Random rand = new Random();
        Long ret=-1L;
        List<Game> unf = dbManager.getUnreadyUnfinishedGames(getCurrentUser());
        List<Game> fin = dbManager.getReadyUnfinishedGames(getCurrentUser());
        List<Game> games = new ArrayList<Game>();
        games.addAll(unf);
        games.addAll(fin);
        List<Long> IDs = new ArrayList<Long>();
        for (Game x : games) {
            IDs.add(x.getUser1ID());
            IDs.add(x.getUser2ID());
        }

        if(games.size()+1>=dbManager.getUserCount()){
            return redirect(routes.Application.randomGameError());
        }
        while(ret==-1){
            int id = rand.nextInt(dbManager.getUserCount()) + 1;
            Long ID=new Long(id);
            if(!IDs.contains(ID)){
                ret=ID;
            }
        }
        dbManager.createGame(getCurrentUser(), dbManager.getUser(ret));
        return redirect(routes.Application.play_menu());
    }

    /**
     * Display the admin webinterface
     * @return the admin page
     */
    public static Result admin() {
        return ok(admin.render());
    }

    public static Result approveReport(Long id) {
        dbManager.acceptReportChanges(dbManager.getReport(id));
        return redirect(routes.Application.reports(1));
    }

    /**
     * Display the authentication page
     * @return the authenticate page
     */
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

    /**
     * For debugging reasons. Display the blank template without any content
     * @return the blank template page for previewing reasons
     */
    public static Result blank() {
        return ok(blank.render("Blank",null,null));
    }

    /**
     * For debugging reasons. Display a bootstrap example page
     * @return a page containing useful bootstrap features
     */
    public static Result bootstrap() {
        return ok(bootstrap.render());
    }

    /**
     * Evaluate a POST request to change password
     * @param i to differ between i=0 change email and i=1 change password
     * @return a page informing the user if changing password succeeded
     */
    public static Result changeEmailPassw(int i) {  // i=0 change email, i=1 change password
        HashManager hashManager = HashManager.getInstance();
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        int changedOrNot = 0;
        if (i==0){
            String oldemail = dynamicForm.get("oldemail");
            String newemail1 = dynamicForm.get("newemail1");
            String newemail2 = dynamicForm.get("newemail2");
            if(newemail1.equals(newemail2) && hashManager.codeString(oldemail).equals(getCurrentUser().getEmail())){
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
            if(newpassword1.equals(newpassword2) && hashManager.codeString(oldpassword).equals(getCurrentUser().getPassword())){
                dbManager.changeUserPassword(getCurrentUser(), newpassword1);
                changedOrNot=3;
            }
            else{
                changedOrNot=4;
            }
            //System.out.println("passw " + oldpassword +  newpassword1 + newpassword2 + " " + dbManager.getActiveUser(session().get("username")).getPassword());
        }
        return ok(account.render(changedOrNot, getCurrentUser()));
    }

    /**
     * To get the current User
     * @return the current User object
     */
    private static User getCurrentUser() {
        return dbManager.getActiveUser(session().get("username"));
    }

    /**
     * To change the password if the current password has been forgotten
     * @param token Token
     * @return the page to change the password
     */
    public static Result changePassword(String token){
        return ok(changePassword.render(token,0));
    }

    /**
     * Evaluate a POST request to change password
     * @param token Token
     * @return the page to change the password
     */
    public static Result changePasswordAction(String token){
        System.out.println(token);
        System.out.println(dbManager.getTokenFromString(token).getEmail());
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        int changedOrNot = 0;
        String newpassword1 = dynamicForm.get("password1");
        String newpassword2 = dynamicForm.get("password2");
        if(newpassword1.equals(newpassword2) && dbManager.getTokenFromString(token).isValid()){
            changedOrNot=1;
            dbManager.changeUserPassword(dbManager.getActiveUserFromEmail(dbManager.getTokenFromString(token).getEmail()), newpassword1);
        }
        else{
            changedOrNot=2;
        }
        return ok(changePassword.render("",changedOrNot));
    }

    /**
     * Clears attribute lat and lng from session
     * @return http ok
     */
    public static Result clearLatLng(){
        session().remove("lat");
        session().remove("lng");
        return ok();
    }

    /**
     * Dissapprove a report
     * @param id the id of the report in concern
     * @return
     */
    public static Result disapproveReport(Long id) {
        Report report = dbManager.getReport(id);
        report.setHandled(true);
        report.save();
        return redirect(routes.Application.reports(1));
    }

    /**
     * Displays the friendlist
     * @return the page with the freindlist
     */
    public static Result friends() {
        return ok(friends.render(dbManager.getFriends(getCurrentUser())));
    }

    /**
     * Displays the forgot password page
     * @param sentornot depending on the value of this variable the page is generated dynamically 0=no additional text, 1=email sent successfully, 2=email couldn't be sent
     * @param email the email adress in concern
     * @return
     */
    public static Result forgotPassword(int sentornot,String email){
        return ok(forgotPassword.render(sentornot, email));
    }

    /**
     * POST action when pressing send at forgotPassword.scala.html
     * Sends an email to a given address with a link to change a Password
     * @return
     */
    public static Result forgotPasswordSend(){
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        String email = dynamicForm.get("email");
        int i=2;
        if(dbManager.isEmailTaken(email)){
            EmailManager emailManager = new EmailManager();
            String text1 = emailManager.read("resetPassword1");
            String text2 = emailManager.read("resetPassword2");
            RandomStringUtils randString = new RandomStringUtils();
            Random rand = new Random();
            int length = rand.nextInt(15)+20;
            String token = randString.randomAlphanumeric(length);
            String tosend = token + "<br><a href='localhost:9000/changePassword?token=' " + token + " style='color:#225A92; text-decoration:none;'>";
            boolean sentornot = emailManager.send(1,email,"Where am I, Reset your password",text1+tosend+text2);
            i = (sentornot) ? 1 : 2;
            dbManager.saveToken(email, token);
            System.out.println(token);
            System.out.println(dbManager.getTokenFromEmail(email).getTokenString());
        }
        return ok(forgotPassword.render(i,email));
    }

    /**
     * Generates a page containing all pictures paginated (10 pictures per page)
     * @param currentpage the current page index
     * @return
     */
    public static Result gallery(Integer currentpage) {
        Pagination p = new Pagination(dbManager.getPictureCount(),10,currentpage);
        return ok(gallery.render(p.getCurrentPageIndex(), p.getMaxPageIndex(), dbManager.getPictureRange(p.getStartPageIndex(), p.getEndPageIndex())));
    }

    /**
     * POST action when modifying a picture using gallery.scala.html
     * @return
     */
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
                Double.parseDouble(longitude), title, description, accepted != null);
        return redirect(routes.Application.gallery(1));
    }

    /**
     * GET action when deleting a picture using gallery.scala.html
     * @param id
     * @return
     */
    public static Result gallery_delete(Long id) {
        System.out.println(id);
        dbManager.unacceptPicture(dbManager.getAcceptedPicture(id));
        return redirect(routes.Application.gallery(1));
    }

    /**
     * Display the game page
     * @param id
     * @return
     */
    public static Result game(Long id) {
        Game g=dbManager.getGame(id);
        dbManager.onGameStart(g, getCurrentUser());
        return ok(game.render(dbManager.getCurrentRound(g).getPictureID(), id));
    }

    /**
     * Action after a game is played
     * @param id the picture id
     * @param gameID the game id
     * @return
     */
    public static Result gameAction(Long id,Long gameID) {
        Game game =dbManager.getGame(gameID);
        User user= getCurrentUser();
        Double distance=getDistance(dbManager.getAcceptedPicture(id));
        if (distance==null){
            distance=-1.0;
        }
        dbManager.gameAction(game,user,distance);
        return result(id);
    }

    /**
     * The formula to calculate the distance between two points (point from picture and from session)
     * @param picture
     * @return
     */
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

    /**
     * Get latitude from session
     * @return latitude if session contains attribute lat, else return null
     */
    public static Double getLat() {
        if(session().get("lat") != null) {
            String session = session().get("lat").toString();
            double lat = session != null ? Double.parseDouble(session) : 0.0;
            return lat;
        }
        return null;
    }

    /**
     * Get longitude from session
     * @return longitude if session contains attribute lng, else return null
     */
    public static Double getLng() {
        if (session().get("lng") != null) {
            String session = session().get("lng").toString();
            double lng = session != null ? Double.parseDouble(session) : 0.0;
            return lng;
        }
        return null;
    }

    /**
     * Get latitude and longitude from session and create a LatLng object
     * @return a LatLng object retrieved from session
     */
    public static LatLng getLatLng() {
        LatLng latlng = null;
        Double lat = getLat();
        Double lng = getLng();
        if(lat==null || lng==null) return null;
        return new LatLng(lat,lng);
    }

    /**
     * A page previewing latitude and longitude retrieved from session
     * @return
     */
    public static Result getLocation() {
        return log("" + getLat() + " " + getLng());
    }

    /**
     * Register page
     * @return
     */
    public static Result gotoRegister() {
        Form<Register> registerForm = Form.form(Register.class);
        return ok(register.render(registerForm));
    }

    /**
     * Main page
     * @return
     */
    //@Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(index.render());
    }

    /**
     * Page for debugging reasons to display strings
     * @param text
     * @return
     */
    public static Result log(String text) {
        return ok(log.render(text));
    }

    /**
     * Login page
     * @return
     */
    public static Result login() {
        Form<Login> loginForm = Form.form(Login.class);
        return ok(login.render(loginForm));
    }

    /**
     * Page to logout/clear session
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result logout() {
        session().clear();
        return redirect(routes.Application.login());
    }

    /**
     * Map page. Will only be referenced by iframe
     * @param id the picture id to preview on map. if id=0: no picture will be displayed
     * @return
     */
    public static Result map(long id) {
        if (id == 0L) {
            return ok(map.render(0L,0,0,getLatLng()));
        }
        Picture p = dbManager.getAcceptedPicture(id);
        return ok(map.render(id, p.getWidth(), p.getHeight(), getLatLng()));
    }

    /**
     * This page will be opened if javascript is disabled
     * @return
     */
    public static Result no_javascript() {return ok(no_javascript.render());}

    /**
     * Displays a picture using the id from parameters
     * @param id the id of the picture from database
     * @return
     * @throws IOException
     */
    public static Result picture(Long id) throws IOException {
        return ok(Files.toByteArray(new File("public/images/pictures/" + id + ".jpg"))).as("image/jpg");
    }

    /**
     * Menü mit allen offenen und wartenden Spielen
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result play_menu() {
        return ok(play_menu.render(dbManager.getReadyUnfinishedGames(getCurrentUser()), dbManager.getUnreadyUnfinishedGames((getCurrentUser())), dbManager.getFinishedGames(getCurrentUser(),10)));
    }

    public static Result practise() {
        long id=dbManager.getRandomAcceptedPicture().getId();
        return ok(practise.render(id));
    }

    public static Result practiseAction(Long id) {
        return result(id);
    }

    /**
     * Round distance for pretty printing
     * @param distance
     * @return
     */
    public static String prettifyDistance(double distance) {
        return String.format("%.1f", distance / 1000.0);
    }

    public static Result randomGameError(){
        return ok(randomGameError.render());
    }

    /**
     * Display register page
     * @return
     */
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

    /**
     * @param id the id to display picture from database on report
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result report(Long id) {
        Picture picture = dbManager.getAcceptedPicture(id);
        double lat=picture.getLat();
        double lng=picture.getLng();
        session("lat", String.valueOf(lat));
        session("lng", String.valueOf(lng));
        return ok(report.render(picture));
    }

    /**
     * POST action when submitting a report
     * @return
     */
    public static Result reportAction() {
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        String title = dynamicForm.get("title");
        String description = dynamicForm.get("description");
        String optional = dynamicForm.get("optional");
        Double lat=Double.parseDouble(dynamicForm.get("latitude"));
        Double lng=Double.parseDouble(dynamicForm.get("longitude"));
        String username=dynamicForm.get("username");
        User user = dbManager.getUser(username);
        Picture picture = dbManager.getAcceptedPicture(Long.parseLong(dynamicForm.get("old_id")));
        session().remove("lat");
        session().remove("lng");
        Report report=new Report(lat,lng,title,description,optional,user,picture); //Do Something with thr Object
        dbManager.saveReport(report);
        return redirect(routes.Application.index());
    }

    /**
     * Display a paginated table containing all reports
     * @param currentpage the current page index
     * @return
     */
    public static Result reports(Integer currentpage) {
        Pagination p = new Pagination(dbManager.getUnhandledReportCount(),10,currentpage);
        return ok(reports.render(p.getCurrentPageIndex(), p.getMaxPageIndex(), dbManager.getUnhandledReportRange(p.getStartPageIndex(), p.getEndPageIndex())));
    }

    /**
     * Show result containing the distance the player's guess is off including a map to preview the game visually
     * @param id
     * @return
     */
    public static Result result(long id) {
        Picture picture = dbManager.getAcceptedPicture(id);
        if (getDistance(picture)!=null) {
            return ok(result.render(picture, "You were off by " + prettifyDistance(getDistance(picture)) + " km"));
        }
        return ok(result.render(picture, "You didn't place a marker."));
    }

    /**
     * Show the map which will be used in result as iframe
     * @param id
     * @return
     */
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

    /**
     * Show a list containing all users which fulfil the search string
     * @param str the username you want to search for
     * @return
     */
    public static Result search_user(String str) {
        if (str.equals("")){
            List<User> list = new ArrayList<User>();
            return ok(search_user.render("", list));
        }
        DBManager dbman = DBManager.getInstance();
        return ok(search_user.render("", dbman.findUser(str)));
    }

    /**
     * Send email page
     * @return
     */
    public static Result send_email() {
        return ok(send_email.render());
    }

    /**
     * Read post form, send email and give feedback if it was sent successfully
     * @return
     */
    public static Result send_email_action() {
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        String email = dynamicForm.get("email");
        EmailManager emailManager = new EmailManager();
        String text1 = emailManager.read("inviteEmail");
        boolean sentornot = emailManager.send(1,email,"Where Am I Invitation (From " + getCurrentUser().getName() + ")",text1);
        return redirect(routes.Application.send_email_done(sentornot, email));
    }

    /**
     *
     * @param sentornot is true if email was sent successfully. False otherwise.
     * @param email the emailadress
     * @return
     */
    public static Result send_email_done(boolean sentornot, String email) {
        return ok(send_email_done.render(sentornot, email));
    }

    /**
     * Used to define latitude and longitude in session
     * @param lat the latitude
     * @param lng the longitude
     * @return
     */
    public static Result setLocation(double lat, double lng) {
        session("lat", String.valueOf(lat));
        session("lng",String.valueOf(lng));
        return ok();
    }

    /**
     * Used for AJAX GET requests to display searched users dynamically
     * @param str the username
     * @return
     */
    public static Result setUser(String str) {
        StringBuilder strbuild = new StringBuilder();
        for (User user:dbManager.findUser(str)){
            strbuild.append(user.getName()).append(" ");
        }
        return ok(strbuild.toString());
    }

    /**
     * Used for debugging reasons. Displays the template without any content
     * @return
     */
    public static Result template() {
        return ok(template.render("Template", null, null));
    }

    /**
     * Used for debugging reasons. Displays the template_navbar without any content
     * @return
     */
    public static Result template_navbar() {
        return ok(template_navbar.render("Template", null));
    }

    /**
     * Used for debugging reasons. Displays the template_with_navbar without any content
     * @return
     */
    public static Result template_with_navbar() {
        return ok(template_with_navbar.render("Template with navbar", null, null));
    }

    /**
     * Uses a binary stream to display a picture in the browser
     * @param id the picture id
     * @return
     * @throws IOException
     */
    public static Result thumbnail(Long id) throws IOException {
        return ok(Files.toByteArray(new File("public/images/thumbnails/" + id + ".jpg"))).as("image/jpg");
    }

    /**
     * (De)Activates a user
     * @param id the user id
     * @return
     */
    public static Result toggleUser(Long id) {
        dbManager.toggleUser(id);
        System.out.println(id);
        return ok();
    }

    /**
     * Display the upload page
     * @return
     */
    public static Result upload() {
        return ok(upload.render());
    }

    /**
     * Evaluate a multipart form and put a new Picture object in the database
     * @return
     * @throws IOException
     */
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
            Picture pic = new Picture(getLat(),getLng(),title,description, img.getWidth(), img.getHeight(), getCurrentUser());
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

    /**
     * Displays a paginated table including all users
     * @param currentpage the current page index
     * @return
     */
    public static Result users(Integer currentpage) {
        Pagination p = new Pagination(dbManager.getUserCount(),10,currentpage);
        return ok(users.render(p.getCurrentPageIndex(), p.getMaxPageIndex(), dbManager.getUserRange(p.getStartPageIndex(), p.getEndPageIndex())));
    }
}
