package com.khalidtouch.classifiadmin.model


enum class MessageType(val type: String) {
    TextMessage("Text Message"),
    ImageMessage("Image Message"),
    AudioMessage("Audio Message"),
    VideoMessage("Video Message"),
    FileMessage("File Message"),
    Unknown("")
}

fun String?.asMessageType() = when (this) {
    null -> MessageType.Unknown
    else -> MessageType.values()
        .firstOrNull { type -> type.type == this }
        ?: MessageType.Unknown
}