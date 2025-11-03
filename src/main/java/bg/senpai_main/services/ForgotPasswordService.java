package bg.senpai_main.services;


public interface ForgotPasswordService {

    /**
     * Изпраща код за нулиране на паролата на даден имейл.
     * @param email имейл адресът на потребителя
     */
    void sendResetCode(String email);

    /**
     * Проверява дали подаденият код е валиден и не е изтекъл.
     * @param email имейл адресът на потребителя
     * @param code кодът, въведен от потребителя
     * @return true, ако кодът е валиден, иначе false
     */
    boolean verifyCode(String email, String code);
}

