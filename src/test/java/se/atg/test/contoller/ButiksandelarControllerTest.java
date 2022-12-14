package se.atg.test.contoller;

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

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
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
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    public void givenMissingGamesEventData_thenStatus400() throws Exception {
        mvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}