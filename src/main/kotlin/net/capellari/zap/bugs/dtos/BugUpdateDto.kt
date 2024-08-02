package net.capellari.zap.bugs.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
import net.capellari.zap.bugs.BugStatus
import java.util.*

data class BugUpdateDto(
    @field:Size(max = 200)
    @JsonProperty val title: String?,
    @JsonProperty val date: Date?,
    @field:Min(1) @field:Max(5)
    @JsonProperty val severity: Int?,
    @JsonProperty val status: BugStatus?,
    @JsonProperty val description: String?,
)
