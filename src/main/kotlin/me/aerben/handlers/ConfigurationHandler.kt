package me.aerben.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import me.aerben.handlers.ConfigurationHandler.ConfigurationResponse
import me.aerben.service.Configuration

class ConfigurationHandler : RequestHandler<Object, ConfigurationResponse> {

    override fun handleRequest(input: Object, context: Context): ConfigurationResponse {
        return ConfigurationResponse(Configuration.targetHeights, Configuration.targetBucket)
    }

    data class ConfigurationResponse(val targetHeights: List<Int>, val targetBucket: String)
}
