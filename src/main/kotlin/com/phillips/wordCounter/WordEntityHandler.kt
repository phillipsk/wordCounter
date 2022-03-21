package com.phillips.wordCounter

import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromPublisher
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Flux

import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import reactor.core.scheduler.Schedulers
import reactor.kafka.sender.SenderRecord
import reactor.kafka.sender.SenderResult
import java.time.Instant
import javax.annotation.PostConstruct

@Component
class WordEntityHandler(
    private val messageRepository: MessageRepository,
    private val messageService: MessageService, // TODO: consolidate
    private val kafkaConfig: KafkaConfig,
) {


    fun create(request: ServerRequest): Mono<ServerResponse> {
        var wordEntity = request.bodyToMono(WordEntity::class.java)
//        wordEntity = cleanRequest(wordEntity)
        val mono = request.bodyToMono(WordEntity::class.java)
        return ok()
            .json()
            .body(
                fromPublisher(
                    messageRepository.save(wordEntity)
                    , WordRecord::class.java
//                    wordEntity.flatMap { request }
/*                    wordEntity.flatMap {
                        messageService.post(it)
                    }, WordRecord::class.java*/
                )
            )
    }


    @Bean
    fun routes() = router {
        "/".nest {
            contentType(MediaType.APPLICATION_JSON)
            POST("/wordCount") {
                ok().body(
                    it.bodyToMono(WordEntity::class.java)
                        .map { ProducerRecord<String, WordEntity>(wordEntityTopic, it) }
                        .map { SenderRecord.create<String, WordEntity, Void>(it, null) }
                        .map { it.toMono() }
                        //.map { throw RuntimeException("oh...") } // uncomment this and comment next line to test failure...
                        .map {
                            kafkaConfig.kafkaWordEntitySender()
                                .send(it)
                                .subscribe { println("command {} sent.") }
                        }
                        .then("Command sent.".toMono())
                        .subscribeOn(Schedulers.elastic()))
            }
        }
    }

}

@Component
class reciever{

    @PostConstruct
    fun subscribe() {
        KafkaConfig().kafkaWordEntityReceiver()
            .doOnConsumer { cleanRequest(it.) }
            .receive()
            .doOnNext {
                println("received: ${it.value()} at ${Instant.ofEpochMilli(it.timestamp())}")
            }
            .map { it.value().message to it.timestamp() }
            .map { WordEntity(it.first,it.second).cleanRequest(it) }
            .map { ProducerRecord<String, WordEntity>(eventTopic, it) }
            .map { SenderRecord.create<String, WordEntity, Void>(it, null) }
            .map { it.toMono() } //.map { Flux.just(Flux.from(it)) }
            .map {
                //val flux = eventGateway.sendTransactionally(Flux.from(it)) ; //flux.subscribe()
                val send: Flux<SenderResult<Void>> = eventGateway.send(it) ; send.subscribe()
            }
            .subscribeOn(Schedulers.elastic())
            .subscribe { println("event fired.") }
    }

    fun cleanRequest(wordEntity: WordEntity): WordRecord {
        val totalWordCount: Int = wordEntity.message.split(" ".toRegex()).count()
        return WordRecord(null, wordEntity.id, totalWordCount)
    }
}


fun WordEntity.cleanRequest(wordEntity: WordEntity): WordRecord {
    val totalWordCount: Int = wordEntity.message.split(" ".toRegex()).count()
    return WordRecord(null, wordEntity.id, totalWordCount)
}
