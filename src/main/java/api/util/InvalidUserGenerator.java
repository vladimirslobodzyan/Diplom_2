package api.util;

import api.model.Credentials;
import org.apache.commons.lang3.RandomStringUtils;

public class InvalidUserGenerator {
    public static Credentials getInvalidUser() {
        String email = RandomStringUtils.randomAlphanumeric(8) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(8);
        return new Credentials(email, password);
    }
}
