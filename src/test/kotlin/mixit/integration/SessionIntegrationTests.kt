package mixit.integration

import mixit.model.Session
import mixit.support.getJson
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.reactive.function.client.bodyToFlux
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SessionIntegrationTests {

    @LocalServerPort
    lateinit var port: Integer

    val webClient = WebClient.builder(ReactorClientHttpConnector()).build()

    fun findDanNorthSession() {
        StepVerifier.create(webClient
                .getJson("http://localhost:8080/api/session/2421")
                .flatMap { r -> r.bodyToFlux<Session>() })
                .consumeNextWith {
                    assertEquals("Selling BDD to the Business", it.title)
                    assertEquals("NORTH", it.speakers.iterator().next().lastname)
                }
                .verifyComplete()
    }

     fun findMixit12Sessions() {
        StepVerifier.create(webClient
                .getJson("http://localhost:8080/api/mixit12/session/")
                .flatMap { it.bodyToFlux<Session>() })
                .expectNextCount(27)
                .verifyComplete()

    }
}