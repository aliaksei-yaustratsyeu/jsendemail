package by.jsendemail.core;

import by.jsendemail.util.FileScanner;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;

/**
 * User: Aleksey
 * Date: 11/22/12
 * Time: 10:41 PM
 */
public class EmailMessageInfoBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(JEmailSenderMain.class);

    public static final String JSENDEMAIL_USAGE =
            "jsendemail -from <FROM address> -to <TO address> -s <subject> -m <message> [other options]";

    private Options options;

    private String from;
    private String to;
    private String subject;
    private String message;
    private String file;
    private Collection<File> attachments;
    private String replyTo;
    private String cc;
    private String bcc;
    private Collection<File> inlines;

    private EmailMessageInfoBuilder(Options options){
        this.options = options;
    }

    public static EmailMessageInfoBuilder getInstance() {
        Options options = new Options();

        // -from --from <FROM address>
        options.addOption(
                OptionBuilder.hasArg(true)
                        .withArgName("FROM address")
                        .withLongOpt("from")
                        .withDescription("FROM address. It's required.")
                        .create("from"));

        // -to --to <TO address>
        options.addOption(
                OptionBuilder.hasArg(true)
                        .withArgName("TO address")
                        .withLongOpt("to")
                        .withDescription("TO address. It's required.")
                        .create("to"));

        // -s --subject <subject>
        options.addOption(
                OptionBuilder.hasArg(true)
                        .withArgName("subject")
                        .withLongOpt("subject")
                        .withDescription("Subject of the message.")
                        .create("s"));

        // -m --message <message text>
        options.addOption(
                OptionBuilder.hasArg(true)
                        .withArgName("message")
                        .withLongOpt("message")
                        .withDescription("Text for the message.")
                        .create("m"));

        // -f --file <message file>
        options.addOption(
                OptionBuilder.hasArg(true)
                        .withArgName("file")
                        .withLongOpt("file")
                        .withDescription("Text from file for the message.")
                        .create("f"));

        // -a --attachment <attachment(s)>
        options.addOption(
                OptionBuilder.hasArgs()
                        .withArgName("attachment(s)")
                        .withLongOpt("attachment")
                        .withValueSeparator(';')
                        .withDescription("Attachment(s) for the message.")
                        .create("a"));

        // -r --replyTo <replyTo>
        options.addOption(
                OptionBuilder.hasArg()
                        .withArgName("replyTo")
                        .withLongOpt("replyTo")
                        .withDescription("Reply To address.")
                        .create("r"));

        // -c --cc <CC>
        options.addOption(
                OptionBuilder.hasArg()
                        .withArgName("CC")
                        .withLongOpt("cc")
                        .withDescription("CC address.")
                        .create("cc"));

        // -b --bcc <BCC>
        options.addOption(
                OptionBuilder.hasArg()
                        .withArgName("BCC")
                        .withLongOpt("bcc")
                        .withDescription("BCC address.")
                        .create("b"));

        // -i --include <include file>
        options.addOption(
                OptionBuilder.hasArgs()
                        .withArgName("inline file")
                        .withLongOpt("inline")
                        .withDescription("Specify a file to include into the body of a message.")
                        .create("i"));

        // -h --help
        options.addOption(
                OptionBuilder.withLongOpt("help")
                        .withDescription("Print help.")
                        .create("h"));

        return new EmailMessageInfoBuilder(options);
    }

    public void parseCommandLine(String[] args) {
        // create the parser
        CommandLineParser parser = new GnuParser();
        CommandLine line = null;
        try {
            // parse the command line arguments
            line = parser.parse( options, args );
        } catch ( ParseException exp ) {
            LOG.error("Parsing failed.  Reason: " + exp.getMessage());
            usage(options);
            System.exit(1);
        }

        //check --help option
        if (line.hasOption("h")) {
            usage(options);
            System.exit(0);
        //check required fields
        } else if (!(line.hasOption("from") && line.hasOption("to") && line.hasOption("subject")
                && (line.hasOption("message") || line.hasOption("file")))) {
            LOG.error("-from, -to, -s, -m (or -f) are required parameters");
            usage(options);
            System.exit(1);
        } else {
            this.from = line.getOptionValue("from");
            this.to = line.getOptionValue("to");
            this.subject = line.getOptionValue("subject");

            if (line.hasOption("message") && line.hasOption("file")) {
                this.message = line.getOptionValue("message");
                this.file = line.getOptionValue("file");
            } else if (line.hasOption("message")) {
                this.message = line.getOptionValue("message");
            } else if (line.hasOption("file")) {
                this.file = line.getOptionValue("file");
            } else {
                LOG.error("-m(or -f) are required parameters");
                usage(options);
                System.exit(1);
            }

            if (line.hasOption("cc")) {
                this.cc = line.getOptionValue("cc");
            }

            if (line.hasOption("bcc")) {
                this.bcc = line.getOptionValue("bcc");
            }

            if (line.hasOption("replyTo")) {
                this.replyTo = line.getOptionValue("replyTo");
            }

            if (line.hasOption("bcc")) {
                this.bcc = line.getOptionValue("bcc");
            }

            if (line.hasOption("attachment")) {
                String[] attachmentsPathes = line.getOptionValues("attachment");
                Collection<File> attachments = FileScanner.getFilesByPath(attachmentsPathes);
                this.attachments = attachments;
            }

            if (line.hasOption("inline")) {
                String[] inlinesPathes = line.getOptionValues("inline");
                Collection<File> inlines = FileScanner.getFilesByPath(inlinesPathes);
                this.inlines = inlines;
            }

        }
    }

    private static void usage(Options options) {
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(100);
        formatter.printHelp(JSENDEMAIL_USAGE, options );
    }

    public EmailMessageInfo getEmailMessageInfo() {

        EmailMessageInfo messageInfo = new EmailMessageInfo();
        messageInfo.setFrom(this.from);
        messageInfo.setTo(this.to);
        messageInfo.setSubject(this.subject);
        messageInfo.setCc(this.cc);
        messageInfo.setBcc(this.bcc);
        messageInfo.setReplyTo(this.replyTo);

        if (this.message != null && this.message.length() != 0
                && this.file != null && this.file.length() != 0) {

            String textFromFile = readTextFromFile();
            String lineSeparator = System.getProperty("line.separator");
            messageInfo.setText(this.message + lineSeparator + textFromFile);

        } else if (this.file != null && this.file.length() != 0) {
            String textFromFile = readTextFromFile();
            messageInfo.setText(textFromFile);
        } else {
            messageInfo.setText(this.message);
        }

        messageInfo.setAttachments(this.attachments);
        messageInfo.setInlines(this.inlines);

        return messageInfo;
    }

    private String readTextFromFile() {
        StringWriter stringWriter = new StringWriter();

        File fileMessage = new File(this.file);

        if (!fileMessage.exists()) {
            LOG.error("File " + this.file + "doesn't exist.");
        }

        try {
            IOUtils.copy(new FileInputStream(fileMessage), stringWriter);
        } catch (IOException e) {
            LOG.error("Couldn't read the " + this.file + "file.", e);
        }
        return stringWriter.toString();
    }
}
