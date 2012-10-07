package com.icode.generic.notifier;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.icode.generic.base.ICGenConstants;
import com.icode.generic.base.ICGenTreeNode;

public class ICNotifChannelEmail extends ICNotifChannel implements ICGenConstants {

	protected String sender = null;

	protected String mailHost = null;
	protected String mailPort = null;
	protected String mailUser = null;
	protected String mailPass = null;
	protected String mailConnTimeout	= "30000";
	protected String mailSmtpTimeout	= "30000";
	
	protected boolean debug = false;
	
	Session session;
	Transport transport;

	public ICNotifChannelEmail() {
		super(NOTIF_TYPE_MAIL);
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		mailHost = (String) config.getMandatory("mailHost");
		sender = (String) config.getMandatory("sender");
		
		mailPort = (String) config.getOptional("mailPort", "25");
		mailUser = (String) config.getOptional("mailUser", null);
		mailPass = (String) config.getOptional("mailPass", null);
				
		mailConnTimeout = (String) config.getOptional("mailConnTimeout", "30000");
		mailSmtpTimeout = (String) config.getOptional("mailSmtpTimeout", "30000");
		
		super.loadDataFrom(config, hint);
	}

	
	public void addNotification(ICNotification notif, Set targets) {
	// put it into the channel queue, the mail will be sent once to all recipients
		addChannelNotification(notif, targets); 
	}
	
	protected void sendInit() throws Exception {	
		// Set smtp properties
		Properties s_props = new Properties();
		s_props.put( "mail.smtp.host", mailHost );
		s_props.put( "mail.smtp.port", mailPort );
		s_props.put( "mail.smtp.connectiontimeout", mailConnTimeout ); 
		s_props.put( "mail.smtp.timeout", mailSmtpTimeout );
		
		// Creating messaging session
		Session session = Session.getInstance( s_props, null );
		session.setDebug( debug );
		
		// Creating transport object for smtp
		transport = session.getTransport( "smtp" );
		transport.connect( mailHost, mailUser, mailPass );
		
	}

	protected void sendNotif(ICNotification notif, Object target) throws Exception {
		// Creating message
		Message s_message = new MimeMessage( session );

		// Set message properties
		s_message.setFrom( new InternetAddress( sender ));
		s_message.setSubject( notif.getAttrib(NOTIF_SUBJECT) );
		s_message.setText( notif.getAttrib(NOTIF_CONTENT) );
		s_message.setSentDate( new Date(notif.getTimeMsec()) );

		for ( Iterator it = ((Set)target).iterator(); it.hasNext(); ) {
			s_message.addRecipient( Message.RecipientType.TO, new InternetAddress( (String)it.next() ));
		}
		// Sending message
		transport.sendMessage( s_message, s_message.getAllRecipients() );
	}

	protected void sendFinished() throws Exception {
		if ( transport.isConnected() ) {
			transport.close();
		}
	}

}
