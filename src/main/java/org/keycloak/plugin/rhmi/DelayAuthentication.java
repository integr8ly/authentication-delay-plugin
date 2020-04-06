package org.keycloak.plugin.rhmi;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

public class DelayAuthentication implements Authenticator {

	public void close() {
		// TODO Auto-generated method stub

	}

	public void authenticate(AuthenticationFlowContext context) {
		// TODO Auto-generated method stub

	}

	public void action(AuthenticationFlowContext context) {
		// TODO Auto-generated method stub

	}

	public boolean requiresUser() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
		// TODO Auto-generated method stub

	}

}
