package uz.glasspro;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.glasspro.controller.UserController;
import uz.glasspro.dto.UserDTO;
import uz.glasspro.util.ReplyKeyboardUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class TelegramBot extends TelegramLongPollingBot {

    private final Dotenv dotenv = Dotenv.configure().filename(".env").load();

    @Autowired
    private UserController userController;
    @Override
    public String getBotUsername() {
        return dotenv.get("BOT_USERNAME");
    }

    @Override
    public String getBotToken() {
        return dotenv.get("BOT_TOKEN");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.enableHtml(true);

            SendContact sendContact = new SendContact();
            sendContact.setChatId(chatId);
            switch (messageText){
                case "/start":
                    startCommandReceived(update.getMessage().getChat().getFirstName(), message);
                    break;
                default:
                    message.setText("Cannot process it now");
                    sendContent(message);
            }
        }else if(update.getMessage().hasContact()){
            UserDTO currentUser = new UserDTO();
            Contact contact = update.getMessage().getContact();
            currentUser.setId(update.getMessage().getChat().getId());
            currentUser.setFirstName(update.getMessage().getChat().getFirstName());
            currentUser.setUserName(update.getMessage().getChat().getUserName());
            currentUser.setLastName(update.getMessage().getChat().getLastName());
            currentUser.setPhoneNumber(contact.getPhoneNumber());
            userController.createUser(currentUser);
            System.out.println(contact);
        }
    }


    private void startCommandReceived(String firstName, SendMessage message) {
        String greeting = "Здраствуйте <b>"+firstName+"</b>\n"+
                "Добро пожаловать в бот <b>Glass Pro</b>";
        message.setText(greeting);
        sendContent(message);
        String contactRequest = "\uD83D\uDCF1 Отправьте свой номер телефона";
        message.setText(contactRequest);
        message.setReplyMarkup(ReplyKeyboardUtil.sendPhone());
        sendContent(message);

    }


    private void sendContent(Object content) {
        try {
            if(content instanceof  SendMessage){
                execute((SendMessage) content);
            }
            if(content instanceof SendContact){
                execute((SendContact) content);
            }
        }catch (TelegramApiException e){
            log.error("Sending content = "+ e.getMessage());
        }
    }
}
