package io.strmprivacy.demo.grpc.server

import io.grpc.ServerBuilder
import io.grpc.protobuf.services.ProtoReflectionService
import io.strmprivacy.demo.grpc.v1.GrpcDemoServiceGrpc
import io.strmprivacy.demo.grpc.v1.GrpcDemoServiceGrpcKt
import io.strmprivacy.demo.grpc.v1.HelloWorldRequest
import io.strmprivacy.demo.grpc.v1.HelloWorldResponse
import io.strmprivacy.demo.grpc.v1.helloWorldResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun main() {
    val server = ServerBuilder
        .forPort(50051) // Default gRPC port
        .addService(GrpcDemoService)
        .addService(ProtoReflectionService.newInstance())
        .build()
        .start()

    server.awaitTermination()
}

object GrpcDemoService : GrpcDemoServiceGrpcKt.GrpcDemoServiceCoroutineImplBase() {

    override suspend fun helloWorld(request: HelloWorldRequest): HelloWorldResponse {
        return HelloWorldResponse.newBuilder()
            .setMessage("Hello ${request.name}")
            .build()
    }

    override fun helloWorldResStreaming(request: HelloWorldRequest): Flow<HelloWorldResponse> {
        return flow {
            request.name.forEach { name ->
                emit(helloWorldResponse {
                    message = "Hello $name!"
                })
                delay(1000)
            }
        }
    }

}
