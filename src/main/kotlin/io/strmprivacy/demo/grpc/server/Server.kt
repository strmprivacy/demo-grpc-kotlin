package io.strmprivacy.demo.grpc.server

import io.grpc.ServerBuilder
import io.grpc.protobuf.services.ProtoReflectionService
import io.strmprivacy.demo.grpc.v1.GrpcDemoServiceGrpcKt
import io.strmprivacy.demo.grpc.v1.HelloWorldRequest
import io.strmprivacy.demo.grpc.v1.HelloWorldResponse
import io.strmprivacy.demo.grpc.v1.helloWorldResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun main() {
    val server = ServerBuilder
        .forPort(50051)
        .addService(GrpcDemoService)
        .addService(ProtoReflectionService.newInstance())
        .build()
        .start()

    server.awaitTermination()
}

object GrpcDemoService : GrpcDemoServiceGrpcKt.GrpcDemoServiceCoroutineImplBase() {

    override suspend fun helloWorld(request: HelloWorldRequest): HelloWorldResponse {
        return sayHelloResponse(request.name)
    }

    override fun helloWorldResStreaming(request: HelloWorldRequest): Flow<HelloWorldResponse> {
        return flow {
            request.name.forEach { name ->
                emit(sayHelloResponse(name.toString()))
                delay(1000)
            }
        }
    }

    private fun sayHelloResponse(name: String) = helloWorldResponse {
        message = sayHello(name)
    }

    private fun sayHello(name: String) = "Hello $name!"
}
