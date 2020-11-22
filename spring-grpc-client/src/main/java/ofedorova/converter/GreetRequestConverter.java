package ofedorova.converter;

import ofedorova.dto.GreetRequestDto;
import ofedorova.greet.dto.Greet;
import ofedorova.greet.dto.GreetRequest;
import org.springframework.stereotype.Component;

@Component
public class GreetRequestConverter {

    public GreetRequest fromDto(GreetRequestDto dto){
        return GreetRequest.newBuilder()
                .setGreet(Greet.newBuilder()
                        .setFirstName(dto.getFirstName())
                        .setLastName(dto.getLastName())
                        .build())
                .build();
    }
}
