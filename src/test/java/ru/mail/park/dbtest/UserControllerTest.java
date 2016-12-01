package ru.mail.park.dbtest;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.park.model.User.UserCreate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(/*print = MockMvcPrint.NONE*/)
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreate() throws Exception {
        final String user = "{\"login\": \"user1\", \"email\": \"example@mail.ru\", \"password\": \"GOD\" }";
        final UserCreate user1 = new UserCreate("user1","example@mail.ru","GOD");

        mockMvc.perform(post("/api/user/")
                .content(user1.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
               /* .andExpect(jsonPath("response.sessionid").isString())
                .andExpect(jsonPath("response.id").isNumber())*/;

      //  mockMvc.perform(post("/api/session")
      //          .content(user)
      //          .contentType(MediaType.APPLICATION_JSON))
      //          .andExpect(jsonPath("code").value("200"))
              /*  .andExpect(jsonPath("response.sessionid").isNumber())
                .andExpect(jsonPath("response.id").isNumber())*/;

    }

}
