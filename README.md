# KeyCloak Delay Authentication Flow

Plugin to delay authentication of users until they get created in Keycloak and added in 3scale by RHMI operator.

# System Requirements

You need to have Keycloak 3.2.0.Final running.

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later.

# Build and Deploy

### Build the plugin

To build the plugin you need to first intall the dependencies:

`$ mvn install` 

and then gerenate the `.jar` file:

`$ mvn package`

Once the plugin is built you should be able to find the .jar file in `./target/<projectname>.jar`.

### Install Keycloak locally using Docker Compose

There is a `docker-compose.yaml` file available in the root of the project that can be used to install Keycloak locally with Maria DB and deploy the plugin.

Steps to install Keycloak locally:

1) Open the terminal and navigate to the root of the project.
2) Run ```docker-compose up```.
3) When keycloak finishes the install open http://0.0.0.0:8080/ in a browser and it you should be able to see keycloak login screen.
4) Admin credentials for Keycloak are set in the docker-compose.yaml file `KEYCLOAK_USER=admin-test` and `KEYCLOAK_PASSWORD=admin-test`.

### Deploy the plugin 

To deploy the plugin you need to copy the `.jar` file to `/opt/jboss/keycloak/providers` in Keycloak.

PS.: this directory is mapped in the docker-compose.yaml file available in the repo, just create a `./plugin` directory in the root of the project and add the `.jar` file there. 
