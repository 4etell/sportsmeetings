package com.foretell.sportsmeetings.util.telegrambot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public void onUpdateReceived(Update update) {
        update.getUpdateId();
        SendMessage.SendMessageBuilder textBuilder = SendMessage.builder();
        SendAnimation.SendAnimationBuilder animationBuilder = SendAnimation.builder();
        String messageText = update.getMessage().getText();
        String chatId = update.getMessage().getChatId().toString();
        textBuilder.chatId(chatId);
        animationBuilder.chatId(chatId);

        if (messageText.contains("/hello")) {
            textBuilder.text("Приветствую смотрящих");
            InputFile papichInputFile = new InputFile();
            papichInputFile.setMedia("https://daniilkaranov.ru/static/media/papich.e6b97668.gif");
            animationBuilder.animation(papichInputFile);
            try {
                execute(textBuilder.build());
                execute(animationBuilder.build());
            } catch (TelegramApiException e) {
                log.error(e.toString());
            }
        }
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
