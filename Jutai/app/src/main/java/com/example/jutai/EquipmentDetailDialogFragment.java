package com.example.jutai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class EquipmentDetailDialogFragment extends DialogFragment {



    private static final String ARG_EQUIPMENT = "equipment";

    private EquipmentModel equipment;

    public static EquipmentDetailDialogFragment newInstance(EquipmentModel equipment) {
        EquipmentDetailDialogFragment fragment = new EquipmentDetailDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EQUIPMENT, equipment);  // pass the equipment data
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            equipment = (EquipmentModel) getArguments().getSerializable(ARG_EQUIPMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for the dialog
        View view = inflater.inflate(R.layout.dialog_equipment_details, container, false);

        // Bind the views and display the equipment details
        Button btnRent = view.findViewById(R.id.btn_rent);
        Button btnReview = view.findViewById(R.id.btn_review);
        ImageView imageView = view.findViewById(R.id.dialog_equip_image);
        TextView nameTextView = view.findViewById(R.id.dialog_equip_name);
        TextView categoryTextView = view.findViewById(R.id.dialog_equip_category);
        TextView descriptionTextView = view.findViewById(R.id.dialog_equip_description);
        TextView priceTextView = view.findViewById(R.id.dialog_equip_price);
        TextView availabilityTextView = view.findViewById(R.id.dialog_equip_availability);
        Button btnLocation = view.findViewById(R.id.btn_location);
        DatabaseHelper db = new DatabaseHelper(requireContext());

        if (equipment.getImage() != null && equipment.getImage().length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(equipment.getImage(), 0, equipment.getImage().length);
            imageView.setImageBitmap(bitmap);
        }

        nameTextView.setText(equipment.getName());
        categoryTextView.setText(equipment.getCategory());
        descriptionTextView.setText(equipment.getDescription());
        priceTextView.setText("₹" + equipment.getPrice() + " / hr");
        availabilityTextView.setText(equipment.getAvailabilityStatus());
        btnRent.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RentalDetailsActivity.class);
            intent.putExtra("equipmentId", equipment.getId());
            intent.putExtra("equipmentName", equipment.getName());
            intent.putExtra("pricePerHour", equipment.getPrice());
            // Add more extras if needed
            startActivity(intent);
            dismiss(); // dismiss dialog
        });

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ViewReviewActivity.class);
                intent.putExtra("equipmentId", equipment.getId());
                startActivity(intent);



            }
        });


        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EquipmentLocation.class);
                intent.putExtra("equipmentId", equipment.getId());
                startActivity(intent);

            }
        });

        return view;
    }
}
