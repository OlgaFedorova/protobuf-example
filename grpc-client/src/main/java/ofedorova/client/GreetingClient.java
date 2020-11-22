package ofedorova.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;
import ofedorova.greet.dto.GreetRequest;
import ofedorova.greet.dto.GreetResponse;
import ofedorova.greet.service.GreetServiceGrpc;

import javax.net.ssl.SSLException;
import java.io.File;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class GreetingClient {

    private final String host;
    private final int port;


    public GreetingClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public GreetResponse greetUnary(GreetRequest greetRequest) throws SSLException {
        ManagedChannel channel = channel();

        GreetServiceGrpc.GreetServiceBlockingStub stub
                = GreetServiceGrpc.newBlockingStub(channel);

        try {
            GreetResponse response = stub.greetUnary(greetRequest);
            return response;
        } catch (StatusRuntimeException ex) {
            System.out.println("Get ERROR: " + ex.getMessage());
            return null;
        } finally {
            channel.shutdown();
        }
    }

    public void greetServerStreaming(GreetRequest greetRequest) throws SSLException {
        ManagedChannel channel = channel();

        GreetServiceGrpc.GreetServiceBlockingStub stub
                = GreetServiceGrpc.newBlockingStub(channel);

        Iterator<GreetResponse> response = stub.greetServerStreaming(greetRequest);
        response.forEachRemaining(greetResponse -> System.out.println(greetResponse));

        channel.shutdown();
    }

    public void greetClientStreaming(GreetRequest greetRequest) throws SSLException {
        ManagedChannel channel = channel();

        GreetServiceGrpc.GreetServiceStub asyncStub
                = GreetServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<GreetRequest> requestObserver = asyncStub.greetClientStreaming(new StreamObserver<>() {
            @Override
            public void onNext(GreetResponse value) {
                //get response from server
                System.out.println("Received a response from the server: ");
                System.out.println(value);
            }

            @Override
            public void onError(Throwable t) {
                //get error from server
            }

            @Override
            public void onCompleted() {
                //server is done sending us data
                System.out.println("Server has completed sending us something");
                latch.countDown();
            }
        });

        for (int i = 1; i <= 5; i++) {
            System.out.println("Send message " + i);
            requestObserver.onNext(greetRequest);
        }

        //tell the server that client is done sending data
        requestObserver.onCompleted();

        try {
            latch.await(10L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        channel.shutdown();
    }

    public void greetBiDirectionalStreaming(GreetRequest greetRequest) throws SSLException {
        ManagedChannel channel = channel();

        GreetServiceGrpc.GreetServiceStub asyncStub
                = GreetServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<GreetRequest> requestObserver = asyncStub.greetBiDirectionalStreaming(new StreamObserver<>() {
            @Override
            public void onNext(GreetResponse value) {
                //get response from server
                System.out.println("Received a response from the server: ");
                System.out.println(value);
            }

            @Override
            public void onError(Throwable t) {
                //get error from server
            }

            @Override
            public void onCompleted() {
                //server is done sending us data
                System.out.println("Server has completed sending us something");
                latch.countDown();
            }
        });

        try {
            for (int i = 1; i <= 5; i++) {
                System.out.println("Send message " + i);
                requestObserver.onNext(greetRequest);
                Thread.sleep(1000L);
            }

            //tell the server that client is done sending data
            requestObserver.onCompleted();

            latch.await(10L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        channel.shutdown();
    }

    private ManagedChannel channel() throws SSLException {
        return NettyChannelBuilder.forAddress(host, port)
                .sslContext(GrpcSslContexts.forClient().trustManager(new File("ssl/ca.crt")).build())
                .build();
    }
}
