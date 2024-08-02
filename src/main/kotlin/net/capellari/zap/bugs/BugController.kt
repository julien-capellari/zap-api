package net.capellari.zap.bugs

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
    fun createBug(@RequestBody body: BugCreateDto): BugResponseDto {
        return this.bugService.createBug(body)
    }

    @GetMapping("/{id}")
    fun getBug(@PathVariable("id") id: UUID): BugResponseDto {
        return this.bugService.getBug(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Bug not found")
    }
}