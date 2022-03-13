package com.phillips.wordCounter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

@SpringBootApplication
class WordCounterApplication

fun main(args: Array<String>) {
    runApplication<WordCounterApplication>(*args)
}

@RestController // TODO: this was edited
//@RequestMapping("/wordCount")
class WordCountController(
    private val service: MessageService,
//    private val suspendRepo: SuspendRepository
) {

    @PostMapping("/wordCount")
    @ResponseStatus(HttpStatus.CREATED)
    fun postWord(@RequestBody wordEntity: WordEntity): ResponseEntity<WordResponse> {
        val wordRecord = cleanRequest(wordEntity)
                updateDb(wordRecord)
//        updateDbSuspend(wordRecord)
        val dbCount = getWordCountSum()
        return ResponseEntity(WordResponse(dbCount), HttpStatus.OK)
    }

    private fun cleanRequest(wordEntity: WordEntity): WordRecord {
        val totalWordCount: Int = wordEntity.message.split(" ".toRegex()).count()
        return WordRecord(wordEntity.id, totalWordCount)
    }

    private fun updateDb(wordRecord: WordRecord) {
        val dbMessages = service.findMessages()
        service.post(wordRecord)
    }

    private suspend fun updateDbSuspend(wordRecord: WordRecord) {
//        suspendRepo.save(wordRecord)
    }

    private fun getWordCountSum(): Int {
        return service.findWordCountSum()
    }
}

data class WordEntity(val id: Int, var message: String)
data class WordResponse(val count: Int)

@Table("messages")
data class WordRecord(@Id val id: Int, val word_count: Int)

interface MessageRepository : CrudRepository<WordRecord, Int> {

    @Query("select * from messages")
    fun findMessages(): List<WordRecord>

    @Query("sum word_count from messages")
    fun findMessagesSum(): Int

/*    @Query("select distinct id from messages where id ")
    fun findMessagesMaxID(): Int*/
}

@Service
class MessageService(val db: MessageRepository) {

    fun findMessages() = db.findMessages()
    fun findWordCountSum() = db.findMessagesSum()

    fun post(message: WordRecord) {
        db.save(message)
    }
}

//interface SuspendRepository : CoroutineCrudRepository<WordRecord, Int> {
//    @Query("SELECT * FROM messages")
//    suspend fun getAll(): WordRecord
//    //    suspend fun getAll(): Flow<WordRecord>
//
//    @Query("sum word_count from messages")
//    suspend fun findMessagesSum(): Int
//}