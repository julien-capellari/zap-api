package net.capellari.zap.bugs

import jakarta.persistence.*
import net.capellari.zap.bugs.dtos.BugCreateDto
import java.util.*

@Entity
data class Bug (
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column val id: UUID?,
    @Column(length = 200) val title: String,
    @Column val date: Date,
    @Column val severity: Int,
    @Enumerated(EnumType.STRING)
    @Column val status: BugStatus,
    @Column val description: String,
) {
    constructor() : this(null, "", Date(), 1, BugStatus.TODO, "")
    constructor(data: BugCreateDto) : this(null, data.title, data.date, data.severity, data.status, data.description)
}
