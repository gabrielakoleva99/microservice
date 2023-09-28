import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.album.AlbumApplication;
import com.microservices.album.controller.AlbumController;
import com.microservices.album.repository.AlbumRepository;
import com.microservices.album.requests.AlbumRequest;
import com.microservices.album.responses.AlbumResponse;
import com.microservices.album.service.AlbumService;
import org.assertj.core.api.Assertions;
import org.bouncycastle.crypto.signers.ISOTrailers;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AlbumApplication.class)
@TestPropertySource(locations = "/test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlbumApplicationTest {

    private static MockMvc mockMvc;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    AlbumService albumService = new AlbumService(restTemplate, albumRepository);

    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AlbumController(albumService)).build();
    }


    @Test
    @Order(1)
    void getWrongWithNonExistingId() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/album/100"))
                .andExpect(status().isNotFound());

    }

    @Test
    @Order(2)
    void getAlbumByIdJson() throws Exception {
        String expectedJson = "{\"id\":1,\"name\":\"Live and More\",\"year\":1968,\"artist\":\"Richard Harris\",\"songs\":[]}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/album/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        System.out.println(responseJson);
        Assertions.assertThat(responseJson).isEqualToIgnoringCase(expectedJson);

        }

        @Test
        @Order(3)
        void getAllAlbumsShouldSucceed() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/album")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        @Order(4)
    void getAllAlbumsReturnsAllAlbums() throws Exception {

        String expectedJson = "[{\"id\":1,\"name\":\"Live and More\",\"year\":1968,\"artist\":\"Richard Harris\",\"songs\":[]},{\"id\":2,\"name\":\"Starland Vocal Band\",\"year\":1976,\"artist\":\"Starland Vocal Band\",\"songs\":[]},{\"id\":3,\"name\":\"Song of Joy\",\"year\":1976,\"artist\":\"Muskrat Love\",\"songs\":[]}]";

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/album"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            System.out.println(responseJson);
            Assertions.assertThat(responseJson).isEqualToIgnoringCase(expectedJson);

        }

        @Test
        @Order(5)
        void registerAlbumWithEmptySongsShouldFail() throws Exception {
            AlbumRequest albumRequest = new AlbumRequest();
            albumRequest.setArtist("Artist");
            albumRequest.setYear(2000);
            albumRequest.setName("Empty Album");
            albumRequest.setSongs(Collections.emptyList());

            mockMvc.perform(MockMvcRequestBuilders.post("/songsWS-gabs-KBE/rest/album")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(albumRequest)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @Order(6)
        void registerAlbumWithEMptyNameShouldFail() throws Exception {
            AlbumRequest albumRequest = new AlbumRequest();
            albumRequest.setArtist("Artist");
            albumRequest.setYear(2000);
            albumRequest.setName(null);
            List<Integer> songs = new ArrayList<>();
            songs.add(4);
            songs.add(3);

            albumRequest.setSongs(songs);

            mockMvc.perform(MockMvcRequestBuilders.post("/songsWS-gabs-KBE/rest/album")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(albumRequest)))
                    .andExpect(status().isNotFound());

        }

        @Test
        @Order(7)
        void registerAlbumShouldSucceed() throws Exception {
            AlbumRequest albumRequest = new AlbumRequest();
            albumRequest.setArtist("Artist");
            albumRequest.setYear(2000);
            albumRequest.setName("Album Name");
            List<Integer> songs = new ArrayList<>();
            songs.add(4);
            songs.add(3);

            albumRequest.setSongs(songs);

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/songsWS-gabs-KBE/rest/album")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(albumRequest)))
                    .andReturn();

            Assertions.assertThat(result).isNotNull();
            String str = result.getResponse().getHeader("location");
            System.out.println(str);

            mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/album/4"))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk());
        }

        @Test
        @Order(8)
        void putAlbumWithNonExistingIdShouldFail() throws Exception {
            AlbumRequest albumRequest = new AlbumRequest();
            albumRequest.setArtist("Artist");
            albumRequest.setYear(2000);
            albumRequest.setName("Album Name");
            List<Integer> songs = new ArrayList<>();
            songs.add(4);
            songs.add(3);

            albumRequest.setSongs(songs);

            mockMvc.perform(MockMvcRequestBuilders.put("/songsWS-gabs-KBE/rest/album/98").contentType(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(albumRequest)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @Order(9)
        void putWithNoSongsShouldFail() throws Exception {
            AlbumRequest albumRequest = new AlbumRequest();
            albumRequest.setArtist("Artist");
            albumRequest.setYear(2000);
            albumRequest.setName("Empty Album");
            albumRequest.setSongs(Collections.emptyList());

            mockMvc.perform(MockMvcRequestBuilders.put("/songsWS-gabs-KBE/rest/album/2").contentType(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(albumRequest)))
                    .andExpect(status().isNotFound());

        }

        @Test
        @Order(10)
        void putWithEmptyAlbumTitleSHouldFail() throws Exception {

            AlbumRequest albumRequest = new AlbumRequest();
            albumRequest.setArtist("Artist");
            albumRequest.setYear(2000);
            albumRequest.setName(null);
            List<Integer> songs = new ArrayList<>();
            songs.add(4);
            songs.add(3);

            albumRequest.setSongs(songs);

            mockMvc.perform(MockMvcRequestBuilders.put("/songsWS-gabs-KBE/rest/album/2").contentType(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(albumRequest)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @Order(11)
        void putAlbumSHouldSucceed() throws Exception {
            AlbumRequest albumRequest = new AlbumRequest();
            albumRequest.setArtist("Artist");
            albumRequest.setYear(2000);
            albumRequest.setName("Album Name");
            List<Integer> songs = new ArrayList<>();
            songs.add(4);
            songs.add(3);

            albumRequest.setSongs(songs);

            mockMvc.perform(MockMvcRequestBuilders.put("/songsWS-gabs-KBE/rest/album/2").contentType(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(albumRequest)))
                    .andExpect(status().isOk());

            mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/album/2")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
    @Order(12)
    void deleteWorks() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/songsWS-gabs-KBE/rest/album/1"))
                    .andExpect(status().isNoContent());

            mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/album/1"))
                    .andExpect(status().isNotFound());
        }

        @Test
    @Order(13)
    void deleteAlbumWithNonExistingIdFails() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/songsWS-gabs-KBE/rest/album/98"))
                    .andExpect(status().isNotFound());
        }


}