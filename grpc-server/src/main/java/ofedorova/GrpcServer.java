package ofedorova;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import ofedorova.service.GreetServiceImpl;

import java.io.File;
import java.io.IOException;

public class GrpcServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Hello from server");

        Server server = ServerBuilder
                .forPort(50051)
                .addService(new GreetServiceImpl())
                .addService(ProtoReflectionService.newInstance())
                .useTransportSecurity(
                        new File("ssl/server.crt"),
                        new File("ssl/server.pem")
                )
                .build();
        server.start();

        server.awaitTermination();
    }
}
