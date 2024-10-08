package net.capellari.zap.bugs

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import net.capellari.zap.bugs.dtos.BugFiltersDto
import net.capellari.zap.bugs.dtos.BugRequestDto
import java.util.Date
import java.util.UUID

@Entity
data class Bug(
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id @Column val id: UUID?,
    @Column(length = 200) var title: String,
    @Column var date: Date,
    @Column var severity: Int,
    @Enumerated(EnumType.STRING)
    @Column var status: BugStatus,
    @Column var description: String,
) {
    constructor() : this(null, "", Date(), 1, BugStatus.TODO, "")
    constructor(data: BugRequestDto) : this(null, data.title, data.date, data.severity, data.status, data.description)
    constructor(filters: BugFiltersDto) : this(null, "", Date(), filters.severity ?: 1, filters.status ?: BugStatus.TODO, "")
}
