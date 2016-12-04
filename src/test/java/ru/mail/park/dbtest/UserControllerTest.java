package ru.mail.park.dbtest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.park.model.User.UserCreate;

import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(/*print = MockMvcPrint.NONE*/)
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreate() throws Exception {
        int countUser = 150;
        HashMap<Integer,UserCreate> userHashMap = userCreateHashMap(countUser);

    }

    public HashMap<Integer,UserCreate> userCreateHashMap(int cause) throws Exception{
        HashMap<Integer,UserCreate> userHashMap = new HashMap<>();
        UserCreate user;
        for (int i=0; i<cause; i++){
            user =new UserCreate("user"+i,"example"+i+"@mail.ru","GOD"+hashCode());
            userHashMap.put(i,user);
            mockMvc.perform(post("/api/user/")
                    .content(user.toString())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("code").value(0))
                    .andExpect(jsonPath("response.id").isNumber())
                    .andExpect(jsonPath("response.login").value(user.getLogin()))
                    .andExpect(jsonPath("response.score").value(0));
        }
        return userHashMap;
    }


/*
    @Test
    public HashMap<>

*/
}
