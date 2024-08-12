package net.capellari.zap

import com.fasterxml.jackson.annotation.JsonValue
import org.springframework.data.domain.Sort.Direction

enum class Order(
    @JsonValue val value: String,
) {
    ASC("ASC"),
    DESC("DESC");

    fun toDirection() = Direction.fromString(value)
}
