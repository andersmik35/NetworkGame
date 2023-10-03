package game.client;

public class PlayerUpdate {
    private String name;
    private Pair oldPos, newPos;
    private String direction;
    private int points;

    public PlayerUpdate(String name, Pair oldPos, Pair newPos, String direction, int points) {
        this.name = name;
        this.oldPos = oldPos;
        this.newPos = newPos;
        this.direction = direction;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public Pair getOldPos() {
        return oldPos;
    }

    public Pair getNewPos() {
        return newPos;
    }

    public String getDirection() {
        return direction;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return name+":   " + points;
    }
}
