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
import uz.glasspro.component.ComponentContainer;
import uz.glasspro.controller.OrderController;
import uz.glasspro.controller.UserController;
import uz.glasspro.dto.OrderDTO;
import uz.glasspro.dto.UserDTO;
import uz.glasspro.enums.RoleEnum;
import uz.glasspro.enums.UserCurrentStatus;
import uz.glasspro.util.ReplyKeyboardUtil;
import uz.glasspro.util.ReplyKeyboardConstants;

import java.util.*;

@Slf4j
@Service
public class TelegramBot extends TelegramLongPollingBot {

    private final Dotenv dotenv = Dotenv.configure().filename(".env").load();

    private OrderDTO orderDTO;
    @Autowired
    private UserController userController;

    @Autowired
    private OrderController orderController;
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
            if(ComponentContainer.USER_STATUS.containsValue(UserCurrentStatus.ENTER_PRODUCT_AMOUNT)){
                orderDTO.setName(messageText);
                message.setText("????????????????????:");
                sendContent(message);
                ComponentContainer.USER_STATUS.put(chatId, UserCurrentStatus.ENTER_PRODUCT_WIDTH);
            }else if(ComponentContainer.USER_STATUS.containsValue(UserCurrentStatus.ENTER_PRODUCT_WIDTH)){
                orderDTO.setAmount(Integer.getInteger(messageText));
                message.setText("????????????:");
                sendContent(message);
            }

            if(update.getMessage().hasText()){
                Chat chat = update.getMessage().getChat();

                    switch (messageText) {
                        case "/start":
                            ComponentContainer.USER_STATUS.put(chatId, UserCurrentStatus.START_CHAT);
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
                            ComponentContainer.USER_STATUS.put(chatId, UserCurrentStatus.ENTER_PRODUCT_NAME);
                            orderDTO = new OrderDTO();
                            message.setText("???????????????? ????????????????:");
                            sendContent(message);
                            ComponentContainer.USER_STATUS.put(chatId, UserCurrentStatus.ENTER_PRODUCT_AMOUNT);
                            break;
                        case ReplyKeyboardConstants.ORDER_HISTORY:
                            orderHistory(message, chat.getId());
                            break;
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
                    message.setText("???? ???????????? ?????????????? ??????????. \n\n" +
                            "?????? ?????? ???????????????????????? ???????????? ???????????? ???? ?????????????????????? ?? ??????????????:\n" +
                            "<b>(+9989x xxx xx xx)</b>");
                    sendContent(message);
                    message.setText("????????????????????, ?????????????????????????? ?????? ?????? ?????? ?? ?????????????????? ???????????????????? ??????. ??????????.");
                    sendContent(message);
                }
            }
        }
    }

    private void orderProduct(long chatId, String messageText, SendMessage message) {
        if(ComponentContainer.USER_STATUS.containsValue(UserCurrentStatus.ENTER_PRODUCT_NAME)){
            message.setText("Afa");
            sendContent(message);
            ComponentContainer.USER_STATUS.put(chatId, UserCurrentStatus.ENTER_PRODUCT_AMOUNT);
            System.out.println(ComponentContainer.USER_STATUS.containsValue(UserCurrentStatus.ENTER_PRODUCT_AMOUNT));
        }else if(ComponentContainer.USER_STATUS.containsValue(UserCurrentStatus.ENTER_PRODUCT_AMOUNT)){
            message.setText("ASfaf");
            sendContent(message);
        }
    }

    private void orderHistory(SendMessage message, long userId) {
        message.setText("?????????????? ??????????????");
        List<OrderDTO> orderDTOS = orderController.getUserOrderList(userId).getBody();
        if(orderDTOS == null){
            message.setText("???? ?????? ???? ???????????? ?????????????? ??????????????");
            sendContent(message);
        }else{
            StringBuilder orderList = new StringBuilder();
            int counter = 1;
            orderDTOS.forEach(orderDTO -> {
                orderList.append(counter + ". " + orderDTO.getName() + ":\n??????-????" + orderDTO.getAmount() + "\n???????? " + orderDTO.getPrice()
                + "\n???????? ???????????? " + orderDTO.getCreatedDate());
                orderList.append("\n");
            });

            message.setText(orderList.toString());
            sendContent(message);
        }
    }

    private void settingsPage(SendMessage message, long chatId) {
        message.setText("??????????????????:");
        sendContent(message);
    }

    private void aboutUsPage(SendMessage message, long chatId) {
        message.setText("GlassPro ???????? ?????????????? ?? 2015 ????????.....");
        message.setReplyMarkup(ReplyKeyboardUtil.baseMenu());
        sendContent(message);

    }

    private void adminPage(SendMessage message, String firstName) {
        message.setText("?????????? <b>"+firstName +"</b>");
        sendContent(message);
        message.setText("???????????????? ???????????????? ?????????????? ???? ???????????? ??????????????:");
        message.setReplyMarkup(ReplyKeyboardUtil.adminMenu());
        sendContent(message);
    }

    private void removeUserPage(SendMessage message, String text) {
        message.setText("?????????????????? ?????????? ???????????????? ???????????????? ???? ???????????? ??????????????.");
        sendContent(message);

        String res = userController.removeUser(text);
        sendContent(res);
    }

    private void redirectToHomePage(SendMessage message) {
        message.setText("???????????????? ????????????????:");
        message.setReplyMarkup(ReplyKeyboardUtil.baseMenu());
        sendContent(message);
    }


    private void startCommandReceived(String firstName, Long userId, SendMessage message) {
        String greeting = "?????????????????????? <b>"+firstName+"</b>\n"+
                "?????????? ???????????????????? ?? ?????? <b>Glass Pro</b>";
        message.setText(greeting);
        sendContent(message);
        if(userController.getUserById(userId).getStatusCode() == HttpStatus.NOT_FOUND) {
            String contactRequest = "\uD83D\uDCF1 ?????????????????? ???????? ?????????? ????????????????";
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
