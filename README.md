# pdf417-barcode-reader

[![Main](https://github.com/tkonsta/pdf417-barcode-reader/workflows/Nightly/badge.svg)](https://github.com/tkonsta/pdf417-barcode-reader/actions?query=workflow%3ANightly)

A small webapp which reads PDF417 barcodes from a pdf file and outputs the barcode contents as a string

The webapp uses the ZXing ("Zebra Crossing") barcode scanning library (see https://github.com/zxing/zxing) to read the barcodes and  Apache pdfbox to work with PDF documents (see https://pdfbox.apache.org/).

The web aplication can be run standalone and started with the following command:
 
    java -jar pdf417-reader-1.1.3.jar

The released jar files can be found under releases in Github.
  
The webapp can also be run as a Docker container.  The latest build as a Docker container can be obtained on docker hub: https://hub.docker.com/r/tkonsta/pdf417-reader-webapp 
