package net.capellari.zap.bugs.dtos

import net.capellari.zap.bugs.BugStatus

data class BugFiltersDto(
    val status: BugStatus?,
    val severity: Int?,
)
