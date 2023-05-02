package ru.tinkoff.app.jdbc;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dto.db.DataUser;

import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JdbcRequestClientRepositoryTest extends JdbcRequestTableRepository {

    public JdbcRequestClientRepositoryTest() throws URISyntaxException {
        super();
    }

    @Transactional
    @Rollback
    @Test
    public void addUser__addUserInDB_CountUserIncrement() {
        List<DataUser> listUsers = userTable.findAllUsers();
        int wasSize = listUsers.size();

        userTable.addUser(CHAT_ID, USER_NAME);

        List<DataUser> listUsersNow = userTable.findAllUsers();
        assert (listUsersNow.size() > 0);
        DataUser dataUser = listUsersNow.get(0);
        assertAll(
                () -> assertEquals(wasSize + 1, listUsersNow.size()),
                () -> assertNotNull(dataUser),
                () -> assertEquals(CHAT_ID, dataUser.getChatId()),
                () -> assertEquals(USER_NAME, dataUser.getUserName())
        );
    }

    @Transactional
    @Rollback
    @Test
    public void removeUser__removeUserInDB__CountUserDecrement() {
        userTable.addUser(CHAT_ID, USER_NAME);
        List<DataUser> listUsersWas = userTable.findAllUsers();
        int wasSize = listUsersWas.size();

        userTable.removeUser(CHAT_ID);

        List<DataUser> listUsers = userTable.findAllUsers();
        assertEquals(listUsers.size(), wasSize - 1);
    }

    @Transactional
    @Rollback
    @Test
    public void findAllUser__addLink_checkedFindThisLink() {
        List<DataUser> listUsersWas = userTable.findAllUsers();
        int wawSize = listUsersWas.size();
        userTable.addUser(CHAT_ID, USER_NAME);

        List<DataUser> listUsers = userTable.findAllUsers();
        assertEquals(wawSize + 1, listUsers.size());
        DataUser dataUser = listUsers.get(0);
        assertAll(
                () -> assertNotNull(dataUser),
                () -> assertEquals(dataUser.getChatId(), CHAT_ID),
                () -> assertEquals(dataUser.getUserName(), USER_NAME)
        );
    }

}