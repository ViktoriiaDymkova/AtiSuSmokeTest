package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;

public class GoogleSteps {
    //locators
    SelenideElement search = $("[name=q]"),
            searchInput = $(".gLFyf");

    ElementsCollection
            collection = $$(".g .LC20lb");

    //actions
    public GoogleSteps clickToSearch() {
        search.click();
        searchInput.setValue("ati.su").pressEnter();
        return this;
    }

    public GoogleSteps finfirst() {
        collection.first().click();
        return this;
    }
}