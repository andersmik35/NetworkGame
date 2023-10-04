package game.client;

public class PlayerUpdate {
    private String name;
    private Pair newPos;
    private String direction;
    private int points;

    public PlayerUpdate(String name, Pair newPos, String direction, int points) {
        this.name = name;
        this.newPos = newPos;
        this.direction = direction;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public Pair getPos() {
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
        return name + ":   " + points;
    }
}
