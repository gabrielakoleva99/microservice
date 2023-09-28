
import com.microservices.song.SongApplication;
import com.microservices.song.controller.SongController;
import com.microservices.song.repository.SongRepository;
import com.microservices.song.service.SongService;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;

import java.sql.SQLOutput;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest(classes = SongApplication.class)
@TestPropertySource(locations = "/test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SongApplicationTest {


    private static MockMvc mockMvc;

    @Autowired
    private SongRepository sRepo;

    @Autowired
    SongService songService = new SongService(sRepo);

    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(new SongController(songService)).build();
    }

        @Test
    @Order(1)
    void getAllSongsJson() throws Exception {
        String example = "[{\"id\":1,\"title\":\"MacArthur Park\",\"artist\":\"Richard Harris\",\"label\":\"Dunhill Records\",\"released\":\"1968\",\"album\":\"Live and More\"},{\"id\":2,\"title\":\"Afternoon Delight\",\"artist\":\"Starland Vocal Band\",\"label\":\"Windsong\",\"released\":\"1976\",\"album\":\"Starland Vocal Band\"},{\"id\":3,\"title\":\"Muskrat Love\",\"artist\":\"Captain and Tennille\",\"label\":\"A&M\",\"released\":\"1976\",\"album\":\"Song of Joy\"}]";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rest/songs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();


        String str = result.getResponse().getContentAsString();
        Assertions.assertThat(str).isEqualToIgnoringCase(example);

    }

    @Test
    @Order(2)
    void getSongByIdJson() throws Exception {
        String expectedJson = "{\"id\":1,\"title\":\"MacArthur Park\",\"artist\":\"Richard Harris\",\"label\":\"Dunhill Records\",\"released\":\"1968\",\"album\":\"Live and More\"}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rest/songs/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        Assertions.assertThat(responseJson).isEqualToIgnoringCase(expectedJson);
    }

    @Test
    @Order(3)
    void getSongByIdJsonWrongId() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/songs/100"))
                .andExpect(status().isNotFound());
    }

        @Test
    @Order(4)
    void postCorrectReturnUrl() throws Exception {
        String example = "{\"title\":\"Wrecking Ball\",\"artist\":\"MILEY CYRUS\",\"label\":\"RCA\",\"released\":2013}    \n";
        String expected = "Location: /songsWS-gabs-KBE/rest/songs?songId=4";
        MvcResult result =  mockMvc.perform(MockMvcRequestBuilders.post("/rest/songs").contentType(MediaType.APPLICATION_JSON)
                        .content(example))
                .andReturn();
        Assertions.assertThat(result).isNotNull();
        String str = result.getResponse().getHeader("location");
        Assertions.assertThat(str).isEqualToIgnoringCase(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/rest/songs/4")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

    }

       @Test
    @Order(5)
    void postWrongWithoutTitle() throws Exception{
        String example = "{\"artist\":\"MILEY CYRUS\",\"label\":\"RCA\",\"released\":2013}    \n";
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/songs").contentType(MediaType.APPLICATION_JSON)
                        .content(example))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Order(6)
    void putWorks200() throws Exception{
        String example = "{\"id\":\"1\",\"title\":\"Wrecking Ball\",\"artist\":\"MILEY CYRUS\",\"label\":\"RCA\",\"released\":2013}    \n";
        mockMvc.perform(MockMvcRequestBuilders.put("/rest/songs/1").contentType(MediaType.APPLICATION_JSON)
                        .content(example))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/rest/songs/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

    }

        @Test
    @Order(7)
    void putWrongWithNonExistingIdInURL() throws Exception{
        String example = "{\"id\":\"1\",\"title\":\"Wrecking Ball\",\"artist\":\"MILEY CYRUS\",\"label\":\"RCA\",\"released\":2013}    \n";
        mockMvc.perform(MockMvcRequestBuilders.put("/rest/songs/50").contentType(MediaType.APPLICATION_JSON)
                        .content(example))
                .andExpect(status().isNotFound());
    }

        @Test
    @Order(8)
    void deleteWorks204() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/songs/4"))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/rest/songs/4"))
                .andExpect(status().isNotFound());

    }

        @Test
    @Order(9)
    void deleteWrongWithNonExistingId() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/songs/20"))
                .andExpect(status().isNotFound());
    }
    }






