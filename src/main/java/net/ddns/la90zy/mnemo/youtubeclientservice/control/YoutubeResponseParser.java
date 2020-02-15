package net.ddns.la90zy.mnemo.youtubeclientservice.control;

import net.ddns.la90zy.mnemo.syncservice.entity.Playlist;
import net.ddns.la90zy.mnemo.youtubeclientservice.entity.PlaylistPage;

import javax.json.JsonObject;

public interface YoutubeResponseParser {

    PlaylistPage parsePlaylistPage(JsonObject page);
    Playlist parsePlaylistInfo(JsonObject info);
}
