package net.capellari.zap.comments

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import java.util.Date
import java.util.UUID

@Entity
@IdClass(BugComment::class)
data class BugComment(
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id @Column val id: UUID?,
    @Id @Column val bugId: UUID,
    @Column val date: Date,
    @Column val username: String,
    @Column val content: String,
) {
    constructor() : this(null, UUID.randomUUID(), Date(), "", "")
}
