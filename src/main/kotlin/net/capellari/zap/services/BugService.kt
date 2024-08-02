package net.capellari.zap.services

import net.capellari.zap.dtos.BugResponse
import net.capellari.zap.repositories.BugRepository
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
class BugService(val bugRepository: BugRepository) {
    fun listBugs(): List<BugResponse> {
        return this.bugRepository.findAll()
            .map { BugResponse(it) }
    }

    fun getBug(id: UUID): BugResponse? {
        return this.bugRepository.findById(id)
            .map { BugResponse(it) }
            .getOrNull()
    }
}