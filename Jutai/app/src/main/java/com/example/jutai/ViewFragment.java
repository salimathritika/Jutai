package com.example.jutai;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.appbar.MaterialToolbar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewFragment extends Fragment {

    RecyclerView recyclerView;
    EquipmentAdapter adapter;
    DatabaseHelper dbHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        recyclerView = view.findViewById(R.id.equipmentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        dbHelper = new DatabaseHelper(getContext());
        List<EquipmentModel> equipmentList = dbHelper.getAllEquipment();


        recyclerView.setAdapter(new EquipmentAdapter(getContext(), equipmentList, "public"));

        MaterialToolbar toolbar = view.findViewById(R.id.viewToolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.action_category_tractor) {
                Toast.makeText(getContext(), "Filtering: Tractor", Toast.LENGTH_SHORT).show();
                List<EquipmentModel> arr = dbHelper.getEquipmentByCategory("Tractor");
                recyclerView.setAdapter(new EquipmentAdapter(getContext(), arr, "public"));
                return true;

            } else if (id == R.id.action_category_implement) {
                Toast.makeText(getContext(), "Filtering: Implement", Toast.LENGTH_SHORT).show();
                List<EquipmentModel> arr = dbHelper.getEquipmentByCategory("Implement");
                recyclerView.setAdapter(new EquipmentAdapter(getContext(), arr, "public"));
                return true;

            } else if (id == R.id.action_price_low_to_high) {
                Toast.makeText(getContext(), "Sorting: Low to High", Toast.LENGTH_SHORT).show();
                List<EquipmentModel> arr = dbHelper.getAllEquipmentSortedByPriceLowToHigh();
                recyclerView.setAdapter(new EquipmentAdapter(getContext(), arr, "public"));
                return true;

            } else if (id == R.id.action_price_high_to_low) {
                Toast.makeText(getContext(), "Sorting: High to Low", Toast.LENGTH_SHORT).show();
                List<EquipmentModel> arr = dbHelper.getAllEquipmentSortedByPriceHighToLow();
                recyclerView.setAdapter(new EquipmentAdapter(getContext(), arr, "public"));
                return true;

            } else if (id == R.id.action_filter_price) {
                Toast.makeText(getContext(), "Filter by price clicked", Toast.LENGTH_SHORT).show();
                // Future filter dialog or range selector
                return true;
            }

            return false;
        });



        return view;
    }
}
