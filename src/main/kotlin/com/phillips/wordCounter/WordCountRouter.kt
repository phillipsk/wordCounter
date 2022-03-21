package com.phillips.wordCounter

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.kafka.sender.KafkaSender

@Configuration
class WordCountRouter(
    private val commandGateway: KafkaSender<String, WordEntity>,
//    private val eventStore: EventStore
) {

    @Bean("router")
    fun route(handler: WordEntityHandler): RouterFunction<ServerResponse> =
        router {
            accept(MediaType.APPLICATION_JSON)
                .nest {
                    POST(wordEntityTopic, handler::create)
                }
        }
}