package jeremi.codeformatter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class CodeFormatterApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(CodeFormatterApplication.class, args);

    }

}
