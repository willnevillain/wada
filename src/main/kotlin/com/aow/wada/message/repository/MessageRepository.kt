package com.aow.wada.message.repository

import com.aow.wada.message.model.Message
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface MessageRepository : ReactiveMongoRepository<Message, String> {
    fun findByPointNear(location: Point, distance: Distance): Flux<Message>
}