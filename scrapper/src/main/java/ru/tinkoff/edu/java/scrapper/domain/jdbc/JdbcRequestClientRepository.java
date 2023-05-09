package ru.tinkoff.edu.java.scrapper.domain.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dto.db.DataUser;
import ru.tinkoff.edu.java.scrapper.dto.db.DataUserWithInfo;

import java.util.List;

@RequiredArgsConstructor
public class JdbcRequestClientRepository {

    private final JdbcTemplate template;
    private final static String DEFAULT_USER_STATE = "NONE";

    @Transactional
    public void addUser(Long chatId, String userName) {
        if (!userAlreadyRegister(chatId)) {
            template.update("""
                    insert into client (chat_id, user_name, user_state)
                    values (?, ?, ?)""", chatId, userName, DEFAULT_USER_STATE
            );
        }
    }

    @Transactional
    public void removeUser(Long chatId) {
        template.update("""
                delete from client where chat_id = ?""", chatId
        );
    }

    public List<DataUser> findAllUsers() {
        return template.query("select chat_id, user_name from client", new BeanPropertyRowMapper<>(DataUser.class));
    }

    public boolean userAlreadyRegister(Long chatId) {
        Integer count = template.queryForObject("""
                select count(chat_id) from client
                where chat_id = ?""", Integer.class, chatId
        );
        return count > 0;
    }

    @Transactional
    public void updateStateUser(Long chatId, String userState) {
        template.update("""
                update client set user_state = ?
                where chat_id = ?""", userState, chatId
        );
    }

    public DataUserWithInfo getUser(long tgChatId) {
        List<DataUserWithInfo> dataUserWithInfo = template.query("""
                select chat_id, user_name, user_state
                from client
                where chat_id = ?""", new BeanPropertyRowMapper<>(DataUserWithInfo.class), tgChatId);
        if (dataUserWithInfo.size() == 0) {
            return null;
        } else {
            return dataUserWithInfo.get(0);
        }
    }
}
