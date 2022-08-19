package AtiSu;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pages.GoogleSteps;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class SmokeTests extends TestBase {

    GoogleSteps googleSteps = new GoogleSteps();

    @Tag("smokeTest")
    @Test
    @DisplayName("Автоматизация скрипта проверки сайта ati.su")
    void mainPageTest() {
        step("Открыть популярный сайт-поисковик https://www.google.ru/", () -> {
            open(baseUrl);
        });
        step("В поисковой строке введите ati.su", () -> {
            googleSteps.clickToSearch();
        });
        step("Перейдите по первой ссылке и убедиться, что это сайт ati.su", () -> {
            googleSteps.finfirst();
        });
    }

    @Test
    @DisplayName("Проверка отображения заголовка страницы")
    void titleTest() {
        step("Открыть главную страницу ati.su ", () ->
                open("https://ati.su/"));
        step("Проверка соответствия заголовка 'ATI.SU – биржа грузоперевозок. Грузы, транспорт, тендеры'", () -> {
            String expectedTitle = "ATI.SU – биржа грузоперевозок. Грузы, транспорт, тендеры.";
            String actualTitle = title();
            assertThat(actualTitle).isEqualTo(expectedTitle);
        });
    }

    @Test
    @CsvSource(value = {
            "Грузы, Поиск грузов",
            "Транспорт, Поиск транспорта",
            "Тендеры, Тендеры на автоперевозки"
    })
    @ParameterizedTest(name = "При клике по полю {0} в результатах отображается текст {1}")
    void menuItemsTest(String data, String result) {
        step("Открыть главную страницу ati.su", () -> {
            open("https://ati.su/");
        });
        step("Выбрать на странице искомый раздел", () -> {
            $("[data-name=main-menu-links]").$(byText(data)).click();
        });
        step("Убедиться, что при переходе в искомый раздел, отображается ожидаемый результат", () -> {
            $$("#root").find(text(result)).shouldBe(visible);
        });
    }

    @Test
    @DisplayName("Проверка негативной авторизации и возвращения статус-кода 401 в ответе")
    void apiTest01() {
        String body = "{\"login\":\"1234\",\"password\":\"1234\",\"remember\":false,\"captcha\":null}";

        given()
                .log().uri()
                .log().body()
                .body(body)
                .contentType(JSON)
                .when()
                .post("https://id.ati.su/api/auth/?encode=true")
                .then()
                .log().status()
                .log().body()
                .statusCode(401);
    }
    @Test
    @DisplayName("Проверка негативной авторизации и возвращения статус-кода 401 в ответе")
    void apiTest02() {
        String body = "{\n" +
                "    \"error\": \"unauthorized\",\n" +
                "    \"reason\": \"Обновите страницу. Если ошибка повторится, обратитесь в техподдержку\"\n" +
                "}";

        given()
                .formParam("login","123")
                .formParam("password","123")
                .log().all()
                .log().body()
                .body(body)
                .contentType(JSON)
                .when().log().all()
                .post("https://id.ati.su/api/auth/?encode=true")
                .then()
                .log().status()
                .log().body()
                .statusCode(401);
    }

    @Test
    void proverkaLocator() {
        step("Открыть главную страницу ati.su", () -> {
            open("https://ati.su/");
        });
        step("Выбрать на странице искомый раздел", () -> {
           // $(".AtiHeader__main-menu____tRP5").$(byText("Грузы")).click();
            $("[data-name=main-menu-links]").$(byText("Грузы")).click();
        });
        step("Убедиться, что при переходе в искомый раздел, отображается ожидаемый результат", () -> {
            $$("[class=Search_container__JXJhz]").find(text("Поиск грузов")).shouldBe(visible);
        });
    }
}

