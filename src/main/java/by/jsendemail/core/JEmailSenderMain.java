package by.jsendemail.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: a.evstratiev
 * Date: 11/22/12
 * Time: 4:53 AM
 */
public class JEmailSenderMain {

    private static final Logger LOG = LoggerFactory.getLogger(EmailMessageInfoBuilder.class);

    public static void main(String[] args) {

        EmailMessageInfoBuilder builder = EmailMessageInfoBuilder.getInstance();
        builder.parseCommandLine(args);
        EmailMessageInfo emailMessageInfo = builder.getEmailMessageInfo();

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        EmailSenderService emailSenderService = (EmailSenderService) context.getBean("mailSenderService");

        emailSenderService.sendEmail(emailMessageInfo);
    }

}
