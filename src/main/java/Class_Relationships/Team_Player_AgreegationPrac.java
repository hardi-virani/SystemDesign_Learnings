//“Where is the aggregation in this code?”
//
//        That’s a fair question, because aggregation isn’t one special line. It’s the combination of a few things. Think of a team roster sheet: it lists players’ names, but the sheet isn’t the players. Tear up the sheet, and the players are still walking around.
//
//        In your code:
//
//        The players are created in main, not inside Team. new Player("Hardi", ...) happens outside. Team never creates a player.
//        Team only holds a reference to them. The players list is the roster sheet. It points at players who exist on their own.
//        The same player is in two teams. p1 is on both roster sheets at once. Parts can be shared.
//        The team disappears, the player doesn’t. After clubTeam = null, p1 is still printed and still plays for the national team.
//Those four together are aggregation: a whole (Team) made of parts (Players) that live independently.
//
//To see the contrast, here’s what it would look like if it were not aggregation:
//


//class Team {
//    private final List<Player> players = new ArrayList<>();
//
//    public void addPlayer(String name, String role) {
//        players.add(new Player(name, role));   // Team creates its own players
//    }
//}

//Now the players are born inside the team, nobody outside has them, and they can’t be shared. If the team goes, they go too. That’s composition, tomorrow’s topic. Comparing the two is often the easiest way to see aggregation.

package Class_Relationships;

import java.util.ArrayList;
import java.util.List;

class Player {
    private final String name;
    private final String role;

    public Player(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }
    public String getRole() { return role;}

}

class Team {
    private final String name;
    private final List<Player> players =  new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }

    public void addPlayer(Player player) {
        if(!players.contains(player)) {
            players.add(player);
        }
    }

    public void removePlayer(Player player) {
            players.remove(player);

    }

    public List<Player> getPlayers() {
        return List.copyOf(players);
    }
}

public class Team_Player_AgreegationPrac {
    public static void main(String[] args) {
        Player p1 = new Player("Hardi", "Baller");
        Player p2 = new Player("Star", "Batsman");
        Player p3 = new Player("Mann", "Batsman");
        Player p4 = new Player("John", "Baller");

        Team clubTeam = new Team("Club Team");
        clubTeam.addPlayer(p1);


        Team nationalTeam = new Team("National Team");
        nationalTeam.addPlayer(p1);
        nationalTeam.addPlayer(p2);
        nationalTeam.addPlayer(p3);
        nationalTeam.addPlayer(p4);

        clubTeam.removePlayer(p1);
        clubTeam = null;

        System.out.println("All player still exist");
        for(Player p : List.of(p1,p2,p3,p4)) {
            System.out.println(p.getName());
        }

//        for(Player p : clubTeam.getPlayers()) {
//            System.out.println(p.getName() + " is in the club team with role " + p.getRole());
//        }

        for(Player p : nationalTeam.getPlayers()) {
            System.out.println(p.getName() + " is in the National team with role " + p.getRole());
        }
    }
}
