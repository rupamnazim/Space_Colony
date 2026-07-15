package main.spacecolony.model;

public abstract class CrewMember {
    private static int nextId = 1;

    private int id;
    private String name;
    private String specialization;
    private int baseSkill;
    private int resilience;
    private int experience;
    private int energy;
    private int maxEnergy;
    private Location location;

    public enum Location {
        QUARTERS, SIMULATOR, MISSION_CONTROL
    }

    public CrewMember(String name, String specialization, int baseSkill, int resilience, int maxEnergy) {
        this.id = nextId++;
        this.name = name;
        this.specialization = specialization;
        this.baseSkill = baseSkill;
        this.resilience = resilience;
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
        this.experience = 0;
        this.location = Location.QUARTERS;
    }

    public int act() { // Calculates the damage done by the crew member

        return baseSkill + experience;
    }

    public int defend(int incomingPower) { // Reduces energy based on damage and resilience

        int damage = Math.max(incomingPower - resilience, 1);
        energy = Math.max(energy - damage, 0);
        return damage;
    }

    public boolean isAlive() { // Checks if the crew member still has energy
        return energy > 0;
    }

    public void restoreEnergy() { // Fully restores crew member energy
        energy = maxEnergy;
    }

    public void reduceEnergy(int amount) { // Reduces energy by a fixed amount
        energy = Math.max(0, energy - amount);
    }

    public void train() { // Increases experience points by 1
        experience++;
    }

    public int getSkill() { // Returns total skill including experience
        return baseSkill + experience;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getSpecialization() {
        return specialization;
    }
    public int getBaseSkill() {
        return baseSkill;
    }
    public int getResilience() {
        return resilience;
    }
    public int getExperience() {
        return experience;
    }
    public int getEnergy() {
        return energy;
    }
    public int getMaxEnergy() {
        return maxEnergy;
    }
    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return specialization + "(" + name + ") skill: " + getSkill()
                + "; res: " + resilience + "; exp: " + experience
                + "; energy: " + energy + "/" + maxEnergy;
    }
}
