package main.spacecolony.model;

public class Threat {
    private String name;
    private int skill;
    private int resilience;
    private int energy;
    private int maxEnergy;

    public Threat(String name, int skill, int resilience, int energy) {
        this.name = name;
        this.skill = skill;
        this.resilience = resilience;
        this.energy = energy;
        this.maxEnergy = energy;
    }

    public int act() {
        return skill;
    }

    public int defend(int incomingPower) {
        int damage = Math.max(incomingPower - resilience, 1);
        energy = Math.max(energy - damage, 0);
        return damage;
    }

    public boolean isDefeated() {
        return energy <= 0;
    }

    public String getName() {
        return name;
    }
    public int getSkill() {
        return skill;
    }
    public int getResilience() {
        return resilience;
    }
    public int getEnergy() {
        return energy;
    }
    public int getMaxEnergy() {
        return maxEnergy;
    }

    @Override
    public String toString() {
        return name + " (skill: " + skill + ", resilience: " + resilience
                + ", energy: " + energy + "/" + maxEnergy + ")";
    }
}
