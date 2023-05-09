package ru.tinkoff.edu.java.scrapper.domain.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.domain.jooq.codegen.tables.Client;
import ru.tinkoff.edu.java.scrapper.dto.db.DataUser;
import ru.tinkoff.edu.java.scrapper.dto.db.DataUserWithInfo;

import java.util.List;

import static org.jooq.impl.DSL.count;

@RequiredArgsConstructor
public class JooqRequestClientRepository {
    private final DSLContext dslContext;

    @Transactional
    public void addUser(Long chatId, String userName) {
        if (!userAlreadyRegister(chatId)) {
            dslContext.insertInto(Client.CLIENT, Client.CLIENT.CHAT_ID, Client.CLIENT.USER_NAME)
                    .values(chatId, userName).execute();
        }
    }

    @Transactional
    public void removeUser(Long chatId) {
        dslContext.delete(Client.CLIENT).where(Client.CLIENT.CHAT_ID.eq(chatId)).execute();
    }

    public boolean userAlreadyRegister(Long chatId) {
        Long count = dslContext.select(count(Client.CLIENT.CHAT_ID))
                .from(Client.CLIENT)
                .where(Client.CLIENT.CHAT_ID.eq(chatId))
                .fetchOne(0, Long.class);
        return count > 0;
    }

    public List<DataUser> findAllUsers() {
        List<DataUser> dataUserLinks = dslContext.select(Client.CLIENT.CHAT_ID, Client.CLIENT.USER_NAME)
                .from(Client.CLIENT).fetch().into(DataUser.class);
        return dataUserLinks;
    }

    @Transactional
    public void updateStateUser(Long chatId, String userState) {
        dslContext.update(Client.CLIENT)
                .set(Client.CLIENT.USER_STATE, userState)
                .where(Client.CLIENT.CHAT_ID.eq(chatId)).execute();
    }

    public DataUserWithInfo getUser(long tgChatId) {
        List<DataUserWithInfo> dataUserWithInfo = dslContext.select(Client.CLIENT.CHAT_ID, Client.CLIENT.USER_NAME, Client.CLIENT.USER_STATE)
                .from(Client.CLIENT)
                .where(Client.CLIENT.CHAT_ID.eq(tgChatId))
                .fetch().into(DataUserWithInfo.class);
        if (dataUserWithInfo.size() == 0) {
            return null;
        } else {
            return dataUserWithInfo.get(0);
        }
    }
}
