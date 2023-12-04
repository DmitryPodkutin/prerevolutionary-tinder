package ru.liga.telegrambot.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.integration.service.RegistrationService;
import ru.liga.repository.UserStateRepository;
import ru.liga.service.UserService;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.model.StateType;
import ru.liga.telegrambot.sender.TelegramMessageSender;

import java.util.ResourceBundle;

/**
 * Represents the state responsible for handling the registration process in the Telegram bot.
 */
@Slf4j
@Component
public class RegistrationState extends AbstractBotState {
    private final ResourceBundle resourceBundle;
    private final TelegramMessageSender telegramMessageSender;
    private final RegistrationService registrationService;
    private final ResourceBundle logMessages;

    @Autowired
    public RegistrationState(ResourceBundle resourceBundle,
                             TelegramMessageSender telegramMessageSender,
                             RegistrationService registrationService,
                             UserService userService,
                             UserStateRepository userStateRepository,
                             ResourceBundle logMessages) {
        super(StateType.REGISTRATION, userService, userStateRepository);
        this.resourceBundle = resourceBundle;
        this.telegramMessageSender = telegramMessageSender;
        this.registrationService = registrationService;
        this.logMessages = logMessages;
    }

    /**
     * Handles the input during the registration process.
     *
     * @param dialogHandler The dialog handler.
     * @param update        The received update.
     * @return The next bot state.
     */
    @Override
    public BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        log.debug(logMessages.getString("hande.registration.input"));
        final Long chatId = update.getMessage().getChatId();
        final Long userTelegramId = update.getMessage().getFrom().getId();
        final String userInputMessage = update.getMessage().getText();
        if ("/start".equals(userInputMessage)) {
            handleStartCommand(chatId);
        } else {
            final String registerUserAndCheckFormatMessage = tryRegisterUserAndCheckFormat(
                    userTelegramId,
                    userInputMessage);
            if (registerUserAndCheckFormatMessage == null || registerUserAndCheckFormatMessage.isEmpty()) {
                return goToNextStep(StateType.CREATE_PROFILE, dialogHandler, update);
            } else {
                handleInvalidFormatMessage(chatId, registerUserAndCheckFormatMessage);
                return this;
            }
        }
        return this;
    }

    private void handleStartCommand(Long chatId) {
        telegramMessageSender.sendTextMessage(chatId,
                resourceBundle.getString("registration.start.message"));
    }

    private String tryRegisterUserAndCheckFormat(Long userTelegramId, String userInputMessage) {
        return registrationService.registerUser(userTelegramId, userInputMessage);
    }

    private void handleInvalidFormatMessage(Long chatId, String message) {
        telegramMessageSender.sendTextMessage(chatId, message);
    }
}
