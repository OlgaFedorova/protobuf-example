package ofedorova.api;

import ofedorova.dto.GreetRequestDto;
import ofedorova.dto.GreetResponseDto;
import ofedorova.service.SpringGrpcClientService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.SSLException;
import java.util.List;

@RestController
@RequestMapping()
public class SpringGrpcClientResource {

    private final SpringGrpcClientService springGrpcClientService;

    public SpringGrpcClientResource(SpringGrpcClientService springGrpcClientService) {
        this.springGrpcClientService = springGrpcClientService;
    }

    @PostMapping("/greetUnary")
    public GreetResponseDto greetUnary(@RequestBody GreetRequestDto greetRequest) throws SSLException {
        return springGrpcClientService.greetUnary(greetRequest);
    }

    @PostMapping("/greetServerStreaming")
    public List<GreetResponseDto> greetServerStreaming(@RequestBody GreetRequestDto greetRequest) throws SSLException {
        return springGrpcClientService.greetServerStreaming(greetRequest);
    }

    @PostMapping("/greetClientStreaming")
    public List<GreetResponseDto> greetClientStreaming(@RequestBody GreetRequestDto greetRequest) throws SSLException {
        return springGrpcClientService.greetClientStreaming(greetRequest);
    }

    @PostMapping("/greetBiDirectionalStreaming")
    public List<GreetResponseDto> greetBiDirectionalStreaming(@RequestBody GreetRequestDto greetRequest) throws SSLException {
        return springGrpcClientService.greetBiDirectionalStreaming(greetRequest);
    }
}
