package net.capellari.zap.bugcomments

import java.util.UUID

data class BugCommentPk(
    val bugId: UUID,
    val id: UUID?,
) {
    constructor() : this(UUID.randomUUID(), null)
}
