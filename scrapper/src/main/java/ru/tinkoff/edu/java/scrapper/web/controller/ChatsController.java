package ru.tinkoff.edu.java.scrapper.web.controller;

import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.UserInfoResponse;
import ru.tinkoff.edu.java.scrapper.dto.db.DataUserWithInfo;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;

@RestController
//@Slf4j
public class ChatsController {

    private final TgChatService tgChatService;

    public ChatsController(TgChatService tgChatService) {
        this.tgChatService = tgChatService;
    }

    @GetMapping("/tg-chat/{id}")
    public UserInfoResponse getUser(@PathVariable("id") long id) {
        DataUserWithInfo data = tgChatService.getUser(id);
        return new UserInfoResponse(data.getChatId(), data.getUserName(), data.getUserState());
    }

    @PostMapping(path = "/tg-chat/{id}")
    public void registerChat(@PathVariable(name = "id") long id, @RequestBody String userName) {
        tgChatService.register(id, userName);
    }

    @DeleteMapping("/tg-chat/{id}")
    public void deleteChat(@PathVariable("id") long id) {
        tgChatService.unregister(id);
    }

    @PostMapping("tg-chat/update/{id}")
    public void updateUser(@PathVariable("id") long id, @RequestBody String stateUser) {
        tgChatService.updateStateUser(id, stateUser);
    }
}
