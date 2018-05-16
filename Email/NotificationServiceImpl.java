package com.allconnect.filemerge.service.impl;

import static com.allconnect.filemerge.utils.ConfigCommonProperties.EMAIL_DFLT_MIME_TYPE;
import static com.allconnect.filemerge.utils.ConfigCommonProperties.EMAIL_HOST;
import static com.allconnect.filemerge.utils.ConfigCommonProperties.EMAIL_PASSWORD;
import static com.allconnect.filemerge.utils.ConfigCommonProperties.EMAIL_PORT;
import static com.allconnect.filemerge.utils.ConfigCommonProperties.EMAIL_PROTOCOL;
import static com.allconnect.filemerge.utils.ConfigCommonProperties.EMAIL_SECURE_CONNECTION_TYPE;
import static com.allconnect.filemerge.utils.ConfigCommonProperties.EMAIL_USERNAME;
import static com.allconnect.filemerge.utils.ProcessConfigProperties.EMAIL_CC;
import static com.allconnect.filemerge.utils.ProcessConfigProperties.EMAIL_CONTENT;
import static com.allconnect.filemerge.utils.ProcessConfigProperties.EMAIL_FROM;
import static com.allconnect.filemerge.utils.ProcessConfigProperties.EMAIL_SUBJECT;
import static com.allconnect.filemerge.utils.ProcessConfigProperties.EMAIL_TO;
import static com.allconnect.filemerge.utils.ProcessConfigProperties.ERROR_EMAIL_CC;
import static com.allconnect.filemerge.utils.ProcessConfigProperties.ERROR_EMAIL_CONTENT;
import static com.allconnect.filemerge.utils.ProcessConfigProperties.ERROR_EMAIL_FROM;
import static com.allconnect.filemerge.utils.ProcessConfigProperties.ERROR_EMAIL_SUBJECT;
import static com.allconnect.filemerge.utils.ProcessConfigProperties.ERROR_EMAIL_TO;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.BodyPart;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.allconnect.filemerge.common.utils.Utils;
import com.allconnect.filemerge.exceptions.EmailException;
import com.allconnect.filemerge.service.NotificationService;
import com.allconnect.filemerge.service.email.EmailManager;

@Service("notificationService")
public class NotificationServiceImpl implements NotificationService {
	
	private static final Logger log = Logger
			.getLogger(NotificationServiceImpl.class);
	
		public static final String BLANK_STRING = "";
	// for success email
		private String fromKey = EMAIL_FROM;

		private String toKey = EMAIL_TO;

		private String subjectKey = EMAIL_SUBJECT;
		private String contentKey = EMAIL_CONTENT;

		// for error email
		private String errFromKey = ERROR_EMAIL_FROM;
		private String errToKey = ERROR_EMAIL_TO;
		private String errCcKey = ERROR_EMAIL_CC;
		private String errSubjectKey = ERROR_EMAIL_SUBJECT;
		private String errContentKey = ERROR_EMAIL_CONTENT;

	@Override
	public void sendSuccessEmail(String[] subjectParams, String[] contentParams, File[] files) throws EmailException {
		// TODO Auto-generated method stub
		sendEmail(fromKey, toKey, EMAIL_CC, subjectKey, subjectParams,
				contentKey, contentParams, files);
		

	}


	@Override
	public void sendErrorEmail(String[] subjectParams, String[] contentParams, File[] files) throws EmailException {
		// TODO Auto-generated method stub

		sendEmail(errFromKey, errToKey, errCcKey, errSubjectKey, subjectParams,
				errContentKey, contentParams, files);
	}

	private void sendEmail(String fromKey2, String toKey2, String ccKey, String subjectKey2, String[] subjectParams,
			String contentKey2, String[] contentParams, File[] files) throws EmailException {
		String from = fromKey != null ? fromKey : "";
		log.debug("from Email key: " + from);
		List<String> to = Arrays.asList(toKey.split("\\s*,\\s*"));
		log.debug("TO Email key: " + to);
		String subject = getStringValue(subjectKey, subjectParams);
		String content = getStringValue(contentKey, contentParams);
		List<BodyPart> attachments = getBodyPart(files);
		sendEmail(from, to,
				ccKey != null ? Arrays.asList(ccKey.split("\\s*,\\s*")) : null,
				subject, content, attachments);
	
	}

	private void sendEmail(String from, List<String> to, List<String> cc, String subject, String content,
			List<BodyPart> attachments)throws EmailException {
		debug(from, to, cc, subject, content, attachments);

		EmailManager.sendEmail(EMAIL_PROTOCOL, EMAIL_SECURE_CONNECTION_TYPE,
				EMAIL_HOST, Integer.valueOf(EMAIL_PORT), EMAIL_USERNAME,
				EMAIL_PASSWORD, from, to, cc, subject, EMAIL_DFLT_MIME_TYPE,
				content, attachments);
	}


	private String getStringValue(String key, String[] args) {
		String val = null;
		if (!Utils.isBlank(key))
			val = MessageFormat.format(key, args);

		if (!Utils.isBlank(val))
			return val;

		return BLANK_STRING;
	}
	
	 public static boolean isEmpty(Object[] arr)
	  {
	    return arr == null || arr.length == 0;
	  }
	private List<BodyPart> getBodyPart(File[] files) {

		if (Utils.isEmpty(files))
			return null;

		List<BodyPart> list = new ArrayList<BodyPart>();
		BodyPart part = null;

		for (int i = 0; i < files.length; i++) {
			part = null;
			try {
				part = EmailManager.bodyPartFromFile(files[i]);
				if (part != null) {
					// attachment = Collections.singletonList(part);
					list.add(part);
				}
			} catch (EmailException ex) {
				log.info("Exception while attaching file in sendMail:"
						+ ex.getMessage());
				log.error(ex);
			}
		}

		return list;
	}
	
	private void debug(String from, List<String> to, List<String> cc,
			String subject, String content, List<BodyPart> attachments) {

		if (log.isDebugEnabled()) {

			log.debug("EMAIL_HOST :" + EMAIL_HOST);
			log.debug("EMAIL_PORT :" + EMAIL_PORT);
			log.debug("EMAIL_PROTOCOL :" + EMAIL_PROTOCOL);
			log.debug("EMAIL_SECURE_CONNECTION_TYPE :"
					+ EMAIL_SECURE_CONNECTION_TYPE);
			log.debug("EMAIL_USERNAME :" + EMAIL_USERNAME);
			log.debug("EMAIL_PASSWORD :" + EMAIL_PASSWORD);
			log.debug("EMAIL_DFLT_MIME_TYPE :" + EMAIL_DFLT_MIME_TYPE);

			log.debug("from:" + from);
			log.debug("to:" + to);
			log.debug("cc:" + cc);
			log.debug("subject:" + subject);
			log.debug("content:" + content);
			log.debug("attachments:" + attachments);
		}
	}

}
