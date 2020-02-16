package net.ddns.la90zy.mnemo.youtubeclientservice.control;

import javax.ejb.ApplicationException;

@ApplicationException
public class YoutubeParserException extends RuntimeException{

    public YoutubeParserException() { super(); }

    public YoutubeParserException(String message) {
        super(message);
    }
}
