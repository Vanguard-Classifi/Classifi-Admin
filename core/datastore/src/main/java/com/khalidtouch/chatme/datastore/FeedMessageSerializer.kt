package com.khalidtouch.chatme.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class FeedMessageSerializer @Inject constructor() : Serializer<ComposeFeedMessageProto> {
    override val defaultValue: ComposeFeedMessageProto
        get() = ComposeFeedMessageProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): ComposeFeedMessageProto {
        return try {
            @Suppress("BlockingMethodInNonBlockingContext")
            ComposeFeedMessageProto.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto", e)
        }
    }

    override suspend fun writeTo(t: ComposeFeedMessageProto, output: OutputStream) {
        @Suppress("BlockingMethodInNonBlockingContext")
        t.writeTo(output)
    }
}