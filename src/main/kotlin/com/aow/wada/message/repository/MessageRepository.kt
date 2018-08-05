package com.aow.wada.message.repository

import com.aow.wada.message.model.Message
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MessageRepository : ReactiveMongoRepository<Message, String>