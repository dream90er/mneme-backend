package net.ddns.la90zy.mnemo.youtubeclientservice.control;

import javax.ejb.ApplicationException;

/**
 *Exception that occurs if {@link YoutubeResponseParser} methods receive bad blank or error response.
 *
 * @author DreameR
 */
@ApplicationException
public class YoutubeParserException extends RuntimeException{

    public YoutubeParserException() { super(); }

    public YoutubeParserException(String message) {
        super(message);
    }
}
