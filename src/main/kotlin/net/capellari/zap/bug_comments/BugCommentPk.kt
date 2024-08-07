package net.capellari.zap.bug_comments

import java.util.UUID

data class BugCommentPk(
    val id: UUID?,
    val bugId: UUID,
) {
    constructor() : this(null, UUID.randomUUID())
}
