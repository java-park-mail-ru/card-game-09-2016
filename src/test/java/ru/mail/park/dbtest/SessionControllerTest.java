package ru.mail.park.dbtest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.park.model.User.UserAuto;
import ru.mail.park.model.User.UserCreate;
import ru.mail.park.model.User.UserProfile;
import ru.mail.park.main.MainController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(/*print = MockMvcPrint.NONE*/)
@Transactional
public class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    List<UserCreate> userCreateList;
    List<UserAuto> userAutoList;
    Map<String,UserProfile> userMap;
    List<UserProfile> userProfileList;

    @Before
    public void setup() throws Exception {
        int countUser = 5;
        userCreateList = UserControllerTest.userCreateList(countUser);
        userAutoList = new ArrayList<>();
        for (int i =0; i<userCreateList.size();i++){
        UserCreate anUserCreateList = userCreateList.get(i);
            userAutoList.add(i, new UserAuto(anUserCreateList.getLogin(), anUserCreateList.getPassword()));
        }
        userMap = new HashMap<>();
        userProfileList = MainController.getAccountService().getTop(0,0);
        for (UserProfile anUserProfileList : userProfileList)
            userMap.put(anUserProfileList.getLogin(), anUserProfileList);
    }

    @Test
    public void testUserAuto() throws Exception {
        for (UserAuto anUserAutoList : userAutoList) {
            mockMvc.perform(post("/api/session")
                    .content(anUserAutoList.toString())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("code").value(0))
                    .andExpect(jsonPath("response.id").value(userMap.get(anUserAutoList.getLogin()).getId()))
                    .andExpect(jsonPath("response.login").value(userMap.get(anUserAutoList.getLogin()).getLogin()))
                    .andExpect(jsonPath("response.score").value(userMap.get(anUserAutoList.getLogin()).getScore()));
        }
    }


}
