import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TimerTest {

    private static WebDriver driver;

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("http://localhost:3000");
    }


         /*     1)Start butonuna basıldığında zamanlayıcı çalışmaya başlamalı.
                2)Başlangıç zamanını kontrol et (25:00).
                3)Start butonuna tıklanıldığında zamanlayıcının ilerlediğini test et. */

    @Test
    public void testStartButton() throws InterruptedException {
        WebElement timerDisplay = driver.findElement(By.className("time-display"));
        WebElement startButton = driver.findElement(By.xpath("//p[text()='Start']"));

        assertEquals("25:00", timerDisplay.getText(), "Başlangıç zamanı yanlış");
        startButton.click();

        Thread.sleep(4000);
        assertNotEquals("25:00", timerDisplay.getText(), "Sayaç çalışmıyor!");
    }

             /*   1)Reset butonuna basıldığında zamanlayıcı başlangıç değerine "25:00" dönmeli.
                  2)Start butonuna tıkla ve zamanlayıcının ilerlemesine izin ver.
                  3)Reset butonuna tıkla ve zamanlayıcının sıfırlandığını kontrol et.*/

    @Test
    public void testResetButton() throws InterruptedException {
        WebElement timerDisplay = driver.findElement(By.className("time-display"));
        WebElement startButton = driver.findElement(By.xpath("//p[text()='Start']"));
        WebElement resetButton = driver.findElement(By.xpath("//p[text()='Reset']"));

        startButton.click();

        Thread.sleep(4000);

        resetButton.click();

        Thread.sleep(1000);

        assertEquals("25:00", timerDisplay.getText(), "Reset sonrası zaman yanlış");
    }


     /* 1)Stop butonuna basıldığında zamanlayıcı durmalı.
        2)Start butonuna tıklanarak zamanlayıcı başlatılır.
        3)Stop butonuna tıklandıktan sonra zamanlayıcının durduğunu kontrol et.
     */
    @Test
    public void testStopButton() throws InterruptedException {
        WebElement startButton = driver.findElement(By.xpath("//p[text()='Start']"));
        WebElement stopButton = driver.findElement(By.xpath("//p[text()='Stop']"));
        WebElement timerDisplay = driver.findElement(By.className("time-display"));

        startButton.click();
        Thread.sleep(4000);
        stopButton.click();

        String stoppedTime = timerDisplay.getText();
        Thread.sleep(2000);
        assertEquals(stoppedTime, timerDisplay.getText(), "Sayaç durmadı");
    }

    /*
      1)Tüm UI elemanları doğru şekilde yükleniyor mu?
      2)Start, Stop ve Reset butonlarının metinlerini kontrol et.
      3)Timer ekranının başlangıç değerini (25:00) kontrol et.
     */
    @Test
    public void testUIElementsExist() {
        WebElement startButton = driver.findElement(By.xpath("//p[text()='Start']"));
        WebElement stopButton = driver.findElement(By.xpath("//p[text()='Stop']"));
        WebElement resetButton = driver.findElement(By.xpath("//p[text()='Reset']"));
        WebElement timerDisplay = driver.findElement(By.className("time-display"));

        assertEquals("Start", startButton.getText(), "Start butonu bulunamadı");
        assertEquals("Stop", stopButton.getText(), "Stop butonu bulunamadı");
        assertEquals("Reset", resetButton.getText(), "Reset butonu bulunamadı");
        assertEquals("25:00", timerDisplay.getText(), "Başlangıç zamanı yanlış");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}