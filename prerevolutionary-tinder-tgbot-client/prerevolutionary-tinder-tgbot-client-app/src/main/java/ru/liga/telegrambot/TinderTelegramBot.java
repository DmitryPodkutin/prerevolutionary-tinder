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

import java.util.ResourceBundle;

/**
 * Represents the Telegram bot used for Tinder-related interactions.
 */
@Slf4j
@Component
@AllArgsConstructor
public class TinderTelegramBot extends TelegramLongPollingBot {

    private final TelegramBotDialogHandler telegramBotDialogHandler;
    private final ResourceBundle logMessages;

    /**
     * Processes the received update from Telegram.
     *
     * @param update The received update.
     */
    @Override
    public void onUpdateReceived(Update update) {
        log.debug(logMessages.getString("debug.update.received"), update);
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
        log.info(logMessages.getString("info.initializing.bot"));
        final TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(this);
            log.info(logMessages.getString("info.bot.registered"));
        } catch (TelegramApiException e) {
            log.error(logMessages.getString("error.bot.registration"), e.getMessage());
            throw new TelegramApiException(e);
        }
    }
}
