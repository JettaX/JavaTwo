import java.util.Random;

public class Course {
    private final Obstacles[] obstacles;

    public Course() {
        obstacles = Obstacles.values();
    }

    public void passObstacles(Team team) {
        System.out.println("The team " + team.getNameTeam() + " starts running");
        for (Member member : team.getMembers()) {
            for (Obstacles obst : obstacles) {
                if (new Random().nextInt(0, 10) > 1) {
                    System.out.println(member.getName() + " passed " + obst);
                } else {
                    System.out.println(member.getName() + " didn't pass " + obst);
                    member.setPassed(false);
                    break;
                }
                member.setPassed(true);
            }
        }
    }
}
