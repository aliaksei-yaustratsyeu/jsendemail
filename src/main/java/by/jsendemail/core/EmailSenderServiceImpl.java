package by.jsendemail.core;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpleemail.AWSJavaMailTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * User: a.evstratiev
 * Date: 11/22/12
 * Time: 4:49 AM
 */
@Component("mailSenderService")
public class EmailSenderServiceImpl implements EmailSenderService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailSenderServiceImpl.class);
    public static final String AMAZON_SES = "amazonaws";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AWSJavaMailSenderImpl awsMailSender;

    public JavaMailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(EmailMessageInfo emailMessageInfo) {

        MimeMessage message = mailSender.createMimeMessage();
        composeMessage(emailMessageInfo, message);

        JavaMailSenderImpl javaMailSenderImpl = null;
        if (mailSender.getClass().isAssignableFrom(JavaMailSenderImpl.class)) {
            javaMailSenderImpl = (JavaMailSenderImpl) mailSender;
        }

        LOG.info("Sending...");

        if (javaMailSenderImpl != null
                && javaMailSenderImpl.getHost().contains(AMAZON_SES)
                && (javaMailSenderImpl.getPassword() == null || javaMailSenderImpl.getPassword().length() == 0
                    || javaMailSenderImpl.getUsername() == null || javaMailSenderImpl.getUsername().length() == 0)) {

            awsMailSender.send(message);
        } else {
            mailSender.send(message);
        }

        LOG.info("Finished.");
    }

    private void composeMessage(EmailMessageInfo emailMessageInfo, MimeMessage message) {
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            InternetAddress from = new InternetAddress(emailMessageInfo.getFrom());
            helper.setFrom(from);

            InternetAddress[] to = InternetAddress.parse(emailMessageInfo.getTo());
            helper.setTo(to);

            helper.setSubject(emailMessageInfo.getSubject());

            helper.setText(emailMessageInfo.getText());

            if (emailMessageInfo.getCc() != null && emailMessageInfo.getCc().length() != 0) {
                InternetAddress[] cc = InternetAddress.parse(emailMessageInfo.getCc());
                helper.setCc(cc);
            }

            if (emailMessageInfo.getBcc() != null && emailMessageInfo.getBcc().length() != 0) {
                InternetAddress[] bcc = InternetAddress.parse(emailMessageInfo.getBcc());
                helper.setBcc(bcc);
            }

            if (emailMessageInfo.getAttachments() != null) {
                for (File attachment : emailMessageInfo.getAttachments()) {
                    helper.addAttachment(attachment.getName(), attachment);
                }
            }

            if (emailMessageInfo.getInlines() != null) {
                for (File inline : emailMessageInfo.getInlines()) {
                    helper.addInline(inline.getName(), inline);
                }
            }

        } catch (MessagingException e) {
            LOG.error("Error during composing email messsage.", e);
        }
    }
}
