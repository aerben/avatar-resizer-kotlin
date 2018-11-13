package me.aerben.service

import com.amazonaws.services.s3.AmazonS3ClientBuilder
import me.aerben.service.Configuration.resizeAvatarsBucket
import org.apache.log4j.Logger

class DeleteResizedImagesService {
    private val s3 = AmazonS3ClientBuilder.defaultClient()

    fun deleteResizedAvatars(sourceBucket: String, sourceKey: String) {

        // Determine the prefix of the resized images in the target bucket
        val prefix = "$sourceBucket/$sourceKey"
        LOG.info("Looking for objects with prefix $prefix in bucket $resizeAvatarsBucket")

        // List all objects under the prefix and delete each object that matches
        s3.listObjects(resizeAvatarsBucket, prefix).objectSummaries.forEach { summary ->
            LOG.info("Deleting object ${summary.key} in bucket $resizeAvatarsBucket")
            s3.deleteObject(resizeAvatarsBucket, summary.key)
        }

        LOG.info("Finished deleting objects with prefix $prefix in bucket $resizeAvatarsBucket")
    }

    companion object {
        private val LOG = Logger.getLogger(DeleteResizedImagesService::class.java)
    }
}
