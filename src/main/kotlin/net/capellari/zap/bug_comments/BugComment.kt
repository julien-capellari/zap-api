package net.capellari.zap.bug_comments

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import net.capellari.zap.bug_comments.dtos.BugCommentRequestDto
import java.util.Date
import java.util.UUID

@Entity
@IdClass(BugCommentPk::class)
data class BugComment(
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id @Column val id: UUID?,
    @Id @Column val bugId: UUID,
    @Column var date: Date,
    @Column var username: String,
    @Column var content: String,
) {
    constructor() : this(null, UUID.randomUUID(), Date(), "", "")
    constructor(bugId: UUID, data: BugCommentRequestDto) : this(null, bugId, data.date, data.username, data.content)
}
