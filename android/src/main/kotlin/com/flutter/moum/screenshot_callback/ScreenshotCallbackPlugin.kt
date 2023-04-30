package com.flutter.moum.screenshot_callback

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding

class ScreenshotCallbackPlugin : FlutterPlugin {
    private var handler: MethodCallHandlerImpl? = null

    override fun onAttachedToEngine(binding: FlutterPluginBinding) {
        handler = MethodCallHandlerImpl(binding.applicationContext)
        handler?.startListening(binding.binaryMessenger)
    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
        handler?.stopListening()
        handler = null
    }
}
