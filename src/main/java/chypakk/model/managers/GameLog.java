package chypakk.model.managers;

import java.util.LinkedList;
import java.util.List;

public class GameLog {
    private final int maxMessages;
    private final List<String> messages = new LinkedList<>();

    public GameLog(int maxMessages) {
        this.maxMessages = maxMessages;
    }

    public void addMessage(String message){
        messages.add(message);
        if (messages.size() > maxMessages){
            messages.removeFirst();
        }
    }

    public List<String> getMessages(){
        return new LinkedList<>(messages);
    }
}
