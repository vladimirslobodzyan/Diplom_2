import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {

    public static User getDefaultUser() {
        String email = RandomStringUtils.randomAlphanumeric(8) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(8);
        String name = RandomStringUtils.randomAlphabetic(8);
        return new User(email, password, name);
    }

    public static User getDefaultUserWithoutEmail() {
        String password = RandomStringUtils.randomAlphabetic(8);
        String name = RandomStringUtils.randomAlphabetic(8);
        return new User(null, password, name);
    }

    public static User getDefaultUserWithoutPass() {
        String email = RandomStringUtils.randomAlphanumeric(8) + "@yandex.ru";
        String name = RandomStringUtils.randomAlphabetic(8);
        return new User(email, null, name);
    }
    public static UserLogin getNewUser() {
        String email = RandomStringUtils.randomAlphanumeric(8) + "@yandex.ru";
        String name = RandomStringUtils.randomAlphabetic(8);
        return new UserLogin(email, name);
    }
}
