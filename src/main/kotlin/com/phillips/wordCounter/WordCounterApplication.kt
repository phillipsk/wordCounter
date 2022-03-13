package com.phillips.wordCounter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class WordCounterApplication

fun main(args: Array<String>) {
    runApplication<WordCounterApplication>(*args)
}

@RestController
class WordCountController(
    private val service: MessageService,
) {

    @PostMapping("/wordCount")
    @ResponseStatus(HttpStatus.CREATED)
    fun postWord(@RequestBody wordEntity: WordEntity): ResponseEntity<WordResponse>? {
        val records = service.getById(wordEntity.id)
        return if (records.isEmpty()) {
            val wordRecord = cleanRequest(wordEntity)
            updateDb(wordRecord)
            val dbCount = getWordCountSum()
            ResponseEntity(WordResponse(dbCount), HttpStatus.OK)
        } else {
            null
        }
    }

    private fun cleanRequest(wordEntity: WordEntity): WordRecord {
        val totalWordCount: Int = wordEntity.message.split(" ".toRegex()).count()
        return WordRecord(null, wordEntity.id, totalWordCount)
    }

    private fun updateDb(wordRecord: WordRecord) {
        service.post(wordRecord)
    }

    private fun getWordCountSum(): Int {
        return service.findWordCountSum()
    }
}

data class WordEntity(val id: Int, var message: String)
data class WordResponse(val count: Int)

@Table("MESSAGES")
data class WordRecord(@Id val id: String?, val word_entity_id: Int, val word_count: Int)

interface MessageRepository : CrudRepository<WordRecord, String> {

    @Query("select * from messages")
    fun findMessages(): List<WordRecord>

    @Query("select * from messages m where m.word_entity_id = :entityId")
    fun findMessagesById(@Param("entityId") entityId: Int): List<WordRecord>

    @Query("select sum(m.word_count) from messages m")
    fun findMessagesSum(): Int
}

@Service
class MessageService(val db: MessageRepository) {

    fun findMessages() = db.findMessages()
    fun findWordCountSum() = db.findMessagesSum()
    fun post(message: WordRecord) = db.save(message)
    fun getById(entityId: Int) = db.findMessagesById(entityId)
}