package ru.netology.test;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.CreditPage;
import ru.netology.page.PaymentPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Путешествие дня")
@Feature("Оплата тура")
public class PaymentTest {

    @BeforeAll
    static void setup() {
        Configuration.baseUrl = "http://localhost:8080";
    }

    @Test
    @DisplayName("Успешная оплата тура картой со статусом APPROVED")
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
    @DisplayName("Отказ в оплате тура картой со статусом DECLINED")
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
    @DisplayName("Ошибка при пустом номере карты")
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
    @DisplayName("Ошибка при неполном номере карты")
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
    @DisplayName("Ошибка при пустом месяце")
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
    @DisplayName("Ошибка при некорректном месяце")
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
    @DisplayName("Ошибка при пустом годе")
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
    @DisplayName("Ошибка при истёкшем сроке действия карты")
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
    @DisplayName("Ошибка при пустом поле владельца")
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
    @DisplayName("Ошибка при некорректном имени владельца")
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
    @DisplayName("Ошибка при пустом CVC")
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
    @DisplayName("Ошибка при некорректном CVC")
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
    @DisplayName("Сохранение статуса DECLINED в БД при оплате отклонённой картой")
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
    @DisplayName("Успешная покупка тура в кредит картой со статусом APPROVED")
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
    @DisplayName("Отказ в покупке тура в кредит картой со статусом DECLINED")
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
    @DisplayName("Сохранение статуса DECLINED в БД при покупке в кредит")
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
    @DisplayName("Сохранение статуса APPROVED в БД при покупке в кредит")
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

    @Test
    @DisplayName("Кредит: ошибка при пустом номере карты")
    void shouldShowErrorWithEmptyCardNumberInCreditForm() {
        open("/");

        CreditPage creditPage = new CreditPage();
        creditPage.openCreditForm();

        creditPage.fillCard(
                "",
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidHolder(),
                DataHelper.getValidCvc()
        );

        creditPage.submit();
        creditPage.checkCardNumberError();
    }

    @Test
    @DisplayName("Кредит: ошибка при неполном номере карты")
    void shouldShowErrorWithIncompleteCardNumberInCreditForm() {
        open("/");

        CreditPage creditPage = new CreditPage();
        creditPage.openCreditForm();

        creditPage.fillCard(
                "4444 4444 4444 444",
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidHolder(),
                DataHelper.getValidCvc()
        );

        creditPage.submit();
        creditPage.checkCardNumberError();
    }

    @Test
    @DisplayName("Кредит: ошибка при пустом месяце")
    void shouldShowErrorWithEmptyMonthInCreditForm() {
        open("/");

        CreditPage creditPage = new CreditPage();
        creditPage.openCreditForm();

        creditPage.fillCard(
                DataHelper.getApprovedCardNumber(),
                "",
                DataHelper.getValidYear(),
                DataHelper.getValidHolder(),
                DataHelper.getValidCvc()
        );

        creditPage.submit();
        creditPage.checkMonthError();
    }

    @Test
    @DisplayName("Кредит: ошибка при некорректном месяце")
    void shouldShowErrorWithInvalidMonthInCreditForm() {
        open("/");

        CreditPage creditPage = new CreditPage();
        creditPage.openCreditForm();

        creditPage.fillCard(
                DataHelper.getApprovedCardNumber(),
                "13",
                DataHelper.getValidYear(),
                DataHelper.getValidHolder(),
                DataHelper.getValidCvc()
        );

        creditPage.submit();
        creditPage.checkMonthError();
    }

    @Test
    @DisplayName("Кредит: ошибка при пустом годе")
    void shouldShowErrorWithEmptyYearInCreditForm() {
        open("/");

        CreditPage creditPage = new CreditPage();
        creditPage.openCreditForm();

        creditPage.fillCard(
                DataHelper.getApprovedCardNumber(),
                DataHelper.getValidMonth(),
                "",
                DataHelper.getValidHolder(),
                DataHelper.getValidCvc()
        );

        creditPage.submit();
        creditPage.checkYearError();
    }

    @Test
    @DisplayName("Кредит: ошибка при истёкшем сроке действия карты")
    void shouldShowErrorWithExpiredYearInCreditForm() {
        open("/");

        CreditPage creditPage = new CreditPage();
        creditPage.openCreditForm();

        creditPage.fillCard(
                DataHelper.getApprovedCardNumber(),
                DataHelper.getValidMonth(),
                "25",
                DataHelper.getValidHolder(),
                DataHelper.getValidCvc()
        );

        creditPage.submit();
        creditPage.checkYearError();
    }

    @Test
    @DisplayName("Кредит: ошибка при пустом поле владельца")
    void shouldShowErrorWithEmptyHolderInCreditForm() {
        open("/");

        CreditPage creditPage = new CreditPage();
        creditPage.openCreditForm();

        creditPage.fillCard(
                DataHelper.getApprovedCardNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                "",
                DataHelper.getValidCvc()
        );

        creditPage.submit();
        creditPage.checkHolderError();
    }

    @Test
    @DisplayName("Кредит: ошибка при некорректном имени владельца")
    void shouldShowErrorWithInvalidHolderInCreditForm() {
        open("/");

        CreditPage creditPage = new CreditPage();
        creditPage.openCreditForm();

        creditPage.fillCard(
                DataHelper.getApprovedCardNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                "12345",
                DataHelper.getValidCvc()
        );

        creditPage.submit();
        creditPage.checkHolderError();
    }

    @Test
    @DisplayName("Кредит: ошибка при пустом CVC")
    void shouldShowErrorWithEmptyCvcInCreditForm() {
        open("/");

        CreditPage creditPage = new CreditPage();
        creditPage.openCreditForm();

        creditPage.fillCard(
                DataHelper.getApprovedCardNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidHolder(),
                ""
        );

        creditPage.submit();
        creditPage.checkCvcError();
    }

    @Test
    @DisplayName("Кредит: ошибка при некорректном CVC")
    void shouldShowErrorWithInvalidCvcInCreditForm() {
        open("/");

        CreditPage creditPage = new CreditPage();
        creditPage.openCreditForm();

        creditPage.fillCard(
                DataHelper.getApprovedCardNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidHolder(),
                "12"
        );

        creditPage.submit();
        creditPage.checkCvcError();
    }
}