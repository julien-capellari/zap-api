package net.capellari.zap.bugs.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import net.capellari.zap.bugs.Bug
import net.capellari.zap.bugs.BugStatus
import java.util.Date
import java.util.UUID

data class BugResponseDto(
    @JsonProperty val id: UUID,
    @JsonProperty val title: String,
    @JsonProperty val date: Date,
    @JsonProperty val severity: Int,
    @JsonProperty val status: BugStatus,
    @JsonProperty val description: String,
) {
    constructor(bug: Bug) : this(bug.id!!, bug.title, bug.date, bug.severity, bug.status, bug.description)
}
