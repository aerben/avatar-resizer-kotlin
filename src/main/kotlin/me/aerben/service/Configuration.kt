package me.aerben.service

import org.apache.log4j.Logger
import java.util.*

object Configuration {

    private val LOG = Logger.getLogger(Configuration::class.java)

    private val DEFAULT_RESIZED_AVATARS_BUCKET = "bonn-serverless-avatars-resized"

    private val DEFAULT_HEIGHTS = Arrays.asList(128, 256, 512)

    val targetResizedHeights = loadHeightsFromEnv()

    val resizeAvatarsBucket = loadAvatarsBucketFromEnv()

    private fun loadAvatarsBucketFromEnv(): String {
        val env = System.getenv("RESIZED_AVATARS_BUCKET")
        if (env == null) {
            LOG.debug("No resize avatar bucket configured. Using default $DEFAULT_RESIZED_AVATARS_BUCKET")
            return DEFAULT_RESIZED_AVATARS_BUCKET
        } else {
            LOG.debug("Using resize avatar bucket from env: $env")
            return env
        }
    }

    private fun loadHeightsFromEnv(): List<Int> {
        val env = System.getenv("TARGET_RESIZED_HEIGHTS")
        if (env == null) {
            LOG.debug("No target heights configured. Using default $DEFAULT_HEIGHTS")
            return DEFAULT_HEIGHTS
        } else {
            val heights = ArrayList<Int>()
            for (part in env.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()) {
                try {
                    heights.add(Integer.parseInt(part))
                } catch (e: NumberFormatException) {
                    throw RuntimeException("Failed to initialize application: " +
                            "environment variable \$TARGET_RESIZED_HEIGHT contained invalid value " + part)
                }
            }
            LOG.debug("Using configured heights from env: $heights")
            return heights
        }
    }

}
