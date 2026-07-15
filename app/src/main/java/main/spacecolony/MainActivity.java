package main.spacecolony;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import main.spacecolony.model.CrewMember;
import main.spacecolony.model.Storage;

public class MainActivity extends AppCompatActivity {
    private TextView quartersCount, simulatorCount, missionControlCount;
    private TextView totalCrew, missionsCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        quartersCount = findViewById(R.id.quarters_count);
        simulatorCount = findViewById(R.id.simulator_count);
        missionControlCount = findViewById(R.id.mission_control_count);
        totalCrew = findViewById(R.id.total_crew);
        missionsCompleted = findViewById(R.id.missions_completed);

        findViewById(R.id.btn_recruit).setOnClickListener(v -> {
            startActivity(new Intent(this, RecruitActivity.class));
        });
        findViewById(R.id.btn_quarters).setOnClickListener(v -> {
            startActivity(new Intent(this, QuartersActivity.class));
        });
        findViewById(R.id.btn_simulator).setOnClickListener(v -> {
            startActivity(new Intent(this, SimulatorActivity.class));
        });
        findViewById(R.id.btn_mission_control).setOnClickListener(v -> {
            startActivity(new Intent(this, MissionControlActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCounts();
    }

    private void updateCounts() {
        Storage storage = Storage.getInstance();
        quartersCount.setText(String.valueOf(
                storage.getCrewCountByLocation(CrewMember.Location.QUARTERS)));
        simulatorCount.setText(String.valueOf(
                storage.getCrewCountByLocation(CrewMember.Location.SIMULATOR)));
        missionControlCount.setText(String.valueOf(
                storage.getCrewCountByLocation(CrewMember.Location.MISSION_CONTROL)));
        totalCrew.setText("Total Crew: " + storage.getTotalCrewCount());
        missionsCompleted.setText("Missions: " + storage.getMissionCount());
    }
}
