package main.spacecolony.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import main.spacecolony.R;
import main.spacecolony.model.CrewMember;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CrewMemberAdapter extends RecyclerView.Adapter<CrewMemberAdapter.ViewHolder> {
    private List<CrewMember> crewMembers;
    private Set<Integer> selectedIds;
    private int maxSelections;
    private OnSelectionChangedListener listener;

    public interface OnSelectionChangedListener {
        void onSelectionChanged(Set<Integer> selectedIds);
    }

    public CrewMemberAdapter(List<CrewMember> crewMembers, int maxSelections,
                             OnSelectionChangedListener listener) {
        this.crewMembers = new ArrayList<>(crewMembers);
        this.selectedIds = new HashSet<>();
        this.maxSelections = maxSelections;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_crew_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CrewMember cm = crewMembers.get(position);
        holder.bind(cm);
    }

    @Override
    public int getItemCount() {
        return crewMembers.size();
    }

    public void updateData(List<CrewMember> newData) {
        crewMembers.clear();
        crewMembers.addAll(newData);
        selectedIds.clear();
        notifyDataSetChanged();
        if (listener != null) {
            listener.onSelectionChanged(selectedIds);
        }
    }

    public Set<Integer> getSelectedIds() {
        return new HashSet<>(selectedIds);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;
        ImageView icon;
        TextView name, stats, energy;
        ProgressBar energyBar;

        ViewHolder(View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.crew_checkbox);
            icon = itemView.findViewById(R.id.crew_icon);
            name = itemView.findViewById(R.id.crew_name);
            stats = itemView.findViewById(R.id.crew_stats);
            energy = itemView.findViewById(R.id.crew_energy);
            energyBar = itemView.findViewById(R.id.crew_energy_bar);
        }

        void bind(CrewMember cm) {
            name.setText(cm.getSpecialization() + " - " + cm.getName());
            stats.setText("Skill: " + cm.getSkill() + " | Res: " + cm.getResilience()
                    + " | XP: " + cm.getExperience());
            energy.setText("HP: " + cm.getEnergy() + "/" + cm.getMaxEnergy());
            energyBar.setMax(cm.getMaxEnergy());
            energyBar.setProgress(cm.getEnergy());

            switch (cm.getSpecialization()) {
                case "Pilot":
                    icon.setImageResource(R.drawable.ic_pilot);
                    break;
                case "Engineer":
                    icon.setImageResource(R.drawable.ic_engineer);
                    break;
                case "Medic":
                    icon.setImageResource(R.drawable.ic_medic);
                    break;
                case "Scientist":
                    icon.setImageResource(R.drawable.ic_scientist);
                    break;
                case "Soldier":
                    icon.setImageResource(R.drawable.ic_soldier);
                    break;
            }

            checkbox.setChecked(selectedIds.contains(cm.getId()));

            itemView.setOnClickListener(v -> {
                toggleSelection(cm);
            });
            checkbox.setOnClickListener(v -> {
                checkbox.setChecked(selectedIds.contains(cm.getId()));
                toggleSelection(cm);
            });
        }

        private void toggleSelection(CrewMember cm) {
            if (selectedIds.contains(cm.getId())) {
                selectedIds.remove(cm.getId());
                checkbox.setChecked(false);
            } else {
                if (maxSelections > 0 && selectedIds.size() >= maxSelections) {
                    return;
                }
                selectedIds.add(cm.getId());
                checkbox.setChecked(true);
            }
            if (listener != null) {
                listener.onSelectionChanged(selectedIds);
            }
        }
    }
}
