package se.atg.test.contoller;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import se.atg.test.ButiksandelarApplication;
import se.atg.test.util.FixedClockConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = {ButiksandelarApplication.class, FixedClockConfig.class})
@AutoConfigureMockMvc
public class ButiksandelarControllerTest {

    @Autowired
    private MockMvc mvc;


    @Test
    public void givenCorrectGamesEventData_thenStatus200() throws Exception {
        String content = """
                [
                  {
                    "name": "Monday: V64",
                    "type": "V64",
                    "start": "2022-12-12T09:30:00"
                  },
                  {
                    "name": "Thursday: V64",
                    "type": "V64",
                    "start": "2022-12-15T09:30:00"
                  }
                 ]""";

        mvc.perform(post("/")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0]", Matchers.equalTo("V64(Monday)")))
                .andExpect(jsonPath("$[1]", Matchers.equalTo("V64(Thursday)")));
        // .andDo(print());
    }


    @Test
    public void givenCorrectGamesEventDataFromInputJsonFile_thenStatus200() throws Exception {
        Path path = Paths.get("src/test/resources/input.json");
        String content = String.join("", Files.readAllLines(path));

        mvc.perform(post("/")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(11))))
                .andExpect(jsonPath("$[0]", Matchers.equalTo("V64(Monday)")))
                .andExpect(jsonPath("$[1]", Matchers.equalTo("V86(Wednesday)")))
                .andExpect(jsonPath("$[2]", Matchers.equalTo("V75(Saturday)")))
                .andExpect(jsonPath("$[3]", Matchers.equalTo("GS75(Sunday)")))
                .andExpect(jsonPath("$[4]", Matchers.equalTo("V64(Tuesday)")))
                .andExpect(jsonPath("$[5]", Matchers.equalTo("V64(Thursday)")))
                .andExpect(jsonPath("$[6]", Matchers.equalTo("V86(Friday)")))
                .andExpect(jsonPath("$[7]", Matchers.equalTo("V86(Wednesday w2)")))
                .andExpect(jsonPath("$[8]", Matchers.equalTo("V64(Monday w2)")))
                .andExpect(jsonPath("$[9]", Matchers.equalTo("V64(Tuesday w2)")))
                .andExpect(jsonPath("$[10]", Matchers.equalTo("V64(Thursday w2)")));
        // .andDo(print());
    }


    @Test
    public void givenPastGamesEventData_thenStatus200() throws Exception {
        String content = """
                [
                  {
                    "name": "Monday: V64",
                    "type": "V64",
                    "start": "2022-12-10T09:30:00"
                  },
                  {
                    "name": "Thursday: V64",
                    "type": "V64",
                    "start": "2022-12-11T09:30:00"
                  }
                 ]""";

        mvc.perform(post("/")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))))
                .andDo(print());
    }

    @Test
    public void givenMissingGamesEventData_thenStatus400() throws Exception {
        mvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.detail", Matchers.equalTo("Failed to read request")))
                .andExpect(status().isBadRequest()).andDo(print());
    }
}