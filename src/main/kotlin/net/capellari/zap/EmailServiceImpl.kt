package net.capellari.zap

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val emailSender: JavaMailSender,
) {
    fun sendEmail(to: String, subject: String, text: String) {
        emailSender.send(SimpleMailMessage().apply {
            setTo(to)
            setFrom("noreply@zap.net")
            setSubject(subject)
            setText(text)
        })
    }
}
