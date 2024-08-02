package net.capellari.zap

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ZapApiApplication

fun main(args: Array<String>) {
    runApplication<ZapApiApplication>(*args)
}
