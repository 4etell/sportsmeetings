package com.foretell.sportsmeetings.util.telegrambot;

import com.foretell.sportsmeetings.exception.notfound.UserNotFoundException;
import com.foretell.sportsmeetings.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {


    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    private final UserService userService;

    public TelegramBot(UserService userService) {
        this.userService = userService;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        update.getUpdateId();
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder();
        SendAnimation.SendAnimationBuilder animationBuilder = SendAnimation.builder();
        String messageText = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        String stringChatId = chatId.toString();
        messageBuilder.chatId(stringChatId);
        animationBuilder.chatId(stringChatId);
        List<String> splitMessageText = Arrays.asList(messageText.split(" "));

        if (messageText.contains("/hello")) {
            messageBuilder.text("Приветствую смотрящих");
            InputFile papichInputFile = new InputFile();
            papichInputFile.setMedia("https://daniilkaranov.ru/static/media/papich.e6b97668.gif");
            animationBuilder.animation(papichInputFile);
            execute(messageBuilder.build());
            execute(animationBuilder.build());
        } else if (splitMessageText.size() == 2 && splitMessageText.get(0).contains("/активировать")) {
            String activationCode = splitMessageText.get(1);
            try {
                if (userService.activateTelegramBot(activationCode, chatId)) {
                    messageBuilder.text("Вы успешно подключили свой аккаунт SportsMeetings");
                    execute(messageBuilder.build());
                }
            } catch (UserNotFoundException e) {
                messageBuilder.text("Пользователь с таким кодом не найден");
                execute(messageBuilder.build());
            }
        }
    }

    @SneakyThrows
    public void sendNewRequestToMeetingNotification(Long chatId) {
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder();
        messageBuilder.chatId(chatId.toString());
        messageBuilder.text("У вас новый запрос на вступление в вашу встречу, " +
                "зайдите на сайт, чтобы узнать подробности: https://daniilkaranov.ru/static/media/papich.e6b97668.gif");
        execute(messageBuilder.build());
    }

    @SneakyThrows
    public void sendRequestAcceptedNotification(Long chatId) {
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder();
        messageBuilder.chatId(chatId.toString());
        messageBuilder.text("Вас приняли во встречу, " +
                "зайдите на сайт, чтобы узнать подробности: https://daniilkaranov.ru/static/media/papich.e6b97668.gif");
        execute(messageBuilder.build());
    }

    @SneakyThrows
    public void sendStartMeetingNotification(Long chatId, String date) {
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder();
        messageBuilder.chatId(chatId.toString());
        messageBuilder.text("Не забудьте, у вас встреча " + (date) + " " +
                "зайдите на сайт, чтобы узнать подробности: https://daniilkaranov.ru/static/media/papich.e6b97668.gif");
        execute(messageBuilder.build());
        log.info("StartMeetingNotification sent");
    }

    @SneakyThrows
    public void sendRateMeetingNotification(Long chatId) {
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder();
        messageBuilder.chatId(chatId.toString());
        messageBuilder.text("У вас только что закончилась встреча, оставьте отзыв на странице организатора: " +
                "https://daniilkaranov.ru/static/media/papich.e6b97668.gif");
        execute(messageBuilder.build());
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
