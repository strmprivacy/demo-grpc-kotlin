package io.strmprivacy.demo.grpc.client

import io.grpc.ManagedChannelBuilder
import io.strmprivacy.demo.grpc.v1.GrpcDemoServiceGrpcKt
import io.strmprivacy.demo.grpc.v1.helloWorldRequest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val channel = ManagedChannelBuilder
        .forAddress("localhost", 50051)
        .usePlaintext()
        .build()

    val client: GrpcDemoServiceGrpcKt.GrpcDemoServiceCoroutineStub = GrpcDemoServiceGrpcKt.GrpcDemoServiceCoroutineStub(channel)

    println("-".repeat(20))
    val helloWorldResponse = client.helloWorld(helloWorldRequest {
        name = "Jan-Kees van Andel"
    })
    println("Unary RPC response: ${helloWorldResponse.message}")

    println("-".repeat(20))
    val helloWorldResStreamingResponse = client.helloWorldResStreaming(helloWorldRequest {
        name = "Robin Trietsch"
    })
    helloWorldResStreamingResponse.collect { response ->
        println("Res Streaming RPC response: " + response.message)
    }

}
