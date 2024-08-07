package net.capellari.zap.bug_comments

import net.capellari.zap.bug_comments.dtos.BugCommentResponseDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
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

    @GetMapping("/{id}")
    fun getBugComment(
        @PathVariable("bugId") bugId: UUID,
        @PathVariable("id") id: UUID
    ): BugCommentResponseDto {
        return this.bugCommentService.getBug(bugId, id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Bug comment not found")
    }
}
