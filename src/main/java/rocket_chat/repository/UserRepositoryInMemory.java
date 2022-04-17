package rocket_chat.repository;

import rocket_chat.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepositoryInMemory implements UserRepository {

    public static List<User> list = new ArrayList<>();

    public void saveUser(User user) {
        list.add(user);
    }

    public User getUserById(String userName) {
        for (User user : list) {
            if (user.getUserLogin().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    public User getUserByUserLogin(String userLogin) {
        for (User user : list) {
            if (user.getUserLogin().toLowerCase().equals(userLogin.toLowerCase())) {
                return user;
            }
        }
        return null;
    }

    public List<User> getUsers() {
        return list;
    }

    public void deleteUserById(String userName) {
        list.removeIf(user -> user.getUserLogin().equals(userName));
    }
}
