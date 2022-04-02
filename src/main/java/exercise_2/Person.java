package exercise_2;

public class Person {
    private final String surname;

    public Person(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public String toString() {
        return "surname " + surname + " ";
    }
}
