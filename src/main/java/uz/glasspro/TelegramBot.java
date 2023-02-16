package uz.glasspro;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.glasspro.controller.UserController;
import uz.glasspro.dto.UserDTO;
import uz.glasspro.enums.RoleEnum;
import uz.glasspro.util.ReplyKeyboardUtil;
import uz.glasspro.util.ReplyKeyboardConstants;

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
        if(update.hasMessage()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.enableHtml(true);

            if(update.getMessage().hasText()){
                SendContact sendContact = new SendContact();
                sendContact.setChatId(chatId);
                Chat chat = update.getMessage().getChat();
                switch (messageText){
                    case "/start":
                        startCommandReceived(chat.getFirstName(), chat.getId(), message);
                        break;
                    case ReplyKeyboardConstants.DELETE_USER:
                        removeUserPage(message, messageText);
                        break;
                    case ReplyKeyboardConstants.ABOUT_US:
                        aboutUsPage(message, chatId);
                        break;
                    case ReplyKeyboardConstants.SETTINGS:
                        settingsPage(message, chatId);
                        break;
                    case ReplyKeyboardConstants.ORDER:
                        makeOrder(message, chatId);
                        break;
                    case ReplyKeyboardConstants.ORDER_HISTORY:
                        orderHistory(message, chatId);
                    default:
                        message.setText("Cannot process it now");
                        sendContent(message);
                }
            }
            if(update.getMessage().hasContact()){
                UserDTO currentUser = new UserDTO();
                Contact contact = update.getMessage().getContact();
                currentUser.setId(update.getMessage().getChat().getId());
                currentUser.setFirstName(update.getMessage().getChat().getFirstName());
                currentUser.setUserName(update.getMessage().getChat().getUserName());
                currentUser.setLastName(update.getMessage().getChat().getLastName());
                currentUser.setPhoneNumber(contact.getPhoneNumber());
                UserDTO userDTO = userController.createUser(currentUser).getBody();
                if(userDTO != null){
                    redirectToHomePage(message);
                }else {
                    message.setText("Не верный телефон номер. \n\n" +
                            "Наш бот поддерживает только номера из Узбекистана в формате:\n" +
                            "<b>(+9989x xxx xx xx)</b>");
                    sendContent(message);
                    message.setText("Пожалуйста, Перезапустите бот ещё раз и отправьте правильный тел. номер.");
                    sendContent(message);
                }
            }
        }
    }

    private void orderHistory(SendMessage message, long chatId) {
        message.setText("История заказов");
        sendContent(message);
    }

    private void makeOrder(SendMessage message, long chatId) {
        message.setText("Заказ");
        sendContent(message);
    }

    private void settingsPage(SendMessage message, long chatId) {
        message.setText("Настройки:");
        sendContent(message);
    }

    private void aboutUsPage(SendMessage message, long chatId) {
        message.setText("GlassPro была создана в 2015 году.....");
        message.setReplyMarkup(ReplyKeyboardUtil.baseMenu());
        sendContent(message);

    }

    private void adminPage(SendMessage message, String firstName) {
        message.setText("Админ <b>"+firstName +"</b>");
        sendContent(message);
        message.setText("Выберите операцию которую вы хотите сделать:");
        message.setReplyMarkup(ReplyKeyboardUtil.adminMenu());
        sendContent(message);
    }

    private void removeUserPage(SendMessage message, String text) {
        message.setText("Отправьте номер человека которого вы хотите удалить.");
        sendContent(message);

        String res = userController.removeUser(text);
        sendContent(res);
    }

    private void redirectToHomePage(SendMessage message) {
        message.setText("Выберите операцию:");
        message.setReplyMarkup(ReplyKeyboardUtil.baseMenu());
        sendContent(message);
    }


    private void startCommandReceived(String firstName, Long userId, SendMessage message) {
        String greeting = "Здраствуйте <b>"+firstName+"</b>\n"+
                "Добро пожаловать в бот <b>Glass Pro</b>";
        message.setText(greeting);
        sendContent(message);
        if(userController.getUserById(userId).getStatusCode() == HttpStatus.NOT_FOUND) {
            String contactRequest = "\uD83D\uDCF1 Отправьте свой номер телефона";
            message.setText(contactRequest);
            message.setReplyMarkup(ReplyKeyboardUtil.sendPhone());
            sendContent(message);
        }else{
            redirectToHomePage(message);
        }
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
