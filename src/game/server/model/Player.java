package game.server.model;

public class Player {
    private final String name;
    private Pair location;
    private int point;
    private String direction;

    public Player(String name, Pair loc, String direction) {
        this.name = name;
        this.location = loc;
        this.direction = direction;
        this.point = 0;
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

    public void setXpos(int xpos) {
        this.location.setX(xpos);
    }

    public int getYpos() {
        return location.getY();
    }

    public void setYpos(int ypos) {
        this.location.setY(ypos);
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void addPoints(int p) {
        point += p;
    }

    public String serializePlayer() {
        return name + ":" + location.getX() + ":" + location.getY() + ":" + direction + ":" + point;
    }

    public String toString() {
        return name + ":   " + point;
    }
}
