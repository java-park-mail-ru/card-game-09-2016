package ru.mail.park.services;


import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.mail.park.model.UserProfile;
import java.sql.PreparedStatement;
import java.sql.Statement;


@Service
public class AccountService {
    private final JdbcTemplate template;
    
    public AccountService(JdbcTemplate template) {
        this.template = template;

    }
    
    public UserProfile addUser(String login, String password, String email) {
        try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(cnctn -> {
                        PreparedStatement pst = cnctn.prepareStatement(
                                "INSERT INTO `Users` (`email`,`login`,`password`) VALUES (?,?,?)",
								Statement.RETURN_GENERATED_KEYS);
                        int index = 0;
                        pst.setString(++index, email);
                        pst.setString(++index, login);
                        pst.setString(++index, password);
                        return pst;
                    }
					, keyHolder);
			return new UserProfile(keyHolder.getKey().intValue(),login, email, password);
		} catch (DuplicateKeyException dk) {
			return null;
		}
    }

    public UserProfile getUser(String login) {
        String sql = "SELECT * FROM `Users` WHERE `login` = ?;";
        return template.queryForObject(sql, userProfileRowMapper, login);
    }

    public int removeUser(String login) {
        try {
            String sql = "DELETE  FROM `Users` WHERE `login` = ?;";
            return template.update(sql, login);
        } catch (EmptyResultDataAccessException na){
            return 0;
        }
    }

    public int changeUser(String oldLogin, String newLogin, String password, String email) {
        try {
            String sql = "UPDATE `Users` SET `login` = ?, `email` = ?, `password` = ?  WHERE `login` = ?;";
            return template.update(sql,newLogin,email, password, oldLogin);
        } catch (EmptyResultDataAccessException na){
            return 0;
        }catch (DuplicateKeyException dk) {
            return -1;
        }
    }

    private final RowMapper<UserProfile> userProfileRowMapper = (rs, rowNum) ->
            new UserProfile(rs.getInt("id"),
                rs.getString("login"),
                rs.getString("email"),
                rs.getString("password"));
}
