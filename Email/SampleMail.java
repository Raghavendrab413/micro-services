
import java.io.File;

import com.allconnect.filemerge.exceptions.EmailException;
import com.allconnect.filemerge.service.NotificationService;
import com.allconnect.filemerge.service.impl.NotificationServiceImpl;

public class SampleMail {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		File file = new File("C:\\Users\\rbondalapati\\Downloads\\allconnect.eastout20161129.csv");
		sendEmail(file,2);

	}
	
	private static void sendEmail(File file, int count) throws Exception  {
		NotificationService emailManager = new NotificationServiceImpl();
		String[] subject = new String[3];
		String[] content = new String[3];
		File[] attachments = null;
		String fileName = null;
		String hostName = "HOST_NAME";
		if (file != null) {
			attachments = new File[1];
			attachments[0] = file;
			fileName = file.getName();
		}
		subject[0] = hostName;
		subject[2] = String.valueOf(count);
		content[0] = hostName;
		content[1] = fileName;
		content[2] = String.valueOf(count);
		try {
			System.out.println("Sending success email for file " + fileName);
			emailManager.sendSuccessEmail(subject, content, attachments);
			//log.info("Success email sent for file " + fileName);
		} catch (EmailException ee) {
			//log.error(ee);
			
			
		}
	}

}
