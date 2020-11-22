package ofedorova;


import ofedorova.client.GreetingClient;
import ofedorova.greet.dto.Greet;
import ofedorova.greet.dto.GreetRequest;

import javax.net.ssl.SSLException;

public class GrpcClient {

    public static void main(String[] args) throws SSLException {
        System.out.println("Hello from client");

        GreetingClient greetingClient = new GreetingClient("localhost", 50051);

        GreetRequest greetRequest = GreetRequest.newBuilder()
                .setGreet(Greet.newBuilder()
                                .setFirstName("John")
                                .setLastName("Smith")
                                .build())
                .build();

        //Test unary call
        System.out.println("#############################################################");
        System.out.println("RPC greet unary:" + System.lineSeparator() +
                greetingClient.greetUnary(greetRequest));

        //Test unary error call
        System.out.println("#############################################################");
        System.out.println("RPC greet error unary:" + System.lineSeparator() +
                greetingClient.greetUnary(GreetRequest.newBuilder().build()));

        //Test server streaming call
        System.out.println("#############################################################");
        System.out.println("RPC greet server streaming:");
        greetingClient.greetServerStreaming(greetRequest);

        //Test client streaming call
        System.out.println("#############################################################");
        System.out.println("RPC greet client streaming:");
        greetingClient.greetClientStreaming(greetRequest);

        //Test bi directional streaming call
        System.out.println("#############################################################");
        System.out.println("RPC greet bi directional streaming:");
        greetingClient.greetBiDirectionalStreaming(greetRequest);


        System.out.println("Finish from client");

    }

}
