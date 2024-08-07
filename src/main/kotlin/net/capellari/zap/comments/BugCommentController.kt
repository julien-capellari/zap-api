package net.capellari.zap.comments

import net.capellari.zap.comments.dtos.BugCommentResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/bugs/{bugId}/comments")
class BugCommentController(
    private val bugCommentService: BugCommentService
) {
    @GetMapping
    fun listBugComments(
        @PathVariable("bugId") bugId: UUID
    ): List<BugCommentResponseDto> {
        return bugCommentService.listBugComments(bugId)
    }
}
