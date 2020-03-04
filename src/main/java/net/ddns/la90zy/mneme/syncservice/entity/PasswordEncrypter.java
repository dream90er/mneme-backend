package net.ddns.la90zy.mneme.syncservice.entity;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import java.util.HashMap;
import java.util.Map;

/**
 * JPA converter for password hashing.
 *
 * @author DreameR
 */
@Converter
public class PasswordEncrypter implements AttributeConverter<String, String> {

    @Inject
    @ConfigProperty(name = "net.ddns.la90zy.mneme.Pbkdf2PasswordHash.Iterations",
            defaultValue = "3072")
    private String hashIterations;

    @Inject
    @ConfigProperty(name = "net.ddns.la90zy.mneme.Pbkdf2PasswordHash.Algorithm",
            defaultValue = "PBKDF2WithHmacSHA512")
    private String hashAlgorithm;

    @Inject
    @ConfigProperty(name = "net.ddns.la90zy.mneme.Pbkdf2PasswordHash.SaltSizeBytes",
            defaultValue = "64")
    private String hashSaltSizeBytes;

    @Inject
    private Pbkdf2PasswordHash pbkdf2PasswordHash;

    @Override
    public String convertToDatabaseColumn(String rawPassword) {
        Map<String, String> parameters= new HashMap<>();
        parameters.put("Pbkdf2PasswordHash.Iterations", hashIterations);
        parameters.put("Pbkdf2PasswordHash.Algorithm", hashAlgorithm);
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", hashSaltSizeBytes);
        pbkdf2PasswordHash.initialize(parameters);
        return pbkdf2PasswordHash.generate(rawPassword.toCharArray());
    }

    @Override
    public String convertToEntityAttribute(String passwordHash) {
        return passwordHash;
    }
}
