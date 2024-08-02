package net.capellari.zap.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class BugResponse(
    @JsonProperty("id")
    val id: UUID
)
