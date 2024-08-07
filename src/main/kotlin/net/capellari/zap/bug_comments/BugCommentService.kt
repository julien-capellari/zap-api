package net.capellari.zap.bug_comments

import net.capellari.zap.bug_comments.dtos.BugCommentResponseDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class BugCommentService(
    private val bugCommentRepository: BugCommentRepository
) {
    fun listBugComments(bugId: UUID): List<BugCommentResponseDto> {
        return this.bugCommentRepository.findByBugId(bugId)
            .map { BugCommentResponseDto(it) }
    }

    fun getBug(bugId: UUID, id: UUID): BugCommentResponseDto? {
        return this.bugCommentRepository.findByIdOrNull(BugCommentPk(id, bugId))
            ?.let { BugCommentResponseDto(it) }
    }
}
