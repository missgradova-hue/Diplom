package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;


import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CreditPage {

    private final SelenideElement creditButton =
            $$("button.button").get(1);

    private final SelenideElement cardNumber =
            $("input[placeholder='0000 0000 0000 0000']");

    private final SelenideElement month =
            $("input[placeholder='08']");

    private final SelenideElement year =
            $("input[placeholder='22']");

    private final SelenideElement holder =
            $$("input").get(3);

    private final SelenideElement cvc =
            $("input[placeholder='999']");

    private final SelenideElement continueButton =
            $$("button.button").last();

    private final SelenideElement successNotification =
            $(".notification_status_ok");

    private final SelenideElement errorNotification =
            $(".notification_status_error");


    public void openCreditForm() {
        creditButton.click();
    }

    public void fillCard(String number,
                         String monthValue,
                         String yearValue,
                         String holderValue,
                         String cvcValue) {

        cardNumber.setValue(number);
        month.setValue(monthValue);
        year.setValue(yearValue);
        holder.setValue(holderValue);
        cvc.setValue(cvcValue);
    }

    public void submit() {
        continueButton.click();
    }

    public void checkSuccessNotification() {
        successNotification.shouldBe(
                visible,
                Duration.ofSeconds(15)
        );
    }

    public void checkErrorNotification() {
        errorNotification.shouldBe(
                visible,
                Duration.ofSeconds(15)
        );
    }


}
