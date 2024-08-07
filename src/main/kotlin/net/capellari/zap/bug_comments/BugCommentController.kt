package net.capellari.zap.bug_comments

import jakarta.validation.Valid
import net.capellari.zap.bug_comments.dtos.BugCommentRequestDto
import net.capellari.zap.bug_comments.dtos.BugCommentResponseDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBugComment(
        @PathVariable("bugId") bugId: UUID,
        @RequestBody @Valid body: BugCommentRequestDto
    ): BugCommentResponseDto {
        return bugCommentService.createBugComment(bugId, body)
    }

    @GetMapping("/{id}")
    fun getBugComment(
        @PathVariable("bugId") bugId: UUID,
        @PathVariable("id") id: UUID
    ): BugCommentResponseDto {
        return bugCommentService.getBugComment(bugId, id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Bug comment not found")
    }

    @PutMapping("/{id}")
    fun updateBugComment(
        @PathVariable("bugId") bugId: UUID,
        @PathVariable("id") id: UUID,
        @RequestBody @Valid body: BugCommentRequestDto
    ): BugCommentResponseDto {
        return bugCommentService.updateBugComment(bugId, id, body)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Bug comment not found")
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBugComment(
        @PathVariable("bugId") bugId: UUID,
        @PathVariable("id") id: UUID
    ) {
        bugCommentService.deleteBugComment(bugId, id)
    }
}
