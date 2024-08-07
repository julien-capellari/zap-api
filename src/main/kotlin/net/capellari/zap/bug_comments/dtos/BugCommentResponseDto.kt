package net.capellari.zap.bug_comments.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import net.capellari.zap.bug_comments.BugComment
import java.util.Date
import java.util.UUID

data class BugCommentResponseDto(
    @JsonProperty val id: UUID,
    @JsonProperty val bugId: UUID,
    @JsonProperty val date: Date,
    @JsonProperty val username: String,
    @JsonProperty val content: String,
) {
    constructor(bugComment: BugComment)
            : this(bugComment.id!!, bugComment.bugId, bugComment.date, bugComment.username, bugComment.content)
}
