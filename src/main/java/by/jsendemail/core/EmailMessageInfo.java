package by.jsendemail.core;

import java.io.File;
import java.util.Collection;

/**
 * User: a.evstratiev
 * Date: 11/22/12
 * Time: 7:09 AM
 */
public class EmailMessageInfo {

    private String from;
    private String to;
    private String replyTo;
    private String cc;
    private String bcc;
    private String subject;
    private String text;
    private Collection<File> attachments;
    private Collection<File> inlines;

    public Collection<File> getAttachments() {
        return attachments;
    }

    public void setAttachments(Collection<File> attachments) {
        this.attachments = attachments;
    }

    public Collection<File> getInlines() {
        return inlines;
    }

    public void setInlines(Collection<File> inlines) {
        this.inlines = inlines;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
