package com.rockwellits.rw_plugins.rw_barcode_reader

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar


class BarcodeReaderPluginActivity : Activity() {
    private val BARCODE_REQUEST = 1984
    private val ACTION_BARCODE = "com.realwear.barcodereader.intent.action.SCAN_BARCODE"
    private val EXTRA_RESULT = "com.realwear.barcodereader.intent.extra.RESULT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(ACTION_BARCODE)

        startActivityForResult(intent, BARCODE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (resultCode == RESULT_OK && requestCode == BARCODE_REQUEST) {
            var result: String? = null

            if (intent != null) {
                result = intent.getStringExtra(EXTRA_RESULT)
            }

            RwBarcodeReaderPlugin.onBarcodeResult(result)
        } else {
            RwBarcodeReaderPlugin.onBarcodeResult(null)
        }

        finish()
    }
}

class RwBarcodeReaderPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    lateinit var activity: Activity

    companion object {
        private const val CHANNEL = "com.rockwellits.rw_plugins/rw_barcode_reader"
        private lateinit var methodResult: Result

        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), CHANNEL)
            val plugin = RwBarcodeReaderPlugin()

            plugin.activity = registrar.activity()
            channel.setMethodCallHandler(plugin)
        }

        @JvmStatic
        fun onBarcodeResult(content: String?) {
            methodResult.success(content)
        }
    }

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        val channel = MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), CHANNEL)

        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "scanBarcode" -> {
                val intent = Intent(activity, BarcodeReaderPluginActivity::class.java)

                methodResult = result
                activity.startActivity(intent)
            }
            else ->
                result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        this.activity = binding.activity
    }

    override fun onDetachedFromActivity() {
    }

    override fun onDetachedFromActivityForConfigChanges() {
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    }
}
