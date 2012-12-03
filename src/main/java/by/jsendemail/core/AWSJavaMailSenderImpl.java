package by.jsendemail.core;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpleemail.AWSJavaMailTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * User: Aleksey
 * Date: 11/25/12
 * Time: 7:57 PM
 */
public class AWSJavaMailSenderImpl {

    private static final Logger LOG = LoggerFactory.getLogger(AWSJavaMailSenderImpl.class);

    private static final String JSENDEMAIL_HOME = System.getProperty("jsendemail.home");

    public static final File jsendemailConfigurationHome = new File( JSENDEMAIL_HOME, "conf" );

    public static final File AWSCREDENTIALS_FILE = new File(jsendemailConfigurationHome, "AwsCredentials.properties");

    Properties props;

    public AWSJavaMailSenderImpl() {

        PropertiesCredentials credentials = null;
        try {
            credentials = new PropertiesCredentials(AWSCREDENTIALS_FILE);
        } catch (IOException e) {
            LOG.error("Couldn't load credentials from AwsCredentials.properties file.", e);
        }

        this.props = new Properties();
        props.setProperty("mail.transport.protocol", "aws");
        props.setProperty("mail.aws.user", credentials.getAWSAccessKeyId());
        props.setProperty("mail.aws.password", credentials.getAWSSecretKey());
    }

    public void send(MimeMessage mimeMessage) {
        Session session = Session.getInstance(props);

        try {
            Transport t = new AWSJavaMailTransport(session, null);
            t.connect();
            t.sendMessage(mimeMessage, null);
            t.close();
        } catch (Exception e) {
            LOG.error("Caught an AddressException, which means one or more of your "
                    + "addresses are improperly formatted.");
        }
    }
}
