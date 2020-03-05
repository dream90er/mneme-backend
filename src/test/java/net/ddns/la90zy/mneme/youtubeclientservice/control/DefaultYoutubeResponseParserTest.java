package net.ddns.la90zy.mneme.youtubeclientservice.control;

import net.ddns.la90zy.mneme.syncservice.entity.Playlist;
import net.ddns.la90zy.mneme.syncservice.entity.Track;
import net.ddns.la90zy.mneme.youtubeclientservice.entity.PlaylistPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonCollectors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultYoutubeResponseParserTest {

    private static final Track TRACK_A = Track.builder()
            .title("TestTitleA")
            .trackIdInHostProvider("TestIdA")
            .description("TestDescriptionA")
            .thumbnail("TestThumbnailA")
            .build();

    private static final Track TRACK_B = Track.builder()
            .title("TestTitleB")
            .trackIdInHostProvider("TestIdB")
            .description("TestDescriptionB")
            .thumbnail("TestThumbnailB")
            .build();

    private DefaultYoutubeResponseParser defaultYoutubeResponseParser = new DefaultYoutubeResponseParser();

    @ParameterizedTest
    @CsvSource({
            "TestTitle, TestOwnerId",
            "AnotherTitle, AnotherId"
    })
    public void shouldParseValidPlaylistInfoYoutubeResponse(String title, String ownerId) {
        //given
        JsonObject response = Helper.createSuccessPlaylistInfoYoutubeResponse(title, ownerId);

        //when
        Playlist playlist = defaultYoutubeResponseParser.parsePlaylistInfo(response);

        //then
        assertEquals(title, playlist.getTitle());
        assertEquals(ownerId, playlist.getOwnerId());
    }

    @Test
    public void shouldParseValidPlaylistPageYoutubeResponse() {
        //given
        JsonObject response = Helper.createSuccessPlaylistPageYoutubeResponse(
                Arrays.asList(TRACK_A, TRACK_B), "TestToken");

        //when
        PlaylistPage playlistPage = defaultYoutubeResponseParser.parsePlaylistPage(response);

        //then
        assertTrue(playlistPage.getTracks().containsAll(Arrays.asList(TRACK_A, TRACK_B)));
        assertTrue(playlistPage.getTracks().size() == 2);
        assertTrue(playlistPage.getNextPageToken().isPresent());
        assertEquals("TestToken", playlistPage.getNextPageToken().get());


    }

    @Test void shouldReturnPlaylistPageWithEmptyNextPageTokenIfLastPage() {
        //given
        JsonObject response = Helper.createSuccessPlaylistPageYoutubeResponse(Collections.emptyList(), "");

        //when
        PlaylistPage playlistPage = defaultYoutubeResponseParser.parsePlaylistPage(response);

        //then
        assertFalse(playlistPage.getNextPageToken().isPresent());
    }

    @Test
    public void shouldThrowExceptionIfEmptyPlaylistInfoYoutubeResponse() {
        //given
        JsonObject response = Helper.createEmptyPlaylistInfoYoutubeResponse();

        //when
        Executable parse = () -> defaultYoutubeResponseParser.parsePlaylistInfo(response);

        //then
        assertThrows(YoutubeParserException.class, parse);
    }

    @Test
    public void shouldThrowExceptionIfEmptyPlaylistPageYoutubeResponse() {
        //given
        JsonObject response = Helper.createEmptyPlaylistPageYoutubeResponse();

        //when
        Executable parsePlaylistPage = () -> defaultYoutubeResponseParser.parsePlaylistPage(response);

        //then
        assertThrows(YoutubeParserException.class, parsePlaylistPage);
    }

    private static class Helper {

        private static JsonObject createSuccessPlaylistInfoYoutubeResponse(String title, String ownerId) {
            return Json.createObjectBuilder()
                    .add("items", Json.createArrayBuilder()
                            .add(Json.createObjectBuilder()
                                    .add("snippet", Json.createObjectBuilder()
                                            .add("title", title)
                                            .add("channelId", ownerId)
                                            .build())
                                    .build())
                            .build())
                    .add("pageInfo", Json.createObjectBuilder()
                            .add("totalResults", 1)
                            .build())
                    .build();
        }

        private static JsonObject createEmptyPlaylistPageYoutubeResponse() {
            return Json.createObjectBuilder()
                    .addNull("error")
                    .build();
        }

        private static JsonObject createEmptyPlaylistInfoYoutubeResponse() {
            return Json.createObjectBuilder()
                    .add("pageInfo", Json.createObjectBuilder()
                            .add("totalResults", 0)
                            .build())
                    .build();
        }

        private static  JsonObject createSuccessPlaylistPageYoutubeResponse(List<Track> tracks, String nextPageToken) {
            JsonArray jsonTracks = tracks.stream()
                    .map(t -> Json.createObjectBuilder()
                            .add("snippet", Json.createObjectBuilder()
                                    .add("resourceId", Json.createObjectBuilder()
                                            .add("videoId", t.getTrackIdInHostProvider())
                                            .build())
                                    .add("title", t.getTitle())
                                    .add("description", t.getDescription().get())
                                    .add("thumbnails", Json.createObjectBuilder()
                                            .add("default", Json.createObjectBuilder()
                                                    .add("url", t.getThumbnail().get())
                                                    .build())
                                            .build())
                                    .build())
                            .build())
                    .collect(JsonCollectors.toJsonArray());
            JsonObject response = Json.createObjectBuilder()
                    .add("items", jsonTracks)
                    .add("pageInfo", Json.createObjectBuilder()
                            .add("totalResults", tracks.size())
                            .build())
                    .build();
            if (!nextPageToken.isEmpty()) {
                response = Json.createObjectBuilder(response)
                        .add("nextPageToken", nextPageToken)
                        .build();
            }
            return response;
        }
    }
}