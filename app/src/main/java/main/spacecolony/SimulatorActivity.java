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

public class SimulatorActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CrewMemberAdapter adapter;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulator);

        recyclerView = findViewById(R.id.simulator_recycler);
        emptyText = findViewById(R.id.simulator_empty);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btn_train).setOnClickListener(v -> {
            trainSelected();
        });
        findViewById(R.id.btn_to_quarters).setOnClickListener(v -> {
            moveToQuarters();
        });
        findViewById(R.id.btn_simulator_home).setOnClickListener(v -> {
            finish();
        });

        loadCrew();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCrew();
    }

    private void loadCrew() { // Loads and displays crew members in the simulator
        List<CrewMember> crew = Storage.getInstance()
                .getCrewByLocation(CrewMember.Location.SIMULATOR);
        if (crew.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        if (adapter == null) {
            adapter = new CrewMemberAdapter(crew, 0, selectedIds -> {

            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(crew);
        }
    }

    private void trainSelected() { // Trains selected crew and consumes energy
        Set<Integer> selected = adapter.getSelectedIds();
        if (selected.isEmpty()) {
            Toast.makeText(this, "Select crew members to train",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Storage storage = Storage.getInstance();
        int trainedCount = 0;
        int failedCount = 0;

        for (int id : selected) {
            CrewMember cm = storage.getCrewMember(id);
            if (cm != null) {
                if (cm.getEnergy() >= 5) {
                    cm.train();
                    cm.reduceEnergy(5);
                    trainedCount++;
                } else {
                    failedCount++;
                }
            }
        }

        if (trainedCount > 0) {
            Toast.makeText(this,
                    trainedCount + " crew trained! (+1 XP, -5 Energy)",
                    Toast.LENGTH_SHORT).show();
        }
        if (failedCount > 0) {
            Toast.makeText(this,
                    failedCount + " crew too tired to train",
                    Toast.LENGTH_SHORT).show();
        }

        loadCrew();
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
                selected.size() + " crew returned to Quarters (energy restored)",
                Toast.LENGTH_SHORT).show();
        loadCrew();
    }
}
