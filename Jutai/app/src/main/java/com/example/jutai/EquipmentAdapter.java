package com.example.jutai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> {

    private Context context;
    private List<EquipmentModel> equipmentList;
    private String mode; // "owner" or "public"

    public EquipmentAdapter(Context context, List<EquipmentModel> equipmentList, String mode) {
        this.context = context;
        this.equipmentList = equipmentList;
        this.mode = mode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EquipmentModel item = equipmentList.get(position);
        holder.equip_name.setText(item.getName());
        holder.equip_price.setText("₹" + item.getPrice() + " / hr");

        if (item.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length);
            holder.equip_image.setImageBitmap(bitmap);
        }

        holder.itemView.setOnClickListener(v -> {
            if (mode.equals("owner")) {
                // Show popup menu
                PopupMenu popupMenu = new PopupMenu(context, holder.itemView);
                popupMenu.getMenuInflater().inflate(R.menu.owner_equipment_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    int id = menuItem.getItemId();
                    DatabaseHelper dbHelper = new DatabaseHelper(context);

                    if (id == R.id.menu_edit) {
                        Intent intent = new Intent(context, EditEquipmentActivity.class);
                        intent.putExtra("equipment_id", item.getId());
                        context.startActivity(intent);
                        return true;
                    } else if (id == R.id.menu_status) {
                        String newStatus = item.getAvailabilityStatus().equals("available") ? "unavailable" : "available";
                        boolean updated = dbHelper.updateAvailability(item.getId(), newStatus);
                        if (updated) {
                            Toast.makeText(context, "Status changed to " + newStatus, Toast.LENGTH_SHORT).show();
                            item.setAvailabilityStatus(newStatus);
                            notifyItemChanged(position);
                        }
                        return true;
                    } else if (id == R.id.menu_delete) {
                        boolean deleted = dbHelper.deleteEquipment(item.getId());
                        if (deleted) {
                            equipmentList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, equipmentList.size());
                            Toast.makeText(context, "Equipment deleted", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                    return false;
                });

                popupMenu.show();
            } else {
                // Public view
                SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                int userId = prefs.getInt("UserId", -1);

                if (userId != -1) {
                    DatabaseHelper dbHelper = new DatabaseHelper(context);
                    boolean added = dbHelper.addRecentlyViewed(userId, item.getId());

                    if (added) {
                        Toast.makeText(context, "Added to recently viewed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Not added to recently viewed", Toast.LENGTH_SHORT).show();
                    }
                }

                EquipmentDetailDialogFragment dialog = EquipmentDetailDialogFragment.newInstance(item);
                dialog.show(((FragmentActivity) context).getSupportFragmentManager(), "EquipmentDetailDialog");
            }
        });
    }

    @Override
    public int getItemCount() {
        return equipmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView equip_image;
        TextView equip_name, equip_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            equip_image = itemView.findViewById(R.id.equip_image);
            equip_name = itemView.findViewById(R.id.equip_name);
            equip_price = itemView.findViewById(R.id.equip_price);
        }
    }
}
