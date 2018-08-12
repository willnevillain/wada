package com.aow.wada.message.web

import com.aow.wada.message.model.Message
import com.aow.wada.message.repository.MessageRepository
import com.mongodb.client.model.Indexes
import org.bson.BSON
import org.bson.BSONObject
import org.bson.conversions.Bson

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics.KILOMETERS
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import java.net.URI
import java.util.*


@Component
class MessageHandler @Autowired constructor(
        private val messageRepository: MessageRepository,
        private val mongoTemplate: MongoTemplate
) {
    fun listMessages(request: ServerRequest): Mono<ServerResponse> {
        return ok()
                .contentType(APPLICATION_JSON)
                .body(messageRepository.findAll(), Message::class.java)
    }

    fun getMessagesNearLocationWithMaxDistance(request: ServerRequest): Mono<ServerResponse> {
        val location = getPointFromPathVars(request)
        val distance = getDistanceFromPathVar(request)
        return ok()
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(
                        messageRepository.findByPointNear(location, distance),
                        Message::class.java))
    }

    fun getMessagesNearMessageWithMaxDistance(request: ServerRequest): Mono<ServerResponse> {
        return noContent().build()
//        val distance = getDistanceFromPathVar(request)
//        return messageRepository.findById(request.pathVariable("id")).flatMap { message ->
//            ok()
//                    .contentType(APPLICATION_JSON)
//                    .body(BodyInserters.fromPublisher(
//                            messageRepository.findByLocationNear(message.locationToPoint(), distance),
//                            Message::class.java))
//        }
//                .switchIfEmpty(notFound().build())
    }

    fun getMessage(request: ServerRequest): Mono<ServerResponse> {
        return messageRepository.findById(request.pathVariable("id")).flatMap { message ->
            ok().contentType(APPLICATION_JSON).body(BodyInserters.fromObject(message))
        }
                .switchIfEmpty(notFound().build())
    }

    fun createMessage(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono<Message>().flatMap { message ->
            val id = UUID.randomUUID().toString()
            message.id = id
            message.point = message.locationToPoint()
            messageRepository.save(message).flatMap { created ->
                created(URI.create("${request.uri()}${created.id}")).build()
            }
        }
    }

    fun updateMessage(request: ServerRequest): Mono<ServerResponse> {
        return noContent().build()
    }

    fun deleteMessage(request: ServerRequest): Mono<ServerResponse> {
        return messageRepository.findById(request.pathVariable("id")).flatMap { message ->
            noContent().build(messageRepository.deleteById(message.id))
        }
                .switchIfEmpty(notFound().build())
    }

    fun getPointFromPathVars(request: ServerRequest): Point {
        return Point(request.pathVariable("lon").toDouble(), request.pathVariable("lat").toDouble())
    }

    fun getDistanceFromPathVar(request: ServerRequest): Distance {
        return Distance(request.pathVariable("distance").toDouble(), KILOMETERS)
    }
}