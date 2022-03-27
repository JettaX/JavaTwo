public class Main {
    public static void main(String[] args) {
        Team team = new Team("HotMen",
                new Member("Василий"),
                new Member("Дмитрий"),
                new Member("Владимир"),
                new Member("Александр"));

        Course course = new Course();
        course.passObstacles(team);

        System.out.println("===================================");
        System.out.println("WHO PASSED");
        team.printPassedMembers();

        System.out.println("===================================");
        System.out.println("RESULTS");
        team.printResultsMembers();
    }
}
