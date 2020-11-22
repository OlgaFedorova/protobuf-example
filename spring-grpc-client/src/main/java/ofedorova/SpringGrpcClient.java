package ofedorova;

import ofedorova.properties.GreetClientProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({GreetClientProperties.class})
public class SpringGrpcClient {

    public static void main(String[] args) {
        SpringApplication.run(SpringGrpcClient.class, args);
    }
}
