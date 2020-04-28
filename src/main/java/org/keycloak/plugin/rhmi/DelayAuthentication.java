package org.keycloak.plugin.rhmi;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.authenticators.broker.AbstractIdpAuthenticator;
import org.keycloak.authentication.authenticators.broker.util.PostBrokerLoginConstants;
import org.keycloak.authentication.authenticators.broker.util.SerializedBrokeredIdentityContext;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.models.ClientModel;
import org.keycloak.models.FederatedIdentityModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.services.Urls;
import org.keycloak.sessions.AuthenticationSessionModel;
import org.jboss.logging.Logger;


import java.net.URI;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.core.Response;
import org.keycloak.utils.MediaType;

public class DelayAuthentication extends AbstractIdpAuthenticator implements Authenticator {

	private static final String USER_CREATED_ATTRIBUTE = "3scale_user_created";
	private static final String USER_CREATED_VALUE = "true";
	private static final Logger logger = Logger.getLogger(DelayAuthentication.class);

	
	public void close() {
	}
	
	/**
	 * checks whether user has been created in keycloak and received creation attribute
	 * and received the created attribute set to true 
	 * @param user
	 * @return
	 */
	private boolean isUserCreated(UserModel user) {

		if (user == null) {
			logger.debug("User is null");
			return false;
		}

		List<String> listOfAttributeValue = user.getAttribute(DelayAuthentication.USER_CREATED_ATTRIBUTE);
		if (listOfAttributeValue.isEmpty()) {
			logger.debugf("User is created but %s attribute in keycloak is null", DelayAuthentication.USER_CREATED_ATTRIBUTE);
			return false;
		}
		
		if (!listOfAttributeValue.contains(DelayAuthentication.USER_CREATED_VALUE)) {
			logger.debugf("%s attribute value in keycloak does not contain %s", 
					DelayAuthentication.USER_CREATED_ATTRIBUTE, 
					DelayAuthentication.USER_CREATED_VALUE
			);
			return false;
		}
		
		return true;
	}
	


	public boolean requiresUser() {
		return false;
	}

	public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
		return false;
	}

	public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
	}
	

	@Override
	protected void authenticateImpl(AuthenticationFlowContext context, SerializedBrokeredIdentityContext serializedCtx,
			BrokeredIdentityContext brokerContext) {

		if (isUserCreated(context.getUser())) {
			redirectToAfterFirstBrokerLoginSuccess(context, brokerContext);
		} else {
			showAccountProvisioningPage(context);
		}		
	}

	@Override
	protected void actionImpl(AuthenticationFlowContext context, SerializedBrokeredIdentityContext serializedCtx,
			BrokeredIdentityContext brokerContext) {
		
        // get user by federated identity - IdentityBrokerService method authenticated 
        KeycloakSession session = context.getSession();
        FederatedIdentityModel federatedIdentityModel = new FederatedIdentityModel(brokerContext.getIdpConfig().getAlias(), brokerContext.getId(),
        		brokerContext.getUsername(), brokerContext.getToken());
 
        RealmModel realm = context.getRealm();
        
        UserModel federatedUser = session.users().getUserByFederatedIdentity(federatedIdentityModel, realm);
        context.setUser(federatedUser);

        if (isUserCreated(context.getUser())) {
        	redirectToAfterFirstBrokerLoginSuccess(context, brokerContext);
		} else {
			showAccountProvisioningPage(context);
		}
	}
	
	private void showAccountProvisioningPage(AuthenticationFlowContext context) {
		String accessCode = context.generateAccessCode();
		URI action = context.getActionUrl(accessCode);
		String templatedHTML = getTemplateHTML(action);
		
		Response challengeResponse = Response
				.status(Response.Status.OK)
				.type(MediaType.TEXT_HTML_UTF_8)
				.language(Locale.ENGLISH)
				.entity(templatedHTML)
				.build();
        context.challenge(challengeResponse);		
	}
	
	/**
	 * redirects user 
	 * @param context
	 * @param brokerContext
	 */
	private void redirectToAfterFirstBrokerLoginSuccess(AuthenticationFlowContext context, BrokeredIdentityContext brokerContext) {
		
		AuthenticationSessionModel authSession = context.getAuthenticationSession();
		
        SerializedBrokeredIdentityContext serializedCtx = SerializedBrokeredIdentityContext.serialize(brokerContext);
        serializedCtx.saveToAuthenticationSession(authSession, PostBrokerLoginConstants.PBL_BROKERED_IDENTITY_CONTEXT);
        
        // sets AFTER_FIRST_BROKER_LOGIN to true
        authSession.setAuthNote(PostBrokerLoginConstants.PBL_AFTER_FIRST_BROKER_LOGIN, String.valueOf(true));
        
        String authStateNoteKey = PostBrokerLoginConstants.PBL_AUTH_STATE_PREFIX + brokerContext.getIdpConfig().getAlias();
        authSession.setAuthNote(authStateNoteKey, "true");
        
        RealmModel realm = context.getRealm();
        ClientModel authSessionClientModel = authSession.getClient();
        URI baseUri = context.getUriInfo().getBaseUri();
        
        URI redirect = Urls.identityProviderAfterPostBrokerLogin(baseUri, realm.getName(), context.generateAccessCode(), authSessionClientModel.getClientId(), authSession.getTabId());
        Response challengeResponse = Response.status(302)
        		.location(redirect)
        		.build();
        context.challenge(challengeResponse);
	}
	
	private String getTemplateHTML(URI actionURI) {
		return "<!DOCTYPE html><html><head>"
				+ "<meta charset=\"UTF-8\"><meta http-equiv=\"refresh\" content=\"5;" + actionURI + "\" />"
				+ "<title>Red Hat Managed Integrations</title></head>"
				+ "<body>"
				+ "<h1>Your account is being provisioned</h1>"
				+ "<h3>Please wait for a moment...</h3></body></html>";
	}

}
