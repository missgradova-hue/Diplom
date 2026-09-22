package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.CreditPage;
import ru.netology.page.PaymentPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentTest {

    @BeforeAll
    static void setup() {
        Configuration.baseUrl = "http://localhost:8080";
    }

    @Test
    void shouldPayWithApprovedCard() {
        open("/");

        PaymentPage paymentPage = new PaymentPage();

        paymentPage.openPaymentForm();

        paymentPage.fillCard(
                DataHelper.getApprovedCardNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidHolder(),
                DataHelper.getValidCvc()
        );

        paymentPage.submit();

        paymentPage.checkSuccessNotification();

        String status = SQLHelper.waitForPaymentStatus("APPROVED");
        assertEquals("APPROVED", status);
    }

    @Test
    void shouldNotPayWithDeclinedCard() {
        open("/");

        PaymentPage paymentPage = new PaymentPage();

        paymentPage.openPaymentForm();

        paymentPage.fillCard(
                DataHelper.getDeclinedCardNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidHolder(),
                DataHelper.getValidCvc()
        );

        paymentPage.submit();

        paymentPage.checkErrorNotification();
    }

    @Test
    void shouldShowErrorWithEmptyCardNumber() {
        open("/");

        PaymentPage paymentPage = new PaymentPage();

        paymentPage.openPaymentForm();

        paymentPage.fillCard(
                "",
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidHolder(),
                DataHelper.getValidCvc()
        );

        paymentPage.submit();

        paymentPage.checkCardNumberError();
    }

    @Test
    void shouldShowErrorWithIncompleteCardNumber() {
        open("/");

        PaymentPage paymentPage = new PaymentPage();

        paymentPage.openPaymentForm();

        paymentPage.fillCard(
                "4444 4444 4444 444",
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidHolder(),
                DataHelper.getValidCvc()
        );

        paymentPage.submit();

        paymentPage.checkCardNumberError();
    }

    @Test
    void shouldShowErrorWithEmptyMonth() {
        open("/");

        PaymentPage paymentPage = new PaymentPage();

        paymentPage.openPaymentForm();

        paymentPage.fillCard(
                DataHelper.getApprovedCardNumber(),
                "",
                DataHelper.getValidYear(),
                DataHelper.getValidHolder(),
                DataHelper.getValidCvc()
        );

        paymentPage.submit();

        paymentPage.checkMonthError();
    }

    @Test
    void shouldShowErrorWithInvalidMonth() {
        open("/");

        PaymentPage paymentPage = new PaymentPage();

        paymentPage.openPaymentForm();

        paymentPage.fillCard(
                DataHelper.getApprovedCardNumber(),
                "13",
                DataHelper.getValidYear(),
                DataHelper.getValidHolder(),
                DataHelper.getValidCvc()
        );

        paymentPage.submit();

        paymentPage.checkMonthError();
    }

    @Test
    void shouldShowErrorWithEmptyYear() {
        open("/");

        PaymentPage paymentPage = new PaymentPage();

        paymentPage.openPaymentForm();

        paymentPage.fillCard(
                DataHelper.getApprovedCardNumber(),
                DataHelper.getValidMonth(),
                "",
                DataHelper.getValidHolder(),
                DataHelper.getValidCvc()
        );

        paymentPage.submit();

        paymentPage.checkYearError();
    }

    @Test
    void shouldShowErrorWithExpiredYear() {
        open("/");

        PaymentPage paymentPage = new PaymentPage();

        paymentPage.openPaymentForm();

        paymentPage.fillCard(
                DataHelper.getApprovedCardNumber(),
                DataHelper.getValidMonth(),
                "25",
                DataHelper.getValidHolder(),
                DataHelper.getValidCvc()
        );

        paymentPage.submit();

        paymentPage.checkYearError();
    }

    @Test
    void shouldShowErrorWithEmptyHolder() {
        open("/");

        PaymentPage paymentPage = new PaymentPage();

        paymentPage.openPaymentForm();

        paymentPage.fillCard(
                DataHelper.getApprovedCardNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                "",
                DataHelper.getValidCvc()
        );

        paymentPage.submit();

        paymentPage.checkHolderError();
    }

    @Test
    void shouldShowErrorWithInvalidHolder() {
        open("/");

        PaymentPage paymentPage = new PaymentPage();

        paymentPage.openPaymentForm();

        paymentPage.fillCard(
                DataHelper.getApprovedCardNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                "12345",
                DataHelper.getValidCvc()
        );

        paymentPage.submit();

        paymentPage.checkHolderError();
    }

    @Test
    void shouldShowErrorWithEmptyCvc() {
        open("/");

        PaymentPage paymentPage = new PaymentPage();

        paymentPage.openPaymentForm();

        paymentPage.fillCard(
                DataHelper.getApprovedCardNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidHolder(),
                ""
        );

        paymentPage.submit();

        paymentPage.checkCvcError();
    }

    @Test
    void shouldShowErrorWithInvalidCvc() {
        open("/");

        PaymentPage paymentPage = new PaymentPage();

        paymentPage.openPaymentForm();

        paymentPage.fillCard(
                DataHelper.getApprovedCardNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidHolder(),
                "12"
        );

        paymentPage.submit();

        paymentPage.checkCvcError();
    }


    @Test
    void shouldSaveDeclinedStatusInDatabase() {
        open("/");

        PaymentPage paymentPage = new PaymentPage();

        paymentPage.openPaymentForm();

        paymentPage.fillCard(
                DataHelper.getDeclinedCardNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidHolder(),
                DataHelper.getValidCvc()
        );

        paymentPage.submit();


        String status = SQLHelper.waitForPaymentStatus("DECLINED");
        assertEquals("DECLINED", status);
    }

    @Test
    void shouldCreditWithApprovedCard() {
        open("/");

        CreditPage creditPage = new CreditPage();

        creditPage.openCreditForm();

        creditPage.fillCard(
                DataHelper.getApprovedCardNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidHolder(),
                DataHelper.getValidCvc()
        );

        creditPage.submit();

        creditPage.checkSuccessNotification();
    }

    @Test
    void shouldNotCreditWithDeclinedCard() {
        open("/");

        CreditPage creditPage = new CreditPage();

        creditPage.openCreditForm();

        creditPage.fillCard(
                DataHelper.getDeclinedCardNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidHolder(),
                DataHelper.getValidCvc()
        );

        creditPage.submit();

        creditPage.checkErrorNotification();
    }

    @Test
    void shouldSaveDeclinedCreditStatusInDatabase() {
        open("/");

        CreditPage creditPage = new CreditPage();

        creditPage.openCreditForm();

        creditPage.fillCard(
                DataHelper.getDeclinedCardNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidHolder(),
                DataHelper.getValidCvc()
        );

        creditPage.submit();

        String status = SQLHelper.waitForCreditStatus("DECLINED");
        assertEquals("DECLINED", status);
    }

    @Test
    void shouldSaveApprovedCreditStatusInDatabase() {
        open("/");

        CreditPage creditPage = new CreditPage();

        creditPage.openCreditForm();

        creditPage.fillCard(
                DataHelper.getApprovedCardNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidHolder(),
                DataHelper.getValidCvc()
        );

        creditPage.submit();

        String status = SQLHelper.waitForCreditStatus("APPROVED");
        assertEquals("APPROVED", status);
    }
}