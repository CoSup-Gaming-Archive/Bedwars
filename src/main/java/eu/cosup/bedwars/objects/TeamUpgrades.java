package eu.cosup.bedwars.objects;

import java.util.ArrayList;

public class TeamUpgrades {
    public static enum traps{
        BLINDNESS,
        OFFENSIVE,
        ALARM,
        MINING_FATIGUE
    }
    public int sharpness = 0;
    public int protection = 0;
    public int haste = 0;
    public int ressources = 0;
    public boolean heal = false;
    public ArrayList<traps> activatedTraps = new ArrayList<traps>();
    public TeamUpgrades(){

    }
}
