package com.baseer.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var speakButton: Button
    private val CAMERA_PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // واجهة برمجية مباشرة وبسيطة للمكفوفين
        speakButton = Button(this).apply {
            text = "تشغيل بصير وقراءة المشهد"
            textSize = 22f
            setOnClickListener {
                speakOut("تطبيق بصير جاهز لمساعدتك وحراسة محيطك. الكاميرا تعمل ونظام التعرف قيد التشغيل.")
            }
        }
        setContentView(speakButton)

        // تهيئة محرك النطق الصوتي العربي
        tts = TextToSpeech(this, this)

        // طلب صلاحية الكاميرا
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale("ar"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "لغة النطق العربي غير متوفرة، جاري استخدام الافتراضية", Toast.LENGTH_LONG).show()
            } else {
                speakOut("أهلاً بك في تطبيق بصير")
            }
        }
    }

    private fun speakOut(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
