import 'package:flutter/material.dart';
import 'package:screenshot_callback/screenshot_callback.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String text = "Ready..";

  @override
  void initState() {
    super.initState();

    init();
  }

  void init() async {
    await initScreenshotCallback();
  }

  //It must be created after permission is granted.
  Future<void> initScreenshotCallback() async {
    await ScreenshotCallback.instance.initialize();
    ScreenshotCallback.instance.addListener(() {
      setState(() {
        text = "Screenshot callback Fired!";
      });
    });

    ScreenshotCallback.instance.addListener(() {
      print("We can add multiple listeners ");
    });
  }

  @override
  void dispose() {
    ScreenshotCallback.instance.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Detect Screenshot Callback Example'),
        ),
        body: Center(
          child: Text(text,
              style: TextStyle(
                fontWeight: FontWeight.bold,
              )),
        ),
      ),
    );
  }
}
