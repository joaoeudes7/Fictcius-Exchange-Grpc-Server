package org.mvnsearch.greeter

import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GreeterServiceTest {
//  lateinit var grpcChannel: ManagedChannel
//  lateinit var greeterService: GreeterService
//
//  @BeforeAll
//  fun setUp() {
//    this.grpcChannel = ManagedChannelBuilder.forAddress("localhost", 50051)
//      .usePlaintext()
//      .executor(Dispatchers.Default.asExecutor())
//      .build()
//    greeterService = GreeterServiceStub(this.grpcChannel)
//  }
//
//  @AfterAll
//  fun tearDown() {
//    this.grpcChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
//  }
//
//  @Test
//  fun testHello() = runBlocking {
//    val request = HelloRequest.newBuilder().setName("Jackie").build()
//    val response = async { greeterService.sayHello(request) }
//    println("Received: ${response.await().message}")
//  }
}
