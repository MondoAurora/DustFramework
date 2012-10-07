package com.icode.generic.msg;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.icode.generic.ICGenIndexer;
import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.ICGenConstants;
import com.icode.generic.base.ICGenTreeNode;
import com.icode.generic.resolver.ICGenResolver;
import com.icode.generic.resource.ICGenResource;
import com.icode.generic.resource.ICGenResourceRef;

public class ICGenMsgChannelEmail extends ICGenMsgChannel implements ICGenConstants {
	private static final String[] MAIL_FIELDS = new String[]{MSG_FLD_SUBJECT, MSG_FLD_CONTENT};

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

	public ICGenMsgChannelEmail() {
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

	public void transmitMsg(ICGenMsg notif, Set targets) {
	// put it into the channel queue, the mail will be sent once to all recipients
		enqueueMsg(new QueuedMsgSet(notif, targets)); 
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

	protected void doSendMsg(QueuedMsg queuedMsg) throws Exception {
		final ICGenResource res = (ICGenResource) ICAppFrame.getComponent(APP_RESOURCES, null);
		
		QueuedMsgSet qms = (QueuedMsgSet) queuedMsg;
		ICGenResourceRef rref = qms.msg.def.getResRef(type);
		Map locales = new TreeMap();
		
		for ( Iterator it = qms.setListeners.iterator(); it.hasNext(); ) {
			ICGenMsgListener l = (ICGenMsgListener) it.next();
			ICGenIndexer.Index idx = res.getLocaleIndex(l.getPreferredLocale());
			
			ArrayList al = (ArrayList) locales.get(idx); 
			if ( null == al ) {
				al = new ArrayList();
				locales.put(idx, al);
			}
			al.add(l);
		}

		for ( Iterator it = locales.values().iterator(); it.hasNext(); ) {
			ArrayList al = (ArrayList) it.next(); 
			ICGenMsgListener l = (ICGenMsgListener) al.get(0);
			Locale locale = l.getPreferredLocale();
			
			ICGenResolver.PathElementMap pm = new ICGenResolver.PathElementMap();
			((Map)qms.msg.context).put("listener", l);
			rref.getFormattedContent(qms.msg.param, qms.msg.context, locale, MAIL_FIELDS, pm);

			Message s_message = new MimeMessage( session );
			s_message.setFrom( new InternetAddress( sender ));
			
			s_message.setSubject( (String) pm.get(MSG_FLD_SUBJECT) );
			s_message.setText( (String) pm.get(MSG_FLD_CONTENT) );
			s_message.setSentDate( qms.msg.dateCreate );

			for ( int i = 0; i < al.size(); ++i ) {
				s_message.addRecipient( Message.RecipientType.TO, new InternetAddress( ((ICGenMsgListener)al.get(i)).getChannelAddress(type) ));
			}
			
			// Sending message
			transport.sendMessage( s_message, s_message.getAllRecipients() );
			
		}
	}

	protected void sendFinished() throws Exception {
		if ( transport.isConnected() ) {
			transport.close();
		}
	}

}
