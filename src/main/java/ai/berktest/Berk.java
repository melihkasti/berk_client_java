package ai.berktest;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.*;

public class Berk {


    String port= "5003";
    String host= "http://0.0.0.0:";
    WebDriver driver;

    public Berk (WebDriver driver, String host,String port) {
        this.port=port;
        this.host=host;
        this.driver=driver;
    }

    public Berk() {

    }

    public Berk(String host) {
        this.host = host;
    }

    public Berk( WebDriver driver){
        this.driver=driver;

    }

    /**
     * This method returns the port number.
     *
     * @return the port number as a String
     */
    public String getPort(){
        return this.port;
    }

    /**
     * This method sets the port number.
     *
     * @param port the port number to set
     */
    public void setPort(String port){
        this.port = port;
    }

    /**
     * This method returns the host.
     *
     * @return the host as a String
     */
    public String getHost(){
        return this.host;
    }

    /**
     * This method sets the host.
     *
     * @param host the host to set
     */
    public void setHost(String host){
        this.host = host;
    }

    /**
     * Given a WebDriver, an element path, and a wait time, this method returns the center coordinates of the element.
     *
     * @param driver        the WebDriver instance
     * @param elementPath   the path to the element
     * @param waitTimeSecond the maximum time to wait for the element to be displayed
     * @return a list of integers representing the center coordinates of the element
     * @throws IOException if an I/O error occurs
     */

    public List<Integer> getCenterCoordinates (WebDriver driver, String elementPath, int waitTimeSecond) throws IOException {
        Dimension screenSize = driver.manage().window().getSize();
        int deviceScreenWidth = screenSize.width;
        int deviceScreenHeight = screenSize.height;
        List <Integer> centerPoint =new ArrayList<>();
        BerkRequest berkRequest = new BerkRequest(driver);
        isElementDisplayed(driver,elementPath,waitTimeSecond);
        BerkResponse response = berkRequest.getCoordinates(elementPath,deviceScreenWidth,deviceScreenHeight);
        centerPoint.add(response.getCenterX());
        centerPoint.add(response.getCenterY());

        return centerPoint;
    }

    public List<Integer> getCenterCoordinates (WebDriver driver, String elementPath) throws IOException {

        return getCenterCoordinates(driver, elementPath, 90);
    }

    /**
     * Waits until the specified element is displayed.
     *
     * @param driver      the WebDriver instance
     * @param elementPath the path to the element
     * @return true if the element is displayed, false otherwise
     */
    public boolean waitUntilElementDisplayed (WebDriver driver, String elementPath) throws IOException {
     return waitUntilElementDisplayed(driver,elementPath,90);
    }

    public boolean waitUntilElementDisplayed (WebDriver driver, String elementPath, int waitTime) throws IOException {
            return isElementDisplayed(driver,elementPath,waitTime);

    }

    public Boolean isElementDisplayed (WebDriver driver, String elementPath, int waitTimeSecond) throws IOException {
        takeScreenshot(driver);
        Dimension screenSize = driver.manage().window().getSize();
        int deviceScreenWidth = screenSize.width;
        int deviceScreenHeight = screenSize.height;
        int count = 0;
        BerkRequest coordinateRequest = new BerkRequest(driver);
        BerkResponse coordinateResponse = coordinateRequest.getCoordinates(elementPath,deviceScreenWidth,deviceScreenHeight);
        Instant currentTime = Instant.now();
        LocalTime waitTime = LocalTime.now().plusSeconds(waitTimeSecond);

        while (!coordinateResponse.getStatus().equals("success") && waitTime.isAfter(LocalTime.now())) {
            takeScreenshot(driver);
            Path path = Paths.get(elementPath);
            String fileName = path.getFileName().toString();
            coordinateResponse=coordinateRequest.getCoordinates(elementPath,deviceScreenWidth,deviceScreenHeight);
            System.out.println(count+2+"."+" try "+ fileName + (coordinateResponse.getStatus().equals("error") ? " not found" : " found"));
            count++;
        }

        Instant endTime = Instant.now();
        Duration duration = Duration.between(currentTime, endTime);
        Path path = Paths.get(elementPath);
        String fileName = path.getFileName().toString();
        System.out.println(fileName +" : "+  " found in " +duration.getSeconds() +" second" );

        return coordinateResponse.getStatus().equals("success");


    }


    /**
     * Checks if the specified element is displayed and returns a BerkResponse wrapped in an Optional.
     *
     * @param driver        the WebDriver instance
     * @param elementPath   the path to the element
     * @param waitTimeSecond the maximum time to wait for the element to be displayed
     * @return an Optional containing a BerkResponse if the element is displayed, or an empty Optional otherwise
     * @throws IOException if an I/O error occurs
     */
    public Map<Boolean,BerkResponse> ifElementDisplayedGetCoordinates(WebDriver driver, String elementPath, int waitTimeSecond) throws IOException {
        takeScreenshot(driver);
        Dimension screenSize = driver.manage().window().getSize();
        int deviceScreenWidth = screenSize.width;
        int deviceScreenHeight = screenSize.height;
        int count = 0;
        Map<Boolean,BerkResponse> result = new HashMap<Boolean,BerkResponse>();
        BerkRequest coordinateRequest = new BerkRequest(driver);
        BerkResponse coordinateResponse = coordinateRequest.getCoordinates(elementPath,deviceScreenWidth,deviceScreenHeight);
        Instant currentTime = Instant.now();
        LocalTime waitTime = LocalTime.now().plusSeconds(waitTimeSecond);

        while (!coordinateResponse.getStatus().equals("success") && waitTime.isAfter(LocalTime.now())) {
            takeScreenshot(driver);
            Path path = Paths.get(elementPath);
            String fileName = path.getFileName().toString();
            coordinateResponse=coordinateRequest.getCoordinates(elementPath,deviceScreenWidth,deviceScreenHeight);
            System.out.println(count+2+"."+" try "+ fileName + (coordinateResponse.getStatus().equals("error") ? " not found" : " found"));
            count++;
        }
        Instant endTime = Instant.now();
        Duration duration = Duration.between(currentTime, endTime);
        Path path = Paths.get(elementPath);
        String fileName = path.getFileName().toString();
        //System.out.println(fileName +" : "+  " found in " +duration.getSeconds() +" second" );

        if (coordinateResponse.getStatus().equals("success")){
            result.put(true,coordinateResponse);
        }
        else{
            result.put(false,coordinateResponse);
        }
        return result;

    }

    /**
     * This method clicks on the specified element.
     *
     * @param driver the WebDriver instance
     * @param elementName the path to the element
     * @param waitTime the maximum time to wait for the element to be displayed
     * @throws IOException if an I/O error occurs
     */
    public void click (WebDriver driver, String elementName, int waitTime ) throws IOException {
        takeScreenshot(driver);
        Map<Boolean, BerkResponse> coordinateResponse = ifElementDisplayedGetCoordinates(driver,elementName,waitTime);
        if (coordinateResponse.containsKey(true)){
            BerkResponse response = coordinateResponse.get(true);
            int coordinate_X = response.getCenterX();
            int coordinate_Y = response.getCenterY();
            TouchAction touchAction = new TouchAction((AppiumDriver) driver);
            touchAction.tap(PointOption.point(coordinate_X, coordinate_Y)).perform();
        }else {
            throw new NoSuchElementException(elementName + " is not available");
        }
    }



    /**
     * Takes a screenshot of the current state of the WebDriver.
     *
     * @param driver the WebDriver instance
     * @throws IOException if an I/O error occurs
     */
    public void takeScreenshot (WebDriver driver) throws IOException {

        byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        File targetFile = new File("screenshot.png");
        FileUtils.writeByteArrayToFile(targetFile, screenshotBytes);
    }

    public void scrollUntilElementDisplayed (WebDriver driver, String elementPath) throws IOException {

        Dimension screenSize = driver.manage().window().getSize();
        int deviceScreenWidth = screenSize.width;
        int deviceScreenHeight = screenSize.height;

        while (!isElementDisplayed(driver, elementPath,1)) {
            TouchAction touchAction = new TouchAction((AppiumDriver) driver);
            touchAction.press(PointOption.point(deviceScreenWidth / 2, deviceScreenHeight / 2))
                    .waitAction()
                    .moveTo(PointOption.point(deviceScreenWidth / 2, deviceScreenHeight / 4))
                    .release()
                    .perform();
        }
    }


    /**
     * This method sends keys to the specified element.
     *
     * @param driver the AppiumDriver instance
     * @param elementPath the path to the element
     * @param text the text to send
     * @param waitTime the maximum time to wait for the element to be displayed
     * @throws IOException if an I/O error occurs
     */
    public void sendKeys (AppiumDriver driver, String elementPath, String text, int waitTime) throws IOException {
        takeScreenshot(driver);
        Map<Boolean, BerkResponse> coordinateResponse = ifElementDisplayedGetCoordinates(driver,elementPath,waitTime);
        if (coordinateResponse.containsKey(true)){
            BerkResponse response = coordinateResponse.get(true);
            int x= response.getCenterX();
            int y = response.getCenterY();
            TouchAction touchAction = new TouchAction((AppiumDriver) driver);
            touchAction.tap(PointOption.point(x, y)).perform();
            driver.getKeyboard().sendKeys(text);

        }else {
            throw new NoSuchElementException(elementPath + " is not available");
        }
    }

    /**
     * This method gets the text of the specified element.
     *
     * @param driver the WebDriver instance
     * @param elementName the path to the element
     * @param waitTime the maximum time to wait for the element to be displayed
     * @throws IOException if an I/O error occurs
     */
    public void getText (WebDriver driver, String elementName, int waitTime ) throws IOException {
        takeScreenshot(driver);
        Map<Boolean, BerkResponse> berkResponse = ifElementDisplayedGetCoordinates(driver,elementName,waitTime);
        if (berkResponse.containsKey(true)){
            BerkResponse response = berkResponse.get(true);
            String text = response.getText();

        }else {
            throw new NoSuchElementException(elementName + " is not available");
        }
    }

    /**
     * This method checks if the specified element is displayed and returns a BerkResponse wrapped in a Map.
     *
     * @param driver the WebDriver instance
     * @param elementPath the path to the element
     * @param waitTimeSecond the maximum time to wait for the element to be displayed
     * @return a Map containing a Boolean and a BerkResponse
     * @throws IOException if an I/O error occurs
     */
    public Map<Boolean,BerkResponse> ifElementDisplayedGetText(WebDriver driver, String elementPath, int waitTimeSecond) throws IOException {
        takeScreenshot(driver);
        Dimension screenSize = driver.manage().window().getSize();
        int deviceScreenWidth = screenSize.width;
        int deviceScreenHeight = screenSize.height;
        int count = 0;
        Map<Boolean,BerkResponse> result = new HashMap<>();
        BerkRequest berkRequest = new BerkRequest(driver);
        BerkResponse berkResponse = berkRequest.getCoordinates(elementPath,deviceScreenWidth,deviceScreenHeight);
        Instant currentTime = Instant.now();
        LocalTime waitTime = LocalTime.now().plusSeconds(waitTimeSecond);

        while (!berkResponse.getStatus().equals("success") && waitTime.isAfter(LocalTime.now())) {
            takeScreenshot(driver);
            Path path = Paths.get(elementPath);
            String fileName = path.getFileName().toString();
            berkResponse=berkRequest.getCoordinates(elementPath,deviceScreenWidth,deviceScreenHeight);
            //System.out.println(count+2+"."+" try "+ fileName + (berkResponse.getStatus().equals("error") ? " not found" : " found"));
            count++;
        }
        Instant endTime = Instant.now();
        Duration duration = Duration.between(currentTime, endTime);
        Path path = Paths.get(elementPath);
        String fileName = path.getFileName().toString();
        //System.out.println(fileName +" : "+  " found in " +duration.getSeconds() +" second" );

        if (berkResponse.getStatus().equals("success")){
            result.put(true,berkResponse);
        }
        else{
            result.put(false,berkResponse);
        }
        return result;

    }


}
