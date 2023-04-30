package com.flutter.moum.screenshot_callback

import android.content.Context
import android.os.Handler
import android.os.Looper
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

class MethodCallHandlerImpl(private val context: Context) : MethodCallHandler {
    private val handler = Handler(Looper.getMainLooper())

    private var channel: MethodChannel? = null
    private var detector: ScreenshotDetector? = null
    private var lastScreenshotName: String = ""

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "initialize" -> {
                detector = ScreenshotDetector(context) { screenshotName ->
                    if (screenshotName != lastScreenshotName) {
                        lastScreenshotName = screenshotName
                        handler.post {
                            channel?.invokeMethod("onCallback", null)
                        }
                    }
                }.apply { start() }
                result.success("initialize")
            }

            "dispose" -> {
                detector?.stop()
                detector = null
                lastScreenshotName = ""
                result.success("dispose")
            }

            else -> {
                result.notImplemented()
            }
        }
    }

    fun startListening(messenger: BinaryMessenger) {
        channel = MethodChannel(messenger, "flutter.moum/screenshot_callback").also {
            it.setMethodCallHandler(this)
        }
    }

    fun stopListening() {
        channel?.setMethodCallHandler(null)
        channel = null
    }
}
