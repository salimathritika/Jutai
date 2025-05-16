package com.example.jutai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddNewActivity extends AppCompatActivity {
    Button addimagebtn,donebtn,backbtn;
    EditText equipment_name,equipment_price,equipment_desc;
    private final int CAMERA_REQ_CODE = 100;
    private final int GALLERY_REQ_CODE = 1000;
    ImageView image;
    Spinner category_spinner;

    ArrayList<String> category = new ArrayList<>();

    String equip_name,equip_desc,equip_category;
    double equip_price;
    byte[] imageBytes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        category.add("Tractor");
        category.add("Implement");
        DatabaseHelper db = new DatabaseHelper(this);



        image = findViewById(R.id.imageView2);
        addimagebtn = findViewById(R.id.addimagebtn);
        equipment_price = findViewById(R.id.equipment_price);
        equipment_name = findViewById(R.id.equipment_name);
        equipment_desc = findViewById(R.id.equipment_desc);
        category_spinner = findViewById(R.id.category_spinner);
        donebtn = findViewById(R.id.donebtn);
        backbtn = findViewById(R.id.backbtn);

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,category);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        category_spinner.setAdapter(adapter);
        addimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(AddNewActivity.this,v);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    int itemid = item.getItemId();
                    if(itemid == R.id.camera)
                    {
                        Intent icamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(icamera,CAMERA_REQ_CODE);

                        return true;
                    }
                    else if(itemid == R.id.gallery)
                    {
                        Intent igallery = new Intent(Intent.ACTION_PICK);
                        igallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(igallery,GALLERY_REQ_CODE);
                        return true;
                    }


                    return false;

                });
                popup.show();


            }
        });

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                equip_category = category.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getApplicationContext(),"Select the suitable category",Toast.LENGTH_SHORT).show();

            }
        });

        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(equipment_name.getText().toString().isEmpty() || equipment_price.getText().toString().isEmpty() || equipment_desc.getText().toString().isEmpty() ||equip_category.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Fill all the fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(imageBytes == null)
                {
                    Toast.makeText(getApplicationContext(),"Upload Image",Toast.LENGTH_SHORT).show();
                }
                equip_name = equipment_name.getText().toString();
                equip_desc = equipment_desc.getText().toString();
                equip_price = Double.parseDouble(equipment_price.getText().toString());

                SharedPreferences sharedpref = getSharedPreferences("MyAppPrefs",MODE_PRIVATE);
               int ownerId =  sharedpref.getInt("UserId",-1);

                if (ownerId == -1) {
                    Toast.makeText(getApplicationContext(), "User is not logged in", Toast.LENGTH_SHORT).show();
                    return; // Exit the method if user is not logged in
                }


                if(db.insertEquipment(ownerId,equip_name,equip_category,equip_desc,imageBytes,equip_price))
                {
                    Toast.makeText(getApplicationContext(),"Added successfully",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Failed to add",Toast.LENGTH_SHORT).show();
                }

            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewActivity.this, MainPageActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQ_CODE) {
                Bitmap bitmap = (Bitmap) (data.getExtras().get("data"));
                image.setImageBitmap(bitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                this.imageBytes = stream.toByteArray();  // ✅ Corrected

            } else if (requestCode == GALLERY_REQ_CODE) {
                image.setImageURI(data.getData());
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    this.imageBytes = stream.toByteArray();  // ✅ Corrected
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
        }
    }

}
