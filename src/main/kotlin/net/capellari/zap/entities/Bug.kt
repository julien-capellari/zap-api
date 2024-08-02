package net.capellari.zap.entities

import jakarta.persistence.*
import java.util.*

@Entity
data class Bug (
    @Id @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID?,
    @Column(name = "title", nullable = false)
    var title: String,
    @Column(name = "date", nullable = false)
    var date: Date,
    @Column(name = "severity", nullable = false)
    var severity: Int,
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: BugStatus,
    @Column(name = "description", nullable = false)
    var description: String,
) {
    constructor() : this(null, "", Date(), 1, BugStatus.TODO, "")
}
