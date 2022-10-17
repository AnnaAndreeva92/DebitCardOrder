package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CallbackTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {

        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSubmitAnApplication() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] .input__control")).sendKeys("Андреева Анна");
        driver.findElement(By.cssSelector("[data-test-id='phone'] .input__control")).sendKeys("+79401234567");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.tagName("button")).click();
        String expectedMessage = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actualMessage = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldBeFilledInWithNonValidData() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] .input__control")).sendKeys("Andreeva Anna");
        driver.findElement(By.cssSelector("[data-test-id='phone'] .input__control")).sendKeys("+79401234567");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.tagName("button")).click();
        String expectedMessage = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actualMessage = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText().trim();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldEnterTheWrongPhoneNumber() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] .input__control")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id='phone'] .input__control")).sendKeys("8940123456");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.tagName("button")).click();
        String expectedMessage = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualMessage = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText().trim();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldLeaveTheNameFieldsBlank() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] .input__control")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='phone'] .input__control")).sendKeys("+79012345678");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.tagName("button")).click();
        String expectedMessage = "Поле обязательно для заполнения";
        String actualMessage = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText().trim();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldLeaveThePhoneFieldsBlank() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] .input__control")).sendKeys("Андреева Анна");
        driver.findElement(By.cssSelector("[data-test-id='phone'] .input__control")).sendKeys("");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.tagName("button")).click();
        String expectedMessage = "Поле обязательно для заполнения";
        String actualMessage = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText().trim();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldLeaveTheCheckboxBlank() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] .input__control")).sendKeys("Андреева Анна");
        driver.findElement(By.cssSelector("[data-test-id='phone'] .input__control")).sendKeys("+79401234567");
        driver.findElement(By.tagName("button")).click();
        String expectedMessage = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        String actualMessage = driver.findElement(By.cssSelector("[data-test-id='agreement']")).getText().trim();
        assertEquals(expectedMessage, actualMessage);

    }
}

