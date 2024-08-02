package net.capellari.zap.repositories

import net.capellari.zap.entities.Bug
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BugRepository : JpaRepository<Bug, UUID>