package net.capellari.zap.bug_comments

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BugCommentRepository : JpaRepository<BugComment, BugCommentPk> {
    @Query("SELECT bc FROM BugComment bc WHERE bc.bugId = :bugId")
    fun findByBugId(bugId: UUID): List<BugComment>
}
