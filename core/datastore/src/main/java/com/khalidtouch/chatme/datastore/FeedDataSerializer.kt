package com.khalidtouch.chatme.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class FeedDataSerializer @Inject constructor() : Serializer<ComposeFeedDataProto> {
    override val defaultValue: ComposeFeedDataProto
        get() = ComposeFeedDataProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): ComposeFeedDataProto {
        return try {
            @Suppress("BlockingMethodInNonBlockingContext")
            ComposeFeedDataProto.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto", e)
        }
    }

    override suspend fun writeTo(t: ComposeFeedDataProto, output: OutputStream) {
        @Suppress("BlockingMethodInNonBlockingContext")
        t.writeTo(output)
    }
}