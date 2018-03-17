import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Sougata Bhattacharjee
 * On 13.03.18
 */
@SpringBootApplication(scanBasePackages = {"com.jpmorgan"})
public class TestApplication {
    public static void main(final String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
