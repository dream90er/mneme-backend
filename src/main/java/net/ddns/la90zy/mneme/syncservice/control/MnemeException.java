package net.ddns.la90zy.mneme.syncservice.control;

/**
 * Mneme service base exception.
 *
 * @author DreameR
 */
public class MnemeException extends RuntimeException{

    public MnemeException() { super(); }

    public MnemeException(String message) {
        super(message);
    }
}
