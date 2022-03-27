public class Member {
    private String name;
    private Boolean passed;

    public Member(String name) {
        this.name = name;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

    public String getName() {
        return name;
    }

    public Boolean getPassed() {
        return passed;
    }
}
