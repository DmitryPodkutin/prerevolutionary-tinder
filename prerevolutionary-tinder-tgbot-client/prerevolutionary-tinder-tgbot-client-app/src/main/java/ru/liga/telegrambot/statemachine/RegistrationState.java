package ru.liga.telegrambot.statemachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.service.RegistrationService;
import ru.liga.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.telegrambot.model.StateType;
import ru.liga.telegrambot.sender.TelegramMessageSender;

import java.util.ResourceBundle;

@Component

public class RegistrationState extends AbstractBotState {
    private final ResourceBundle resourceBundle;
    private final TelegramMessageSender telegramMessageSender;
    private final CreateProfileState createProfileState;
    private final RegistrationService registrationService;

    @Autowired
    public RegistrationState(ResourceBundle resourceBundle,
                             TelegramMessageSender telegramMessageSender,
                             CreateProfileState createProfileState, RegistrationService registrationService) {
        super(StateType.REGISTRATION);
        this.resourceBundle = resourceBundle;
        this.telegramMessageSender = telegramMessageSender;
        this.createProfileState = createProfileState;
        this.registrationService = registrationService;
    }

    @Override
    public BotState handleInput(TelegramBotDialogHandler dialogHandler, Update update) {
        final Long chatId = update.getMessage().getChatId();
        final Long userTelegramId = update.getMessage().getFrom().getId();
        final String userInputMessage = update.getMessage().getText();
        if ("/start".equals(userInputMessage)) {
            handleStartCommand(chatId);
        } else {
            final String registerUserAndCheckFormatMessage = tryRegisterUserAndCheckFormat(
                    userTelegramId,
                    userInputMessage);
            if (registerUserAndCheckFormatMessage.isEmpty()) {
                return createProfileState;
            } else {
                handleInvalidFormatMessage(chatId, registerUserAndCheckFormatMessage);
            }
        }
        return this;
    }

    private void handleStartCommand(Long chatId) {
        telegramMessageSender.sendMessage(chatId,
                resourceBundle.getString("registration.start.message"));
    }

    private String tryRegisterUserAndCheckFormat(Long userTelegramId, String userInputMessage) {
        return registrationService.registerUser(userTelegramId, userInputMessage);
    }

    private void handleInvalidFormatMessage(Long chatId, String message) {
        telegramMessageSender.sendMessage(chatId, message);
    }
}
