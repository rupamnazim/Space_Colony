package main.spacecolony;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import main.spacecolony.adapter.CrewMemberAdapter;
import main.spacecolony.model.CrewMember;
import main.spacecolony.model.Storage;

import java.util.List;
import java.util.Set;

public class QuartersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CrewMemberAdapter adapter;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quarters);

        recyclerView = findViewById(R.id.quarters_recycler);
        emptyText = findViewById(R.id.quarters_empty);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btn_to_simulator).setOnClickListener(v -> {
            moveSelected(CrewMember.Location.SIMULATOR);
        });
        findViewById(R.id.btn_to_mission_control).setOnClickListener(v -> {
            moveSelected(CrewMember.Location.MISSION_CONTROL);
        });
        findViewById(R.id.btn_back_home).setOnClickListener(v -> {
            finish();
        });

        loadCrew();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCrew();
    }

    private void loadCrew() { // Loads crew members currently in quarters
        List<CrewMember> crew = Storage.getInstance()
                .getCrewByLocation(CrewMember.Location.QUARTERS);
        if (crew.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        adapter = new CrewMemberAdapter(crew, 0, selectedIds -> {

        });
        recyclerView.setAdapter(adapter);
    }

    private void moveSelected(CrewMember.Location destination) { // Moves selected crew to simulator or mission control
        Set<Integer> selected = adapter.getSelectedIds();
        if (selected.isEmpty()) {
            Toast.makeText(this, "Select crew members first", Toast.LENGTH_SHORT).show();
            return;
        }
        Storage storage = Storage.getInstance();
        for (int id : selected) {
            CrewMember cm = storage.getCrewMember(id);
            if (cm != null) {
                cm.setLocation(destination);
            }
        }
        String destName = destination == CrewMember.Location.SIMULATOR
                ? "Simulator" : "Mission Control";
        Toast.makeText(this,
                selected.size() + " crew moved to " + destName,
                Toast.LENGTH_SHORT).show();
        loadCrew();
    }
}
