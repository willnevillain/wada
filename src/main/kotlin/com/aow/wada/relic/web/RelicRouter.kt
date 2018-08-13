package com.aow.wada.relic.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class RelicRouter {
    @Bean
    fun routes(relicHandler: RelicHandler): RouterFunction<ServerResponse> = router {
        "/messages".nest {
            GET("/", relicHandler::listRelics)
            GET("/{id}", relicHandler::getRelic)
            GET("/{lat}/{lon}/{distance}", relicHandler::listRelicsNearLocation)
            GET("/{id}/{distance}", relicHandler::listRelicsNearRelic)
            POST("/", relicHandler::createRelic)
            DELETE("/{id}", relicHandler::deleteRelic)
        }
    }
}