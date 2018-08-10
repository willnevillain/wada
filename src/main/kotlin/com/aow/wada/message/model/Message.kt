package com.aow.wada.message.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "messages")
data class Message(val text: String) {
    @Id var id: String = "NO_ID"
}