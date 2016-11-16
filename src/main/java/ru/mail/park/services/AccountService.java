package ru.mail.park.services;


import org.springframework.context.annotation.Bean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mail.park.model.UserProfile;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;


@Service
public class AccountService {
    private final JdbcTemplate template;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
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
                        pst.setString(++index, passwordEncoder().encode(password));
                        return pst;
                    }
					, keyHolder);
			return new UserProfile(keyHolder.getKey().intValue(),login, 0);
		} catch (DuplicateKeyException dk) {
			return null;
		}
    }

    public UserProfile getUser(Integer id) {
        try {
            String sql = "SELECT `id`,`login`, `score` FROM `Users` WHERE `login` = ?;";
            return template.queryForObject(sql, userProfileRowMapper, id);
        } catch (EmptyResultDataAccessException na) {
            return null;
        }
    }

    public Integer getId(String login, String password) {
        try {
            String sql = "SELECT `id`, `password` FROM `Users` WHERE `login` = ? ;";
            Security security = template.queryForObject(sql, securityRowMapper,login);
            if (!passwordEncoder().matches(password, security.getPassword())){
                return -1;
            }
            return security.getId();
        } catch (EmptyResultDataAccessException na) {
            return 0;
        }
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
            new UserProfile(rs.getInt("id") ,rs.getString("login"),rs.getInt("score"));

    private final RowMapper<Security> securityRowMapper = (rs,rowNum)->
            new Security(rs.getInt("id"),rs.getString("password"));

    private static final class Security{
        private final int id;
        private final String password;

        private Security(int id, String password) {
            this.id = id;
            this.password = password;
        }

        public int getId() {
            return id;
        }

        public String getPassword() {
            return password;
        }
    }
}
