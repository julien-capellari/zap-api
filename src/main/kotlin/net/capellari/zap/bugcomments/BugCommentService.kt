package net.capellari.zap.bugcomments

import net.capellari.zap.bugcomments.dtos.BugCommentRequestDto
import net.capellari.zap.bugcomments.dtos.BugCommentResponseDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class BugCommentService(
    private val bugCommentRepository: BugCommentRepository
) {
    fun listBugComments(bugId: UUID): List<BugCommentResponseDto> {
        return bugCommentRepository.findByBugId(bugId)
            .map { BugCommentResponseDto(it) }
    }

    fun getBugComment(bugId: UUID, id: UUID): BugCommentResponseDto? {
        return bugCommentRepository.findByIdOrNull(BugCommentPk(bugId, id))
            ?.let { BugCommentResponseDto(it) }
    }

    fun createBugComment(bugId: UUID, data: BugCommentRequestDto): BugCommentResponseDto {
        return BugCommentResponseDto(bugCommentRepository.save(BugComment(bugId, data)))
    }

    fun updateBugComment(bugId: UUID, id: UUID, update: BugCommentRequestDto): BugCommentResponseDto? {
        return bugCommentRepository.findByIdOrNull(BugCommentPk(bugId, id))
            ?.apply {
                date = update.date
                username = update.username
                content = update.content
            }
            ?.let {
                BugCommentResponseDto(bugCommentRepository.save(it))
            }
    }

    fun deleteBugComment(bugId: UUID, id: UUID) {
        bugCommentRepository.deleteById(BugCommentPk(bugId, id))
    }
}
