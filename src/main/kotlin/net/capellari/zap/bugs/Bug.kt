package net.capellari.zap.bugs

import jakarta.persistence.*
import java.util.*

@Entity
data class Bug (
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column val id: UUID?,
    @Column val title: String,
    @Column val date: Date,
    @Column val severity: Int,
    @Enumerated(EnumType.STRING)
    @Column val status: BugStatus,
    @Column val description: String,
) {
    constructor() : this(null, "", Date(), 1, BugStatus.TODO, "")
}
