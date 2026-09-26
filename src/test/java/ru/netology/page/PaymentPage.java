package ru.netology.page;


import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentPage {

    private final SelenideElement buyButton =
            $$("button.button").first();

    private final SelenideElement cardNumber =
            $("input[placeholder='0000 0000 0000 0000']");

    private final SelenideElement month =
            $("input[placeholder='08']");

    private final SelenideElement year =
            $("input[placeholder='22']");

    private final SelenideElement holder =
            $("input.input__control:not([placeholder])");

    private final SelenideElement cvc =
            $("input[placeholder='999']");

    private final SelenideElement continueButton =
            $("form").$("button.button_view_extra");

    private final SelenideElement successNotification =
            $(".notification_status_ok");

    private final SelenideElement errorNotification =
            $(".notification_status_error");

    private final SelenideElement cardNumberError =
            cardNumber.closest(".input").$(".input__sub");
    private final SelenideElement monthError =
            month.closest(".input").$(".input__sub");
    private final SelenideElement yearError =
            year.closest(".input").$(".input__sub");
    private final SelenideElement holderError =
            holder.closest(".input").$(".input__sub");
    private final SelenideElement cvcError =
            cvc.closest(".input").$(".input__sub");

    public void checkCardNumberError() {
        cardNumberError.shouldBe(visible);
    }

    public void openPaymentForm() {
        buyButton.click();
    }

    public void fillCard(String number, String monthValue,
                         String yearValue, String holderValue,
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
        successNotification.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void checkErrorNotification() {
        errorNotification.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void checkMonthError() {
        monthError.shouldBe(visible);
    }

    public void checkYearError() {
        yearError.shouldBe(visible);
    }

    public void checkHolderError() {
        holderError.shouldBe(visible);
    }

    public void checkCvcError() {
        cvcError.shouldBe(visible);
    }
}