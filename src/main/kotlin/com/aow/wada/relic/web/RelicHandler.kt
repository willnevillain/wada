package com.aow.wada.relic.web

import com.aow.wada.relic.model.RelicEntity
import com.aow.wada.relic.repository.RelicRepository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics.KILOMETERS
import org.springframework.data.geo.Point
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
class RelicHandler @Autowired constructor(
        private val relicRepository: RelicRepository
) {
    fun listRelics(request: ServerRequest): Mono<ServerResponse> {
        return ok()
                .contentType(APPLICATION_JSON)
                .body(relicRepository.findAll(), RelicEntity::class.java)
    }

    fun listRelicsNearLocation(request: ServerRequest): Mono<ServerResponse> {
        val location: Point = getPointFromPathVars(request)
        val distance: Distance = getDistanceFromPathVar(request)
        return ok()
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(
                        relicRepository.findByPointNear(location, distance),
                        RelicEntity::class.java))
    }

    fun listRelicsNearRelic(request: ServerRequest): Mono<ServerResponse> {
        val distance: Distance = getDistanceFromPathVar(request)
        return relicRepository.findById(request.pathVariable("id")).flatMap { message ->
            ok()
                    .contentType(APPLICATION_JSON)
                    .body(BodyInserters.fromPublisher(
                            relicRepository.findByPointNear(message.locationToPoint(), distance),
                            RelicEntity::class.java))
        }
                .switchIfEmpty(notFound().build())
    }

    fun getRelic(request: ServerRequest): Mono<ServerResponse> {
        return relicRepository.findById(request.pathVariable("id")).flatMap { message ->
            ok().contentType(APPLICATION_JSON).body(BodyInserters.fromObject(message))
        }
                .switchIfEmpty(notFound().build())
    }

    fun createRelic(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono<RelicEntity>().flatMap { message ->
            val id = UUID.randomUUID().toString()
            message.id = id
            message.point = message.locationToPoint()
            relicRepository.save(message).flatMap { created ->
                created(URI.create("${request.uri()}${created.id}")).build()
            }
        }
    }

    fun deleteRelic(request: ServerRequest): Mono<ServerResponse> {
        return relicRepository.findById(request.pathVariable("id")).flatMap { message ->
            noContent().build(relicRepository.deleteById(message.id))
        }
                .switchIfEmpty(notFound().build())
    }

    private fun getPointFromPathVars(request: ServerRequest): Point {
        return Point(request.pathVariable("lon").toDouble(), request.pathVariable("lat").toDouble())
    }

    private fun getDistanceFromPathVar(request: ServerRequest): Distance {
        return Distance(request.pathVariable("distance").toDouble(), KILOMETERS)
    }
}