package ai.berktest;



import org.openqa.selenium.WebDriver;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
public class BerkRequest extends Berk{

    String workingDir = System.getProperty("user.dir");
    String relativePath = "screenshot.png";
    String fullPath = workingDir + File.separator + relativePath;

    WebDriver driver;

    public BerkRequest(WebDriver appiumDriver) {
        this.driver = appiumDriver;
    }

    public BerkResponse getCoordinates(String elementPath, int deviceScreenWidth, int deviceScreenHeight) {
        String elementFullPath = workingDir + File.separator + elementPath;
        Map<String, Object> data = new HashMap<>();
        data.put("screenshot_path", fullPath);
        data.put("element_path", elementFullPath);
        data.put("deviceScreenWidth", deviceScreenWidth);
        data.put("deviceScreenHeight", deviceScreenHeight);
        data.put("image_exif", "true");

        BerkResponse coordinateResponse;

        coordinateResponse =
                given()
                        .contentType(ContentType.JSON)
                        .when()
                        .body(data)
                        .post(getHost()+getPort()+"/get-coordinates")
                        .as(BerkResponse.class);

        return coordinateResponse;
    }

    public String getText(String elementPath, int deviceScreenWidth, int deviceScreenHeight) {
        String elementFullPath = workingDir + File.separator + elementPath;
        Map<String, Object> data = new HashMap<>();
        data.put("screenshot_path", fullPath);
        data.put("element_path", elementFullPath);
        data.put("deviceScreenWidth", deviceScreenWidth);
        data.put("deviceScreenHeight", deviceScreenHeight);
        data.put("image_exif", "true");

        BerkResponse coordinateResponse;

        coordinateResponse =
                given()
                        .contentType(ContentType.JSON)
                        .when()
                        .body(data)
                        .post(getHost() + getPort() + "/get-text")
                        .as(BerkResponse.class);

        if ("success".equals(coordinateResponse.getStatus())) {
            return coordinateResponse.getText();
        }

        return null;

    }

}


