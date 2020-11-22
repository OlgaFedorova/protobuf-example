package ofedorova.client;

import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import ofedorova.greet.dto.GreetRequest;
import ofedorova.greet.dto.GreetResponse;
import ofedorova.greet.service.GreetServiceGrpc;
import ofedorova.properties.GreetClientProperties;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLException;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class GreetingClient {

    private final GreetClientProperties clientProperties;

    public GreetingClient(GreetClientProperties clientProperties) {
        this.clientProperties = clientProperties;
    }

    public GreetResponse greetUnary(GreetRequest greetRequest) throws SSLException {
        ManagedChannel channel = channel();

        GreetServiceGrpc.GreetServiceBlockingStub stub
                = GreetServiceGrpc.newBlockingStub(channel);

        try {
            GreetResponse response = stub.greetUnary(greetRequest);
            return response;
        } catch (StatusRuntimeException ex) {
            log.error("Get ERROR", ex);
            return null;
        } finally {
            channel.shutdown();
        }
    }

    public List<GreetResponse> greetServerStreaming(GreetRequest greetRequest) throws SSLException {
        ManagedChannel channel = channel();

        GreetServiceGrpc.GreetServiceBlockingStub stub
                = GreetServiceGrpc.newBlockingStub(channel);

        Iterator<GreetResponse> response = stub.greetServerStreaming(greetRequest);
        List<GreetResponse> result = new ArrayList<>();
        response.forEachRemaining(greetResponse -> {
            log.info("Receive response {}", greetResponse);
            result.add(greetResponse);
        });

        channel.shutdown();
        return result;
    }

    public List<GreetResponse> greetClientStreaming(GreetRequest greetRequest) throws SSLException {
        ManagedChannel channel = channel();

        GreetServiceGrpc.GreetServiceStub asyncStub
                = GreetServiceGrpc.newStub(channel);

        List<GreetResponse> result = new ArrayList<>();

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<GreetRequest> requestObserver = asyncStub.greetClientStreaming(new StreamObserver<>() {
            @Override
            public void onNext(GreetResponse value) {
                //get response from server
                log.info("Received a response from the server: {}", value);
                result.add(value);
            }

            @Override
            public void onError(Throwable t) {
                //get error from server
            }

            @Override
            public void onCompleted() {
                //server is done sending us data
                log.info("Server has completed sending us something");
                latch.countDown();
            }
        });

        for (int i = 1; i <= 5; i++) {
            log.info("Send message {}", i);
            requestObserver.onNext(greetRequest);
        }

        //tell the server that client is done sending data
        requestObserver.onCompleted();

        try {
            latch.await(10L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Get error", e);
        }

        channel.shutdown();
        return result;
    }

    public List<GreetResponse> greetBiDirectionalStreaming(GreetRequest greetRequest) throws SSLException {
        ManagedChannel channel = channel();

        GreetServiceGrpc.GreetServiceStub asyncStub
                = GreetServiceGrpc.newStub(channel);

        List<GreetResponse> result = new ArrayList<>();

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<GreetRequest> requestObserver = asyncStub.greetBiDirectionalStreaming(new StreamObserver<>() {
            @Override
            public void onNext(GreetResponse value) {
                //get response from server
                log.info("Received a response from the server: {}", value);
                result.add(value);
            }

            @Override
            public void onError(Throwable t) {
                //get error from server
            }

            @Override
            public void onCompleted() {
                //server is done sending us data
                log.info("Server has completed sending us something");
                latch.countDown();
            }
        });

        try {
            for (int i = 1; i <= 5; i++) {
                log.info("Send message {}",  i);
                requestObserver.onNext(greetRequest);
                Thread.sleep(1000L);
            }

            //tell the server that client is done sending data
            requestObserver.onCompleted();

            latch.await(10L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Get error", e);
        }

        channel.shutdown();
        return result;
    }

    private ManagedChannel channel() throws SSLException {
        return NettyChannelBuilder.forAddress(clientProperties.getHost(),
                clientProperties.getPort())
                .sslContext(GrpcSslContexts.forClient().trustManager(
                        new File(clientProperties.getTrustCertCollectionFile())
                ).build())
                .build();
    }
}
