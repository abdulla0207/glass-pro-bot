package uz.glasspro.util;

import org.checkerframework.checker.units.qual.K;
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

    private static List<KeyboardRow> getRowList(KeyboardRow... rows) {
        return Arrays.asList(rows);
    }

    private static KeyboardRow getRow(KeyboardButton... buttons) {
        KeyboardRow row = new KeyboardRow();
        row.addAll(Arrays.asList(buttons));
        return row;
    }
}
