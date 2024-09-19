package dev.trindadedev.tinjector

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.graphics.PixelFormat

import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.widget.CompoundButtonCompat

import dev.trindadedev.tinjector.databinding.ActivityMainBinding
import dev.trindadedev.tinjector.databinding.FloatingBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = checkNotNull(_binding) { "Activity has been destroyed" }

    private lateinit var windowManager: WindowManager
    private lateinit var layoutParams: WindowManager.LayoutParams
    private lateinit var displayView: View
    private var flag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initial()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000) {
            // Lógica após permissão
        }
    }

    private fun initial() {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        layoutParams = WindowManager.LayoutParams()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        }

        layoutParams.format = PixelFormat.RGBA_8888
        layoutParams.gravity = Gravity.LEFT or Gravity.TOP
        layoutParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        layoutParams.width = 850
        layoutParams.height = 1000
        layoutParams.x = 0
        layoutParams.y = 0
        
        
        binding.buttonShowMenu.setOnClickListener {
             /*  TO-DO: Logic to check if permissions are granted before open*/
             showFloatingWindow()
        }
    }

    private fun showFloatingWindow() {
        val inflater = LayoutInflater.from(this)
        val fBinding = FloatingBinding.inflate(inflater)
        displayView = fBinding.root
        displayView.setOnTouchListener(FloatingOnTouchListener())

        fBinding.option1Switch.setOnCheckedChangeListener { _, isChecked ->
            val path = "/storage/emulated/0/Android/data/com.dts.freefireth/files/contentcache/Compulsory/android/gameassetbundles/avatar/"
            if (isChecked) {
                // todo: a logic when user enable option
            } else {
                // todo: a logic when user disable option
            }
        }

        fBinding.buttonClose.setOnClickListener {
            closeFloatingWindow()
        }

        fBinding.modmenuIcon.setOnClickListener {
            if (flag) {
                layoutParams.width = 850
                layoutParams.height = 1000
            } else {
                layoutParams.width = 140
                layoutParams.height = 140
            }
            flag = !flag
            closeFloatingWindow()
            showFloatingWindow()
        }

        windowManager.addView(displayView, layoutParams)
    }

    private fun closeFloatingWindow() {
        try {
            windowManager.removeView(displayView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private inner class FloatingOnTouchListener : View.OnTouchListener {
        private var x = 0
        private var y = 0

        override fun onTouch(view: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = event.rawX.toInt()
                    y = event.rawY.toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    val nowX = event.rawX.toInt()
                    val nowY = event.rawY.toInt()
                    val movedX = nowX - x
                    val movedY = nowY - y
                    x = nowX
                    y = nowY
                    layoutParams.x += movedX
                    layoutParams.y += movedY
                    windowManager.updateViewLayout(view, layoutParams)
                }
            }
            return true
        }
    }
}