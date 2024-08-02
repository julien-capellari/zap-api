package net.capellari.zap.bugs.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import net.capellari.zap.bugs.BugStatus
import java.util.Date

data class BugRequestDto(
    @field:Size(max = 200)
    @JsonProperty val title: String,
    @JsonProperty val date: Date,
    @field:Min(1) @field:Max(5)
    @JsonProperty val severity: Int,
    @JsonProperty val status: BugStatus = BugStatus.TODO,
    @JsonProperty val description: String = "",
)
