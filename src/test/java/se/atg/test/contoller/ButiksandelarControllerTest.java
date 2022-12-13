package se.atg.test.contoller;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import se.atg.test.ButiksandelarApplication;
import se.atg.test.service.GamesSortService;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ButiksandelarApplication.class)
@AutoConfigureMockMvc
public class ButiksandelarControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private GamesSortService gamesSortService;

    @Test
    public void getStatus() {
    }


    @Ignore
    public void givenEmployees_whenGetEmployees_thenStatus200()
            throws Exception {

        mvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is("bob")));
    }
}