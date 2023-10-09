package game.client;

public class PlayerUpdate {
    private String name;
    private Pair oldPos, newPos;
    private String direction;
    private int points;

    public PlayerUpdate(String name, Pair oldPos, Pair newPos, String direction, int points) {
        this.name = name;
        this.newPos = newPos;
        this.oldPos = oldPos;
        this.direction = direction;
        this.points = points;
    }

    public Pair getPos() {
        return newPos;
    }

    public String getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return name + ":   " + points;
    }

    public Pair getOldPos() {
        return oldPos;
    }
}
