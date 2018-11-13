package me.aerben.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.S3Event
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord
import me.aerben.service.DeleteResizedImagesService

class DeleteAvatarHandler : RequestHandler<S3Event, String> {

    private val service = DeleteResizedImagesService()

    override fun handleRequest(input: S3Event, context: Context): String {
        // Read all incoming S3 records
        for (record in input.records) {

            // Determine source bucket and key of the object that has been created
            val sourceBucket = record.s3.bucket.name
            val sourceKey = record.s3.getObject().key

            // Delete all resized images from the target bucket
            service.deleteResizedAvatars(sourceBucket, sourceKey)
        }
        return "done"
    }
}
