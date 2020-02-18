package net.ddns.la90zy.mnemo;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.FacesConfig;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/login.xhtml",
                useForwardToLogin = false,
                errorPage = ""
        )
)
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "jdbc/MnemoDb",
        callerQuery = "SELECT password FROM mnemo_user WHERE login = ?",
        groupsQuery = "SELECT mnemo_group FROM mnemo_user WHERE login = ?",
        hashAlgorithm = Pbkdf2PasswordHash.class,
        hashAlgorithmParameters = {
                "${applicationConfig.hashAlgorithmParameters}"
        }
)
@FacesConfig
@ApplicationScoped
@Named
public class ApplicationConfig {

        @Inject
        @ConfigProperty(name = "net.ddns.la90zy.mnemo.Pbkdf2PasswordHash.Iterations")
        private String hashIterations = "3072";

        @Inject
        @ConfigProperty(name = "net.ddns.la90zy.mnemo.Pbkdf2PasswordHash.Algorithm")
        private String hashAlgorithm = "PBKDF2WithHmacSHA512";

        @Inject
        @ConfigProperty(name = "net.ddns.la90zy.mnemo.Pbkdf2PasswordHash.SaltSizeBytes")
        private String hashSaltSizeBytes = "64";

        public String[] getHashAlgorithmParameters() {
                return new String[] {"Pbkdf2PasswordHash.Iterations=" + hashIterations,
                        "Pbkdf2PasswordHash.Algorithm=" + hashAlgorithm,
                        "Pbkdf2PasswordHash.SaltSizeBytes=" + hashSaltSizeBytes};
        }
}
