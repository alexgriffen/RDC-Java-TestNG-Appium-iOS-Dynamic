package com.saucelabs.example;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by grago on 28/02/2017.
 */
public class TestSetup {

//    private ResultReporter reporter;
//    private ThreadLocal<IOSDriver> driver = new ThreadLocal<IOSDriver>();

    public String username = System.getenv("SAUCE_USERNAME");

    public String accesskey = System.getenv("SAUCE_ACCESS_KEY");

    public String buildTag = System.getenv("BUILD_TAG");

    /**
     * ThreadLocal variable which contains the  {@link IOSDriver} instance which is used to perform browser interactions with.
     */
    private ThreadLocal<IOSDriver> driver = new ThreadLocal<IOSDriver>();

    /**
     * ThreadLocal variable which contains the Sauce Job Id.
     */
    private ThreadLocal<String> sessionId = new ThreadLocal<String>();

    /**
     * @return the {@link IOSDriver} for the current thread
     */
    public IOSDriver getIOSDriver() {
        return driver.get();
    }

    /**
     *
     * @return the Sauce Job id for the current thread
     */
    public String getSessionId() {
        return sessionId.get();
    }

  /**
   * DataProvider that explicitly sets the browser combinations to be used.
   *
   * @param testMethod
   * @return
   */
  @DataProvider(name = "devices", parallel = true)
  public static Object[][] sauceBrowserDataProvider(Method testMethod) {
      return new Object[][]{
              new Object[]{"iOS", "13", "iphone.*"},
              new Object[]{"iOS", "12", "iPhone.*"},
              new Object[]{"iOS", "13", "iphone.*"},
//              new Object[]{"iOS", "12", "iPhone.*"},
//              new Object[]{"iOS", "13", "iphone.*"},
//              new Object[]{"iOS", "14", "iPhone.*"},
//              new Object[]{"iOS", "13", "iphone.*"},
//              new Object[]{"iOS", "12", "iPhone.*"},
//              new Object[]{"iOS", "13", "iphone.*"},
//              new Object[]{"iOS", "12", "iPhone.*"},
//              new Object[]{"iOS", "13", "iphone.*"},
             // new Object[]{"iOS", "iPad Air 3"}

              /* Simulators here */
//              new Object[]{"iOS", "13.4", "iPhone XS Simulator"},
      };
  }

  protected IOSDriver createDriver(String platformName, String platformVersion, String deviceName, String methodName) throws MalformedURLException {

      DesiredCapabilities capabilities = new DesiredCapabilities();
//       capabilities.setCapability("testobject_api_key", "REDACTED");
      capabilities.setCapability("username", username);
      capabilities.setCapability("accessKey", accesskey);
      capabilities.setCapability("deviceName", deviceName);
      capabilities.setCapability("platformVersion", platformVersion);
      capabilities.setCapability("platformName", platformName);
      capabilities.setCapability("name",  methodName);
      capabilities.setCapability("build",  buildTag);
      capabilities.setCapability("tunnelIdentifier", "allTheRDC");
      capabilities.setCapability("appiumVersion", "1.17.1");
//      capabilities.setCapability("browserName", "Safari");
//      capabilities.setCapability("app", "storage:dc565179-cb52-4ca3-a25d-484b8136a542"); //UP RDC
//      capabilities.setCapability("app", "storage:bcd8c2ef-3e9b-46cc-ad29-36176c07c00b"); //emusim
      capabilities.setCapability("app", "https://github.com/alexgriffen/RDC-Java-TestNG-Appium-iOS-Dynamic/raw/master/CalculatorAlex.ipa"); // remote hosted for CI



      driver.set(new IOSDriver<WebElement>(
//               new URL("https://us1.appium.testobject.com/wd/hub"), //Legacy RDC TestObject
//              new URL("http://localhost:4455/wd/hub"),
              new URL("https://ondemand.us-west-1.saucelabs.com:443/wd/hub"), //New Unified Platform Awesomeness
              capabilities));
      String id = getIOSDriver().getSessionId().toString();
      sessionId.set(id);
      System.out.println(id + " is the ID for this session");
      return driver.get();

  }




    /* A simple addition, it expects the correct result to appear in the result field. */
    @Test(dataProvider = "devices")
    public void twoPlusThreeOperation(String platformName, String platformVersion, String deviceName, Method method) throws MalformedURLException {

    	IOSDriver driver = createDriver(platformName, platformVersion, deviceName, method.getName());

        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//        /* Get the elements. Bypass this for everything but resultField */
        MobileElement buttonTwo = (MobileElement)(driver.findElement(MobileBy.AccessibilityId("2")));
//        (new WebDriverWait(driver, 30)).until(ExpectedConditions.elementToBeClickable(buttonTwo));
//        MobileElement buttonTwo = (MobileElement)(driver.findElement(MobileBy.AccessibilityId("2")));
//        MobileElement buttonThree = (MobileElement)(driver.findElement(MobileBy.AccessibilityId("3")));
//        MobileElement buttonPlus = (MobileElement)(driver.findElement(MobileBy.AccessibilityId("+")));
//        MobileElement buttonEquals = (MobileElement)(driver.findElement(MobileBy.AccessibilityId("=")));
        MobileElement resultField = (MobileElement)(driver.findElement(By.xpath("//XCUIElementTypeStaticText|//UIAApplication[1]/UIAWindow[1]/UIAStaticText[1]")));
//
        /* Add two and two. */
//        buttonTwo.click();
        driver.findElement(MobileBy.AccessibilityId("2")).click();
//        buttonPlus.click();
        driver.findElement(MobileBy.AccessibilityId("+")).click();
//        buttonThree.click();
        driver.findElement(MobileBy.AccessibilityId("3")).click();
        driver.getScreenshotAs(OutputType.FILE);
//        buttonEquals.click();
        driver.findElement(MobileBy.AccessibilityId("=")).click();
        driver.getScreenshotAs(OutputType.FILE);
//
//        /* Check if within given time the correct result appears in the designated field. */
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.textToBePresentInElement(resultField, "5"));

    }

    /* A simple GET URL to test internal routing. */
//    @Test(dataProvider = "devices")
//    public void tunnelOrNo(String platformName, String platformVersion, String deviceName, Method method) throws MalformedURLException {
//
//      IOSDriver driver = createDriver(platformName, platformVersion, deviceName, method.getName());
//
//      driver.get("SECRETURLREDACTED");
//      driver.getScreenshotAs(OutputType.FILE);
//      new WebDriverWait(driver, 10);
//    }

    /* A simple addition, it expects the correct result to appear in the result field. */
    @Test(dataProvider = "devices")
    public void twoPlusThreeTest(String platformName, String platformVersion, String deviceName, Method method) throws MalformedURLException {

      IOSDriver driver = createDriver(platformName, platformVersion, deviceName, method.getName());

        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        /* Get the elements. */
        MobileElement buttonTwo = (MobileElement)(driver.findElement(MobileBy.AccessibilityId("2")));
        MobileElement buttonThree = (MobileElement)(driver.findElement(MobileBy.AccessibilityId("3")));
        MobileElement buttonPlus = (MobileElement)(driver.findElement(MobileBy.AccessibilityId("+")));
        MobileElement buttonEquals = (MobileElement)(driver.findElement(MobileBy.AccessibilityId("=")));
        MobileElement resultField = (MobileElement)(driver.findElement(By.xpath("//XCUIElementTypeStaticText|//UIAApplication[1]/UIAWindow[1]/UIAStaticText[1]")));

        /* Add two and two. */
        buttonTwo.click();
        buttonPlus.click();
        buttonThree.click();
        driver.getScreenshotAs(OutputType.FILE);
        buttonEquals.click();
        driver.getScreenshotAs(OutputType.FILE);

        /* Check if within given time the correct result appears in the designated field. */
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.textToBePresentInElement(resultField, "5"));
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        /* for the Legacy RDC Solution
    	IOSDriver driver = getWebDriver();
    	reporter = new ResultReporter();
        boolean success = result.isSuccess();
        String sessionId = driver.getSessionId().toString();
        */

        ((JavascriptExecutor) driver.get()).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed")); // use this to update Sauce with pass/fail

//        reporter.saveTestStatus(sessionId, success);
        driver.get().quit();
    }

    /**
     * @return the {@link WebDriver} for the current thread
     */
    public IOSDriver getWebDriver() {
        return driver.get();
    }
}
