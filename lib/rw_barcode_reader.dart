import 'dart:async';

import 'package:flutter/services.dart';

class RwBarcodeReader {
  static MethodChannel _channel =
      MethodChannel('com.rockwellits.rw_plugins/rw_barcode_reader');

  /// Scans barcode.
  ///
  /// Scans barcode using built-in applet and [returns] its contents.
  static Future<String> scanBarcode() async {
    return await _channel.invokeMethod('scanBarcode');
  }
}
