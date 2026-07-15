package main.spacecolony;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import main.spacecolony.model.CrewMember;
import main.spacecolony.model.Engineer;
import main.spacecolony.model.Medic;
import main.spacecolony.model.Pilot;
import main.spacecolony.model.Scientist;
import main.spacecolony.model.Soldier;
import main.spacecolony.model.Storage;

public class RecruitActivity extends AppCompatActivity {
    private EditText nameInput;
    private RadioGroup specGroup;
    private TextView statsPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit);

        nameInput = findViewById(R.id.recruit_name);
        specGroup = findViewById(R.id.spec_group);
        statsPreview = findViewById(R.id.stats_preview);

        specGroup.setOnCheckedChangeListener((group, checkedId) -> {
            updateStatsPreview(checkedId);
        });
        specGroup.check(R.id.radio_pilot);

        findViewById(R.id.btn_create).setOnClickListener(v -> {
            createCrewMember();
        });
        findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            finish();
        });
    }

    private void updateStatsPreview(int checkedId) {
        String stats;
        if (checkedId == R.id.radio_pilot) {
            stats = "Skill: 5  |  Resilience: 4  |  Energy: 20";
        } else if (checkedId == R.id.radio_engineer) {
            stats = "Skill: 6  |  Resilience: 3  |  Energy: 19";
        } else if (checkedId == R.id.radio_medic) {
            stats = "Skill: 7  |  Resilience: 2  |  Energy: 18";
        } else if (checkedId == R.id.radio_scientist) {
            stats = "Skill: 8  |  Resilience: 1  |  Energy: 17";
        } else if (checkedId == R.id.radio_soldier) {
            stats = "Skill: 9  |  Resilience: 0  |  Energy: 16";
        } else {
            stats = "";
        }
        statsPreview.setText(stats);
    }

    private void createCrewMember() {
        String name = nameInput.getText().toString().trim();
        if (name.isEmpty()) {
            nameInput.setError("Enter a name");
            return;
        }

        int checkedId = specGroup.getCheckedRadioButtonId();
        CrewMember cm;
        if (checkedId == R.id.radio_pilot) {
            cm = new Pilot(name);
        } else if (checkedId == R.id.radio_engineer) {
            cm = new Engineer(name);
        } else if (checkedId == R.id.radio_medic) {
            cm = new Medic(name);
        } else if (checkedId == R.id.radio_scientist) {
            cm = new Scientist(name);
        } else if (checkedId == R.id.radio_soldier) {
            cm = new Soldier(name);
        } else {
            return;
        }

        Storage.getInstance().addCrewMember(cm);
        Toast.makeText(this,
                cm.getSpecialization() + " " + name + " recruited!",
                Toast.LENGTH_SHORT).show();
        finish();
    }
}
