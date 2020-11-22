package ofedorova.service;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import ofedorova.greet.dto.Greet;
import ofedorova.greet.dto.GreetRequest;
import ofedorova.greet.dto.GreetResponse;
import ofedorova.greet.service.GreetServiceGrpc;
import org.lognet.springboot.grpc.GRpcService;

import java.util.HashMap;
import java.util.Map;

@GRpcService
@Slf4j
public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {

    @Override
    public void greetUnary(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        log.info("Greet unary: {}", request);

        //extract request data
        Greet greet = request.getGreet();

        //check data and send error
        if (greet.getFirstName().isBlank() ||
                greet.getLastName().isBlank()){
            responseObserver.onError(Status.INVALID_ARGUMENT.asException());
        } else {
            // create response
            GreetResponse response = GreetResponse.newBuilder()
                    .setMessage(String.format("Hello to %s %s!", greet.getFirstName(), greet.getLastName()))
                    .build();

            // send response
            responseObserver.onNext(response);
            //complete rpc call
            responseObserver.onCompleted();
        }
    }

    @Override
    public void greetServerStreaming(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        log.info("Greet server streaming: {}", request);
        //extract request data
        Greet greet = request.getGreet();

        try {
            // create response
            for (int i = 1; i <= 10; i++) {
                GreetResponse response = GreetResponse.newBuilder()
                        .setMessage(String.format("Hello to %s %s, response number %s!",
                                greet.getFirstName(),
                                greet.getLastName(),
                                i))
                        .build();

                // send response
                responseObserver.onNext(response);
                Thread.sleep(1000L);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //complete rpc call
            responseObserver.onCompleted();
        }
    }

    @Override
    public StreamObserver<GreetRequest> greetClientStreaming(StreamObserver<GreetResponse> responseObserver) {
        log.info("Greet client streaming");
        StreamObserver<GreetRequest> requestObserver = new StreamObserver<>() {

            private Map<Greet, Integer> greets = new HashMap<>();

            @Override
            public void onNext(GreetRequest value) {
                //client send a message
                log.info("Receive request from client {}", value);
                Greet greet = value.getGreet();
                if (greets.containsKey(greet)) {
                    greets.put(greet, greets.get(greet) + 1);
                } else {
                    greets.put(greet, 1);
                }
            }

            @Override
            public void onError(Throwable t) {
                //client sends a error
            }

            @Override
            public void onCompleted() {
                //client is done, server return response
                StringBuilder result = new StringBuilder();
                greets.entrySet().
                        forEach(pair -> result.append(
                                String.format("Hello to %s %s, request times %s!",
                                        pair.getKey().getFirstName(), pair.getKey().getLastName(), pair.getValue())));

                responseObserver.onNext(GreetResponse.newBuilder()
                        .setMessage(result.toString())
                        .build());

                responseObserver.onCompleted();
            }
        };

        return requestObserver;
    }

    @Override
    public StreamObserver<GreetRequest> greetBiDirectionalStreaming(StreamObserver<GreetResponse> responseObserver) {
        log.info("Greet bi directional streaming");
        StreamObserver<GreetRequest> requestObserver = new StreamObserver<>() {

            private int i = 1;

            @Override
            public void onNext(GreetRequest value) {
                //client send a message
                log.info("Receive request from client {}", value);

                // create response

                GreetResponse response = GreetResponse.newBuilder()
                        .setMessage(String.format("Hello to %s %s, response number %s!",
                                value.getGreet().getFirstName(),
                                value.getGreet().getLastName(),
                                i++))
                        .build();

                // send response
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable t) {
                //client sends a error
            }

            @Override
            public void onCompleted() {
                //client is done, server return response
                responseObserver.onCompleted();
            }
        };

        return requestObserver;
    }
}
