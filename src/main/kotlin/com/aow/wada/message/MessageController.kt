package com.aow.wada.message

import com.aow.wada.message.model.Message
import com.aow.wada.message.repository.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
class MessageController @Autowired constructor(
        private val messageRepository: MessageRepository
) {
    @GetMapping("/messages")
    fun getAllMessages(): Flux<Message> {
        return messageRepository.findAll()
    }

    @PostMapping("/messages")
    fun createMessage(@RequestBody message: Message): Mono<Message> {
        return messageRepository.save(message)
    }
}