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
		// TODO Auto-generated method stub
		
	}

	public void postInit(KeycloakSessionFactory factory) {
		// TODO Auto-generated method stub
		
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

	public String getId() {
		// TODO Auto-generated method stub
		return "delay-authentication";
	}

	public String getDisplayType() {
		// TODO Auto-generated method stub
		return "Delay Authentication";
	}

	public String getReferenceCategory() {
		// TODO Auto-generated method stub
		return "Delay Authentication";
	}

	/**
	 * Delay needs to be mandatory 
	 */
	public boolean isConfigurable() {
		return false;
	}

	public Requirement[] getRequirementChoices() {
		// TODO Auto-generated method stub
		Requirement[] req = {
				AuthenticationExecutionModel.Requirement.REQUIRED
		};
		return req;
	}

	public boolean isUserSetupAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getHelpText() {
		// TODO Auto-generated method stub
		return "Delay authentication until user is created on 3scale by the rhmi operator";
	}

	public List<ProviderConfigProperty> getConfigProperties() {
		// TODO Auto-generated method stub
		return null;
	}

}
