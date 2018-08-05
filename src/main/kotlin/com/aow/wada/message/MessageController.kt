package com.aow.wada.message

import com.aow.wada.message.model.Message
import com.aow.wada.message.repository.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class MessageController @Autowired constructor(
        private val messageRepository: MessageRepository
) {
    @GetMapping("/messages")
    fun getAllMessages(): Flux<Message> {
        return messageRepository.findAll()
    }

    @GetMapping("/messages/{id}")
    fun getMessageById(@PathVariable id: String): Mono<Message> {
        return messageRepository.findById(id)
    }

    @PostMapping("/messages")
    fun createMessage(@RequestBody message: Message): Mono<Message> {
        return messageRepository.save(message)
    }
}