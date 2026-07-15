package main.spacecolony.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Storage {
    private static Storage instance;
    private HashMap<Integer, CrewMember> crew;
    private int missionCount;

    private Storage() {
        crew = new HashMap<>();
        missionCount = 0;
    }

    public static Storage getInstance() { // Provides the single instance of Storage
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    public void addCrewMember(CrewMember cm) {
        crew.put(cm.getId(), cm);
    }

    public CrewMember getCrewMember(int id) {
        return crew.get(id);
    }

    public void removeCrewMember(int id) {
        crew.remove(id);
    }

    public List<CrewMember> getCrewByLocation(CrewMember.Location location) { // Filters crew by their location
        List<CrewMember> result = new ArrayList<>();
        for (CrewMember cm : crew.values()) {
            if (cm.getLocation() == location) {
                result.add(cm);
            }
        }
        return result;
    }

    public int getCrewCountByLocation(CrewMember.Location location) {
        return getCrewByLocation(location).size();
    }

    public int getTotalCrewCount() {
        return crew.size();
    }

    public int getMissionCount() {
        return missionCount;
    }

    public void incrementMissionCount() {
        missionCount++;
    }
}
