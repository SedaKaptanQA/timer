import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TimerNegativeTest {
    private static WebDriver driver;
    private static ExtentReports extent;
    private static ExtentTest test;

    private WebElement startButton;
    private WebElement stopButton;
    private WebElement resetButton;
    private WebElement timerDisplay;

    @BeforeAll
    public static void setupReport() {
        ExtentManager.initializeReport("TimerNegativeTestReport.html");
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
        if (test.getStatus().toString().equals("PASS")) {
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
    public void testCase_TC_N01() throws InterruptedException {
        test.info("Start butonuna birden fazla kez tıklanıyor.");
        startButton.click();
        Thread.sleep(1000);
        startButton.click();
        Thread.sleep(1000);
        startButton.click();
        Assertions.assertNotEquals("25:00", timerDisplay.getText(), "Sayaç ilerlemiyor!");
    }

    @Test
    @Order(2)
    public void testCase_TC_N02() throws InterruptedException {
        test.info("Stop butonuna birden fazla kez tıklanıyor.");
        startButton.click();
        Thread.sleep(2000);
        stopButton.click();
        String stoppedTime = timerDisplay.getText();
        stopButton.click();
        Assertions.assertEquals(stoppedTime, timerDisplay.getText(), "Sayaç durmadı!");
    }

    @Test
    @Order(3)
    public void testCase_TC_N03() throws InterruptedException {
        test.info("Reset butonuna Start durumu açıkken tıklanıyor.");
        startButton.click();
        Thread.sleep(2000);
        resetButton.click();
        Assertions.assertEquals("25:00", timerDisplay.getText(), "Reset sırasında sayaç sıfırlanmadı!");
    }

    @Test
    @Order(4)
    public void testCase_TC_N04() {
        test.info("Reset butonuna birden fazla kez basılıyor.");
        resetButton.click();
        resetButton.click();
        Assertions.assertEquals("25:00", timerDisplay.getText(), "Reset sonrası sayaç sıfırlanmadı!");
    }

    @Test
    @Order(5)
    public void testCase_TC_N05() {
        test.info("Start butonuna basmadan Stop ve Reset butonlarına tıklanıyor.");
        String initialTime = timerDisplay.getText();
        stopButton.click();
        resetButton.click();
        Assertions.assertEquals(initialTime, timerDisplay.getText(), "Stop veya Reset yanlış çalıştı!");
    }

    @Test
    @Order(6)
    public void testCase_TC_N06() throws InterruptedException {
        test.info("Sayaç her saniyede bir saniye azalıyor mu kontrol ediliyor.");
        startButton.click();
        Thread.sleep(1000);
        String timeAfterOneSecond = timerDisplay.getText();
        Thread.sleep(1000);
        String timeAfterTwoSeconds = timerDisplay.getText();
        Assertions.assertNotEquals(timeAfterOneSecond, timeAfterTwoSeconds, "Sayaç düzgün bir şekilde azalmıyor!");
    }

    @Test
    @Order(7)
    public void testCase_TC_N07() throws InterruptedException {
        test.info("Stop butonuna basıldıktan sonra sayaç kaldığı yerden devam ediyor mu kontrol ediliyor.");
        startButton.click();
        Thread.sleep(2000);
        stopButton.click();
        String stoppedTime = timerDisplay.getText();
        startButton.click();
        Thread.sleep(2000);
        Assertions.assertNotEquals(stoppedTime, timerDisplay.getText(), "Sayaç devam etmiyor!");
    }

    @Test
    @Order(8)
    public void testCase_TC_N08() {
        test.info("Tarayıcı yenilendiğinde sayaç değerlerinin sıfırlanması kontrol ediliyor.");
        startButton.click();


        driver.navigate().refresh();

        // tarayıcı yenilendiğinde webelementleri yenilemek gerekiyor ana sayfadan bir kere kullandığı için
        startButton = driver.findElement(By.xpath("//p[text()='Start']"));
        stopButton = driver.findElement(By.xpath("//p[text()='Stop']"));
        resetButton = driver.findElement(By.xpath("//p[text()='Reset']"));
        timerDisplay = driver.findElement(By.className("time-display"));


        Assertions.assertEquals("25:00", timerDisplay.getText(), "Zaman sıfırlanmadı!");
        Assertions.assertTrue(startButton.isEnabled(), "Start butonu etkin değil!");
        Assertions.assertTrue(stopButton.isEnabled(), "Stop butonu etkin değil!");
        Assertions.assertTrue(resetButton.isEnabled(), "Reset butonu etkin değil!");
    }

   /* @Test
    @Order(9)             // 26 dakika bekleyecek o yüzden yoruma aldım testi
    public void testCase_TC_N09() throws InterruptedException {
        test.info("Sayaç negatif zaman göstermemeli.");
        startButton.click();
        Thread.sleep(26 * 60 * 1000); // 26 dakika bekleyecek
        Assertions.assertNotEquals("-1:00", timerDisplay.getText(), "Sayaç negatif zaman gösteriyor!");
    } */

    @Test
    @Order(10)
    public void testCase_TC_N10() throws InterruptedException {
        test.info("Sayaç her saniyede bir saniye azalıyor mu doğrulama testi.");
        startButton.click();
        Thread.sleep(1000);
        String timerAfterOneSecond = timerDisplay.getText();
        Thread.sleep(1000);
        String timerAfterTwoSeconds = timerDisplay.getText();
        Assertions.assertNotEquals(timerAfterOneSecond, timerAfterTwoSeconds, "Sayaç düzgün bir şekilde azalmıyor!");
    }
}