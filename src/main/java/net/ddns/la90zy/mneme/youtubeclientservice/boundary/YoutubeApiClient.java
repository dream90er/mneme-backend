package net.ddns.la90zy.mneme.youtubeclientservice.boundary;

import net.ddns.la90zy.mneme.syncservice.control.HostProviderNotAvailableException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.net.URI;

/**
 * REST client for processing requests to Youtube API.
 *
 * @author DreameR
 */
@Stateless
public class YoutubeApiClient {

    private Client client;

    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
    }

    /**
     * Process request for given URL query.
     * @param targetUri URL query.
     * @return response from Youtube API in JSON format.
     */
    public JsonObject processRequest(URI targetUri) {
        WebTarget target = client.target(targetUri);
        JsonObject response;
        try {
            response = target.request(MediaType.APPLICATION_JSON).get(JsonObject.class);
        } catch (Exception e) {
            throw new HostProviderNotAvailableException(e.getMessage(), "Youtube");
        }
        return response;
    }

    @PreDestroy
    public void destroy() {
        client.close();
    }
}
