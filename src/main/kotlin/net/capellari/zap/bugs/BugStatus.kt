package net.capellari.zap.bugs

import com.fasterxml.jackson.annotation.JsonValue

enum class BugStatus(
    @JsonValue val value: String,
) {
    TODO("TODO"),
    VALIDATED("VALIDATED"),
    DONE("DONE"),
    ABORTED("ABORTED"),
}
