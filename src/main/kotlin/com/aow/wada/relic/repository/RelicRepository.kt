package com.aow.wada.relic.repository

import com.aow.wada.relic.model.RelicEntity
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface RelicRepository : ReactiveMongoRepository<RelicEntity, String> {
    fun findByPointNear(location: Point, distance: Distance): Flux<RelicEntity>
}