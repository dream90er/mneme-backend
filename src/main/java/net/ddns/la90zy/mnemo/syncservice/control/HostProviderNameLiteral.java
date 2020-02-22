package net.ddns.la90zy.mnemo.syncservice.control;

import javax.enterprise.util.AnnotationLiteral;

/**
 * Annotation literal for selecting concrete host provider client service by name.
 *
 * @author DreameR
 */
public class HostProviderNameLiteral extends AnnotationLiteral<HostProviderName> implements HostProviderName{

    private final String name;

    public HostProviderNameLiteral(String name) {
        this.name = name;
    }

    @Override
    public String value() {
        return name;
    }
}
