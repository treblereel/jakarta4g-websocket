package org.treblereel.gwt.websocket.tests.j2cl.server;

import java.time.Duration;

import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class WebSocketIntegrationTest {

    @ConfigProperty(name = "quarkus.http.test-port")
    int port;

    private static WebDriver driver;

    @BeforeAll
    static void setupDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
    }

    @AfterAll
    static void teardownDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testEchoMessage() {
        driver.get("http://localhost:" + port + "/index.html");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement statusDiv = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("statusDiv")));
        wait.until(d -> statusDiv.getText().contains("CONNECTED"));
        assertTrue(statusDiv.getText().contains("CONNECTED"));

        WebElement messagesDiv = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("messagesDiv")));
        wait.until(d -> !messagesDiv.getText().isEmpty());
        assertTrue(messagesDiv.getText().contains("hello"));
    }
}
