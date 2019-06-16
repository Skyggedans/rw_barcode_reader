import 'package:flutter/material.dart';
import 'package:rw_barcode_reader/rw_barcode_reader.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _barcodeContents;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('RealWear HMT-1 barcode reader example app'),
        ),
        body: Builder(builder: (BuildContext context) {
          if (_barcodeContents != null) {
            WidgetsBinding.instance.addPostFrameCallback((_) {
              showDialog(
                context: context,
                builder: (BuildContext context) {
                  return AlertDialog(
                    title: const Text('Bar Code Contents'),
                    content: Text(_barcodeContents),
                    actions: <Widget>[
                      new FlatButton(
                        child: const Text('OK'),
                        onPressed: () {
                          Navigator.of(context).pop();
                        },
                      ),
                    ],
                  );
                },
              );
            });
          }

          return Center(
            child: RaisedButton(
              child: const Text('Scan Bar Code'),
              onPressed: () async {
                final contents = await RwBarcodeReader.scanBarcode();

                setState(() {
                  _barcodeContents = contents;
                });
              },
            ),
          );
        }),
      ),
    );
  }
}
