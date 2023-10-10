package game.server.model;

public class Player {
    private final String name;
    private Pair location, oldLoc;
    private int point;
    private String direction;

    public Player(String name, Pair loc, String direction) {
        this.name = name;
        this.location = loc;
        this.oldLoc = loc;
        this.direction = direction;
        this.point = 0;
    }

    public void setOldLoc(Pair oldLoc) {
        this.oldLoc = oldLoc;
    }

    public Pair getLocation() {
        return this.location;
    }

    public void setLocation(Pair p) {
        this.location = p;
    }

    public int getXpos() {
        return location.getX();
    }

    public int getYpos() {
        return location.getY();
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void addPoints(int p) {
        point += p;
    }

    public void takePoints(int p) {
        point -= p;
    }

    public String serializePlayer() {
        return "{" + name + ":" + location + ":" + direction + ":" + point + "}";
    }

    public String toString() {
        return name + ":   " + point;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return point;
    }

}
