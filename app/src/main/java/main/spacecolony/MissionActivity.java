package main.spacecolony;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import main.spacecolony.model.CrewMember;
import main.spacecolony.model.Storage;
import main.spacecolony.model.Threat;

import java.util.Random;

public class MissionActivity extends AppCompatActivity {
    private TextView crewAStats, crewBStats, threatStats, missionLog;
    private ProgressBar crewAEnergy, crewBEnergy, threatEnergy;
    private ScrollView logScroll;

    private static final String[] THREAT_NAMES = {
            "Asteroid Storm", "Alien Ambush", "Solar Flare",
            "Fuel Leakage", "Fire Outbreak", "Meteor Shower",
            "System Malfunction", "Gravity Anomaly",
            "Radiation Surge", "Hull Breach"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);

        crewAStats = findViewById(R.id.crew_a_stats);
        crewBStats = findViewById(R.id.crew_b_stats);
        threatStats = findViewById(R.id.threat_stats);
        crewAEnergy = findViewById(R.id.crew_a_energy_bar);
        crewBEnergy = findViewById(R.id.crew_b_energy_bar);
        threatEnergy = findViewById(R.id.threat_energy_bar);
        missionLog = findViewById(R.id.mission_log);
        logScroll = findViewById(R.id.log_scroll);

        findViewById(R.id.btn_return).setOnClickListener(v -> {
            finish();
        });

        int crewAId = getIntent().getIntExtra("crew_a_id", -1);
        int crewBId = getIntent().getIntExtra("crew_b_id", -1);
        Storage storage = Storage.getInstance();
        CrewMember crewA = storage.getCrewMember(crewAId);
        CrewMember crewB = storage.getCrewMember(crewBId);

        if (crewA == null || crewB == null) {
            Toast.makeText(this, "Error: crew members not found",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        runMission(crewA, crewB);
    }

    private void runMission(CrewMember crewA, CrewMember crewB) { // Runs the turn-based mission combat logic
        Storage storage = Storage.getInstance();
        int missionNum = storage.getMissionCount();
        Random random = new Random();

        String threatName = THREAT_NAMES[random.nextInt(THREAT_NAMES.length)];
        int threatSkill = 4 + missionNum;
        int threatResilience = 1 + missionNum / 3;
        int threatEnergyVal = 20 + missionNum * 3;
        Threat threat = new Threat(threatName, threatSkill, threatResilience, threatEnergyVal);

        StringBuilder log = new StringBuilder();
        log.append("=== MISSION: ").append(threatName).append(" ===\n");
        log.append("Threat: ").append(threat).append("\n");
        log.append("Crew A: ").append(crewA).append("\n");
        log.append("Crew B: ").append(crewB).append("\n\n");

        updateUI(crewA, crewB, threat);

        int round = 1;
        boolean crewAAlive = true;
        boolean crewBAlive = true;

        while (!threat.isDefeated() && (crewAAlive || crewBAlive)) {
            log.append("--- Round ").append(round).append(" ---\n");

            if (crewAAlive) {
                int power = crewA.act() + random.nextInt(3);
                int dmg = threat.defend(power);
                log.append(crewA.getSpecialization()).append("(")
                        .append(crewA.getName()).append(") acts against ")
                        .append(threat.getName()).append("\n");
                log.append("  Damage dealt: ").append(power).append(" - ")
                        .append(threat.getResilience()).append(" = ")
                        .append(dmg).append("\n");
                log.append("  ").append(threat.getName()).append(" energy: ")
                        .append(threat.getEnergy()).append("/")
                        .append(threat.getMaxEnergy()).append("\n");

                if (threat.isDefeated()) break;

                int tPower = threat.act() + random.nextInt(3);
                int tDmg = crewA.defend(tPower);
                log.append(threat.getName()).append(" retaliates against ")
                        .append(crewA.getSpecialization()).append("(")
                        .append(crewA.getName()).append(")\n");
                log.append("  Damage dealt: ").append(tPower).append(" - ")
                        .append(crewA.getResilience()).append(" = ")
                        .append(tDmg).append("\n");
                log.append("  ").append(crewA.getSpecialization()).append("(")
                        .append(crewA.getName()).append(") energy: ")
                        .append(crewA.getEnergy()).append("/")
                        .append(crewA.getMaxEnergy()).append("\n");

                if (!crewA.isAlive()) {
                    crewAAlive = false;
                    log.append("  ").append(crewA.getSpecialization()).append("(")
                            .append(crewA.getName()).append(") has fallen!\n");
                }
            }

            if (threat.isDefeated()) break;
            if (!crewAAlive && !crewBAlive) break;

            if (crewBAlive) {
                int power = crewB.act() + random.nextInt(3);
                int dmg = threat.defend(power);
                log.append(crewB.getSpecialization()).append("(")
                        .append(crewB.getName()).append(") acts against ")
                        .append(threat.getName()).append("\n");
                log.append("  Damage dealt: ").append(power).append(" - ")
                        .append(threat.getResilience()).append(" = ")
                        .append(dmg).append("\n");
                log.append("  ").append(threat.getName()).append(" energy: ")
                        .append(threat.getEnergy()).append("/")
                        .append(threat.getMaxEnergy()).append("\n");

                if (threat.isDefeated()) break;

                int tPower = threat.act() + random.nextInt(3);
                int tDmg = crewB.defend(tPower);
                log.append(threat.getName()).append(" retaliates against ")
                        .append(crewB.getSpecialization()).append("(")
                        .append(crewB.getName()).append(")\n");
                log.append("  Damage dealt: ").append(tPower).append(" - ")
                        .append(crewB.getResilience()).append(" = ")
                        .append(tDmg).append("\n");
                log.append("  ").append(crewB.getSpecialization()).append("(")
                        .append(crewB.getName()).append(") energy: ")
                        .append(crewB.getEnergy()).append("/")
                        .append(crewB.getMaxEnergy()).append("\n");

                if (!crewB.isAlive()) {
                    crewBAlive = false;
                    log.append("  ").append(crewB.getSpecialization()).append("(")
                            .append(crewB.getName()).append(") has fallen!\n");
                }
            }

            if (!crewAAlive && !crewBAlive) break;
            log.append("\n");
            round++;
        }

        log.append("\n=== MISSION ");
        if (threat.isDefeated()) {
            log.append("COMPLETE ===\n");
            log.append("The ").append(threat.getName())
                    .append(" has been neutralized!\n");
            if (crewAAlive) {
                crewA.train();
                log.append(crewA.getSpecialization()).append("(")
                        .append(crewA.getName())
                        .append(") gains 1 experience point. (exp: ")
                        .append(crewA.getExperience()).append(")\n");
            }
            if (crewBAlive) {
                crewB.train();
                log.append(crewB.getSpecialization()).append("(")
                        .append(crewB.getName())
                        .append(") gains 1 experience point. (exp: ")
                        .append(crewB.getExperience()).append(")\n");
            }
            storage.incrementMissionCount();
        } else {
            log.append("FAILED ===\n");
            log.append("Mission failed. All crew members lost.\n");
        }

        if (!crewAAlive) {
            storage.removeCrewMember(crewA.getId());
        }
        if (!crewBAlive) {
            storage.removeCrewMember(crewB.getId());
        }

        missionLog.setText(log.toString());
        updateUI(crewA, crewB, threat);
        findViewById(R.id.btn_return).setVisibility(View.VISIBLE);
        logScroll.post(() -> {
            logScroll.fullScroll(ScrollView.FOCUS_DOWN);
        });
    }

    private void updateUI(CrewMember crewA, CrewMember crewB, Threat threat) { // Updates energy bars and text stats on screen
        crewAStats.setText(crewA.toString());
        crewBStats.setText(crewB.toString());
        threatStats.setText(threat.toString());
        crewAEnergy.setMax(crewA.getMaxEnergy());
        crewAEnergy.setProgress(crewA.getEnergy());
        crewBEnergy.setMax(crewB.getMaxEnergy());
        crewBEnergy.setProgress(crewB.getEnergy());
        threatEnergy.setMax(threat.getMaxEnergy());
        threatEnergy.setProgress(threat.getEnergy());
    }
}
