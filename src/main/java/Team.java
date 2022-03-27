public class Team {
    private final String nameTeam;
    private final Member[] members;

    public String getNameTeam() {
        return nameTeam;
    }

    public Member[] getMembers() {
        return members;
    }

    public Team(String nameTeam, Member memberOne, Member memberTwo, Member memberThree, Member memberFour) {
        this.nameTeam = nameTeam;
        members = new Member[]{memberOne, memberTwo, memberThree, memberFour};
    }

    public void printPassedMembers() {
        for (Member member : members) {
            if (member.getPassed()) {
                System.out.println(member.getName() + " is passed");
            }
        }
    }

    public void printResultsMembers() {
        for (Member member : members) {
            System.out.println(member.getName() + " is " + (member.getPassed() ? "passed" : "dropped out"));
        }
    }
}
