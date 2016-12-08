package ru.mail.park.services;


import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.park.model.User.UserProfile;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class AccountService {
    private final JdbcTemplate template;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(JdbcTemplate template, PasswordEncoder passwordEncoder) {
        this.template = template;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Nullable
    public  UserProfile addUser(String login, String password, String email) {
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
            final String sql = "SELECT `id`,`login`, `score` FROM `Users` WHERE `id` = ?;";
            return template.queryForObject(sql, userProfileRowMapper, id);
        } catch (EmptyResultDataAccessException na) {
            return null;
        }
    }

    public List<String> checkUser(String login, String email){
        List<String> duplicare = new ArrayList<>();
        if(template.queryForObject("SELECT `id` FROM `Users` WHERE `login` = ?;",Integer.class,login)>0)
            duplicare.add("login");
        if(template.queryForObject("SELECT `id` FROM `Users` WHERE `email` = ?;",Integer.class,email)>0)
            duplicare.add("email");

        return duplicare;
    }

    public List<UserProfile> getTop(int limit, int since_id){
        List<UserProfile> top;
        String sqlLimit =(limit>0)?"LIMIT " + limit:"";
        String sql = "SELECT `id`, `login`, `score` FROM `Users` WHERE `id` > ? ORDER BY `score` DESC " + sqlLimit;
        top = template.query(sql, userProfileRowMapper, since_id);
        return top;
    }

    public int addScore(int user_id,int score){
        String sql ="UPDATE `Users` SET `score` = `score` + ? WHERE `id` = ?";
        return template.update(sql,score,user_id);
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
