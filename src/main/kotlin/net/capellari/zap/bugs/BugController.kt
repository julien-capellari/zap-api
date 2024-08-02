package net.capellari.zap.bugs

import jakarta.validation.Valid
import net.capellari.zap.bugs.dtos.BugRequestDto
import net.capellari.zap.bugs.dtos.BugResponseDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@RestController
@RequestMapping("/bugs")
class BugController(private val bugService: BugService) {
    @GetMapping
    fun listBugs(): List<BugResponseDto> {
        return this.bugService.listBugs()
    }

    @PostMapping
    fun createBug(@RequestBody @Valid body: BugRequestDto): BugResponseDto {
        return this.bugService.createBug(body)
    }

    @GetMapping("/{id}")
    fun getBug(@PathVariable("id") id: UUID): BugResponseDto {
        return this.bugService.getBug(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Bug not found")
    }

    @PutMapping("/{id}")
    fun updateBug(@PathVariable("id") id: UUID, @RequestBody @Valid body: BugRequestDto): BugResponseDto {
        return this.bugService.updateBug(id, body)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Bug not found")
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBug(@PathVariable("id") id: UUID) {
        this.bugService.deleteBug(id)
    }
}