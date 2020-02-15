package net.ddns.la90zy.mnemo.youtubeclientservice.boundary;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.net.URI;

@Stateless
public class YoutubeApiClient {

    private Client client;

    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
    }

    public JsonObject processRequest(URI targetUri) {
        WebTarget target = client.target(targetUri);
        JsonObject response = target.request(MediaType.APPLICATION_JSON).get(JsonObject.class);
        return response;
    }

    public JsonObject processRequest(URI targetUri, String accessToken) {
        return processRequest(targetUri);
    }

    @PreDestroy
    public void destroy() {
        client.close();
    }
}
