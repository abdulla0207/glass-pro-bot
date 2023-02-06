package uz.glasspro;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
public class TelegramBot extends TelegramLongPollingBot {

    private final Dotenv dotenv = Dotenv.configure().filename(".env").load();

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

            switch (messageText){
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                default:
                    sendMessage(chatId, "Cannot process it now");
            }
        }
    }

    private void startCommandReceived(long chatId, String firstName) {
        String greeting = "Здраствуйте <b>"+firstName+"</b>\n"+
                "Добро пожаловать в бот <b>Glass Pro</b>";
        sendMessage(chatId, greeting);
        String contactRequest = "\uD83D\uDCF1 Отправьте свой номер телефона";
        sendMessage(chatId, contactRequest);
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        sendContent(message);
    }

    private void sendContent(Object content) {
        try {
            if(content instanceof  SendMessage){
                execute((SendMessage) content);
            }
        }catch (TelegramApiException e){
            log.error("Sending content = "+ e.getMessage());
        }
    }
}
