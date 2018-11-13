package me.aerben.service

import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.ObjectMetadata
import net.coobird.thumbnailator.Thumbnails
import org.apache.commons.io.FilenameUtils.getExtension
import org.apache.commons.io.IOUtils
import org.apache.log4j.Logger

import java.io.*

class ResizeAvatarService {
    private val s3 = AmazonS3ClientBuilder.defaultClient()

    fun resize(sourceBucket: String, sourceKey: String) {
        LOG.info("Resizing image $sourceKey from $sourceBucket")

        // Load the source avatar image.
        // Loading the data as InputStream would make it impossible to reuse it for multiple heights
        val avatarData = loadAvatarImage(sourceBucket, sourceKey)

        for (height in Configuration.targetHeights) {

            val targetKey = "$sourceBucket/$sourceKey/$height.${getExtension(sourceKey)}"

            val resizedImageData = resizeImageToHeight(avatarData, height)

            s3.putObject(
                    Configuration.targetBucket,
                    targetKey,
                    ByteArrayInputStream(resizedImageData),
                    buildMetadata(resizedImageData)
            )

            LOG.info("Resized image to $targetKey")
        }
        LOG.info("Finished resizing image $sourceKey from $sourceBucket")
    }

    private fun buildMetadata(resizedImageData: ByteArray): ObjectMetadata {
        val metadata = ObjectMetadata()
        metadata.contentLength = resizedImageData.size.toLong()
        return metadata
    }

    private fun resizeImageToHeight(imageData: ByteArray, height: Int): ByteArray {
        val baos = ByteArrayOutputStream()
        Thumbnails.of(ByteArrayInputStream(imageData))
                .height(height)
                .toOutputStream(baos)
        return baos.toByteArray()
    }

    private fun loadAvatarImage(sourceBucket: String, sourceKey: String): ByteArray {
        val obj = s3.getObject(sourceBucket, sourceKey)
        val baos = ByteArrayOutputStream()
        IOUtils.copy(obj.objectContent, baos)
        return baos.toByteArray()
    }

    companion object {
        private val LOG = Logger.getLogger(ResizeAvatarService::class.java)
    }
}
