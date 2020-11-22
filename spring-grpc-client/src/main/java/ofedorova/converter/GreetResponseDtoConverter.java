package ofedorova.converter;

import ofedorova.dto.GreetResponseDto;
import ofedorova.greet.dto.GreetResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GreetResponseDtoConverter {

    public GreetResponseDto toDto (GreetResponse greetResponse) {
        GreetResponseDto dto = new GreetResponseDto();
        dto.setMessage(greetResponse.getMessage());
        return dto;
    }

    public List<GreetResponseDto> toDtos (List<GreetResponse> greetResponses) {
        return greetResponses.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
