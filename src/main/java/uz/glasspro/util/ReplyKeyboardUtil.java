package uz.glasspro.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.List;

public class ReplyKeyboardUtil {

    public static ReplyKeyboardMarkup sendPhone(){
        KeyboardButton keyboardButton = new KeyboardButton(ReplyKeyboardConstants.SEND_PHONE);
        keyboardButton.setRequestContact(true);
        KeyboardRow row = getRow(keyboardButton);
        List<KeyboardRow> rowList = getRowList(row);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(rowList);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup adminMenu(){
        KeyboardButton banUser = new KeyboardButton(ReplyKeyboardConstants.BAN_USER);
        KeyboardButton deleteUser = new KeyboardButton(ReplyKeyboardConstants.DELETE_USER);
        KeyboardRow row1 = getRow(banUser, deleteUser);
        List<KeyboardRow> rows = getRowList(row1);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup baseMenu(){
        KeyboardButton aboutButton = new KeyboardButton(ReplyKeyboardConstants.ABOUT_US);
        KeyboardButton orderButton = new KeyboardButton(ReplyKeyboardConstants.ORDER);
        KeyboardButton settingButton = new KeyboardButton(ReplyKeyboardConstants.SETTINGS);
        KeyboardButton orderHistoryButton = new KeyboardButton(ReplyKeyboardConstants.ORDER_HISTORY);
        KeyboardRow row1 = getRow(aboutButton, settingButton);
        KeyboardRow row2 = getRow(orderButton);
        KeyboardRow row3 = getRow(orderHistoryButton);
        List<KeyboardRow> keyboardRows = getRowList(row1, row2, row3);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    private static List<KeyboardRow> getRowList(KeyboardRow... rows) {
        return Arrays.asList(rows);
    }

    private static KeyboardRow getRow(KeyboardButton... buttons) {
        KeyboardRow row = new KeyboardRow();
        row.addAll(Arrays.asList(buttons));
        return row;
    }
}
