package com.aow.wada.relic.model

import org.springframework.data.geo.Point

data class RelicDTO(val id: String, val text: String, val location: Point)