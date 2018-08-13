package com.aow.wada.relic.model


import org.springframework.data.annotation.Id
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "messages")
data class RelicEntity(val text: String, val location: Point) {
    @Id
    var id: String = "NO_ID"
}