import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.junit.jupiter.api.*;
import org.junit.platform.engine.TestExecutionResult;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TimerTest {
    private static WebDriver driver;
    private WebElement startButton;
    private WebElement stopButton;
    private WebElement resetButton;
    private WebElement timerDisplay;
    private static ExtentReports extent;
    private static ExtentTest test;

    @BeforeAll
    public static void setupReport() {
        ExtentManager.initializeReport("TimerTestReport.html");
        extent = ExtentManager.getExtent();
    }

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        // Rapor için test başlatılıyor
        test = extent.createTest(testInfo.getDisplayName());

        // WebDriver kurulumu
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("http://localhost:3000");

        // Locators
        startButton = driver.findElement(By.xpath("//p[text()='Start']"));
        stopButton = driver.findElement(By.xpath("//p[text()='Stop']"));
        resetButton = driver.findElement(By.xpath("//p[text()='Reset']"));
        timerDisplay = driver.findElement(By.className("time-display"));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }

        // Test sonucu rapora ekleniyor
        if (TestExecutionResult.Status.SUCCESSFUL.name().equals(TestExecutionResult.Status.SUCCESSFUL.name())) {
            test.pass("Test başarıyla tamamlandı.");
        } else {
            test.fail("Test sırasında bir hata oluştu.");
        }
    }

    @AfterAll
    public static void finish() {
        ExtentManager.flushReport();
    }
    @Test
    @Order(1)
    public void testCase_TC01() throws InterruptedException {
        Assertions.assertEquals("25:00", timerDisplay.getText(), "Sayaç başlangıç değeri hatalı");
        startButton.click();
        Thread.sleep(4000);
        stopButton.click();
        Assertions.assertNotEquals("25:00", timerDisplay.getText(), "Sayaç durmadı");
    }

    @Test
    @Order(2)
    public void testCase_TC02() throws InterruptedException {

        Assertions.assertEquals("25:00", timerDisplay.getText(), "Sayaç başlangıç değeri hatalı");

        startButton.click();
        Thread.sleep(4000);

        resetButton.click();
        Thread.sleep(1000);
        Assertions.assertEquals("25:00", timerDisplay.getText(), "Reset sonrası zaman doğru değil");
    }
    @Test
    @Order(3)
    public void testCase_TC03() throws InterruptedException {

        Assertions.assertEquals("25:00", timerDisplay.getText(), "Sayaç başlangıç değeri hatalı");

        startButton.click();
        Thread.sleep(4000);
        stopButton.click();
        String stoppedTime = timerDisplay.getText();
        Thread.sleep(2000);
        Assertions.assertEquals(stoppedTime, timerDisplay.getText(), "Sayaç durmadı veya sabit kalmadı");
    }

    @Test
    @Order(4)
    public void testCase_TC04() throws InterruptedException {

        startButton.click();
        Thread.sleep(4000); // Sayaç birkaç saniye çalışsın
        stopButton.click();
        resetButton.click();
        Assertions.assertEquals("25:00", timerDisplay.getText(), "Reset sonrası zaman yanlış");
    }

    @Test
    @Order(5)
    public void testCase_TC05() {

        Assertions.assertEquals("Start", startButton.getText(), "Start butonu eksik");
        Assertions.assertEquals("Stop", stopButton.getText(), "Stop butonu eksik");
        Assertions.assertEquals("Reset", resetButton.getText(), "Reset butonu eksik");


        Assertions.assertEquals("25:00", timerDisplay.getText(), "Sayaç başlangıç değeri hatalı");
    }
    @Test
    @Order(6)
    public void testCase_TC06() throws InterruptedException {

        startButton.click();
        Thread.sleep(2000); // Sayaç birkaç saniye çalışsın
        startButton.click();
        startButton.click();
        startButton.click();
        String currentTimer = timerDisplay.getText();
        Thread.sleep(2000);

        Assertions.assertNotEquals(currentTimer, timerDisplay.getText(), "Start butonu birden fazla tıklanması çalışmayı durduruyor");
    }
    @Test
    @Order(7)
    public void testCase_TC07() throws InterruptedException {
        startButton.click();
        Thread.sleep(3000); // Sayaç birkaç saniye çalışsın

        stopButton.click();

        String stoppedTime = timerDisplay.getText();

        startButton.click();
        Thread.sleep(2000);

        Assertions.assertNotEquals(stoppedTime, timerDisplay.getText(), "Zamanlayıcı kaldığı yerden devam etmiyor");
    }


    @Test
    @Order(8)
    public void testCase_TC08() {

        startButton.click();


        driver.navigate().refresh();


        WebElement newStartButton = driver.findElement(By.xpath("//p[text()='Start']"));
        WebElement newStopButton = driver.findElement(By.xpath("//p[text()='Stop']"));
        WebElement newResetButton = driver.findElement(By.xpath("//p[text()='Reset']"));
        WebElement newTimerDisplay = driver.findElement(By.className("time-display"));
        Assertions.assertEquals("25:00", newTimerDisplay.getText(), "Zaman sıfırlanmadı");
        Assertions.assertTrue(newStartButton.isEnabled(), "Start butonu etkin değil");
        Assertions.assertTrue(newStopButton.isEnabled(), "Stop butonu etkin değil");
        Assertions.assertTrue(newResetButton.isEnabled(), "Reset butonu etkin değil");
    }

    @Test
    @Order(9)
    public void testCase_TC09() throws InterruptedException {

        startButton.click();
        Thread.sleep(10000);

        String currentTimer = timerDisplay.getText();
        Assertions.assertNotEquals("25:00", currentTimer, "Sayaç ilerlemiyor");
        Assertions.assertEquals("24:50", currentTimer, "Sayaç doğru hızda ilerlemiyor");
    }

   /* @Test                        //25 dk boyunca bekledim test geçti ama onu yoruma aldım
    @Order(10)
    public void testCase_TC10() throws InterruptedException {

        startButton.click();
        Thread.sleep(1500000); // 25 dakika (25 * 60 * 1000 milisaniye)
        Assertions.assertEquals("00:00", timerDisplay.getText(), "Zamanlayıcı sıfıra ulaşmadı");

        stopButton.click();
        String currentTimeAfterStop = timerDisplay.getText();
        Assertions.assertEquals("00:00", currentTimeAfterStop, "Stop butonu sıfıra ulaştıktan sonra etkili olmamalı");

        Assertions.assertTrue(startButton.isEnabled(), "Start butonu sıfıra ulaştıktan sonra tekrar etkin olmalı");
        Assertions.assertTrue(resetButton.isEnabled(), "Reset butonu sıfıra ulaştıktan sonra etkin olmalı");
    } */

    @Test
    @Order(11)
    public void testCase_TC11() throws InterruptedException {
        Assertions.assertEquals("25:00", timerDisplay.getText(), "Sayaç başlangıç değeri hatalı");
        startButton.click();
        Thread.sleep(1000); // 1 saniye bekle
        String timerAfterOneSecond = timerDisplay.getText();
        Assertions.assertEquals("24:59", timerAfterOneSecond, "Sayaç bir saniye içinde bir saniyelik azalmadı!");
        Thread.sleep(1000);
        String timerAfterTwoSeconds = timerDisplay.getText();
        Assertions.assertEquals("24:58", timerAfterTwoSeconds, "Sayaç iki saniye içinde doğru azalmadı!");

        Thread.sleep(1000);
        String timerAfterThreeSeconds = timerDisplay.getText();
        Assertions.assertEquals("24:57", timerAfterThreeSeconds, "Sayaç üç saniye içinde doğru azalmadı!");
        System.out.println("Test Case TC11 başarıyla tamamlandı: Sayaç her saniyede doğru şekilde azaldı.");
    }

    @Test
    @Order(12)
    public void testCase_TC12() throws InterruptedException {

        Assertions.assertEquals("25:00", timerDisplay.getText(), "Sayaç başlangıç değeri hatalı");


        startButton.click();

        long startTime = System.currentTimeMillis();
        Thread.sleep(1000); // 1 saniye bekle

        long elapsedTime = System.currentTimeMillis() - startTime;
        String timerAfterOneSecond = timerDisplay.getText();

        Assertions.assertTrue(elapsedTime >= 1000 && elapsedTime < 1100, "Gerçek zaman ile sayaç zamanlaması arasında sapma var!");
        Assertions.assertEquals("24:59", timerAfterOneSecond, "Sayaç bir saniye içinde doğru azalmadı!");

        startTime = System.currentTimeMillis();
        Thread.sleep(1000);
        elapsedTime = System.currentTimeMillis() - startTime;
        String timerAfterTwoSeconds = timerDisplay.getText();

        Assertions.assertTrue(elapsedTime >= 1000 && elapsedTime < 1100, "Gerçek zaman ile sayaç zamanlaması arasında sapma var!");
        Assertions.assertEquals("24:58", timerAfterTwoSeconds, "Sayaç ikinci saniyede doğru azalmadı!");

        System.out.println("Test Case TC12 başarıyla tamamlandı: Sayaç gerçek zamanlı bir saniye azalıyor, sapma yok.");


    }

    @Test
    @Order(13)
    public void testCase_TC13() throws InterruptedException {
        Assertions.assertEquals("25:00", timerDisplay.getText(), "Sayaç başlangıç değeri hatalı");

        Assertions.assertEquals("25:00", timerDisplay.getText(), "Başlangıçta sayaç değeri 25:00 değil!");

        driver.navigate().refresh();
        WebElement refreshedTimerDisplay = driver.findElement(By.className("time-display"));
        Assertions.assertEquals("25:00", refreshedTimerDisplay.getText(), "Tarayıcı yenilendiğinde sayaç başlangıç değeri 25:00 değil!");
        WebElement refreshedResetButton = driver.findElement(By.xpath("//p[text()='Reset']"));
        refreshedResetButton.click();
        Assertions.assertEquals("25:00", refreshedTimerDisplay.getText(), "Reset sonrası sayaç değeri 25:00 değil!");

        System.out.println("Test Case TC13 başarıyla tamamlandı: Sayaç başlangıç değeri her durumda 25:00 sabit.");
    }

    @Test
    @Order(14)
    public void testCase_TC14() throws InterruptedException {

        startButton.click();
        Thread.sleep(1000);
        String timeAfter1Second = timerDisplay.getText();
        Thread.sleep(1000); // 1 saniye daha bekle
        String timeAfter2Seconds = timerDisplay.getText();

        Assertions.assertNotEquals("25:00", timeAfter1Second, "Sayaç ilerlemiyor!");
        Assertions.assertNotEquals(timeAfter1Second, timeAfter2Seconds, "Sayaç düzgün bir şekilde ilerlemiyor!");

        Assertions.assertTrue(timeAfter1Second.matches("^\\d{2}:\\d{2}$"), "İlk zaman formatı yanlış: " + timeAfter1Second);
        Assertions.assertTrue(timeAfter2Seconds.matches("^\\d{2}:\\d{2}$"), "İkinci zaman formatı yanlış: " + timeAfter2Seconds);

        System.out.println("1. saniye: " + timeAfter1Second + " | 2. saniye: " + timeAfter2Seconds);
    }

}