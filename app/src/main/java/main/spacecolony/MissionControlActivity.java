package main.spacecolony;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import main.spacecolony.adapter.CrewMemberAdapter;
import main.spacecolony.model.CrewMember;
import main.spacecolony.model.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MissionControlActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CrewMemberAdapter adapter;
    private TextView emptyText;
    private Button launchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_control);

        recyclerView = findViewById(R.id.mc_recycler);
        emptyText = findViewById(R.id.mc_empty);
        launchBtn = findViewById(R.id.btn_launch_mission);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        launchBtn.setOnClickListener(v -> {
            launchMission();
        });
        findViewById(R.id.btn_mc_to_quarters).setOnClickListener(v -> {
            moveToQuarters();
        });
        findViewById(R.id.btn_mc_home).setOnClickListener(v -> {
            finish();
        });

        loadCrew();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCrew();
    }

    private void loadCrew() {
        List<CrewMember> crew = Storage.getInstance()
                .getCrewByLocation(CrewMember.Location.MISSION_CONTROL);
        if (crew.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        adapter = new CrewMemberAdapter(crew, 2, selectedIds -> {
            launchBtn.setEnabled(selectedIds.size() == 2);
        });
        recyclerView.setAdapter(adapter);
        launchBtn.setEnabled(false);
    }

    private void launchMission() {
        Set<Integer> selected = adapter.getSelectedIds();
        if (selected.size() != 2) {
            Toast.makeText(this, "Select exactly 2 crew members",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        List<Integer> ids = new ArrayList<>(selected);
        Intent intent = new Intent(this, MissionActivity.class);
        intent.putExtra("crew_a_id", ids.get(0));
        intent.putExtra("crew_b_id", ids.get(1));
        startActivity(intent);
    }

    private void moveToQuarters() {
        Set<Integer> selected = adapter.getSelectedIds();
        if (selected.isEmpty()) {
            Toast.makeText(this, "Select crew members first",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Storage storage = Storage.getInstance();
        for (int id : selected) {
            CrewMember cm = storage.getCrewMember(id);
            if (cm != null) {
                cm.restoreEnergy();
                cm.setLocation(CrewMember.Location.QUARTERS);
            }
        }
        Toast.makeText(this,
                selected.size() + " crew returned to Quarters",
                Toast.LENGTH_SHORT).show();
        loadCrew();
    }
}
