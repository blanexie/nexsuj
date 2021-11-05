package com.github.blanexie.nexusj.support

import com.google.gson.*
import java.lang.reflect.Type
import java.text.DateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


val gson: Gson = GsonBuilder()
    .setDateFormat(DateFormat.LONG)
    .registerTypeAdapter(LocalDateTime::class.java, JsonDeserializerImpl())
    .registerTypeAdapter(LocalDateTime::class.java, JsonSerializerImpl())
    .setPrettyPrinting()
    .create()!!


class JsonSerializerImpl : JsonSerializer<LocalDateTime> {
    override fun serialize(
        localDateTime: LocalDateTime,
        type: Type,
        jsonSerializationContext: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}

class JsonDeserializerImpl : JsonDeserializer<LocalDateTime> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDateTime {
        return LocalDateTime.parse(
            json.getAsJsonPrimitive().getAsString(),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );
    }
}