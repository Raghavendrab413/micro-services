package com.allconnect.filemerge.service.email;

import static com.allconnect.filemerge.utils.ConfigCommonProperties.EMAIL_DFLT_MIME_TYPE;
import static com.allconnect.filemerge.utils.ConfigCommonProperties.EMAIL_ENABLED;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.allconnect.filemerge.common.utils.Utils;
import com.allconnect.filemerge.exceptions.EmailException;


public class EmailManager {
	
	private static final Logger log=Logger.getLogger(EmailManager.class);

	public static BodyPart bodyPartFromFile(File file) throws EmailException {
		assert file != null;

		BodyPart attach = new MimeBodyPart();

		try
		{
			// Add attachment.
			attach.setDataHandler(new DataHandler(new FileDataSource(file)));
			attach.setFileName(file.getName());
		}
		catch (MessagingException e)
		{
			throw new EmailException("Failed to create body part from file.", e);
		}

		return attach;
	}

	public static void sendEmail(String proto, String connType, String smtpHost, int smtpPort,
			final String username, final String passwd, String from, List to, List cc,
			String subject, String mime, Object content, List attachments)throws EmailException {
		assert proto != null;
		assert connType != null;
		assert smtpHost != null;
		assert from != null;
		assert(to != null && to.isEmpty() == false) || (cc != null && cc.isEmpty() == false);
		assert content != null;
		assert subject != null;
		
		if (Boolean.valueOf( EMAIL_ENABLED ) == false)
		{

			if (log.isDebugEnabled() == true)
			{
				log.debug("Message was not send. Sending disabled.");
			}

			return;
		}
		try {

			Authenticator auth = null;

			Properties props = new Properties();

			// Session properties.
			props.setProperty("mail.transport.protocol", proto);

			String mailProto = "mail." + proto;

			props.setProperty(mailProto + ".host", smtpHost);
			props.setProperty(mailProto + ".port", String.valueOf(smtpPort));

			connType = connType.toUpperCase();

			if ("STARTTLS".equals(connType))
			{
				props.setProperty(mailProto + ".starttls.enable", "true");
			}
			else if ("SSL".equals(connType))
			{
				props.setProperty(mailProto + ".ssl", "true");
			}

			// Add property for authentication by username
			if (username != null)
			{
				props.setProperty(mailProto + ".auth", "true");
				log.info("Email username : " + username);
				auth = new Authenticator()
				{
					/**
					 * @return FIXDOC
					 */

					public PasswordAuthentication getPasswordAuthentication()
					{

						return new PasswordAuthentication(username, passwd);
					}
				};
			}

			log.info("Email Properties : " + props.toString());

			Session ses = Session.getInstance(props, auth);

			// Create MIME message.
			Message msg = new MimeMessage(ses);

			// Set up the message.

			msg.setFrom(new InternetAddress(from));

			msg.setSubject(subject);

			msg.setSentDate(new Date());

			// Add TO:
			
			if (to != null && !to.isEmpty()) {
				log.info("Sending Email To : " + to.toString());
				Iterator iter = to.iterator();

				while (iter.hasNext() == true) {
					String email = (String) iter.next();

					msg.addRecipient(Message.RecipientType.TO,
							new InternetAddress(email));
				}
			}


			// Add CC:
			if (cc != null && !cc.isEmpty()) {
				log.debug("CC Email To : " + cc.toString());
				Iterator iter = cc.iterator();

				while (iter.hasNext() == true) {
					String email = (String) iter.next();

					msg.addRecipient(Message.RecipientType.CC,
							new InternetAddress(email));
				}
			}
			MimeBodyPart main = new MimeBodyPart();

			main.setDataHandler(new DataHandler(content, mime == null ? EMAIL_DFLT_MIME_TYPE : mime));

			Multipart body = new MimeMultipart();

			body.addBodyPart(main);

			// Add attachments, if any.
			
			if (attachments != null && attachments.size() > 0) {
				Iterator iter = attachments.iterator();

				while (iter.hasNext() == true) {
					BodyPart part = (BodyPart) iter.next();

					body.addBodyPart(part);
				}
			}

			// Set the message body.
			msg.setContent(body);

			Transport.send(msg);
			log.info("Email message was sent.");
			// Ack.
			if (log.isDebugEnabled() == true)
			{
				log.debug("Email message was sent.");
			}
		
		}catch (MessagingException e) {
			log.error("EmailManager Failed to send message" , e);
			throw new EmailException("Failed to send message.", e);
		}
		
		
		
	}

}
