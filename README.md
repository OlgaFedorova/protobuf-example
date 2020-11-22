# Install proto-compiler
In order to perform code generation, you will need to install protoc on your computer:
https://grpc.io/docs/protoc-installation/

# Protobuf Plugin for Gradle
https://github.com/google/protobuf-gradle-plugin

# Protocol Buffer
https://developers.google.com/protocol-buffers/docs/proto3
https://developers.google.com/protocol-buffers/docs/javatutorial

# Name convention
https://developers.google.com/protocol-buffers/docs/style

# gRPC
https://github.com/grpc/grpc-java
https://www.baeldung.com/grpc-introduction
https://grpc.io/docs/languages/java/

# gRPC over HTTP2
https://github.com/grpc/grpc/blob/master/doc/PROTOCOL-HTTP2.md
https://blog.cloudflare.com/road-to-grpc/

# Error handling
https://grpc.io/docs/guides/error/

# Deadline
https://grpc.io/blog/deadlines/#setting-a-deadline

# SSL
https://github.com/simplesteph/grpc-go-course/blob/master/ssl/instructions.sh
https://grpc.io/docs/guides/auth/

# Spring boot starter
https://github.com/LogNet/grpc-spring-boot-starter
https://github.com/yidongnan/grpc-spring-boot-starter

https://medium.com/@UditMittal/spring-boot-grpc-maven-java-starter-c3130e751
https://medium.com/@sajeerzeji44/grpc-for-spring-boot-microservices-bd9b79569772

# When to use gRPC?
gRPC can be used only in those use cases where there is internal communication between 
services. Multiplexing, full duplex, proto request/response makes gRPC much faster as 
compared to REST. It is only available for internal services because there is no APIs 
available for external use, but the Google team is working on gRPC-web also so that users 
can also use this for external services communications.

https://github.com/grpc/grpc-web
https://grpc.io/blog/state-of-grpc-web/
https://medium.com/@knoldus/why-grpc-for-inter-microservice-communication-b66053cea54e

# gRPC CLI
https://github.com/ktr0731/evans
https://github.com/fullstorydev/grpcurl
