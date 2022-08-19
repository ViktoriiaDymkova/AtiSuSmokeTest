package AtiSu;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;

public class TestBase {

    @BeforeEach
    public void beforeAll() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());

        String browser = System.getProperty("browser", "chrome");
        Configuration.baseUrl = "https://www.google.ru/";
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "1920x1080";
        Configuration.browser = browser;

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);
        Configuration.browserCapabilities = capabilities;
    }

//    @AfterEach
//    void addAttachments() {
//        Attach.screenshotAs("Скриншот прохождения теста");
//        Attach.pageSource();
//        Attach.browserConsoleLogs();
//        Selenide.closeWebDriver();
//    }
}