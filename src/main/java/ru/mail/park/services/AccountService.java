package ru.mail.park.services;


import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.park.model.UserProfile;
import java.sql.PreparedStatement;
import java.sql.Statement;

@Transactional
@Service
public class AccountService {
    private final JdbcTemplate template;
    private final PasswordEncoder passwordEncoder;

    public AccountService(JdbcTemplate template, PasswordEncoder passwordEncoder) {
        this.template = template;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Nullable
    public UserProfile addUser(String login, String password, String email) {
        try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(cnctn -> {
                        PreparedStatement pst = cnctn.prepareStatement(
                                "INSERT INTO `Users` (`email`,`login`,`password`) VALUES (?,?,?)",
								Statement.RETURN_GENERATED_KEYS);
                        pst.setString(1, email);
                        pst.setString(2, login);
                        pst.setString(3, passwordEncoder.encode(password));
                        return pst;
                    }
					, keyHolder);
			return new UserProfile(keyHolder.getKey().intValue(),login, 0);
		} catch (DuplicateKeyException dk) {
			return null;
		}
    }

    @Nullable
    public UserProfile getUser(Integer id) {
        try {
            final String sql = "SELECT `id`,`login`, `score` FROM `Users` WHERE `login` = ?;";
            return template.queryForObject(sql, userProfileRowMapper, id);
        } catch (EmptyResultDataAccessException na) {
            return null;
        }
    }

    public Integer getId(String login, String password) {
        try {
            final String sql = "SELECT `id`, `password` FROM `Users` WHERE `login` = ? ;";
            final Security security = template.queryForObject(sql, securityRowMapper,login);
            if (!passwordEncoder.matches(password, security.getPassword())){
                return -1;
            }
            return security.getId();
        } catch (EmptyResultDataAccessException na) {
            return 0;
        }
    }
    public int removeUser(String login) {
        final String sql = "DELETE  FROM `Users` WHERE `login` = ?;";
        return template.update(sql, login);
    }

    public int changeUser(String oldLogin, String newLogin, String password, String email) {
        try {
            final String sql = "UPDATE `Users` SET `login` = ?, `email` = ?, `password` = ?  WHERE `login` = ?;";
            return template.update(sql,newLogin,email, passwordEncoder.encode(password), oldLogin);
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
