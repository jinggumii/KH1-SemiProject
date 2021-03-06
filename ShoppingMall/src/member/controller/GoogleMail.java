package member.controller;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GoogleMail {

	public void sendmail(String recipient, String certificationCode)  
	    	throws Exception{
	        
	    	// 1. 정보를 담기 위한 객체
	    	Properties prop = new Properties(); 
	    	
	    	// 2. SMTP(Simple Mail Transfer Protocol)서버의 계정 설정
	   	    //    Google Gmail 과 연결할 경우 Gmail 의 email 주소를 지정 
	    	prop.put("mail.smtp.user", "milkim0907@gmail.com");
	        	
	    	
	    	// 3. SMTP 서버 정보 설정
	    	//    Google Gmail 인 경우  smtp.gmail.com
	    	prop.put("mail.smtp.host", "smtp.gmail.com");
	         	
	    	
	    	prop.put("mail.smtp.port", "465");
	    	prop.put("mail.smtp.starttls.enable", "true");
	    	prop.put("mail.smtp.auth", "true");
	    	prop.put("mail.smtp.debug", "true");
	    	prop.put("mail.smtp.socketFactory.port", "465");
	    	prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    	prop.put("mail.smtp.socketFactory.fallback", "false");
	    	
	    	prop.put("mail.smtp.ssl.enable", "true");
	    	prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
	      	
	    	
	    	Authenticator smtpAuth = new MySMTPAuthenticator();
	    	Session ses = Session.getInstance(prop, smtpAuth);
	    		
	    	// 메일을 전송할 때 상세한 상황을 콘솔에 출력한다.
	    	ses.setDebug(true);
	    	        
	    	// 메일의 내용을 담기 위한 객체생성
	    	MimeMessage msg = new MimeMessage(ses);

	    	// 제목 설정
	    	String subject = "[마켓컬리] 비밀번호 인증번호를 안내 드립니다";
	    	msg.setSubject(subject);
	    	        
	    	// 보내는 사람의 메일주소
	    	String sender = "milkim0907@gmail.com";
	    	Address fromAddr = new InternetAddress(sender);
	    	msg.setFrom(fromAddr);
	    	        
	    	// 받는 사람의 메일주소
	    	Address toAddr = new InternetAddress(recipient);
	    	msg.addRecipient(Message.RecipientType.TO, toAddr);
	    	        
	    	// 메시지 본문의 내용과 형식, 캐릭터 셋 설정
	    	msg.setContent(" 안녕하세요. 마켓컬리입니다.<br/> 회원님의 요청하신 비밀번호 찾기 인증번호를 안내 드립니다. <br/> 아래번호를 인증번호 입력란에 입력해주세요<br/><br/>"
	    			+ " <span style='font-size:11pt; color:#5f0080; font-weight:bold;'>인증번호 : "+certificationCode+"</span>", "text/html;charset=UTF-8");
	    	        
	    	// 메일 발송하기
	    	Transport.send(msg);
	    	
	    }// end of sendmail(String recipient, String certificationCode)-----------------
}
