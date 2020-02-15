package net.ddns.la90zy.mnemo.syncservice.entity;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import java.util.HashMap;
import java.util.Map;

@Converter
public class PasswordEncrypter implements AttributeConverter<String, String> {

    @Inject
    @ConfigProperty(name = "net.ddns.la90zy.mnemo.Pbkdf2PasswordHash.Iterations")
    private String hashIterations = "3072";

    @Inject
    @ConfigProperty(name = "net.ddns.la90zy.mnemo.Pbkdf2PasswordHash.Algorithm")
    private String hashAlgorithm = "PBKDF2WithHmacSHA512";

    @Inject
    @ConfigProperty(name = "net.ddns.la90zy.mnemo.Pbkdf2PasswordHash.SaltSizeBytes")
    private String hashSaltSizeBytes = "64";

    @Inject
    private Pbkdf2PasswordHash pbkdf2PasswordHash;

    @Override
    public String convertToDatabaseColumn(String s) {
        Map<String, String> parameters= new HashMap<>();
        parameters.put("Pbkdf2PasswordHash.Iterations", hashIterations);
        parameters.put("Pbkdf2PasswordHash.Algorithm", hashAlgorithm);
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", hashSaltSizeBytes);
        pbkdf2PasswordHash.initialize(parameters);
        return pbkdf2PasswordHash.generate(s.toCharArray());
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return s;
    }
}
