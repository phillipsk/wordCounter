package com.phillips.wordCounter

import com.fasterxml.jackson.databind.JsonDeserializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderOptions

@EnableKafka
@Configuration
class KafkaConfig() {

    private val topicList = listOf(wordEntityTopic)
    private val configMap = mapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java,
    )

    @Bean
    fun kafkaWordEntitySender(): KafkaSender<String, WordEntity> {
        return KafkaSender.create(SenderOptions.create(configMap))
    }


    @Bean
    fun kafkaWordEntityReceiver(): KafkaReceiver<String, WordEntity> {
        return KafkaReceiver.create(ReceiverOptions.create<String, WordEntity>(configMap).subscription(topicList))
    }

    @Bean
    fun kafkaWordRecordSender(): KafkaSender<String, WordRecord> {
        return KafkaSender.create(SenderOptions.create(configMap))
    }


    @Bean
    fun kafkaWordRecordReceiver(): KafkaReceiver<String, WordRecord> {
        return KafkaReceiver.create(ReceiverOptions.create<String, WordRecord>(configMap).subscription(topicList))
    }

}