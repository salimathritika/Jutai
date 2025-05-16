package com.example.jutai;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class EditEquipmentActivity extends AppCompatActivity {

    private EditText nameEdit, descEdit, priceEdit;
    private Spinner categorySpinner, availabilitySpinner;
    private ImageView equipImage;
    private Button saveBtn;
    private byte[] imageBytes;
    private int equipmentId;

    private static final int IMAGE_PICK_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_equipment);

        nameEdit = findViewById(R.id.edit_name);
        descEdit = findViewById(R.id.edit_desc);
        priceEdit = findViewById(R.id.edit_price);
        categorySpinner = findViewById(R.id.edit_category_spinner);
        availabilitySpinner = findViewById(R.id.edit_availability_spinner);
        equipImage = findViewById(R.id.edit_image);
        saveBtn = findViewById(R.id.btn_save_equipment);

        // Setup spinners
        ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(catAdapter);

        ArrayAdapter<CharSequence> availAdapter = ArrayAdapter.createFromResource(this,
                R.array.availability_array, android.R.layout.simple_spinner_item);
        availAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        availabilitySpinner.setAdapter(availAdapter);

        // Get equipment from intent
        equipmentId = getIntent().getIntExtra("equipment_id", 0);

        if (equipmentId == 0) {
            Toast.makeText(this, "Equipment ID not received", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

// Now fetch the equipment from the database using this ID
        DatabaseHelper db = new DatabaseHelper(this);
        EquipmentModel equipment = db.getEquipmentById(equipmentId);

        if (equipment == null) {
            Toast.makeText(this, "Equipment not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

// Populate the UI fields
        nameEdit.setText(equipment.getName());
        descEdit.setText(equipment.getDescription());
        priceEdit.setText(String.valueOf(equipment.getPrice()));
        categorySpinner.setSelection(catAdapter.getPosition(equipment.getCategory()));
        availabilitySpinner.setSelection(availAdapter.getPosition(equipment.getAvailabilityStatus()));

        if (equipment.getImage() != null) {
            imageBytes = equipment.getImage();
            Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            equipImage.setImageBitmap(bmp);
        }





        equipImage.setOnClickListener(v -> pickImageFromGallery());

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEdit.getText().toString();
                String category = categorySpinner.getSelectedItem().toString();
                String description = descEdit.getText().toString();
                double price = Double.parseDouble(priceEdit.getText().toString());
                String availability = availabilitySpinner.getSelectedItem().toString();
                if(name.isEmpty() || category.isEmpty() || description.isEmpty() || String.valueOf(price).isEmpty() || availability.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Fill all the fields",Toast.LENGTH_SHORT).show();
                }
                else {

                    boolean updated = db.updateEquipment(equipmentId, name, category, description, price, imageBytes, availability);

                    if (updated) {
                        Toast.makeText(getApplicationContext(), "Equipment edited", Toast.LENGTH_SHORT).show();
                        finish(); // optional
                    } else {
                        Toast.makeText(getApplicationContext(), "Equipment not edited", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                equipImage.setImageBitmap(bitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageBytes = stream.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
