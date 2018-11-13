package me.aerben.service

import org.apache.log4j.Logger
import java.util.*

object Configuration {

    private val LOG = Logger.getLogger(Configuration::class.java)

    private val DEFAULT_BUCKET = "bonn-serverless-avatars-resized"

    private val DEFAULT_HEIGHTS = listOf(128, 256, 512)

    val targetHeights = loadHeightsFromEnv()

    val targetBucket = loadAvatarsBucketFromEnv()

    private fun loadAvatarsBucketFromEnv(): String {
        val env = System.getenv("TARGET_BUCKET")
        if (env == null) {
            LOG.debug("No resize avatar bucket configured. Using default $DEFAULT_BUCKET")
            return DEFAULT_BUCKET
        } else {
            LOG.debug("Using resize avatar bucket from env: $env")
            return env
        }
    }

    private fun loadHeightsFromEnv(): List<Int> {
        val env = System.getenv("TARGET_HEIGHTS")
        if (env == null) {
            LOG.debug("No target heights configured. Using default $DEFAULT_HEIGHTS")
            return DEFAULT_HEIGHTS
        } else {
            val heights = env.split(",").map {
                try {
                    Integer.parseInt(it)
                } catch (e: NumberFormatException) {
                    throw RuntimeException("Environment variable TARGET_HEIGHTS contained invalid value $it")
                }
            }
            LOG.debug("Using configured heights from env: $heights")
            return heights
        }
    }

}
