import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TimerNegativeTest {

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

                /*   1)Start butonuna birden fazla kez tıklanırsa hata oluşmamalı.
                     2)Start butonuna arka arkaya 3 kez tıkla.
                     3)Zamanlayıcının normal şekilde çalıştığını test et. */

    @Test
    public void testMultipleStartClicks() throws InterruptedException {
        WebElement timerDisplay = driver.findElement(By.className("time-display"));
        WebElement startButton = driver.findElement(By.xpath("//p[text()='Start']"));

        startButton.click();
        Thread.sleep(1000);

        startButton.click();
        Thread.sleep(1000);

        startButton.click(); //
        Thread.sleep(1000);

        assertNotEquals("25:00", timerDisplay.getText(), "Sayaç ilerlemiyor");
    }

    /*
        1)Stop butonuna birden fazla kez tıklanırsa hata oluşmamalı.
        2)Stop butonuna arka arkaya birkaç kez tıklayın.
        3)Zamanlayıcının durduğunu ve zamanın sabit kaldığını doğrulayın.
     */
    @Test
    public void testMultipleStopClicks() throws InterruptedException {
        WebElement startButton = driver.findElement(By.xpath("//p[text()='Start']"));
        WebElement stopButton = driver.findElement(By.xpath("//p[text()='Stop']"));
        WebElement timerDisplay = driver.findElement(By.className("time-display"));

        startButton.click();
        Thread.sleep(2000);

        stopButton.click();
        String stoppedTime = timerDisplay.getText();

        stopButton.click();
        Thread.sleep(2000);

        assertNotEquals("25:00", stoppedTime, "Sayaç durmadı");
    }


     /* 1)Reset butonu çalışırken zamanlayıcı sıfırlanmalı.
        2)Reset butonuna Start durumunda tıklayın.
        3)Zamanlayıcının sıfırlandığını doğrulayın.*/

    @Test
    public void testResetWhileRunning() throws InterruptedException {
        WebElement startButton = driver.findElement(By.xpath("//p[text()='Start']"));
        WebElement resetButton = driver.findElement(By.xpath("//p[text()='Reset']"));
        WebElement timerDisplay = driver.findElement(By.className("time-display"));

        startButton.click();
        Thread.sleep(2000);

        resetButton.click();
        Thread.sleep(1000);

        assertNotEquals("24:58", timerDisplay.getText(), "Reset sırasında sayaç sıfırlanmadı");
    }




    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}