import 'dart:async';

import 'package:flutter/services.dart';

class ScreenshotCallback {
  static const MethodChannel _channel =
      const MethodChannel('flutter.moum/screenshot_callback');

  static final instance = ScreenshotCallback._();

  /// Functions to execute when callback fired.
  List<VoidCallback> onCallbacks = <VoidCallback>[];

  ScreenshotCallback._() {
    _channel.setMethodCallHandler(_handleMethod);
  }

  /// Initializes screenshot callback plugin.
  Future<void> initialize() => _channel.invokeMethod('initialize');

  /// Add a callback.
  void addListener(VoidCallback callback) {
    onCallbacks.add(callback);
  }

  /// Remove a previously registered callback.
  void removeListener(VoidCallback callback) {
    onCallbacks.remove(callback);
  }

  Future<dynamic> _handleMethod(MethodCall call) async {
    switch (call.method) {
      case 'onCallback':
        for (final callback in onCallbacks) {
          callback();
        }
        break;
      default:
        throw ('method not defined');
    }
  }

  /// Remove callback listener.
  Future<void> dispose() async {
    onCallbacks.clear();
    await _channel.invokeMethod('dispose');
  }
}
