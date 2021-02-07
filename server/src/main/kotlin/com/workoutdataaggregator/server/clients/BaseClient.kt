package com.workoutdataaggregator.server.clients

import org.slf4j.LoggerFactory

abstract class BaseClient {
    protected val logger = LoggerFactory.getLogger(javaClass)
}