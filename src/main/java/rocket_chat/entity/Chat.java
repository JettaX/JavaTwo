package rocket_chat.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"ownerUser", "friendUser", "messages"})
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    @Getter
    private String id;
    @Getter
    private User ownerUser;
    @Getter
    private User friendUser;
    @Getter
    private List<Message> messages = new ArrayList<>();

    public Chat(User ownerUser, User friendUser) {
        this.ownerUser = ownerUser;
        this.friendUser = friendUser;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }
}
