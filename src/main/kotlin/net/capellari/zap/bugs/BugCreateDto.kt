package net.capellari.zap.bugs

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class BugCreateDto(
    @JsonProperty val title: String,
    @JsonProperty val date: Date,
    @JsonProperty val severity: Int,
    @JsonProperty val status: BugStatus = BugStatus.TODO,
    @JsonProperty val description: String = "",
)
