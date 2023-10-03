package com.example.project5translate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import android.widget.Button
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification


/**
 * Set up translation items
 */
var sourceLanguage = TranslateLanguage.ENGLISH
var targetLanguage = TranslateLanguage.GERMAN
val conditions = DownloadConditions.Builder().requireWifi().build()
val languageIdentifier = LanguageIdentification.getClient()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /**
         * set up text boxes
         */
        val write: TextView =findViewById(R.id.viewText)
        val editText: EditText = findViewById(R.id.writeText)


        /**
         * English source language
         */
        val clickEnglishStart: Button =findViewById(R.id.englishStart)
        clickEnglishStart.setOnClickListener {
            sourceLanguage = TranslateLanguage.ENGLISH
            translate(write,  editText)
        }

        /**
         * Spanish source language
         */
        val clickSpanishStart: Button =findViewById(R.id.spanishStart)
        clickSpanishStart.setOnClickListener {
            sourceLanguage = TranslateLanguage.SPANISH
            translate(write,  editText)
        }

        /**
         * German source language
         */
        val clickGermanStart: Button =findViewById(R.id.germanStart)
        clickGermanStart.setOnClickListener {
            sourceLanguage = TranslateLanguage.GERMAN
            translate(write,  editText)
        }

        /**
         * Detect source language
         */
        val clickDetectStart: Button =findViewById(R.id.detectStart)
        clickDetectStart.setOnClickListener {
            sourceLanguage = "und"
            translate(write, editText)
        }

        /**
         * English target language
         */
        val clickEnglishEnd: Button =findViewById(R.id.englishEnd)
        clickEnglishEnd.setOnClickListener {
            targetLanguage = TranslateLanguage.ENGLISH
            translate(write,  editText)
        }

        /**
         * Spanish target language
         */
        val clickSpanishEnd: Button =findViewById(R.id.spanishEnd)
        clickSpanishEnd.setOnClickListener {
            targetLanguage = TranslateLanguage.SPANISH
            translate(write,  editText)
        }

        /**
         * German target language
         */
        val clickGermanEnd: Button =findViewById(R.id.germanEnd)
        clickGermanEnd.setOnClickListener {
            targetLanguage = TranslateLanguage.GERMAN
            translate(write,  editText)
        }


        /**
         * Update text when editText is changed
         */
        editText.doAfterTextChanged {
            translate(write,  editText)
        }
    }


    /**
     * Translate the text
     */
    fun translate(write: TextView, editText: EditText) {
        val sourceText = editText.text.toString()

        /**
         * Identify target language if on Detect Language
         */
        if (sourceLanguage == "und") {
            languageIdentifier.identifyLanguage(sourceText)
                .addOnSuccessListener { languageCode ->
                    // Language identified successfully
                    sourceLanguage = languageCode
                }
                .addOnFailureListener { e ->
                    write.text = "Failed to detect language..."
                }
        }

        /**
         * Set up source and target language options
         */

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(targetLanguage)
            .build()

        /**
         * Download model if needed, then try to translate the text
         */
        val translator = Translation.getClient(options)
        write.text = "Updating... Please wait."
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                translator.translate(sourceText)
                    .addOnSuccessListener { translatedText ->
                        write.text = translatedText
                    }
                    .addOnFailureListener { exception ->
                        write.text = "Translation error..."
                    }
            }
            .addOnFailureListener { exception ->
                write.text = "Could not detect language..."
            }
    }

}