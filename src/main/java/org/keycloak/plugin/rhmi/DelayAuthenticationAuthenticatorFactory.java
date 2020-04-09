package org.keycloak.plugin.rhmi;

import java.util.List;

import org.keycloak.Config.Scope;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.AuthenticationExecutionModel.Requirement;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

public class DelayAuthenticationAuthenticatorFactory implements AuthenticatorFactory {

	
    private static final DelayAuthentication SINGLETON = new DelayAuthentication();

    public Authenticator create(KeycloakSession session) {
        return SINGLETON;
	}

	public void init(Scope config) {		
	}

	public void postInit(KeycloakSessionFactory factory) {
	}

	public void close() {
	}

	public String getId() {
		return "delay-authentication";
	}

	public String getDisplayType() {
		return "Delay Authentication";
	}

	public String getReferenceCategory() {
		return "Delay Authentication";
	}

	/**
	 * Delay needs to be mandatory 
	 */
	public boolean isConfigurable() {
		return false;
	}

	public Requirement[] getRequirementChoices() {
		Requirement[] req = {
				AuthenticationExecutionModel.Requirement.REQUIRED
		};
		return req;
	}

	public boolean isUserSetupAllowed() {
		return false;
	}

	public String getHelpText() {
		return "Delay authentication until user is created on 3scale by the rhmi operator";
	}

	public List<ProviderConfigProperty> getConfigProperties() {
		return null;
	}

}
