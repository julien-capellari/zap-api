package net.capellari.zap.bugcomments.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import net.capellari.zap.bugcomments.BugComment
import java.util.Date
import java.util.UUID

data class BugCommentResponseDto(
    @JsonProperty val bugId: UUID,
    @JsonProperty val id: UUID,
    @JsonProperty val date: Date,
    @JsonProperty val username: String,
    @JsonProperty val content: String,
) {
    constructor(bugComment: BugComment) :
        this(bugComment.bugId, bugComment.id!!, bugComment.date, bugComment.username, bugComment.content)
}
