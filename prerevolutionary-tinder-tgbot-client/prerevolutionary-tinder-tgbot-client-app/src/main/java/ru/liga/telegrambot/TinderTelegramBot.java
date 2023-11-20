package ru.liga.telegrambot;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.liga.config.AppConfig;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;

@Component
@AllArgsConstructor
public class TinderTelegramBot extends TelegramLongPollingBot implements BotMessenger {



    private final Logger log = LoggerFactory.getLogger(TinderTelegramBot.class);

    private final TelegramBotDialogHandler telegramBotDialogHandler;

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Received an update: {}", update);
        telegramBotDialogHandler.handleUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return AppConfig.getInstance().getBotUserName();
    }

    @Override
    public String getBotToken() {
        return AppConfig.getInstance().getBotToken();
    }

    public void sendMessage(Long chatId, String text) {
        final SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace(); // Обработка ошибок отправки сообщения
        }
    }

    public void initializeBot() throws TelegramApiException {
        log.info("Initializing Telegram Bot...");
        final TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(this); // Регистрация вашего бота
            log.info("Telegram Bot successfully registered!");
        } catch (TelegramApiException e) {
            log.error("Failed to register Telegram Bot: {}", e.getMessage());
            // Дополнительные действия при неудачной регистрации бота
        }
    }

    @Override
    public void sendMessageWithKeyboard(Long chatId, String text) {

    }
}
