package uz.glasspro.component;

import uz.glasspro.enums.UserCurrentStatus;

import java.util.HashMap;
import java.util.Map;

public interface ComponentContainer {

    Map<Long, UserCurrentStatus> USER_STATUS = new HashMap<>();

}
