package com.phillips.wordCounter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class WordCounterApplication

fun main(args: Array<String>) {
	runApplication<WordCounterApplication>(*args)
}

@RestController
@RequestMapping("/wordCount")
class WordCountController {

    private var wordMap = mutableMapOf<Int, WordEntity>()

    @PostMapping
    fun postWord(@RequestBody wordEntity: WordEntity): ResponseEntity<WordResponse> {
        updateWordMap(wordEntity)
        val totalWordCount: Int = wordMap.values.flatMap { it.message.split(" ".toRegex()) }.count()
        return ResponseEntity(WordResponse(totalWordCount), HttpStatus.OK)
    }

    private fun updateWordMap(wordEntity: WordEntity) {
        val mapCountNext: Int = if (wordMap.iterator().hasNext()) {
            wordMap.maxOf { it.key }.plus(1)
        } else {
            1
        }
        if (!wordMap.values.any { it.id == wordEntity.id }) {
            wordMap[mapCountNext] = wordEntity
        }
    }
}

data class WordEntity(val id: Int, var message: String)
data class WordResponse(val count: Int)