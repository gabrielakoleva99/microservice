import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.song.SongApplication;
import com.microservices.song.controller.SongController;
import com.microservices.song.controller.SongListController;
import com.microservices.song.model.Song;
import com.microservices.song.repository.SongListRepository;
import com.microservices.song.repository.SongRepository;
import com.microservices.song.request.SongListRequest;
import com.microservices.song.service.SongListService;
import com.microservices.song.service.SongService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SongApplication.class)
@TestPropertySource(locations = "/test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SongListApplicationTest {
    private static MockMvc mockMvc;

    private static MockMvc mockMvc2;

    @Autowired
    private SongListRepository songListRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    SongService songService = new SongService(songRepository);

    @Autowired
    SongListService songListService = new SongListService(songListRepository, songRepository, restTemplate);

    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(new SongListController(songListService)).build();
        mockMvc2 = MockMvcBuilders.standaloneSetup(new SongController(songService)).build();
    }

    @Test
    @Order(1)
    void getWrongWithNonExistingId() throws Exception {
        String token = "N0FlE_wwjoCG2mfqLPYq0NP8TIco8vb-";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/songLists/100")
                        .param("userId", "maxime")
                        .param("password", "pass1234")
                        .header("Authorization", token))
                .andExpect(status().isForbidden());

    }

    @Order(2)
    @Test
    public void testGetSongList_UnsuccessfulAuthorization_ReturnsUnauthorized() throws Exception {

        String token = "N0FlE_wwjoCG2mfqLPYq0NP8TIco8vb-";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);


        mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/songLists/3")
                        .param("userId", "maxime")
                        .param("password", "pass1234")
                        .header("Authorization", token))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(3)
    public void testGetSongListWorksWithSuccessfulAuthorization() throws Exception {

        String token = "yAY-txa3783OSn1Gyjm9Nk69zPkZz55h";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/songLists/3")
                        .param("userId", "jane")
                        .param("password", "pass1234")
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void testGetSongListSearchPrivateSongListShouldFail() throws Exception {

        String janesToken = "yAY-txa3783OSn1Gyjm9Nk69zPkZz55h";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", janesToken);

        mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/songLists/2")
                        .param("userId", "maxime")
                        .param("password", "pass1234")
                        .header("Authorization", janesToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(5)
    void getALlSongListsVonMaximeShouldBeSuccessfull() throws Exception {
        String maximesToken = "N0FlE_wwjoCG2mfqLPYq0NP8TIco8vb-";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", maximesToken);

        mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/songLists")
                        .param("userId", "jane")
                        .param("password", "pass1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", maximesToken))
                .andExpect(status().isOk());

    }

    @Test
    @Order(6)
    void getALlSongListsNonExististingUser() throws Exception {
        String maximesToken = "N0FlE_wwjoCG2mfqLPYq0NP8TIco8vb-";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", maximesToken);

        mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/songLists")
                        .param("userId", "blob")
                        .param("password", "pass1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", maximesToken))
                .andExpect(status().isNotFound());

    }


    @Test
    @Order(7)
    void registerSongListWithValidTokenAndValidRequest() throws Exception {
        String maximesToken = "N0FlE_wwjoCG2mfqLPYq0NP8TIco8vb-";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", maximesToken);

        SongListRequest request = new SongListRequest();
        request.setName("MaximesNewPlaylist");
        request.setPrivate(true);
        request.setOwnerId("maxime");
        List<Song> songs = new ArrayList<>();

        Song song = Song.builder()
                .id(1)
                .title("MacArthur Park")
                .artist("Richard Harris")
                .label("Dunhill Records")
                .released("1968")
                .album("Live and More")
                .build();

        songs.add(song);
        request.setSongs(songs);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/songsWS-gabs-KBE/rest/songLists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .header("Authorization", maximesToken))
                .andReturn();

        Assertions.assertThat(result).isNotNull();
        String str = result.getResponse().getHeader("location");
        System.out.println(str);

        mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/songLists/5")
                        .param("userId", "maxime")
                        .param("password", "pass1234")
                        .header("Authorization", maximesToken))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    void registerSongListWithInvalidToken() throws Exception {
        String maximesTokenInvalid = "N0FlE_wwjoCG2mfqLPYq0NP";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", maximesTokenInvalid);

        SongListRequest request = new SongListRequest();
        request.setName("My Playlist");
        request.setPrivate(false);
        List<Song> songs = new ArrayList<>();
        songs.add(new Song(8, "Song 1", "Artist 1", "label 1", "2000", "Album 1"));
        songs.add(new Song(9, "Song 2", "Artist 2", "label 2", "2006", "Album 2"));
        request.setSongs(songs);

        mockMvc.perform(MockMvcRequestBuilders.post("/songsWS-gabs-KBE/rest/songLists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .header("Authorization", maximesTokenInvalid))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(9)
    void registerSongListWithEmptySongs() throws Exception {
        String maximesToken = "N0FlE_wwjoCG2mfqLPYq0NP8TIco8vb-";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", maximesToken);

        SongListRequest request = new SongListRequest();
        request.setName("My Empty Playlist");
        request.setPrivate(true);
        request.setSongs(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.post("/songsWS-gabs-KBE/rest/songLists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .header("Authorization", maximesToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(10)
    void putSongListWorksReturns200() throws Exception {
        String maximesToken = "N0FlE_wwjoCG2mfqLPYq0NP8TIco8vb-";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", maximesToken);

        SongListRequest request = new SongListRequest();
        request.setName("My Empty Playlist");
        request.setPrivate(true);
        request.setOwnerId("maxime");
        List<Song> songs = new ArrayList<>();

        Song song = Song.builder()
                .id(1)
                .title("MacArthur Park")
                .artist("Richard Harris")
                .label("Dunhill Records")
                .released("1968")
                .album("Live and More")
                .build();

        songs.add(song);
        request.setSongs(songs);
        System.out.println(request);
        mockMvc.perform(MockMvcRequestBuilders.put("/songsWS-gabs-KBE/rest/songLists/1").contentType(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .header("Authorization", maximesToken))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/songLists/1")
                        .param("userId", "maxime")
                        .param("password", "pass1234")
                        .header("Authorization", maximesToken))
                .andExpect(status().isOk());

    }

    @Test
    @Order(11)
    void putSongListNonExistingId() throws Exception {
        String maximesToken = "N0FlE_wwjoCG2mfqLPYq0NP8TIco8vb-";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", maximesToken);

        SongListRequest request = new SongListRequest();
        request.setName("My Empty Playlist");
        request.setPrivate(true);
        request.setOwnerId("maxime");
        List<Song> songs = new ArrayList<>();

        Song song = Song.builder()
                .id(1)
                .title("MacArthur Park")
                .artist("Richard Harris")
                .label("Dunhill Records")
                .released("1968")
                .album("Live and More")
                .build();

        songs.add(song);
        request.setSongs(songs);

        mockMvc.perform(MockMvcRequestBuilders.put("/songsWS-gabs-KBE/rest/songLists/98").contentType(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .header("Authorization", maximesToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(12)
    void putWithEmptySongList() throws Exception {
        String maximesToken = "N0FlE_wwjoCG2mfqLPYq0NP8TIco8vb-";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", maximesToken);

        SongListRequest request = new SongListRequest();
        request.setName("My Empty Playlist");
        request.setPrivate(true);
        request.setSongs(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.put("/songsWS-gabs-KBE/rest/songLists/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .header("Authorization", maximesToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(13)
    void deleteWorks() throws Exception {
        String maximesToken = "N0FlE_wwjoCG2mfqLPYq0NP8TIco8vb-";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", maximesToken);

        mockMvc.perform(MockMvcRequestBuilders.delete("/songsWS-gabs-KBE/rest/songLists/1")
                        .param("userId", "maxime")
                        .param("password", "pass1234")
                        .header("Authorization", maximesToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/songLists/1")
                        .param("userId", "maxime")
                        .param("password", "pass1234")
                        .header("Authorization", maximesToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(9)
    void deleteWrongWithNonExistingId() throws Exception {
        String maximesToken = "N0FlE_wwjoCG2mfqLPYq0NP8TIco8vb-";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", maximesToken);

        mockMvc.perform(MockMvcRequestBuilders.delete("/songsWS-gabs-KBE/rest/songLists/67")
                        .param("userId", "maxime")
                        .param("password", "pass1234")
                        .header("Authorization", maximesToken))
                .andExpect(status().isForbidden());
    }
}




