# pdf417-barcode-reader
A small webapp which reads PDF417 barcodes from a pdf file and outputs the barcode contents as a string

The webapp uses the ZXing ("Zebra Crossing") barcode scanning library (see https://github.com/zxing/zxing) to read the barcodes and  Apache pdfbox to work with PDF documents (see https://pdfbox.apache.org/).

It can be run either standalone as jar (with java -jar) or as a Docker container.
