package exercise_2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Phonebook {
    private Map<Person, PhoneNumber> phoneBook = new HashMap<>();

    public void add(Person person, PhoneNumber number) {
        phoneBook.put(person, number);
    }

    public List<String> getPhoneNumberByPersonSurname(String surname) {
        return phoneBook.entrySet().stream()
                .filter(phone -> phone.getKey().getSurname().equals(surname))
                .map(x -> x.getValue().getPhoneNumber())
                .collect(Collectors.toList());
    }

    public Map<Person, PhoneNumber> getAll() {
        return phoneBook;
    }
}
