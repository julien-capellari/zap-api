package net.capellari.zap.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import net.capellari.zap.entities.Bug
import net.capellari.zap.entities.BugStatus
import java.util.*

data class BugResponse(
    @JsonProperty val id: UUID,
    @JsonProperty val title: String,
    @JsonProperty val date: Date,
    @JsonProperty val severity: Int,
    @JsonProperty val status: BugStatus,
    @JsonProperty val description: String,
) {
    constructor(bug: Bug) : this(bug.id!!, bug.title, bug.date, bug.severity, bug.status, bug.description)
}
