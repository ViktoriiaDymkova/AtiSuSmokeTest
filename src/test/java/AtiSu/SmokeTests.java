package AtiSu;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pages.GoogleSteps;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SmokeTests extends TestBase {

    GoogleSteps googleSteps = new GoogleSteps();

    @Tag("smokeTest")
    @Test
    @DisplayName("Автоматизация скрипта проверки нахождения ati.su в гугле")
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

    @DisplayName("Проверка отображения результатов в разделах сайта")
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
    void apiTest() {
        String body = "{\n" +
                "    \"error\": \"unauthorized\",\n" +
                "    \"reason\": \"Обновите страницу. Если ошибка повторится, обратитесь в техподдержку\"\n" +
                "}";
        Map<String, String> user = new HashMap<>();
        user.put("login", "1234");
        user.put("password", "79999999999");
        given()
                .body(user)
                .contentType(ContentType.JSON)
                .when().log().all()
                .post("https://id.ati.su/api/auth/?encode=true")
                .then().log().all()
                .statusCode(401);
    }
}

