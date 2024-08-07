package net.capellari.zap.comments

import net.capellari.zap.comments.dtos.BugCommentResponseDto
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
}
