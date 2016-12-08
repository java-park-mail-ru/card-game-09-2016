package ru.mail.park.dbtest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.park.model.User.UserCreate;
import ru.mail.park.model.User.UserProfile;
import ru.mail.park.main.MainController;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    public void testUserCreate() throws Exception {
        int countUser = 150;
        userCreateHashMap(countUser);
    }

    @Test
    public void testUserDefault() throws Exception{
        int countUser = 150;
        userCreateHashMap(countUser);
        userDefault();
    }

    @Test
    public void testUserTop() throws Exception{
        int countUser = 150;
        int maxLimit = 25;
        int maxRequest = 10;
        Random random = new Random();
        userCreateHashMap(countUser);
        List<UserProfile> users;
        for (int i=0,limit,since; i<maxRequest; i++){
            limit = random.nextInt(maxLimit);
            since = random.nextInt(countUser);
            users = MainController.getAccountService().getTop(limit, since);
            ResultActions result = mockMvc.perform(get("/api/top")
                    .param("limit", String.valueOf(limit))
                    .param("since_id", String.valueOf(since)));
                result.andExpect(jsonPath("code").value(0));
            for (int j =0 ; j<users.size(); j++){
                result.andExpect(jsonPath("response.["+j+"].id").value(users.get(j).getId()))
                      .andExpect(jsonPath("response.["+j+"].login").value(users.get(j).getLogin()))
                      .andExpect(jsonPath("response.["+j+"].score").value(users.get(j).getScore()));
            }
        }

    }

    public HashMap<String,UserCreate> userCreateHashMap(int cause) throws Exception{
        HashMap<String,UserCreate> userHashMap = new HashMap<>();
        UserCreate user;
        for (int i=0; i<cause; i++){
            user =new UserCreate("user"+i,"example"+i+"@mail.ru","GOD"+hashCode());
            userHashMap.put(user.getLogin(),user);
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

    public List<UserProfile> userDefault() throws Exception {
        List<UserProfile> allUser = MainController.getAccountService().getTop(0, 0);
        for (UserProfile anAllUser : allUser) {
            mockMvc.perform(get("/api/user/"+String.valueOf(anAllUser.getId())))
                    .andExpect(jsonPath("code").value(0))
                    .andExpect(jsonPath("response.id").value(anAllUser.getId()))
                    .andExpect(jsonPath("response.login").value(anAllUser.getLogin()))
                    .andExpect(jsonPath("response.score").value(anAllUser.getScore()));
        }
        return allUser;
    }
}
