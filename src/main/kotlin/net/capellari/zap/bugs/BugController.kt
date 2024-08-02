package net.capellari.zap.bugs

import jakarta.validation.Valid
import net.capellari.zap.bugs.dtos.BugRequestDto
import net.capellari.zap.bugs.dtos.BugResponseDto
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
@RequestMapping("/bugs")
class BugController(
    private val bugService: BugService,
) {
    @GetMapping
    fun listBugs(): List<BugResponseDto> = this.bugService.listBugs()

    @PostMapping
    fun createBug(
        @RequestBody @Valid body: BugRequestDto,
    ): BugResponseDto = this.bugService.createBug(body)

    @GetMapping("/{id}")
    fun getBug(
        @PathVariable("id") id: UUID,
    ): BugResponseDto =
        this.bugService.getBug(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Bug not found")

    @PutMapping("/{id}")
    fun updateBug(
        @PathVariable("id") id: UUID,
        @RequestBody @Valid body: BugRequestDto,
    ): BugResponseDto =
        this.bugService.updateBug(id, body)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Bug not found")

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBug(
        @PathVariable("id") id: UUID,
    ) {
        this.bugService.deleteBug(id)
    }
}
