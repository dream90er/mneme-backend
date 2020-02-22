package net.ddns.la90zy.mnemo.syncservice.control;

/**
 * Mnemo service base exception.
 *
 * @author DreameR
 */
public class MnemoException extends RuntimeException{

    public MnemoException() { super(); }

    public MnemoException(String message) {
        super(message);
    }
}
