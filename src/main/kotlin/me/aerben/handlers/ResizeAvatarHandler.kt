package me.aerben.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.S3Event
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord
import me.aerben.service.ResizeAvatarService

class ResizeAvatarHandler : RequestHandler<S3Event, String> {

    private val service = ResizeAvatarService()

    override fun handleRequest(input: S3Event, context: Context): String {
        // Read all incoming S3 records
        for (record in input.records) {

            // Determine source bucket and key of the object that has been created
            val sourceBucket = record.s3.bucket.name
            val sourceKey = record.s3.getObject().key

            // Create the resized images and store them on S3. Return the list of keys
            service.resize(sourceBucket, sourceKey)
        }
        return "done"
    }

}
