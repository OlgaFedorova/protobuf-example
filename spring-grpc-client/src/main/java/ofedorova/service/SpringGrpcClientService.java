package ofedorova.service;

import ofedorova.converter.GreetRequestConverter;
import ofedorova.converter.GreetResponseDtoConverter;
import ofedorova.client.GreetingClient;
import ofedorova.dto.GreetRequestDto;
import ofedorova.dto.GreetResponseDto;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLException;
import java.util.List;

@Service
public class SpringGrpcClientService {

    private final GreetingClient greetingClient;
    private final GreetResponseDtoConverter greetResponseDtoConverter;
    private final GreetRequestConverter greetRequestConverter;

    public SpringGrpcClientService(GreetingClient greetingClient,
                                   GreetResponseDtoConverter greetResponseDtoConverter,
                                   GreetRequestConverter greetRequestConverter) {
        this.greetingClient = greetingClient;
        this.greetResponseDtoConverter = greetResponseDtoConverter;
        this.greetRequestConverter = greetRequestConverter;
    }

    public GreetResponseDto greetUnary(GreetRequestDto greetRequest) throws SSLException {
        return greetResponseDtoConverter.toDto(
                greetingClient.greetUnary(greetRequestConverter.fromDto(greetRequest))
        );
    }

    public List<GreetResponseDto> greetServerStreaming(GreetRequestDto greetRequest) throws SSLException {
        return greetResponseDtoConverter.toDtos(
                greetingClient.greetServerStreaming(greetRequestConverter.fromDto(greetRequest))
        );
    }

    public List<GreetResponseDto> greetClientStreaming(GreetRequestDto greetRequest) throws SSLException {
        return greetResponseDtoConverter.toDtos(
                greetingClient.greetClientStreaming(greetRequestConverter.fromDto(greetRequest))
        );
    }

    public List<GreetResponseDto> greetBiDirectionalStreaming(GreetRequestDto greetRequest) throws SSLException {
        return greetResponseDtoConverter.toDtos(
                greetingClient.greetBiDirectionalStreaming(greetRequestConverter.fromDto(greetRequest))
        );
    }

}
