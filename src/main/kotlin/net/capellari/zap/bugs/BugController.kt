package net.capellari.zap.bugs

import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import net.capellari.zap.bugs.dtos.BugFiltersDto
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
import org.springframework.web.bind.annotation.RequestParam
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
    fun listBugs(
        @RequestParam("status") status: BugStatus?,
        @RequestParam("severity") @Max(5) @Min(1) severity: Int?,
    ): List<BugResponseDto> {
        return bugService.listBugs(BugFiltersDto(status, severity))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBug(
        @RequestBody @Valid body: BugRequestDto,
    ): BugResponseDto {
        return bugService.createBug(body)
    }

    @GetMapping("/{id}")
    fun getBug(
        @PathVariable("id") id: UUID,
    ): BugResponseDto {
        return bugService.getBug(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Bug not found")
    }

    @PutMapping("/{id}")
    fun updateBug(
        @PathVariable("id") id: UUID,
        @RequestBody @Valid body: BugRequestDto,
    ): BugResponseDto {
        return bugService.updateBug(id, body)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Bug not found")
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBug(
        @PathVariable("id") id: UUID,
    ) {
        bugService.deleteBug(id)
    }
}
