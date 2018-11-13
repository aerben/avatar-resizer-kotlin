package me.aerben.service

import com.amazonaws.services.s3.AmazonS3ClientBuilder
import me.aerben.service.Configuration.targetBucket
import org.apache.log4j.Logger

class DeleteResizedImagesService {
    private val s3 = AmazonS3ClientBuilder.defaultClient()

    fun deleteResizedAvatars(sourceBucket: String, sourceKey: String) {

        // Determine the prefix of the resized images in the target bucket
        val prefix = "$sourceBucket/$sourceKey"
        LOG.info("Looking for objects with prefix $prefix in bucket $targetBucket")

        // List all objects under the prefix and delete each object that matches
        s3.listObjects(targetBucket, prefix).objectSummaries.forEach { summary ->
            LOG.info("Deleting object ${summary.key} in bucket $targetBucket")
            s3.deleteObject(targetBucket, summary.key)
        }

        LOG.info("Finished deleting objects with prefix $prefix in bucket $targetBucket")
    }

    companion object {
        private val LOG = Logger.getLogger(DeleteResizedImagesService::class.java)
    }
}
