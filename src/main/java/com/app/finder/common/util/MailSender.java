package com.app.finder.common.util;



import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * 发送邮件
 * 由于用spring自带的代码发送邮件不好用。
 * 替换spring发送邮件的代码
 */
public class MailSender {
    private Properties props;
    private Session session;
    private String mailHost;
    private String textEncode = "utf-8";
    private boolean debug = false;

    // For login SMTP account
    private String username = null;
    private String password = null;


    public MailSender(String mailHost) {
        this(mailHost, "utf-8");
    }

    public MailSender(String mailHost, String textEncode) {
        this(mailHost, textEncode, null, null);
    }

    public MailSender(String mailHost, String textEncode, String username, String password) {
        this.textEncode = textEncode;
        this.mailHost = mailHost;
        this.username = username;
        this.password = password;
        init();
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void init() {
        props = System.getProperties();

        session = Session.getInstance(props, null);

        if (debug) {
            session.setDebug(true);
        }
    }

    public String getTextEncode() {
        return textEncode;
    }

    public void setTextEncode(String c) {
        textEncode = c;
    }

    public void sendText(String to, String from, String fromName, String subject, String content,
                         String cc, String bcc) throws Exception {
        try {
            // create a message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from, fromName));
//            msg.setReplyTo();
            msg.setRecipients(Message.RecipientType.TO,
                              InternetAddress.parse(to, false));
            msg.setSubject(MimeUtility.encodeText(subject, textEncode, "B"));
            msg.setSentDate(new Date());

            if (cc != null && cc.trim().length() > 0) {
                msg.setRecipients(Message.RecipientType.CC,
                                  InternetAddress.parse(cc, false));
            }
            if (bcc != null && bcc.trim().length() > 0) {
                msg.setRecipients(Message.RecipientType.BCC,
                                  InternetAddress.parse(bcc, false));
            }
            msg.setHeader("X-Mailer", "kepinzhe");
            msg.setText(content, textEncode);

            if (username != null && password != null) {
                Transport transport = session.getTransport("smtp");
                props.put("mail.smtp.auth", "true");
                transport.connect(mailHost, username, password);
                transport.sendMessage(msg, msg.getAllRecipients());
            } else {
                Transport.send(msg);
            }
        }
        catch (Exception e) {
            throw e;
        }
    }


    public Transport getTransport(String mailHost, String username, String password) {
        Transport result = null;
        props.put("mail.smtp.auth", "true");
        session = Session.getInstance(props);
        try {
            result = session.getTransport("smtp");
            result.connect(mailHost, username, password);
        } catch (Exception ex) {
        }
        return result;
    }

    public void sendEdmMail(Transport transport, MimeMessage msg) {
        try {
            transport.sendMessage(msg, msg.getAllRecipients());
        } catch (MessagingException ex) {
        }
    }


    public void sendHtmlMail(String to, String from, String fromName,
                             String subject, String htmlContent,
                             String cc, String bcc) throws Exception {
        try {
            // create a message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from, fromName));
            msg.setRecipients(Message.RecipientType.TO,
                              InternetAddress.parse(to, false));
            msg.setSubject(MimeUtility.encodeText(subject, textEncode, "B"));
            msg.setSentDate(new Date());

            if (cc != null && cc.trim().length() > 0) {
                msg.setRecipients(Message.RecipientType.CC,
                                  InternetAddress.parse(cc, false));
            }
            if (bcc != null && bcc.trim().length() > 0) {
                msg.setRecipients(Message.RecipientType.BCC,
                                  InternetAddress.parse(bcc, false));
            }
            msg.setHeader("X-Mailer", "kepinzhe");

            MimeMultipart mp = null;
            
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(htmlContent, "text/html; charset=utf-8");
            mp = new MimeMultipart();
            mp.addBodyPart(bodyPart);
            
            msg.setContent(mp);

            if (username != null && password != null) {
                Transport transport = session.getTransport("smtp");
                props.put("mail.smtp.auth", "true");
                transport.connect(mailHost, username, password);
                transport.sendMessage(msg, msg.getAllRecipients());
            } else {
                Transport.send(msg);
            }

        } catch (Exception e) {
            throw e;
        }
    }


    public void sendAttachments(String to, String from, String fromName,
                                String subject,
                                String content, String cc, String bcc, List attachFiles) throws
        Exception {
        try {
            // create a message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from, fromName));
            msg.setRecipients(Message.RecipientType.TO,
                              InternetAddress.parse(to, false));
            msg.setSubject(MimeUtility.encodeText(subject, textEncode, "B"));
            msg.setSentDate(new Date());

            if (cc != null && cc.trim().length() > 0) {
                msg.setRecipients(Message.RecipientType.CC,
                                  InternetAddress.parse(cc, false));
            }
            if (bcc != null && bcc.trim().length() > 0) {
                msg.setRecipients(Message.RecipientType.BCC,
                                  InternetAddress.parse(bcc, false));
            }
            msg.setHeader("X-Mailer", "kepinzhe");
            msg.setHeader("Content-Type", "multipart/mixed");


            MimeMultipart mp = new MimeMultipart();
            MimeBodyPart part = new MimeBodyPart();
            part.setContent(content, "text/html; charset=utf-8");
            mp.addBodyPart(part);

            if (attachFiles != null) {
                for (int i = 0; i < attachFiles.size(); i++) {
                    String filename = (String) attachFiles.get(i);
                    File f = new File(filename);
                    if (f.exists()) {
                        part = new MimeBodyPart();
                        DataSource source = new FileDataSource(filename);
                        part.setDataHandler(new DataHandler(source));
                        part.setFileName(f.getName());
                        mp.addBodyPart(part);
                    }
                }
            }

            msg.setContent(mp);

            // send the thing off
            if (username != null && password != null) {
                Transport transport = session.getTransport("smtp");
                props.put("mail.smtp.auth", "true");
                transport.connect(mailHost, username, password);
                transport.sendMessage(msg, msg.getAllRecipients());
            } else {
                Transport.send(msg);
            }


        } catch (Exception e) {
            throw e;
        }
    }


    public void sendPHSAttachments(String to, String from, String fromName,
                                   String subject,
                                   String content,
                                   String cc, String bcc,
                                   String[] attachFles) throws Exception {
        try {
            // create a message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from, fromName));
            msg.setRecipients(Message.RecipientType.TO,
                              InternetAddress.parse(to, false));
            msg.setSubject(MimeUtility.encodeText(subject, textEncode, "B"));
            msg.setSentDate(new Date());

            if (cc != null && cc.trim().length() > 0) {
                msg.setRecipients(Message.RecipientType.CC,
                                  InternetAddress.parse(cc, false));
            }
            if (bcc != null && bcc.trim().length() > 0) {
                msg.setRecipients(Message.RecipientType.BCC,
                                  InternetAddress.parse(bcc, false));
            }
            msg.setHeader("X-Mailer", "55968");
            msg.setHeader("Content-Type", "text/X-PmailDX");
            msg.setHeader("X-PmailDX-CPGFLG", "ON"); // 禁止轉寄

            /*msg.removeHeader("Return-Path");
                         msg.removeHeader("Received");
                         msg.removeHeader("Message-ID");
                         msg.removeHeader("Mime-Version");
                         msg.saveChanges();*/


            MimeMultipart mp = new MimeMultipart();
            MimeBodyPart part = new MimeBodyPart();
            part.setText(content);
            mp.addBodyPart(part);

            if (attachFles != null) {
                for (int i = 0; i < attachFles.length; i++) {
                    String filename = attachFles[i];
                    if (filename == null || filename.trim().length() == 0) {
                        continue;
                    }
                    File f = new File(filename);
                    if (f.exists()) {
                        part = new MimeBodyPart();
                        part.addHeader("X-PmailDX-CPGFLG", "ON"); // 禁止轉寄
                        DataSource source = new FileDataSource(filename);
                        part.setDataHandler(new DataHandler(source));
                        part.setFileName(f.getName());

                        if (f.getName().toLowerCase().endsWith(".bmp") ||
                            f.getName().toLowerCase().endsWith(".jpg") ||
                            f.getName().toLowerCase().endsWith(".jpeg") ||
                            f.getName().toLowerCase().endsWith(".png") ||
                            f.getName().toLowerCase().endsWith(".flm")) { // 圖框圖檔
                            part.addHeader("Content-Type",
                                           "Application/Octet-stream;Name=\"" +
                                           f.getName() + "\"");
                        } else if (f.getName().toLowerCase().endsWith(".mid")) {
                            part.addHeader("Content-Type",
                                           "Audio/Mid;name=\"" + f.getName() +
                                           "\"");
                        } else if (f.getName().toLowerCase().endsWith("als")) { // 單音
                            part.addHeader("Content-Type",
                                           "Audio/X-Alpha5;name=\"" + f.getName() +
                                           "\"");
                        } else if (f.getName().toLowerCase().endsWith("dxm")) { // 和弦
                            part.addHeader("Content-Type",
                                           "Audio/X-PdxMidi;name=\"" +
                                           f.getName() + "\"");
                        } else if (f.getName().toLowerCase().endsWith("dxv")) { // 自錄鈴聲
                            part.addHeader("Content-Type",
                                           "Audio//X-PdxVoice;name=\"" +
                                           f.getName() + "\"");
                        }

                        part.removeHeader("Content-Disposition");
                        mp.addBodyPart(part);
                    }
                }
            }

            msg.setContent(mp);

            // send the thing off
            Transport.send(msg);
        } catch (Exception e) {
            throw e;
        }
    }

    public String getMailHost() {
        return mailHost;
    }

    public static void main(String[] args) {
//        MailSender mailSender1 = new MailSender("walkgame.cn", "BIG5");
        MailSender mailSender1 = new MailSender("smtp.kepinzhe.com");
        try {
            /*
             mailSender1.sendText("mlu@walkgame.com", "mlu@walkgame.com", "測試",
                                             "中文", "mlu@walkgame.com",
                                             "");
             */
//            String to = "chuang@walkgame.cn,davidluo@walkgame.cn";
            mailSender1.setUsername("system@kepinzhe.com");
            mailSender1.setPassword("KePinZhe1234!@#");
//            String to =
//                "chuang@walkgame.cn,casper_huang.tw@yahoo.com.tw,kosic@sina.com,kosic@tom.com";
            String from = "system@kepinzhe.com";
            String to = "yzijun@126.com";
//            String from = "msgapp@walkgame.cn";
            String fromName = "科品者";
            String subject = "这次测试html";

            String content = "<html><head><title>账号激活</title><meta http-equiv='Content-Type' content='text/html; charset=UTF-8' /></head>";
            content+="<body><h1>这是h1</h1><p>测试激活</p> </body></html>";
        
//            String htmlFile = "D:/tmp/DM/walkshow.htm";
//            String htmlFile = "D:/EDMSample/Flash/flash-web.htm";
//            String htmlFile = "D:/EDMSample/JavaScript/jay.htm";
//            String htmlFile = "D:/EDMSample/JavaScript/jay.files/01.htm";

            String cc = null;
            String bcc = null;
//            String[] attachFles = new String[1];
//            attachFles[0] = "D:/walkshow.txt";
            ArrayList htmlFile = new ArrayList();
//            String f = args[2];
//            files.add(f);

            mailSender1.sendHtmlMail(to, from, fromName, subject, content, cc,bcc);
//            mailSender1.sendText(to, from, fromName, subject, content, cc, bcc);
            
                    
//            mailSender1.sendAttachments(to, from,fromName, subject, content, cc, bcc, files);

//            mailSender1.sendPHSAttachments(to, from, fromName, subject, content,
//                                        cc, bcc, attachFles);

            /*mailSender1.sendAttachments("mlu@walkgame.com",
                                        "mlu@walkgame.com", "營管系統",
                                        "測試",
                                        "中文1" + "\n" + "中文2",
                                        "mlu@walkgame.com",
                                        "", new String[] {""});*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
