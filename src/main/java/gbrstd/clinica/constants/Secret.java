package gbrstd.clinica.constants;

import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class Secret {

    public static int rememberMeValiditySeconds;

    public static String rememberMeKey;

    static {
        Properties properties = new Properties();
        try {
            properties.load(Secret.class.getClassLoader().getResourceAsStream("application.properties"));
            rememberMeValiditySeconds = Integer.parseInt(properties.getProperty("app.rememberme.tokenValiditySeconds"));
            rememberMeKey = properties.getProperty("app.rememberme.key");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
