package com.leetbot.leet;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    public final String BOT_TOKEN = "7799234900:AAFVtrZmmzEH1uhuIg5TU-cO5wanPO0mn_4";
    public final String BOT_USERNAME = "leet_money_bot";
    public final long OWNER_ID = 1550327653L;

    public int current_message_id = 0;


    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.getMessage().hasText()){
            switch (update.getMessage().getText()){
                case "/app":
                    sendFile(update.getMessage());
                    break;
                case "/start":
                    startText(update.getMessage());
                    logEvent("Пользователь @" + update.getMessage().getFrom().getUserName() + " прописал старт");
                    break;
            }

            if (update.getMessage().getText().startsWith("/msgid")){
                setCurrentMessageId(update.getMessage());
            }
        }
    }
    public void logEvent(String logText){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(OWNER_ID));
        message.setText(logText);
        message.setParseMode(ParseMode.MARKDOWN);
        try{
            execute(message);
        } catch (TelegramApiException e){
            System.out.println(e.getCause());
        }

    }
    public void startText(Message msg){
        String START_TEXT = """
                *Здравствуйте! Я - Leet Bot. Помогу начать зарабатывать на написании отзывов! \s
                На данный момент весь функционал есть только в приложении. \s
                Пропишите /app чтобы получить его. Желаю удачи!*\s
            
                """;
        sendMessage(msg.getChatId(), START_TEXT);
    }

    public void sendMessage(long chatId, String text){
        SendMessage msg = new SendMessage();
        msg.setText(text);
        msg.setChatId(String.valueOf(chatId));
        msg.setParseMode(ParseMode.MARKDOWN);
        try{
            execute(msg);
        } catch (TelegramApiException e) {
            logEvent("Произошла ошибка при отправке msg.");
        }

    }
    public void setCurrentMessageId(Message msg){
        if (msg.getFrom().getId() == OWNER_ID){
            String id = msg.getText().split(" ")[1];
            current_message_id = Integer.parseInt(id);
            logEvent("*Вы изменили messageId. Теперь он равен: *" + current_message_id);

        } else {          logEvent("*Пользователь id:* " + msg.getFrom().getId() + " * username:* @" + msg.getFrom().getUserName() + " пытался изменить messageId");
        }
    }
    public void sendFile(Message msg) {

        ForwardMessage forwardMessage = new ForwardMessage();
        forwardMessage.setChatId(msg.getChatId().toString());  // Установите ID владельца как получателя
        forwardMessage.setFromChatId(String.valueOf(-1002428675938L)); // Установите ID отправителя
        forwardMessage.setDisableNotification(true);
        forwardMessage.setMessageId(current_message_id);
        forwardMessage.setProtectContent(true);// Установите ID сообщения, которое нужно переслать
        try {
            execute(forwardMessage);
        } catch (TelegramApiException e) {
            logEvent("*Пользователь не получил файл. ST: * \n" + Arrays.toString(e.getStackTrace()));
            return;
        }
        logEvent("Пользователь с id " + msg.getFrom().getId() + " username: @" + msg.getFrom().getUserName() + " получил файл.");
    }
}
