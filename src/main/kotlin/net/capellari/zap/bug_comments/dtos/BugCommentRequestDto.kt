package net.capellari.zap.bug_comments.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Size
import java.util.Date

data class BugCommentRequestDto(
    @JsonProperty val date: Date,
    @field:Size(max = 200)
    @JsonProperty val username: String,
    @JsonProperty val content: String,
)
