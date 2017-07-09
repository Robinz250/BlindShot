package org.academiadecodigo.bootcamp.avatar;

/**
 * Created by codecadet on 05/07/17.
 */
public enum Avatar {
    ASSASSIN(4,1,"images/Avatar/1.png","Assassin \n Walk 4 - Attack 1"),
    FATONE(1,4,"images/Avatar/2.png","Fat One \n Walk 1 - Attack 4"),
    KILLER(3,3,"images/Avatar/3.png","Killer \n Walk 3 - Attack 3");

    private  int moveRange;
    private int killRange;
    private String image;
    private String name;

    Avatar(int moveRange, int killRange, String image, String name) {
        this.moveRange = moveRange;
        this.killRange = killRange;
        this.image = image;
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getMoveRange() {
        return moveRange;
    }

    public void setMoveRange(int moveRange) {
        this.moveRange = moveRange;
    }

    public int getKillRange() {
        return killRange;
    }

    public void setKillRange(int killRange) {
        this.killRange = killRange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "moveRange=" + moveRange +
                ", killRange=" + killRange +
                ", image='" + image + '\'' +
                '}';
    }
}
