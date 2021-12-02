package com.github.blanexie.nexusj.support

import com.google.gson.*
import java.lang.reflect.Type
import java.text.DateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset


val gson: Gson = GsonBuilder()
    .setDateFormat(DateFormat.LONG)
    .registerTypeAdapter(LocalDateTime::class.java, JsonDeserializerImpl())
    .registerTypeAdapter(LocalDateTime::class.java, JsonSerializerImpl())
    .setPrettyPrinting()
    .create()!!

val zoneOffset = ZoneOffset.ofHours(8)!!

class JsonSerializerImpl : JsonSerializer<LocalDateTime> {
    override fun serialize(
        localDateTime: LocalDateTime,
        type: Type,
        jsonSerializationContext: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(localDateTime.toInstant(zoneOffset).toEpochMilli())
    }
}

class JsonDeserializerImpl : JsonDeserializer<LocalDateTime> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDateTime {
        val timestamp = json.asJsonPrimitive.asLong
        return LocalDateTime.ofEpochSecond(timestamp / 1000, 0, zoneOffset)
    }
}