package ru.liga.telegrambot;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.liga.config.AppConfig;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;

/**
 * Represents the Telegram bot used for Tinder-related interactions.
 */
@Slf4j
@Component
@AllArgsConstructor
public class TinderTelegramBot extends TelegramLongPollingBot {

    private final TelegramBotDialogHandler telegramBotDialogHandler;

    /**
     * Processes the received update from Telegram.
     *
     * @param update The received update.
     */
    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Received an update: {}", update);
        telegramBotDialogHandler.handleUpdate(update);
    }

    /**
     * Retrieves the bot's username.
     *
     * @return The bot's username.
     */
    @Override
    public String getBotUsername() {
        return AppConfig.getInstance().getBotUserName();
    }

    /**
     * Retrieves the bot's token for authentication.
     *
     * @return The bot's token.
     */
    @Override
    public String getBotToken() {
        return AppConfig.getInstance().getBotToken();
    }

    /**
     * Initializes the Telegram bot and registers it with the TelegramBotsApi.
     *
     * @throws TelegramApiException If an error occurs during bot registration.
     */
    public void initializeBot() throws TelegramApiException {
        log.info("Initializing Telegram Bot...");
        final TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(this); // Регистрация вашего бота
            log.info("Telegram Bot successfully registered!");
        } catch (TelegramApiException e) {
            log.error("Failed to register Telegram Bot: {}", e.getMessage());
            throw new TelegramApiException(e);
        }
    }
}
