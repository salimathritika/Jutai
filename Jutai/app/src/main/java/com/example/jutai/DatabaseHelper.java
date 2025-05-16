//DatabaseHelper.java:
package com.example.jutai;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "app.db";
    public static final int DATABASE_VERSION = 1;

    // USERS TABLE
    public static final String TABLE_USERS = "Users";
    public static final String COL_USER_ID = "user_id";

    public static final String COL_NAME = "name";
    public static final String COL_EMAIL = "email";
    public static final String COL_PHONE = "phone";
    public static final String COL_PASSWORD_HASH = "password_hash";
    public static final String COL_LOCATION = "location";
    public static final String COL_CREATED_AT = "created_at";


    //EQUIPMENT
    public static final String TABLE_EQUIPMENT = "Equipment";
    public static final String COL_EQUIPMENT_ID = "equipment_id";
   public static final String COL_OWNER_ID = "owner_id";
   public static final String COL_EQUIP_NAME = "name";
  public static final String COL_CATEGORY = "category";
  public static final String COL_DESCRIPTION = "description";

  public static final String COL_IMAGE = "image";
  public static final String COL_PRICE_PER_HOUR = "price_per_hour";

  public static final String COL_AVAILABILITY_STATUS = "availability_status";
   public static final String COL_EQUIP_CREATED_AT = "created_at";

   //RECENTLY VIEWED
   public static final String TABLE_RECENTLY_VIEWED = "RecentlyViewed";
    public static final String COL_VIEW_ID = "view_id";
    public static final String COL_VIEW_USER_ID = "user_id";
    public static final String COL_VIEW_EQUIPMENT_ID = "equipment_id";
    public static final String COL_VIEWED_AT = "viewed_at";
//RENTALS
public static final String TABLE_RENTALS = "Rentals";
    public static final String COL_RENTAL_ID = "rental_id";
    public static final String COL_RENTER_ID = "renter_id";
    public static final String COL_RENTAL_EQUIPMENT_ID = "equipment_id";
    public static final String COL_START_DATE = "start_date";

    public static final String COL_RENTAL_HOUR = "rental_hour";

    public static final String COL_RENTAL_STATUS = "rental_status";
    public static final String COL_TOTAL_PRICE = "total_price";
    public static final String COL_RENTAL_CREATED_AT = "created_at";

    //PAYMENTS

    public static final String TABLE_PAYMENT = "Payment";
    public static final String COL_PAYMENT_ID = "payment_id";
    public static final String COL_PAYMENT_RENTAL_ID = "rental_id";

    public static final String COL_PAYMENT_USER_ID = "user_id";
    public static final String COL_PAYMENT_EQUIP_ID = "equipment_id";
    public static final String COL_PAYMENT_AMOUNT = "amount";
    public static final String COL_PAYMENT_STATUS = "payment_status";
    public static final String COL_PAYMENT_CREATED_AT = "created_at";
    //REVIEWS

    public static final String TABLE_REVIEWS = "Reviews";

    public static final String COL_REVIEW_ID = "review_id";
    public static final String COL_REVIEW_EQUIPMENT_ID = "equipment_id";
    public static final String COL_REVIEWER_ID = "reviewer_id";
    public static final String COL_REVIEW_RATING = "rating";
    public static final String COL_REVIEW_TEXT = "review_text";
    public static final String COL_REVIEW_CREATED_AT = "created_at";

    //Notifications Table

    public static final String TABLE_NOTIFICATIONS = "Notifications";
    public static final String COLUMN_NOTIFICATION_ID = "notification_id";

    public static final String COLUMN_NOTIF_RENTAL_ID = "rental_id";
    public static final String COLUMN_NOTIFICATION_USER_ID = "user_id";

    public static final String COLUMN_NOTIFICATION_MESSAGE = "message";

    public static final String COLUMN_NOTIFICATION_TYPE = "notification_type";
//    "rental_request" – when someone sends a rental request
//
//"rental_approved" – owner approved
//
//"rental_declined" – owner declined
//
//"payment_confirmed" – system confirms payment
//
//"reminder" – scheduled reminder to return equipment
//
//"info" – general messages
    public static final String COLUMN_NOTIFICATION_STATUS = "status";
    public static final String COLUMN_NOTIFICATION_CREATED_AT = "created_at";





    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // USERS table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_PHONE + " TEXT UNIQUE NOT NULL, " +
                COL_EMAIL + " TEXT, " +
                COL_PASSWORD_HASH + " TEXT NOT NULL, " +
                COL_LOCATION + " TEXT, " +
                COL_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(CREATE_USERS_TABLE);


        String CREATE_EQUIPMENT_TABLE = "CREATE TABLE " + TABLE_EQUIPMENT + " (" +
                COL_EQUIPMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_OWNER_ID + " INTEGER NOT NULL, " +

                COL_EQUIP_NAME + " TEXT NOT NULL, " +
                COL_CATEGORY + " TEXT CHECK(" + COL_CATEGORY + " IN ('Tractor', 'Implement')) NOT NULL, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_IMAGE + " BLOB, " +
                COL_PRICE_PER_HOUR + " REAL NOT NULL, " +
               COL_AVAILABILITY_STATUS + " TEXT CHECK(" + COL_AVAILABILITY_STATUS + " IN ('available', 'rented', 'unavailable')) DEFAULT 'available', " +
               COL_EQUIP_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (" + COL_OWNER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + ") ON DELETE CASCADE)";
        db.execSQL(CREATE_EQUIPMENT_TABLE);

        String createRecentlyViewedTable = "CREATE TABLE " + TABLE_RECENTLY_VIEWED + " (" +
                COL_VIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_VIEW_USER_ID + " INTEGER NOT NULL, " +
                COL_VIEW_EQUIPMENT_ID + " INTEGER NOT NULL, " +
                COL_VIEWED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(" + COL_VIEW_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + COL_VIEW_EQUIPMENT_ID + ") REFERENCES " + TABLE_EQUIPMENT + "(" + COL_EQUIPMENT_ID + ") ON DELETE CASCADE)";
        db.execSQL(createRecentlyViewedTable);

        String createRentalsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_RENTALS + " (" +
                COL_RENTAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RENTER_ID + " INTEGER NOT NULL, " +
                COL_RENTAL_EQUIPMENT_ID + " INTEGER NOT NULL, " +
                COL_START_DATE + " TEXT NOT NULL, " +
                COL_RENTAL_HOUR + " INTEGER NOT NULL, " +

                COL_RENTAL_STATUS + " TEXT CHECK(" + COL_RENTAL_STATUS + " IN ('pending', 'confirmed', 'completed', 'cancelled')) DEFAULT 'pending', " +
                COL_TOTAL_PRICE + " REAL NOT NULL, " +
                COL_RENTAL_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(" + COL_RENTER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + COL_RENTAL_EQUIPMENT_ID + ") REFERENCES " + TABLE_EQUIPMENT + "(" + COL_EQUIPMENT_ID + ") ON DELETE CASCADE)";
        db.execSQL(createRentalsTable);

        String CREATE_REVIEWS_TABLE = "CREATE TABLE " + TABLE_REVIEWS + " (" +
                COL_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_REVIEW_EQUIPMENT_ID + " INTEGER NOT NULL, " +
                COL_REVIEWER_ID + " INTEGER NOT NULL, " +
                COL_REVIEW_RATING + " INTEGER CHECK (" + COL_REVIEW_RATING + " BETWEEN 1 AND 5), " +
                COL_REVIEW_TEXT + " TEXT, " +
                COL_REVIEW_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (" + COL_REVIEW_EQUIPMENT_ID + ") REFERENCES " + TABLE_EQUIPMENT + "(" + COL_EQUIPMENT_ID + ") ON DELETE CASCADE, " +
                "FOREIGN KEY (" + COL_REVIEWER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + ") ON DELETE CASCADE" +
                ")";
        db.execSQL(CREATE_REVIEWS_TABLE);

        String createPaymentTable = "CREATE TABLE IF NOT EXISTS " + TABLE_PAYMENT + " (" +
                COL_PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PAYMENT_RENTAL_ID + " INTEGER NOT NULL, " +  // updated name
                COL_PAYMENT_USER_ID + " INTEGER NOT NULL, " +
                COL_PAYMENT_EQUIP_ID + " INTEGER NOT NULL, " +
                COL_PAYMENT_AMOUNT + " REAL NOT NULL, " +
                COL_PAYMENT_STATUS + " TEXT CHECK(" + COL_PAYMENT_STATUS + " IN ('pending', 'success', 'failed')) DEFAULT 'pending', " +
                COL_PAYMENT_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(" + COL_PAYMENT_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + COL_PAYMENT_EQUIP_ID + ") REFERENCES " + TABLE_EQUIPMENT + "(" + COL_EQUIPMENT_ID + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + COL_PAYMENT_RENTAL_ID + ") REFERENCES " + TABLE_RENTALS + "(rental_id) ON DELETE CASCADE)";
        db.execSQL(createPaymentTable);

        String CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE " + TABLE_NOTIFICATIONS + " (" +
                COLUMN_NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOTIFICATION_USER_ID + " INTEGER NOT NULL, " +
                COLUMN_NOTIFICATION_MESSAGE + " TEXT NOT NULL, " +
                COLUMN_NOTIFICATION_TYPE + " TEXT NOT NULL, " +
                COLUMN_NOTIF_RENTAL_ID+ " INTEGER NOT NULL, " +
                COLUMN_NOTIFICATION_STATUS + " TEXT DEFAULT 'unread', " +
                COLUMN_NOTIFICATION_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (" + COLUMN_NOTIFICATION_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + ") ON DELETE CASCADE)";
        db.execSQL(CREATE_NOTIFICATIONS_TABLE);
    }
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQUIPMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // === USERS TABLE METHODS ===

    public boolean insertUser( String name, String phone, String email, String passwordHash, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(COL_FIREBASE_UID, firebaseUid);
        values.put(COL_NAME, name);

        values.put(COL_PHONE, phone);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD_HASH, passwordHash);
        values.put(COL_LOCATION, location);
        return db.insert(TABLE_USERS, null, values) != -1;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    public int getUserid(String phone_number)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+COL_USER_ID + " FROM " + TABLE_USERS + " WHERE " + COL_PHONE + " =? ",new String[]{phone_number} );
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }

        cursor.close();
        return userId;
    }

    public String getUserName(int userId)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_NAME + " FROM "+ TABLE_USERS + " WHERE " + COL_USER_ID + " = ?",new String[]{String.valueOf(userId)});

        if(cursor.moveToFirst())
        {

            return cursor.getString(0);

        }
        else
        {

            return "null";
        }

    }

    public  String getUserPhoneNumber(int userId)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_PHONE + " FROM "+ TABLE_USERS + " WHERE " + COL_USER_ID + " = ?",new String[]{String.valueOf(userId)});

        if(cursor.moveToFirst())
        {

            return cursor.getString(0);

        }
        else
        {

            return "null";
        }
    }







    public boolean verifyUser(String phone, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " +COL_PHONE + "=?" + " AND " + COL_PASSWORD_HASH + "=?", new String[]{phone, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    public boolean userExists(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE "+ COL_PHONE + "= ?", new String[]{phone});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_USERS, COL_USER_ID + "=?", new String[]{String.valueOf(userId)});
        return rows > 0;
    }

    public boolean updateProfile(int userId, String name, String location, String oldPassword, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        // First, fetch current password hash
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COL_PASSWORD_HASH},
                COL_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String currentHash = cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD_HASH));
            cursor.close();

            if (!currentHash.equals(oldPassword)) {  // Assuming hash is stored directly; otherwise compare hash(oldPassword)
                return false;  // old password incorrect
            }

            ContentValues values = new ContentValues();
            values.put(COL_NAME, name);
            values.put(COL_LOCATION, location);
            if (newPassword != null && !newPassword.isEmpty()) {
                values.put(COL_PASSWORD_HASH, newPassword);
            }

            int rows = db.update(TABLE_USERS, values, COL_USER_ID + "=?", new String[]{String.valueOf(userId)});
            return rows > 0;
        }

        return false;
    }

//    // === EQUIPMENT TABLE METHODS ===
//
   public boolean insertEquipment(int ownerId, String name, String category, String description,
                                 byte[] image, double pricePerHour) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_OWNER_ID, ownerId);
        values.put(COL_EQUIP_NAME, name);
        values.put(COL_CATEGORY, category);
        values.put(COL_DESCRIPTION, description);

        values.put(COL_IMAGE, image);
        values.put(COL_PRICE_PER_HOUR, pricePerHour);

    // availability_status and created_at default

        return db.insert(TABLE_EQUIPMENT, null, values) != -1;
    }
    public List<EquipmentModel> getAllEquipment() {
        List<EquipmentModel> equipmentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EQUIPMENT + " WHERE " + COL_AVAILABILITY_STATUS + " = ? ",new String[]{"available"});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_EQUIPMENT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_EQUIP_NAME));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE_PER_HOUR));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COL_IMAGE));
                String availability = cursor.getString(cursor.getColumnIndexOrThrow(COL_AVAILABILITY_STATUS));

                EquipmentModel equipment = new EquipmentModel(id, name, category, desc, price, image, availability);
                equipmentList.add(equipment);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return equipmentList;
    }


    public boolean addRecentlyViewed(int userId, int equipmentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_VIEW_USER_ID, userId);
        values.put(COL_VIEW_EQUIPMENT_ID, equipmentId);
        long result = db.insert(TABLE_RECENTLY_VIEWED, null, values);
        return result != -1;
    }

    public List<byte[]> getRecentlyViewedImagesForUser(int userId) {
        List<byte[]> images = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT e." + COL_IMAGE + " FROM " + TABLE_EQUIPMENT + " e " +
                "INNER JOIN " + TABLE_RECENTLY_VIEWED + " rv ON e." + COL_EQUIPMENT_ID + " = rv." + COL_VIEW_EQUIPMENT_ID + " " +
                "WHERE rv." + COL_VIEW_USER_ID + " = ? " +
                "ORDER BY rv." + COL_VIEWED_AT + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COL_IMAGE));
                images.add(image);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return images;
    }
    public List<EquipmentModel> getEquipmentsByOwner(int ownerId) {
        List<EquipmentModel> equipmentList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EQUIPMENT + " WHERE " + COL_OWNER_ID + " = ?",
                new String[]{String.valueOf(ownerId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_EQUIPMENT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_EQUIP_NAME));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE_PER_HOUR));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COL_IMAGE));
                String availability = cursor.getString(cursor.getColumnIndexOrThrow(COL_AVAILABILITY_STATUS));

                EquipmentModel equipment = new EquipmentModel(id, name, category, description, price, image, availability);
                equipmentList.add(equipment);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return equipmentList;
    }


    public double getEquipmentPrice(int equipmentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double price = -1;

        Cursor cursor = db.rawQuery(
                "SELECT " + COL_PRICE_PER_HOUR + " FROM " + TABLE_EQUIPMENT +
                        " WHERE " + COL_EQUIPMENT_ID + " = ?",
                new String[]{String.valueOf(equipmentId)}
        );

        if (cursor != null && cursor.moveToFirst()) {
            price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE_PER_HOUR));
            cursor.close();
        }

        return price;
    }
    public String getLocationByEquipmentId(int equipmentId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT u." + COL_LOCATION +
                " FROM " + TABLE_EQUIPMENT + " e " +
                " JOIN " + TABLE_USERS + " u ON e." + COL_OWNER_ID + " = u." + COL_USER_ID +
                " WHERE e." + COL_EQUIPMENT_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(equipmentId)});

        String location = null;
        if (cursor.moveToFirst()) {
            location = cursor.getString(0);
        }
        cursor.close();
        return location;
    }
    public boolean updateEquipment(int equipmentId, String name, String category, String description,
                                   double pricePerHour, byte[] image, String availability) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_EQUIP_NAME, name);
        values.put(COL_CATEGORY, category);
        values.put(COL_DESCRIPTION, description);
        values.put(COL_IMAGE, image);
        values.put(COL_PRICE_PER_HOUR, pricePerHour);
        values.put(COL_AVAILABILITY_STATUS, availability); // ✅ use passed value

        int rowsAffected = db.update(TABLE_EQUIPMENT, values, COL_EQUIPMENT_ID + " = ?",
                new String[]{String.valueOf(equipmentId)});
        return rowsAffected > 0;
    }

    public boolean updateAvailability(int id, String newStatus) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_AVAILABILITY_STATUS,newStatus);
        long res = db.update(TABLE_EQUIPMENT,cv,COL_EQUIPMENT_ID + " = ?",new String[]{String.valueOf(id)});
        return res!=-1;
    }

    public boolean deleteEquipment(int id) {

            SQLiteDatabase db = this.getWritableDatabase();
            int rows = db.delete(TABLE_EQUIPMENT, COL_EQUIPMENT_ID + " = ?", new String[]{String.valueOf(id)});
            return rows > 0;

    }
    public EquipmentModel getEquipmentById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EQUIPMENT + " WHERE " + COL_EQUIPMENT_ID +  " = ?", new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            EquipmentModel equipment = new EquipmentModel(
                    cursor.getInt(cursor.getColumnIndex(COL_EQUIPMENT_ID)),
                    cursor.getString(cursor.getColumnIndex(COL_EQUIP_NAME)),
                    cursor.getString(cursor.getColumnIndex(COL_CATEGORY)),
                    cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION)),
                    cursor.getDouble(cursor.getColumnIndex(COL_PRICE_PER_HOUR)),
                    cursor.getBlob(cursor.getColumnIndex(COL_IMAGE)),
                    cursor.getString(cursor.getColumnIndex(COL_AVAILABILITY_STATUS))
            );



            cursor.close();
            return equipment;
        }
        return null;
    }

    public int getOwnerIdForEquipment( int equipmentId) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT owner_id FROM equipment WHERE equipment_id = ?", new String[]{String.valueOf(equipmentId)});
        if (cursor.moveToFirst()) {
            int ownerId = cursor.getInt(0);
            cursor.close();
            return ownerId;
        }
        cursor.close();
        return -1;
    }

    //RENTAL TABLE METHODS
    public long insertRental(int renterId, int equipmentId, String startDate, int hours , double totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_RENTER_ID, renterId);
        values.put(COL_RENTAL_EQUIPMENT_ID, equipmentId);
        values.put(COL_START_DATE, startDate);
        //values.put(COL_END_DATE, endDate);
        values.put(COL_RENTAL_HOUR, hours);

        values.put(COL_TOTAL_PRICE, totalPrice);
        values.put(COL_RENTAL_STATUS, "pending"); // default

        return db.insert(TABLE_RENTALS, null, values);
    }

    public Cursor getRentalDetails(int rentalId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT renter_id, (SELECT name FROM equipment WHERE equipment_id = rentals.equipment_id) AS equipment_name FROM rentals WHERE rental_id = ?", new String[]{String.valueOf(rentalId)});
    }


    //debug method
    public String getRentalStatus(int rentalId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT rental_status FROM rentals WHERE rental_id = ?", new String[]{String.valueOf(rentalId)});

        if (cursor != null && cursor.moveToFirst()) {
            String status = cursor.getString(cursor.getColumnIndex("rental_status"));
            cursor.close();
            return status;
        } else {
            cursor.close();
            return null;
        }
    }



    public Cursor getRentalsByUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RENTALS + " WHERE " + COL_RENTER_ID + " = ?", new String[]{String.valueOf(userId)});
    }

    public Cursor getRentalsByEquipment(int equipmentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RENTALS + " WHERE " + COL_RENTAL_EQUIPMENT_ID + " = ?", new String[]{String.valueOf(equipmentId)});
    }

    public Cursor getRentalsWithEquipmentNameByUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Equipment.name AS equipment_name, Rentals.created_at, Rentals.rental_id " +
                "FROM Rentals " +
                "JOIN Equipment ON Rentals.equipment_id = Equipment.equipment_id " +
                "WHERE Rentals.renter_id = ?";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }
    public Cursor getRentalsWithEquipmentNameAndTotalPriceByUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Equipment.name AS equipment_name, Rentals.created_at, Rentals.amount * Rentals.hours AS total_price " +
                "FROM Rentals " +
                "JOIN Equipment ON Rentals.equipment_id = Equipment.equipment_id " +
                "WHERE Rentals.renter_id = ?";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }


    public Cursor getAllRentals() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RENTALS, null);


    }

    public Cursor getRentalPaymentDetailsByUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Equipment.name, Rentals.created_at, Payment.amount, Payment.payment_status, Rentals.equipment_id " +
                "FROM Rentals " +
                "JOIN Equipment ON Rentals.equipment_id = Equipment.equipment_id " +
                "JOIN Payment ON Rentals.rental_id = Payment.rental_id " +
                "WHERE Rentals.renter_id = ?";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }

    public boolean updateRentalStatus(int rentalId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_RENTAL_STATUS, newStatus);
        int rowsAffected = db.update(TABLE_RENTALS, values, COL_RENTAL_ID + " = ?", new String[]{String.valueOf(rentalId)});
        return rowsAffected > 0;
    }
    // === REVIEW TABLE METHODS ===

    public boolean insertReview(int equipmentId, int reviewerId, int rating, String reviewText) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_REVIEW_EQUIPMENT_ID, equipmentId);
        values.put(COL_REVIEWER_ID, reviewerId);
        values.put(COL_REVIEW_RATING, rating);
        values.put(COL_REVIEW_TEXT, reviewText);

        long result = db.insert(TABLE_REVIEWS, null, values);
        return result != -1;
    }

    public Cursor getReviewsForEquipment(int equipmentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COL_REVIEW_RATING + ", " + COL_REVIEW_TEXT +
                " FROM " + TABLE_REVIEWS +
                " WHERE " + COL_REVIEW_EQUIPMENT_ID + " = ?" +
                " ORDER BY " + COL_REVIEW_CREATED_AT + " DESC";
        return db.rawQuery(query, new String[]{String.valueOf(equipmentId)});
    }
    //sorting
    public List<EquipmentModel> getAllEquipmentSortedByPriceLowToHigh() {
        ArrayList<EquipmentModel> equipmentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + TABLE_EQUIPMENT +
                " ORDER BY " + COL_PRICE_PER_HOUR + " ASC", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_EQUIPMENT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_EQUIP_NAME));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE_PER_HOUR));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COL_IMAGE));
                String availability = cursor.getString(cursor.getColumnIndexOrThrow(COL_AVAILABILITY_STATUS));

                EquipmentModel equipment = new EquipmentModel(id, name, category, desc, price, image, availability);
                equipmentList.add(equipment);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return equipmentList;
    }
    public List<EquipmentModel> getAllEquipmentSortedByPriceHighToLow() {
        ArrayList<EquipmentModel> equipmentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + TABLE_EQUIPMENT +
                " ORDER BY " + COL_PRICE_PER_HOUR + " DESC", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_EQUIPMENT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_EQUIP_NAME));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE_PER_HOUR));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COL_IMAGE));
                String availability = cursor.getString(cursor.getColumnIndexOrThrow(COL_AVAILABILITY_STATUS));

                EquipmentModel equipment = new EquipmentModel(id, name, category, desc, price, image, availability);
                equipmentList.add(equipment);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return equipmentList;
    }


    //get either tractor or implement

    public List<EquipmentModel>  getEquipmentByCategory(String category) {

        ArrayList<EquipmentModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EQUIPMENT +
                " WHERE " + COL_CATEGORY + " = ?", new String[]{category});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_EQUIPMENT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_EQUIP_NAME));
                String Category = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE_PER_HOUR));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COL_IMAGE));
                String availability = cursor.getString(cursor.getColumnIndexOrThrow(COL_AVAILABILITY_STATUS));

                EquipmentModel equipment = new EquipmentModel(id, name, Category, desc, price, image, availability);
                list.add(equipment);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }


    public float getAverageRatingForEquipment(int equipmentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT AVG(" + COL_REVIEW_RATING + ") FROM " + TABLE_REVIEWS +
                        " WHERE " + COL_REVIEW_EQUIPMENT_ID + " = ?",
                new String[]{String.valueOf(equipmentId)});
        float avg = 0;
        if (cursor.moveToFirst()) {
            avg = cursor.getFloat(0);
        }
        cursor.close();
        return avg;
    }


    public boolean deleteReview(int reviewId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_REVIEWS, COL_REVIEW_ID + " = ?", new String[]{String.valueOf(reviewId)});
        return rows > 0;
    }

    public Cursor getAllReviews() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_REVIEWS, null);
    }

    //PAYMENTS
    public boolean insertPayment(int rental_id,int userId, int equipmentId, double amount, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PAYMENT_RENTAL_ID, rental_id);
        values.put(COL_PAYMENT_USER_ID, userId);
        values.put(COL_PAYMENT_EQUIP_ID, equipmentId);
        values.put(COL_PAYMENT_AMOUNT, amount);
        values.put(COL_PAYMENT_STATUS, status);

        long result = db.insert(TABLE_PAYMENT, null, values);
        return result != -1;
    }

    //get payment history

    public Cursor getPaymentsByUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PAYMENT +
                        " WHERE " + COL_PAYMENT_USER_ID + " = ?",
                new String[]{String.valueOf(userId)});
    }
    public Cursor getAllPayments() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PAYMENT, null);
    }



    //--NOTIFICATIONS--

    // === Notification TABLE METHODS ===


    // Insert a new notification
    public boolean insertNotification(int userId,int rentalId,String message,String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTIFICATION_USER_ID, userId);
        values.put(COLUMN_NOTIFICATION_MESSAGE, message);
        values.put(COLUMN_NOTIF_RENTAL_ID,rentalId);
        values.put(COLUMN_NOTIFICATION_TYPE,type);
        values.put(COLUMN_NOTIFICATION_STATUS, "unread");

        long result = db.insert(TABLE_NOTIFICATIONS, null, values);
        return result != -1;
    }

    // Get notifications for a user
    public Cursor getNotificationsForUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NOTIFICATIONS + " WHERE " + COLUMN_NOTIFICATION_USER_ID + " = ? ORDER BY " + COLUMN_NOTIFICATION_CREATED_AT + " DESC", new String[]{String.valueOf(userId)});


    }

    // Mark a notification as read
    public boolean markNotificationAsRead(int notificationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTIFICATION_STATUS, "read");
        int rows = db.update(TABLE_NOTIFICATIONS, values, COLUMN_NOTIFICATION_ID + " = ?", new String[]{String.valueOf(notificationId)});
        return rows > 0;
    }

    // Delete notification
    public boolean deleteNotification(int notificationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_NOTIFICATIONS, COLUMN_NOTIFICATION_ID + " = ?", new String[]{String.valueOf(notificationId)});
        return rows > 0;
    }

    public Cursor getRentalIdByNotifId(int notifId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NOTIFICATIONS+ " WHERE " + COLUMN_NOTIFICATION_ID + " = ?", new String[]{String.valueOf(notifId)});
    }

    public Cursor getTypeByNotifId(int notifId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NOTIFICATIONS+" WHERE " + COLUMN_NOTIFICATION_TYPE + " = ?", new String[]{String.valueOf(notifId)});
    }

    public int getEquipmentIdByRentalId(int rentalId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_RENTAL_EQUIPMENT_ID +  " FROM " + TABLE_RENTALS + " WHERE " + COL_RENTAL_ID +" = ?", new String[]{String.valueOf(rentalId)});
        int equipment_id = -1;
        if(cursor.moveToFirst())
        {
             equipment_id = cursor.getInt(0);


        }
        cursor.close();
        return equipment_id;


    }


//
//    public Cursor getAllEquipment() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.rawQuery("SELECT * FROM " + TABLE_EQUIPMENT, null);
//    }
//
//    public Cursor getEquipmentByOwner(int ownerId) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.rawQuery("SELECT * FROM " + TABLE_EQUIPMENT + " WHERE " + COL_OWNER_ID + " = ?", new String[]{String.valueOf(ownerId)});
//    }
//
//    public boolean updateEquipmentStatus(int equipmentId, String newStatus) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COL_AVAILABILITY_STATUS, newStatus);
//        int rowsAffected = db.update(TABLE_EQUIPMENT, values, COL_EQUIPMENT_ID + " = ?", new String[]{String.valueOf(equipmentId)});
//        return rowsAffected > 0;
//    }

}