package gbrstd.clinica.config;

import gbrstd.clinica.constants.AppConstants;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class AppStartup {

    @PostConstruct
    public void init() throws IOException {
        File file = new File(AppConstants.UPLOAD_DIR);
        if (!file.exists()) {
            Files.createDirectories(file.toPath());
        }
    }

}
