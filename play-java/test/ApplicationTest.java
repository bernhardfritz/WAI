import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.Application;
import controllers.DBManager;
import controllers.PictureManager;
import models.LatLng;
import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import play.twirl.api.Content;

import javax.imageio.ImageIO;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;


/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest {

    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }

    /*@Test
    public void renderTemplate() {
        Content html = views.html.index.render("Your new application is ready.");
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("Your new application is ready.");
    }*/

    @Test
    public void checkGetDistance() {
        LatLng l1 = new LatLng(47.259659, 11.400375); // Innsbruck
        LatLng l2 = new LatLng(48.210033, 16.363449); // Vienna
        double distance = Application.getDistance(l1,l2) / 1000.0; //distance in km
        assertThat(distance).isGreaterThan(380.0);
        assertThat(distance).isLessThan(390.0);
    }


    @Test
    public void checkCreateThumbnail() {
        BufferedImage img1=null;
        BufferedImage img2=null;
        try {
            img1= ImageIO.read(new File("../../public/images/smiley1.jpg"));
            img2= ImageIO.read(new File("../../public/images/foto2.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertThat(PictureManager.getInstance().createThumbnail(img1, 100).getHeight()).isEqualTo(100);
        assertThat(PictureManager.getInstance().createThumbnail(img2,100).getHeight()).isEqualTo(100);

        assertThat(PictureManager.getInstance().createThumbnail(img1, 10).getHeight()).isEqualTo(10);
        assertThat(PictureManager.getInstance().createThumbnail(img2,10).getHeight()).isEqualTo(10);
    }



}
