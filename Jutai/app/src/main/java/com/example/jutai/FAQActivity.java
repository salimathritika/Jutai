package com.example.jutai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.nl.translate.*;

import java.util.*;

public class FAQActivity extends AppCompatActivity {

    Spinner langSpinner;
    AutoCompleteTextView questionAutoComplete;
    TextView answerText;
    FAQDatabase db;
    List<FAQItem> faqList;
    String selectedLangCode = "en";
    Button backbtn;
    Map<String, String> translatedQuestionsMap = new HashMap<>();  // Maps translated question -> original

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqactivity);

        langSpinner = findViewById(R.id.langSpinner);
        questionAutoComplete = findViewById(R.id.questionAutoComplete);
        answerText = findViewById(R.id.answerText);
        backbtn = findViewById(R.id.backbtn);

        db = new FAQDatabase(this);
        db.insertSampleFAQs();
        faqList = db.getAllFAQs();

        // Initially populate question box in English
        populateQuestionBoxWithTranslatedQuestions("en");

        langSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                switch (pos) {
                    case 0: selectedLangCode = "en"; break;
                    case 1: selectedLangCode = "hi"; break;
                    case 2: selectedLangCode = "mr"; break;
                }
                populateQuestionBoxWithTranslatedQuestions(selectedLangCode);
                displayTranslatedAnswer();
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FAQActivity.this,MainPageActivity.class);
                startActivity(intent);
            }
        });

        questionAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            displayTranslatedAnswer();
        });
    }

    private void populateQuestionBoxWithTranslatedQuestions(String langCode) {
        List<String> questions = new ArrayList<>();
        translatedQuestionsMap.clear();

        if (langCode.equals("en")) {
            for (FAQItem faq : faqList) {
                questions.add(faq.question);
                translatedQuestionsMap.put(faq.question, faq.question); // map same
            }
            updateAutoComplete(questions);
        } else {
            translateAllQuestions(langCode, translated -> {
                updateAutoComplete(translated);
            });
        }
    }

    private void updateAutoComplete(List<String> questions) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, questions);
        questionAutoComplete.setAdapter(adapter);
    }

    private void translateAllQuestions(String langCode, OnTranslationCompleteListener listener) {
        List<String> originalQuestions = new ArrayList<>();
        for (FAQItem faq : faqList) {
            originalQuestions.add(faq.question);
        }

        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(langCode)
                .build();

        final Translator translator = Translation.getClient(options);
        translator.downloadModelIfNeeded()
                .addOnSuccessListener(unused -> {
                    List<String> translatedList = new ArrayList<>();
                    // Recursive translation to avoid race conditions
                    translateOneByOne(originalQuestions, translatedList, translator, 0, () -> {
                        listener.onComplete(translatedList);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Model download failed.", Toast.LENGTH_SHORT).show();
                });
    }

    private void translateOneByOne(List<String> original, List<String> translated,
                                   Translator translator, int index, Runnable onComplete) {
        if (index >= original.size()) {
            onComplete.run();
            return;
        }

        String question = original.get(index);
        translator.translate(question)
                .addOnSuccessListener(translatedText -> {
                    translated.add(translatedText);
                    translatedQuestionsMap.put(translatedText, question); // Map translated to original
                    translateOneByOne(original, translated, translator, index + 1, onComplete);
                })
                .addOnFailureListener(e -> {
                    translated.add(question); // Fallback to English
                    translatedQuestionsMap.put(question, question);
                    translateOneByOne(original, translated, translator, index + 1, onComplete);
                });
    }

    private void displayTranslatedAnswer() {
        String selectedQuestion = questionAutoComplete.getText().toString();
        if (selectedQuestion == null || selectedQuestion.isEmpty()) return;

        // Get the original English question
        String originalQuestion = translatedQuestionsMap.getOrDefault(selectedQuestion, selectedQuestion);

        FAQItem selectedFAQ = null;
        for (FAQItem faq : faqList) {
            if (faq.question.equals(originalQuestion)) {
                selectedFAQ = faq;
                break;
            }
        }

        if (selectedFAQ == null) return;

        if (selectedLangCode.equals("en")) {
            answerText.setText(selectedFAQ.answer);
        } else {
            translateText(selectedFAQ.answer, selectedLangCode);
        }
    }

    private void translateText(String text, String targetLang) {
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(targetLang)
                .build();
        final Translator translator = Translation.getClient(options);

        translator.downloadModelIfNeeded()
                .addOnSuccessListener(unused ->
                        translator.translate(text)
                                .addOnSuccessListener(translated -> answerText.setText(translated))
                                .addOnFailureListener(e -> answerText.setText("Translation failed."))
                )
                .addOnFailureListener(e -> answerText.setText("Model download failed."));
    }

    interface OnTranslationCompleteListener {
        void onComplete(List<String> translatedQuestions);
    }
}
