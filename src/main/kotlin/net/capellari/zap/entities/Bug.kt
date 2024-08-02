package net.capellari.zap.entities

import jakarta.persistence.*
import java.util.*

@Entity
data class Bug (
    @Id @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID?,
    @Column(name = "title", nullable = false)
    val title: String,
    @Column(name = "date", nullable = false)
    val date: Date,
    @Column(name = "severity", nullable = false)
    val severity: Int,
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: BugStatus,
    @Column(name = "description", nullable = false)
    val description: String,
) {
    constructor() : this(null, "", Date(), 1, BugStatus.TODO, "")
}
