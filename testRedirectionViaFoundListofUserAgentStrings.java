package com.selenium.redirect_tests;

//import com.selenium.environment.MyDriverManager;
import com.selenium.environment.MyDriverManager;
//import com.selenium.environment.Todo;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.selenium.environment.EnvironmentUnderTest.getUrl;
import static com.selenium.environment.MyDriverManager.*;
//import static com.selenium.environment.MyDriverManager.aDriver;

/**
 * Created by Iain Mounsey-Smith on 23/01/2016...
 */

public class testRedirectionViaFoundListofUserAgentStrings {
    //public static WebDriver aDriver;//create variable Adriver of type Webdriver...must be static as accessed in beforeclass..//create as public so can access from other classes
    public static WebDriver newDriver;
    private static String myurl = "url here";
    final private String PROTOCOL="http:";
    final private String DOMAIN = "www.compendiumdev.co.uk/";
    final private String ROOT_URL = PROTOCOL + "//" + DOMAIN;
    final private String A = "AssertThat ";
    WebElement element;
    public static WebDriverWait wait;
    //public static String testurl = "http://www.bbc.com";
    public static String testurl = "http://www.trademe.co.nz";
    public static String testurlStuff = "http://www.stuff.co.nz";
    public static String testurlBBC ="http://www.bbc.co.uk";

    @BeforeClass
    public static void setupDriver() {
        //System.setProperty(MY_DRIVER, driverOrBrowserName.GOOGLECHROME.name());
        //System.setProperty(MY_DRIVER, "firefox");//old

           /*this is now where you set browser name if NOT running from mvn commandline or IDE run configuration
           first parameter (driverOrBrowserName) is the local driver you want to run, if running local only then second and third parameter should be omitted
           driverOrBrowserName can be passed on its own e.g.  set(driverOrBrowserName.FIREFOX);
           second parameter is the remote host name (grid or saucelabs) that you want to run on grid2
           the third parameter is the browser you want to run on the remote host*/


        //set(driverOrBrowserName.REMOTEWEB,remoteHostName.GRID,driverOrBrowserName.FIREFOX);
        //set(driverOrBrowserName.FIREFOX);
        //enter url as below - the get method will return aDriver

        set(driverOrBrowserName.PHANTOM);
        //aDriver = get("http://useragentstring.com/pages/Mobile%20Browserlist/");
        //wait = new WebDriverWait(MyDriverManager.aDriver, 10, 50);
        get("http://useragentstring.com/pages/Mobile%20Browserlist/");
        //System.out.println("local aDriver " + aDriver.getCurrentUrl());
    }
    @Before
    public void miscSetupBeforeTest() {
        //wait = new WebDriverWait(driver, 10, 50);
    }

    @Test //http://useragentstring.com/pages/All/
    public void testReplacingUserAgentString() {
//getWebdriverProfileDirectoryWithLatestLastModificationDate
        //MyDriverManager.aDriver = get("http://useragentstring.com/pages/Mobile%20Browserlist/");
        //String testua = "Mozilla/5.0 (Linux; U; Android";
        String testua = "Mozilla/5.0";
        //String testua = "Mozilla/5.0 (compatible; MSIE 9.0; Windows Phone OS 7.5; Trident/5.0; IEMobile/9.0)";
        String userAgentStringRepository1 = "http://useragentstring.com/pages/Mobile%20Browserlist/";//primary authoritive list of ua strings for mobile devices
        String userAgentStringRepository2 = "http://useragentstring.com/pages/Browserlist/";//secondary authoritive list of ua strings for mobile devices
        List<String> listofURLsContainingUAstrings = new ArrayList<String>();
        listofURLsContainingUAstrings.add(userAgentStringRepository1);
        //listofURLsContainingUAstrings.add(userAgentStringRepository2); //unlikely to need a SECOND authoritive list of mobile ua strings but what the hey

        for (String individualRepository : listofURLsContainingUAstrings) {
            System.out.println("Individual Repository " + individualRepository);
            aDriver.get(individualRepository);//i don't need get method to return a driver as I already have one
            //get(individualRepository);
            List<WebElement> userAgentStringsFound = aDriver.findElements(By.cssSelector("ul>li>a"));
            List<String> UARedirectPASS = new ArrayList<String>();// these UAStrings redirect successfully
            List<String> UARedirectFAIL = new ArrayList<String>();// these UAStrings do NOT redirect successfully
            Integer totalCount = 0;
            Integer targetCount = 0;
            for (WebElement userAgentString : userAgentStringsFound) {
                totalCount++;
                //System.out.println("userAgentString " + userAgentString.getText());
                if (userAgentString.getText().contains(testua)) {
                    //System.out.println("Debugging using testua " + userAgentString.getText());
                    changeFireFoxUserAgent(userAgentString.getText());
                    newDriver.navigate().to(testurl);
                                        if (newDriver.getCurrentUrl().startsWith("http://www")) {
                        System.out.println("Yes indeed itstarts with www");
                        UARedirectFAIL.add(userAgentString.getText());
                    } else {
                        UARedirectPASS.add(userAgentString.getText());
                    }
                }
            }
            System.out.println("The total number of mobile user agent strings found at " + individualRepository + " is  " + totalCount + "\n-------------------------------");

            if (UARedirectPASS.size() == 0) {
                System.out.println("No UA Strings successfully redirected");
            } else{
                System.out.println("The number of UA strings to pass was " + UARedirectPASS.size());
                System.out.println("These string passed;");
                for (String individualUAString:UARedirectPASS){
                    System.out.println(individualUAString);
                }
                System.out.println("\n=====");
        }
            if (UARedirectFAIL.size() == 0) {
                System.out.println("No UA Strings failed redirection");
            } else{
                System.out.println("The number of UA strings to fail was " + UARedirectFAIL.size());
                System.out.println("Theses string failed;");
                for (String individualUAString:UARedirectFAIL){
                    System.out.println(individualUAString);
                }
                System.out.println("\n=====");
            }
        }
    }

    public  WebDriver changeFireFoxUserAgent(String useThisUserAgent) {
    //https://code.google.com/p/selenium/wiki/FirefoxDriver
    String tempDirectoryLocation ="C:\\Users\\Owner\\AppData\\Local\\Temp\\";
    String fireFoxWebdriverProfileDirecory = tempDirectoryLocation+getWebdriverProfileDirectoryWithLatestLastModificationDate();
    //System.out.println("fireFoxWebdriverProfileDirecory " + fireFoxWebdriverProfileDirecory);
        File profileDir = new File(fireFoxWebdriverProfileDirecory);
        FirefoxProfile profile = new FirefoxProfile(profileDir);
    //profile.setPreference("general.useragent.override", "Mozilla/5.0 (Linux; U; Android 4.0.2; ko-kr; LG-L160L Build/IML74K) AppleWebkit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
    profile.setPreference("general.useragent.override", useThisUserAgent);
    //System.out.println("useragent " + profile.getStringPreference("general.useragent.override", ""));
    //aDriver.quit();//if we don't do this we get 2 instances of firefox running
    newDriver = new FirefoxDriver(profile);//instantiate new driver with new user agent set...any other firefox customisations are lost as original driver quit above
    return newDriver;//TODO need to add support for other browser types...just for fun really...phantomjs first(make this default??)
    //aDriver.get("about:support");
        //aDriver.get(testurl);
}

/*default user agent
win7 ff43
 	Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0*/

    public static String getWebdriverProfileDirectoryWithLatestLastModificationDate() {
        String webdriverProfileLocation = "C:\\Users\\Owner\\AppData\\Local\\Temp";
        File f = new File(webdriverProfileLocation); // current directory

        FileFilter directoryFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory(); //only interested in directories, NOT files
            }
        };
        long maximumLongLastModifiedFound=0;
        String directoryName="";
        File[] files = f.listFiles(directoryFilter);
        for (File file : files) {
            Long longLastModified=file.lastModified();
            if ( file.getName().contains("webdriver-profile")){ //only interested in Webdriver created FireFox profile directories
                for (int counter =0;counter<files.length;counter++){//i<files.length
                    if ( maximumLongLastModifiedFound < longLastModified ) {
                        maximumLongLastModifiedFound = longLastModified;
                        directoryName=file.getName();
                    }
                }
            }
        }
        return directoryName;
    }

    @Test //http://useragentstring.com/pages/All/
    //this version reports on the re-directed to currenturl, just for fun...as it uses a 2d array
    public void testReplacingUserAgentString2() {
//getWebdriverProfileDirectoryWithLatestLastModificationDate
        //MyDriverManager.aDriver = get("http://useragentstring.com/pages/Mobile%20Browserlist/");
        String uaFilter = "Mozilla/5.0 (Linux; U; Android 4.0.3; ";
        //String uaFilter = "Mozilla/5.0 (Linux";
        //String uaFilter = "Mozilla/5.0 (compatible; MSIE 9.0; Windows Phone OS 7.5; Trident/5.0; IEMobile/9.0)";
        String userAgentStringRepository1 = "http://useragentstring.com/pages/Mobile%20Browserlist/";//primary authoritive list of ua strings for mobile devices
        String userAgentStringRepository2 = "http://useragentstring.com/pages/Browserlist/";//secondary authoritive list of ua strings for mobile devices
        int columns = 2;//the number of columns in our array
        String[][] redirectionFailures2dArray = new String[0][];
        String[][] redirectionPasses2dArray = new String[0][];

        String useragentgettext = "";
        List<String> listofURLsContainingUAstrings = new ArrayList<String>();
        //List<WebElement> userAgentStringsFound;

        listofURLsContainingUAstrings.add(userAgentStringRepository1);
        //listofURLsContainingUAstrings.add(userAgentStringRepository2); //unlikely to need a SECOND authoritive list of mobile ua strings but what the hey
        int passesArray2dLength = 0;
        int failsArray2dLength = 0;

        int numberOfUserAgentStringsUnderTest = 0;
        Integer totalCountFAILURES = 0;
        Integer totalCountPASSES = 0;
        List<WebElement> userAgentStringsFound = null;
        for (String individualRepository : listofURLsContainingUAstrings) {
            System.out.println("individualRepository " + individualRepository);
            aDriver.get(individualRepository);//i don't need get method to return a driver as I already have one
            //get(individualRepository);
            userAgentStringsFound = aDriver.findElements(By.cssSelector("ul>li>a"));
            List<String> filteredUAStrings = new ArrayList<String>();//these are the filtered ua strings
            List<String> UARedirectPASS = new ArrayList<String>();// these UAStrings redirect successfully
            List<String> UARedirectFAIL = new ArrayList<String>();// these UAStrings do NOT redirect successfully
            System.out.println("The total number of UA strings scraped off of the repositorie(s) at " + individualRepository + " is  " + userAgentStringsFound.size());
            for (WebElement userAgentStringUnderTest : userAgentStringsFound) {
                // System.out.println("userAgentStringUnderTest.getText()"+ userAgentStringUnderTest.getText());
                //System.out.println("uaFilter " + uaFilter);
                if (userAgentStringUnderTest.getText().contains(uaFilter)) {
                    filteredUAStrings.add(userAgentStringUnderTest.getText());
                    numberOfUserAgentStringsUnderTest++;//the number of filtered ua strings
                }
            }
            //System.out.println("The total number of mobile user agent strings found at " + individualRepository + " is  " + totalCount + "\n====");
            System.out.println("The number of user agent strings as determined by filtering is " + numberOfUserAgentStringsUnderTest);
            for (String userAgentString : filteredUAStrings) {
                //int totalCountMinusOne=totalCount-1;
                useragentgettext = userAgentString;
                changeFireFoxUserAgent(userAgentString);
                newDriver.navigate().to(testurlBBC);
                //System.out.println("useragentgettext " + useragentgettext);
                String redirectedToURL = newDriver.getCurrentUrl();
                newDriver.close();//close browser as we no longer need it
                //failures
                if (redirectedToURL.startsWith("http://www")) {//TODO tighten up on this...might not start as www as we assume....better to compare url pre and post and if different assume redirect sucessfull
                    //System.out.println("Yes indeed itstarts with www");
                    if (totalCountFAILURES == 0) {
                        redirectionFailures2dArray = new String[numberOfUserAgentStringsUnderTest][columns];
                    }//only create the array once!
                    //UARedirectFAIL.add(userAgentString);
                    //System.out.println("outerownum " + totalCountFAILURES);
                    redirectionFailures2dArray[totalCountFAILURES][0] = userAgentString;
                    redirectionFailures2dArray[totalCountFAILURES][1] = (redirectedToURL.toString());
                    totalCountFAILURES++;
                    failsArray2dLength = redirectionFailures2dArray.length;
                    //UARedirectFAIL.add(userAgentString.getText());

                } else {
                    //Passes

                    if (totalCountPASSES == 0) {
                        redirectionPasses2dArray = new String[numberOfUserAgentStringsUnderTest][columns];
                    }//only create the array once!
                    //UARedirectFAIL.add(userAgentString);
                    //System.out.println("outerownum " + totalCountPASSES);
                    redirectionPasses2dArray[totalCountPASSES][0] = userAgentString;
                    redirectionPasses2dArray[totalCountPASSES][1] = (redirectedToURL.toString());
                    totalCountPASSES++;
                    passesArray2dLength = redirectionPasses2dArray.length;
                }
            }
        }
        //System.out.println("The number of UA strings that did not result in a redirection is " + (redirectionFailures2dArray.length));
        //PASSED - output the UA strings that redirected successfully
        if (redirectionPasses2dArray.length == 0) {
            System.out.println("\n" +
                    "----------------------\n   No UA Strings resulted in a successful redirection\n----------------------");
        } else {
            System.out.println("\n" +
                    "----------------------\n   These UA strings successfully redirected \n----------------------");
            for (int rownumPasses = 0; rownumPasses < passesArray2dLength; rownumPasses++) {
                System.out.println(redirectionPasses2dArray[rownumPasses][0] + "\n\t to getcurrentURL " + redirectionPasses2dArray[rownumPasses][1]);
                //System.out.println("These UA strings failed ="+ Arrays.deepToString(redirectionFailures2dArray));//alternate print method
            }
        }


        // FAILED -output the UA strings that failed to redirect
        if (redirectionFailures2dArray.length == 0) {
            System.out.println("\n" +
                    "----------------------\n   No UA Strings failed to result in a redirection\n----------------------");
        } else {
            System.out.println("\n" +
                    "----------------------\n  These UA strings failed to result in a redirection \n----------------------");
            for (int rownumFails = 0; rownumFails < failsArray2dLength; rownumFails++) {
                System.out.println(redirectionFailures2dArray[rownumFails][0] + "\n\t to getcurrentURL " + redirectionFailures2dArray[rownumFails][1]);
                //System.out.println("These UA strings failed ="+ Arrays.deepToString(redirectionFailures2dArray));//alternate print method
            }
        }

        System.out.println("\n" +
                "----------------------\nThe total number of UA strings was " + userAgentStringsFound.size() + "\n of which " + numberOfUserAgentStringsUnderTest + " were left after filters were applied");
        System.out.println("The total number of UA strings that sucessfully resulted in a redirection were \t\t\t" + totalCountPASSES);
        System.out.println("The total number of UA strings that did NOT sucessfully result in a redirection were \t" + totalCountFAILURES);
    }

        @AfterClass
        public static void quitDriver () {
           // aDriver.quit();
        }

}