package ru.mail.park.services;


import org.springframework.stereotype.Service;
import ru.mail.park.model.UserProfile;

import java.util.concurrent.ConcurrentHashMap;


@Service
public class AccountService {
    private ConcurrentHashMap<String, UserProfile> userNameToUser = new ConcurrentHashMap<>();
    private final JdbcTemplate template;
    
    public AccountService(JdbcTemplate template) {
        this.template = template;

    }
    
    public UserProfile addUser(String login, String password, String email) {
        try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(cnctn -> {
                        PreparedStatement ps = cnctn.prepareStatement(
                                "INSERT INTO `Users` (`email`,`login`,`password`) VALUES (?,?,?)",
								Statement.RETURN_GENERATED_KEYS);
                        int index = 0;
                        pst.setInt(++index, email);
                        pst.setInt(++index, login);
                        pst.setString(++index, password);
                        return ps;
                    }
					, keyHolder);
			return new UserProfile(keyHolder.getKey().intValue(),login, email, password);
		} catch (DuplicateKeyException dk) {
			return null;
		}
    }

    public UserProfile getUser(String login) {
        String sql = "SELECT * FROM `Users` WHERE `login` = ?;";
        return template.queryForObject(sql, USER_DETAIL_ALL_ROW_MAPPER, login);
    }

    public void removeUser(String login) {
        if(userNameToUser.containsKey(login)) {
            userNameToUser.remove(login);
        }
    }

    public void changeUser(String oldLogin, String newLogin, String password, String email) {
        if (userNameToUser.containsKey(oldLogin)) {
            UserProfile temp = getUser(oldLogin);
            temp.setEmail(email);
            temp.setLogin(newLogin);
            temp.setPassword(password);
        }
    }

    private final RowMapper<UserDetailAll> USER_DETAIL_ALL_ROW_MAPPER = (rs, rowNum) -> {
		return new UserDetailAll(rs.getInt("id"),
				rs.getString("login"),
				rs.getString("email"),
				rs.getString("password"));
	};
}
