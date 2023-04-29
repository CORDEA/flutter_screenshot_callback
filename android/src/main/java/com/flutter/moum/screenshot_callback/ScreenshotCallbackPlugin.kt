package com.flutter.moum.screenshot_callback

import android.content.Context
import android.os.Handler
import android.os.Looper
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

class ScreenshotCallbackPlugin : MethodCallHandler, FlutterPlugin {
    private var channel: MethodChannel? = null
    private var context: Context? = null
    private var detector: ScreenshotDetector? = null
    private var lastScreenshotName: String? = null

    override fun onAttachedToEngine(binding: FlutterPluginBinding) {
        onAttachedToEngine(binding.applicationContext, binding.binaryMessenger)
    }

    private fun onAttachedToEngine(applicationContext: Context, messenger: BinaryMessenger) {
        this.context = applicationContext
        channel = MethodChannel(messenger, "flutter.moum/screenshot_callback").also {
            it.setMethodCallHandler(this)
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
        context = null
        channel?.setMethodCallHandler(null)
        channel = null
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "initialize" -> {
                context?.let {
                    val handler = Handler(Looper.getMainLooper())
                    detector = ScreenshotDetector(it) { screenshotName ->
                        if (screenshotName != lastScreenshotName) {
                            lastScreenshotName = screenshotName
                            handler.post {
                                channel!!.invokeMethod("onCallback", null)
                            }
                        }
                    }.apply { start() }
                    result.success("initialize")
                }
            }

            "dispose" -> {
                detector?.stop()
                detector = null
                lastScreenshotName = null
                result.success("dispose")
            }

            else -> {
                result.notImplemented()
            }
        }
    }
}
