package io.strmprivacy.demo.grpc.server

import io.grpc.ServerBuilder
import io.grpc.protobuf.services.ProtoReflectionService
import io.strmprivacy.demo.grpc.v1.GrpcDemoServiceGrpcKt
import io.strmprivacy.demo.grpc.v1.HelloWorldRequest
import io.strmprivacy.demo.grpc.v1.HelloWorldResponse
import io.strmprivacy.demo.grpc.v1.helloWorldResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

fun main() {
    val server = ServerBuilder
        .forPort(50051)
        .addService(Server)
        .addService(ProtoReflectionService.newInstance())
        .build()
        .start()

    server.awaitTermination()
}

object Server : GrpcDemoServiceGrpcKt.GrpcDemoServiceCoroutineImplBase() {

    private val names = mutableListOf<String>()

    override suspend fun helloWorld(request: HelloWorldRequest): HelloWorldResponse {
        names += request.name
        return sayHelloResponse(request.name)
    }

    override suspend fun helloWorldReqStreaming(requests: Flow<HelloWorldRequest>): HelloWorldResponse {
        requests.collect { request ->
            names += request.name
        }
        return sayHelloResponse(names.lastOrNull() ?: "unknown")
    }

    override fun helloWorldResStreaming(request: HelloWorldRequest): Flow<HelloWorldResponse> {
        return flow {
            names += request.name

            names.forEach { name ->
                emit(sayHelloResponse(name))
                delay(1000)
            }
        }
    }

    override fun helloWorldBiStreaming(requests: Flow<HelloWorldRequest>): Flow<HelloWorldResponse> {
        return flow {
            requests.collect { request ->
                names += request.name
            }

            names.forEach { name ->
                emit(sayHelloResponse(name))
                delay(1000)
            }

        }
    }

    private fun sayHelloResponse(name: String) = helloWorldResponse {
        message = sayHello(name)
    }

    private fun sayHello(name: String) = "Hello $name!"
}
