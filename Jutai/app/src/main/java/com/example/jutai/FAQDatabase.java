package com.example.jutai;



import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;

public class FAQDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "faq.db";
    private static final int DB_VERSION = 1;

    public FAQDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE faq (id INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT, answer TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void insertSampleFAQs() {
        SQLiteDatabase db = this.getWritableDatabase();
        // if (getAllFAQs().isEmpty()) {
        insertFAQ("What is the best time to plant wheat?", "October to December is ideal.");
        insertFAQ("How to improve soil fertility?", "Use crop rotation and organic compost.");
        insertFAQ("What fertilizer to use for rice?", "Use Urea and DAP based on soil test.");
        insertFAQ("Which crop is best for dry land farming?", "Millets like Jowar and Bajra are ideal.");
        insertFAQ("How to control weeds naturally?", "Use mulching and manual weeding techniques.");
        insertFAQ("What is green manure?", "It is a crop grown to be plowed into the soil to improve fertility.");
        insertFAQ("How to conserve soil moisture?", "Use mulching and minimum tillage methods.");
        insertFAQ("What is the best irrigation method for water conservation?", "Drip irrigation is most efficient.");
        insertFAQ("How often should I irrigate paddy fields?", "Maintain shallow water levels and irrigate every 7-10 days.");
        insertFAQ("How to treat fungal infections in crops?", "Use recommended fungicides and avoid overwatering.");
        insertFAQ("Which pest commonly affects cotton?", "Bollworm is a major pest in cotton crops.");
        insertFAQ("How to prevent pest resistance?", "Rotate chemicals and use integrated pest management.");
        insertFAQ("What is the ideal spacing for tomato planting?", "Maintain 60 cm between rows and 45 cm between plants.");
        insertFAQ("What is the best seed treatment method?", "Use fungicide or hot water treatment based on the crop.");
        insertFAQ("How to grow organic vegetables?", "Use organic compost, neem-based pesticides, and avoid chemicals.");
        insertFAQ("How to identify nutrient deficiency in plants?", "Yellow leaves may indicate nitrogen deficiency.");
        insertFAQ("What is the pH level ideal for wheat?", "A pH of 6.0 to 7.5 is suitable.");
        insertFAQ("What are the benefits of vermicompost?", "It improves soil aeration, nutrient content, and water holding.");
        insertFAQ("What is intercropping?", "Growing two or more crops in the same field simultaneously.");
        insertFAQ("What is the average yield of rice per acre?", "It ranges from 2 to 4 tons per acre depending on variety.");
        insertFAQ("How to prevent crop lodging?", "Use proper fertilizer dose and plant growth regulators.");
        insertFAQ("Which fruit crop is suitable for arid zones?", "Pomegranate is drought-tolerant and ideal.");
        insertFAQ("What is precision farming?", "It uses technology to optimize crop input and yield.");
        insertFAQ("How to increase productivity in pulses?", "Use certified seeds and ensure timely irrigation.");
        insertFAQ("What is the ideal temperature for maize?", "Between 18°C to 27°C is best for growth.");
        insertFAQ("What is zero tillage?", "Sowing without plowing to conserve soil structure and moisture.");
        insertFAQ("How to improve organic carbon in soil?", "Add compost, manure, and grow green manures.");
        insertFAQ("What is hydroponics?", "It’s growing plants in nutrient-rich water without soil.");
        insertFAQ("Which fertilizer is best for sugarcane?", "Apply nitrogen, phosphorus, and potash as per soil test.");
        insertFAQ("What are biofertilizers?", "Microorganisms that increase nutrient availability to plants.");
        insertFAQ("How to control termites in fields?", "Use chlorpyrifos or neem cake during land preparation.");
        insertFAQ("What is the role of micronutrients?", "They are essential for plant enzyme functions and growth.");
        insertFAQ("How to store grains safely?", "Keep in airtight containers with pest repellents like neem leaves.");
        insertFAQ("Which variety of mango is high-yielding?", "Alphonso and Kesar are popular high-yielding varieties.");
        insertFAQ("How often should I prune fruit trees?", "Once a year after harvesting season.");
        insertFAQ("What is integrated farming?", "Combining crop, livestock, and fish farming for sustainability.");
        insertFAQ("Which crops are suitable for summer season?", "Groundnut, moong, sunflower are good summer crops.");
        insertFAQ("How to harvest onions correctly?", "Harvest when tops dry out and bend over naturally.");
        insertFAQ("How to prevent fruit drop in citrus?", "Spray gibberellic acid and maintain irrigation.");
        insertFAQ("What is the maturity period of bananas?", "Usually 10 to 12 months after planting.");
        insertFAQ("How to control aphids on vegetables?", "Use neem oil spray or insecticidal soap.");
        insertFAQ("What is the shelf life of harvested potatoes?", "About 2 to 3 months in cool, dry conditions.");
        insertFAQ("Which variety of brinjal is best?", "Arka Shirish and Pusa Purple Long are popular.");
        insertFAQ("What is the best time to sow sunflower?", "During January to March or June to July.");
        insertFAQ("How much water does sugarcane need?", "About 1800 to 2200 mm annually.");
        insertFAQ("What is the spacing for okra planting?", "30 cm between plants and 45 cm between rows.");
        insertFAQ("How to increase milk production in cows?", "Provide balanced diet and regular health checkups.");
        insertFAQ("What is silage?", "Fermented green fodder stored for cattle feeding.");
        insertFAQ("How to manage livestock during summer?", "Provide shade, cool water, and avoid midday exposure.");
        insertFAQ("How to identify healthy seeds?", "Use float test or germination tests before sowing.");
        insertFAQ("How to protect young plants from frost?", "Cover with plastic or straw during cold nights.");
        insertFAQ("What is the best manure for vegetables?", "Well-rotted farmyard manure or compost.");
        insertFAQ("How to control mites on crops?", "Use sulfur sprays or miticides as per recommendation.");
        insertFAQ("Which insect attacks mustard?", "Aphids are common in mustard crops.");
        insertFAQ("What is the life cycle of red gram?", "It takes about 150-180 days to mature.");
        insertFAQ("How to prepare land for soybean?", "Plow and level the field, ensure good drainage.");
        insertFAQ("How to reduce post-harvest losses?", "Use proper storage, transport, and grading practices.");
        insertFAQ("What is the ideal temperature for germination?", "20°C to 30°C for most cereal crops.");
        insertFAQ("What are symptoms of zinc deficiency?", "Stunted growth and white lines on leaves.");
        insertFAQ("Which crops are suitable for saline soils?", "Barley, cotton, and sugar beet are more tolerant.");
        insertFAQ("How to prevent diseases in nurseries?", "Use sterilized soil and treat seeds.");
        insertFAQ("What is ratooning in sugarcane?", "Regrowth from stubble after first harvest.");
        insertFAQ("What is farm mechanization?", "Using machines to improve farming efficiency.");
        insertFAQ("What are hybrid seeds?", "Crossbred seeds with better yield and resistance.");
        insertFAQ("How to increase chickpea yield?", "Use resistant varieties and apply Rhizobium inoculation.");
        insertFAQ("How to test soil health?", "Get lab-tested for pH, nutrients, and organic matter.");
        insertFAQ("What is the role of potassium in crops?", "Improves resistance and grain filling.");
        insertFAQ("How to do terrace farming?", "Make horizontal platforms on slopes to prevent erosion.");
        insertFAQ("What is the importance of crop rotation?", "Reduces pest buildup and improves fertility.");
        insertFAQ("How to control whiteflies?", "Use yellow sticky traps and neem-based sprays.");
        insertFAQ("Which variety of groundnut is suitable for rainfed areas?", "TG-37A and Kadiri 6 are good.");
        insertFAQ("How to raise seedlings for transplantation?", "Use raised beds with compost and good drainage.");
        insertFAQ("What is polyhouse farming?", "Growing crops in controlled, covered environments.");
        insertFAQ("What is the ideal depth for sowing seeds?", "1 to 2 inches depending on the crop and seed size.");
        insertFAQ("How to prevent soil erosion?", "Contour plowing, cover crops, and vegetative barriers.");
        insertFAQ("How to compost farm waste?", "Layer green and dry waste with cow dung and turn it weekly.");
        insertFAQ("What is the importance of organic farming?", "Protects environment and improves long-term soil health.");
        insertFAQ("What is a seed drill?", "A machine for uniform seed sowing and depth control.");
        insertFAQ("How to manage waterlogging?", "Ensure proper drainage and raised bed planting.");
        insertFAQ("What is the growth duration of black gram?", "It matures in about 90 to 100 days.");
        insertFAQ("Which fertilizer promotes root development?", "Phosphorus is essential for root growth.");
        insertFAQ("How to prepare nursery for rice?", "Use well-leveled, fertilized, and irrigated nursery beds.");
        insertFAQ("How to identify bacterial leaf blight?", "Yellowing and drying of leaf edges in rice.");
        insertFAQ("What is the purpose of crop insurance?", "To protect farmers from crop failure and losses.");
        insertFAQ("How to dry harvested grains?", "Sun-dry for 3–4 days with frequent turning.");
        insertFAQ("What are leguminous crops?", "Plants like lentils and beans that fix nitrogen.");
        insertFAQ("What is the role of nitrogen in plants?", "Essential for vegetative growth and leaf color.");
        insertFAQ("How to promote flowering in vegetables?", "Balanced NPK and phosphorus at right stages.");
        insertFAQ("How to detect fake seeds?", "Check labeling, certification, and germination rate.");
        insertFAQ("What is the shelf life of urea fertilizer?", "Indefinite if stored properly in dry conditions.");
        insertFAQ("What is the yield potential of hybrid maize?", "Up to 8-10 tons per hectare under good management.");
        insertFAQ("How to transplant rice seedlings?", "Transplant at 2-3 leaf stage with 20 cm spacing.");
        insertFAQ("What is IPM?", "Integrated Pest Management uses eco-friendly pest control techniques.");
        insertFAQ("How to recognize viral diseases in crops?", "Leaf curling, stunted growth, mosaic patterns.");
        insertFAQ("What is the best time for pesticide application?", "In the early morning or late evening.");
        insertFAQ("Which crop is ideal for intercropping with maize?", "Beans, soybeans, and cowpea work well.");
        insertFAQ("How to maintain farm equipment?", "Clean after use, store in dry areas, and oil moving parts.");

        //}
    }

    public void insertFAQ(String question, String answer) {
        ContentValues values = new ContentValues();
        values.put("question", question);
        values.put("answer", answer);
        this.getWritableDatabase().insert("faq", null, values);
    }

    public List<FAQItem> getAllFAQs() {
        List<FAQItem> list = new ArrayList<>();
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM faq", null);
        while (cursor.moveToNext()) {
            list.add(new FAQItem(cursor.getString(1), cursor.getString(2)));
        }
        cursor.close();
        return list;
    }
    public void deleteAllFAQs() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("faqs", null, null);
        db.close();
    }

}
