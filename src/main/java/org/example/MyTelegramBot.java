package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class MyTelegramBot extends TelegramLongPollingBot {
    private int menuLevel = 1;
    private static final Dotenv dotenv = Dotenv.load();

    @Override
    public String getBotUsername() {
        return dotenv.get("BOT_USER_NAME");
    }

    @Override
    public String getBotToken() {
        return dotenv.get("BOT_TOKEN");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (text) {
                case "/start", "Назад":
                    menuLevel = 1;
                    updateKeyboard(chatId);
                    break;
                case "Далі":
                    menuLevel = 2;
                    updateKeyboard(chatId);
                    break;
                case "Кнопка 1":
                    sendMessage(chatId, "Кнопка 1");
                    break;
                case "Кнопка 2":
                    sendMessage(chatId, "Кнопка 2");
                    break;
            }
        }
    }

    private void updateKeyboard(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(".");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Кнопка 1"));
        row1.add(new KeyboardButton("Кнопка 2"));

        KeyboardRow row2 = new KeyboardRow();
        if (menuLevel == 1) {
            row2.add(new KeyboardButton("Далі"));
        } else {
            row2.add(new KeyboardButton("Назад"));
        }

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (Exception e) {
            throw new RuntimeException("Cannot send the message");
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (Exception e) {
            throw new RuntimeException("Cannot send the message");
        }
    }
}
