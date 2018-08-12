package com.aow.wada.message.model



import org.springframework.data.annotation.Id
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "messages")
data class Message(val text: String, val location: Map<String, Double>) {
    @Id var id: String = "NO_ID"
    var point: Point = Point(0.0, 0.0)

    fun locationToPoint(): Point {
        val latitude = location["latitude"] ?: 0.0
        val longitude = location["longitude"] ?: 0.0
        return Point(longitude, latitude)
    }
}


/** ATTENTION please do the following:
 * 3. Manually test distance queries
 * 4. Write unit tests and a functional test */