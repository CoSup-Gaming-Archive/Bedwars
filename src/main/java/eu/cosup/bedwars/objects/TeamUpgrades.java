package eu.cosup.bedwars.objects;

import java.util.ArrayList;

public class TeamUpgrades {
    public enum traps{
        BLINDNESS,
        OFFENSIVE,
        ALARM,
        MINING_FATIGUE
    }
    private int sharpness = 0;
    private int protection = 0;
    private int haste = 0;
    private int ressources = 0;
    private boolean heal = false;
    private ArrayList<traps> activatedTraps = new ArrayList<traps>();
    public TeamUpgrades(){

    }

    public ArrayList<traps> getActivatedTraps() {
        return activatedTraps;
    }

    public int getHaste() {
        return haste;
    }

    public int getProtection() {
        return protection;
    }

    public boolean getHeal() {
        return heal;
    }

    public int getRessources() {
        return ressources;
    }

    public int getSharpness() {
        return sharpness;
    }

    public void setHaste(int haste) {
        this.haste = haste;
    }

    public void setHeal(boolean heal) {
        this.heal = heal;
    }

    public void setProtection(int protection) {
        this.protection = protection;
    }

    public void setRessources(int ressources) {
        this.ressources = ressources;
    }

    public void setSharpness(int sharpness) {
        this.sharpness = sharpness;
    }
}
