package com.khalidtouch.chatme.database.converter

import androidx.room.TypeConverter
import com.khalidtouch.classifiadmin.model.MessageType
import com.khalidtouch.classifiadmin.model.asMessageType

class MessageTypeConverter {
    @TypeConverter
    fun typeToString(type: MessageType): String {
        return type.type
    }

    @TypeConverter
    fun stringToType(type: String): MessageType = type.asMessageType()
}
