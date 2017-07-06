package org.academiadecodigo.bootcamp.avatar;

/**
 * Created by codecadet on 05/07/17.
 */
public enum Avatar {
    ASSASSIN(5,2,"images/Avatar/1.png"),
    FATONE(1,5,"images/Avatar/2.png"),
    KILLER(3,3,"images/Avatar/3.png");

    private  int moveRange;
    private int killRange;
    private String image;

    Avatar(int moveRange, int killRange,String image) {
        this.moveRange = moveRange;
        this.killRange = killRange;
        this.image = image;
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

    @Override
    public String toString() {
        return "Avatar{" +
                "moveRange=" + moveRange +
                ", killRange=" + killRange +
                ", image='" + image + '\'' +
                '}';
    }
}
