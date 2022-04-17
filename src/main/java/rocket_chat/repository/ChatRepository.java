package rocket_chat.repository;

import rocket_chat.entity.Chat;
import rocket_chat.entity.Message;

import java.util.List;

public interface ChatRepository {
    public void saveChat(Chat chat);

    public List<Chat> getAllChatsByUserLogin(String userLogin);

    public Chat getChatByOwnerIdAndFriendId(String ownerUserName, String friendUserName);

    public void addMessage(Message message);

    public boolean deleteChatByUserIdAndFriendId(String ownerUserName, String friendUserName);
}
