package org.academiadecodigo.bootcamp.avatar;

/**
 * Created by codecadet on 05/07/17.
 */
public enum Avatar {
    ASSASSIN(3, 1, "/images/Avatar/1.jpg", "Assassin \n Walk 3 - Attack 1", "assassin"),
    BIGONE(2, 2, "/images/Avatar/2.jpg", "Fat One \n Walk 2 - Attack 2", "bigOne"),
    RANGED(1, 3, "/images/Avatar/3.jpg", "Ranged \n Walk 1 - Attack 3", "ranged");

    private int moveRange;
    private int killRange;
    private String image;
    private String name;
    private String folder;

    Avatar(int moveRange, int killRange, String image, String name, String folder) {
        this.moveRange = moveRange;
        this.killRange = killRange;
        this.image = image;
        this.name = name;
        this.folder = folder;
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

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
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
