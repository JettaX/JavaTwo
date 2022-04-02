package exercise_2;

public class Main {
    public static void main(String[] args) {
        Phonebook phonebook = new Phonebook();
        initialize(phonebook);

        System.out.println(phonebook.getPhoneNumberByPersonSurname("Abramson"));

    }

    private static void initialize(Phonebook phonebook) {
        phonebook.add(new Person("Abramson"), new PhoneNumber("345-345-45674"));
        phonebook.add(new Person("Wood"), new PhoneNumber("23-56-44798"));
        phonebook.add(new Person("Wood"), new PhoneNumber("035-379-438"));
        phonebook.add(new Person("Gill"), new PhoneNumber("576-66-7689"));
        phonebook.add(new Person("Gill"), new PhoneNumber("345-67-23"));
        phonebook.add(new Person("Gimson"), new PhoneNumber("567-23-568"));
        phonebook.add(new Person("Abramson"), new PhoneNumber("245-678-345"));
        phonebook.add(new Person("Gate"), new PhoneNumber("678-234-567"));
        phonebook.add(new Person("Oliver"), new PhoneNumber("234-678-345"));
        phonebook.add(new Person("Ellington"), new PhoneNumber("456-4567-567"));
        phonebook.add(new Person("Adamson"), new PhoneNumber("86-546-678"));
        phonebook.add(new Person("Abramson"), new PhoneNumber("456-678-345"));
        phonebook.add(new Person("Jerome"), new PhoneNumber("678-4567-345"));
        phonebook.add(new Person("Nash"), new PhoneNumber("678-456-4224"));
        phonebook.add(new Person("Jerome"), new PhoneNumber("56723-5674-5679"));
        phonebook.add(new Person("Abramson"), new PhoneNumber("4568-342-111"));
        phonebook.add(new Person("Gate"), new PhoneNumber("234-456-234"));
    }
}
