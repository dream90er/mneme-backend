package net.ddns.la90zy.mneme.youtubeclientservice.control;

import net.ddns.la90zy.mneme.syncservice.control.PlaylistNotFoundException;
import net.ddns.la90zy.mneme.syncservice.entity.Playlist;
import net.ddns.la90zy.mneme.syncservice.entity.Track;
import net.ddns.la90zy.mneme.youtubeclientservice.boundary.YoutubeApiClient;
import net.ddns.la90zy.mneme.youtubeclientservice.entity.PlaylistPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.json.Json;
import javax.json.JsonObject;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

import static org.junit.jupiter.api.Assertions.*;

class YoutubeClientServiceTest {

    public static final String PLAYLIST_ID = "playlistId";

    public static final String ACCESS_TOKEN = "accessToken";

    private static final URI REQUEST = URI.create("test.request");

    private static final JsonObject RESPONSE = Json.createObjectBuilder().add("test", "response").build();

    private YoutubeRequestBuilder youtubeRequestBuilder = mock(YoutubeRequestBuilder.class);

    private YoutubeResponseParser youtubeResponseParser = mock(YoutubeResponseParser.class);

    private YoutubeApiClient youtubeApiClient  = mock(YoutubeApiClient.class);

    private YoutubeClientService youtubeClientService = new YoutubeClientService(
            youtubeApiClient,
            youtubeResponseParser,
            youtubeRequestBuilder);

    @BeforeEach
    public void init() {
        when(youtubeRequestBuilder.buildPlaylistInfoRequest(PLAYLIST_ID, ACCESS_TOKEN)).thenReturn(REQUEST);
        when(youtubeRequestBuilder.buildPlaylistPageRequest(PLAYLIST_ID, ACCESS_TOKEN)).thenReturn(REQUEST);
        when(youtubeApiClient.processRequest(REQUEST)).thenReturn(RESPONSE);
    }

    @Test
    public void shouldReturnPlaylistInfo() {
        //given
        Playlist givenPlaylist = mock(Playlist.class);
        given(youtubeResponseParser.parsePlaylistInfo(RESPONSE)).willReturn(givenPlaylist);

        //when
        Playlist returnedPlaylist = youtubeClientService.getPlaylistInfo(PLAYLIST_ID, ACCESS_TOKEN);

        //then
        assertEquals(givenPlaylist, returnedPlaylist);
        verify(youtubeRequestBuilder).buildPlaylistInfoRequest(PLAYLIST_ID, ACCESS_TOKEN);
        verify(youtubeApiClient).processRequest(REQUEST);
        verify(youtubeResponseParser).parsePlaylistInfo(RESPONSE);
        verifyNoMoreInteractions(youtubeApiClient, youtubeRequestBuilder, youtubeResponseParser);
    }

    @Test
    public void  shouldReturnPlaylistTracks() {
        //given
        URI firstPageRequest = URI.create("firstPage.request");
        URI pageBRequest = URI.create("pageB.request");
        JsonObject firstPageResponse = Json.createObjectBuilder().add("page", "first").build();
        JsonObject pageBResponse = Json.createObjectBuilder().add("page", "pageB").build();
        Track testTrackA = mock(Track.class);
        Track testTrackB = mock(Track.class);
        Track testTrackC = mock(Track.class);
        String pageBToken = "pageB";
        PlaylistPage firstPage = PlaylistPage.create(
                Arrays.asList(testTrackA, testTrackB),
                pageBToken);
        PlaylistPage pageB = PlaylistPage.create(
                Arrays.asList(testTrackC),
                "");
        given(youtubeRequestBuilder.buildPlaylistPageRequest(PLAYLIST_ID, ACCESS_TOKEN)).willReturn(firstPageRequest);
        given(youtubeRequestBuilder.buildPlaylistPageRequest(PLAYLIST_ID, pageBToken, ACCESS_TOKEN))
                .willReturn(pageBRequest);
        given(youtubeApiClient.processRequest(firstPageRequest)).willReturn(firstPageResponse);
        given(youtubeApiClient.processRequest(pageBRequest)).willReturn(pageBResponse);
        given(youtubeResponseParser.parsePlaylistPage(firstPageResponse)).willReturn(firstPage);
        given(youtubeResponseParser.parsePlaylistPage(pageBResponse)).willReturn(pageB);

        //when
        List<Track> tracks = youtubeClientService.getPlaylistTracks(PLAYLIST_ID, ACCESS_TOKEN);

        //then
        assertTrue(tracks.containsAll(Arrays.asList(testTrackA, testTrackB, testTrackC)));
    }

    @Test
    public void shouldRequestAllPlaylistPages() {
        //given
        String nextPageToken = "nextPageToken";
        PlaylistPage page = PlaylistPage.create(new ArrayList<Track>(), nextPageToken);
        PlaylistPage lastPage = PlaylistPage.create(new ArrayList<Track>(), "");
        given(youtubeRequestBuilder.buildPlaylistPageRequest(PLAYLIST_ID, nextPageToken, ACCESS_TOKEN))
                .willReturn(REQUEST);
        given(youtubeResponseParser.parsePlaylistPage(RESPONSE)).willReturn(page, page, page, lastPage);

        //when
        youtubeClientService.getPlaylistTracks(PLAYLIST_ID, ACCESS_TOKEN);

        //then
        verify(youtubeRequestBuilder).buildPlaylistPageRequest(PLAYLIST_ID, ACCESS_TOKEN);
        verify(youtubeRequestBuilder, times(3))
                .buildPlaylistPageRequest(PLAYLIST_ID, nextPageToken, ACCESS_TOKEN);
        verify(youtubeApiClient, times(4)).processRequest(any());
        verify(youtubeResponseParser, times(4)).parsePlaylistPage(RESPONSE);
        verifyNoMoreInteractions(youtubeApiClient, youtubeRequestBuilder, youtubeResponseParser);
    }

    @Test
    public void shouldThrowPlaylistNotFoundExceptionIfParserThrowYoutubeParserException() {
        //given
        given(youtubeResponseParser.parsePlaylistPage(RESPONSE)).willThrow(YoutubeParserException.class);
        given(youtubeResponseParser.parsePlaylistInfo(RESPONSE)).willThrow(YoutubeParserException.class);

        //when
        Executable getPlaylistTracks = () -> youtubeClientService.getPlaylistTracks(PLAYLIST_ID, ACCESS_TOKEN);
        Executable getPlaylistInfo = () -> youtubeClientService.getPlaylistInfo(PLAYLIST_ID, ACCESS_TOKEN);

        //then
        assertThrows(PlaylistNotFoundException.class, getPlaylistTracks);
        assertThrows(PlaylistNotFoundException.class, getPlaylistInfo);
    }
}