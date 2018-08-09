package com.aow.wada.message.web

import com.aow.wada.message.model.Message
import com.aow.wada.message.repository.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import java.net.URI

@Component
class MessageHandler @Autowired constructor(
        private val messageRepository: MessageRepository
) {
    fun listMessages(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(messageRepository.findAll(), Message::class.java)
    }

    fun getMessage(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(messageRepository.findById(request.pathVariable("id")), Message::class.java)
    }

    fun createMessage(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono<Message>().flatMap { message ->
            messageRepository.save(message).flatMap { created ->
                ServerResponse.created(URI.create("${request.path()}/${created.id}")).build() //todo
            }
        }
    }
}