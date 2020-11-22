package ofedorova.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "greet-client")
@Getter
@AllArgsConstructor
public class GreetClientProperties {
    private String host;
    private int port;
    private String trustCertCollectionFile;
}
