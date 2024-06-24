package zadanie.wzi.wzi.Parser;

import javafx.scene.control.Alert;
import zadanie.wzi.wzi.Enums.ElementTag;
import zadanie.wzi.wzi.Enums.GroupTag;
import zadanie.wzi.wzi.Model.DICOMData;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Parser {
    public DICOMData dicomData = new DICOMData();

    public void parseDICOM(File dicomFile) {
        boolean headerFound = false;
        String[] specialTags = {"OB", "OW", "OF", "SQ", "UT", "UN"};

        byte[] bytes = new byte[0];
        try {
            FileInputStream fis = new FileInputStream(dicomFile);
            bytes = fis.readAllBytes();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int byteIndex = 0;
        for (; byteIndex < bytes.length - 3; byteIndex++) {
            if (bytes[byteIndex] == 'D' &&
                    bytes[byteIndex + 1] == 'I' &&
                    bytes[byteIndex + 2] == 'C' &&
                    bytes[byteIndex + 3] == 'M') {
                headerFound = true;
                break;
            }
        }

        if (!headerFound) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd!");
            alert.setHeaderText("Nie znaleziono naglowka DICM w plikach!");
            alert.setContentText("Proszę załądować poprawne pliki DICOM!");
            alert.showAndWait();
        }

        byteIndex += 4;
        int rows;
        int columns;
        int skipBytesForSpecialTags;
        while (byteIndex < bytes.length) {
            int skipBytes = 0;

            short group = getShort(bytes, byteIndex);
            short element = getShort(bytes, byteIndex + 2);

            skipBytes += 4;

            String typeOfData = new String(new byte[]{bytes[byteIndex + 4], bytes[byteIndex + 5]});
            skipBytes += 2;

            if (isInArray(typeOfData, specialTags)) {
                skipBytesForSpecialTags = getInt(bytes, byteIndex + 8);
                skipBytes += 4;
            } else {
                skipBytesForSpecialTags = getShort(bytes, byteIndex + 6);
            }

            skipBytes += 2 + skipBytesForSpecialTags;
            var index = byteIndex + skipBytes - skipBytesForSpecialTags;
            var end = byteIndex + skipBytes;

            if (group == GroupTag.TAG_0028.getValue() && element == ElementTag.TAG_0010.getValue()) {
                rows = getShort(bytes, index);
                dicomData.setRows(rows);
            } else if (group == GroupTag.TAG_0028.getValue() && element == ElementTag.TAG_0011.getValue()) {
                columns = getShort(bytes, index);
                dicomData.setColumns(columns);
            } else if (group == GroupTag.TAG_0028.getValue() && element == ElementTag.TAG_0101.getValue()) {
                int bitsStored = getShort(bytes, index);
                dicomData.setBitsStored(bitsStored);
            } else if (group == GroupTag.TAG_0028.getValue() && element == ElementTag.TAG_0100.getValue()) {
                int bitsAllocated = getShort(bytes, index);
                dicomData.setBitsAllocated(bitsAllocated);
            } else if (group == GroupTag.TAG_0028.getValue() && element == ElementTag.TAG_1053.getValue()) {
                String buffer = readPattern(index, end, bytes);
                dicomData.setRescaleSlope(Float.parseFloat(buffer));
            } else if (group == GroupTag.TAG_0028.getValue() && element == ElementTag.TAG_1052.getValue()) {
                String buffer = readPattern(index, end, bytes);
                dicomData.setRescaleIntercept(Float.parseFloat(buffer));
            } else if (group == GroupTag.TAG_0028.getValue() && element == ElementTag.TAG_0030.getValue()) {
                String buffer = readPattern(index, end,bytes);
                String[] parts = buffer.split("\\\\");
                float[] pixelSpacing = {Float.parseFloat(parts[0]), Float.parseFloat(parts[1])};
                dicomData.setPixelSpacing(pixelSpacing);
            } else if (group == GroupTag.TAG_0018.getValue() && element == ElementTag.TAG_0050.getValue()) {
                String buffer = readPattern(index, end,bytes);
                dicomData.setSliceThickness(Float.parseFloat(buffer));
            } else if (group == GroupTag.TAG_0020.getValue() && element == ElementTag.TAG_0032.getValue()) {
                String buffer = readPattern(index, end,bytes);
                String[] parts = buffer.split("\\\\");
                float[] imagePosition = {Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2])};
                dicomData.setImagePosition(imagePosition);
            } else if (group == GroupTag.TAG_0028.getValue() && element == ElementTag.TAG_1050.getValue()) {
                String buffer = readPattern(index, end,bytes);
                dicomData.setWindowCenter(Float.parseFloat(buffer));
            } else if (group == GroupTag.TAG_0028.getValue() && element == ElementTag.TAG_1051.getValue()) {
                String buffer = readPattern(index, end,bytes);
                dicomData.setWindowWidth(Float.parseFloat(buffer));
            } else if (group == GroupTag.TAG_0020.getValue() && element == ElementTag.TAG_1041.getValue()) {
                float sliceLocation = ByteBuffer.wrap(bytes, index, skipBytesForSpecialTags).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                dicomData.setSliceLocation(sliceLocation);
            } else if (group == GroupTag.TAG_0002.getValue() && element == ElementTag.TAG_0010.getValue()) {
                String buffer = readPattern(index, end, bytes);
                if (buffer.contains("1.2.840.10008.1.2.1")) {
                    dicomData.setEndian("Little Endian");
                } else {
                    dicomData.setEndian("Big Endian");
                }
            }

            if (group == GroupTag.TAG_7FE0.getValue() && element == ElementTag.TAG_0010.getValue()) {
                byteIndex += (skipBytes - skipBytesForSpecialTags);
                break;
            }

            byteIndex += skipBytes;
        }

        dicomData.setPixelData(new int[dicomData.getRows()][dicomData.getColumns()]);
        int row = 0, col = 0;
        ByteBuffer pixelBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
        int pixelValue;
        for (; byteIndex < bytes.length; byteIndex++) {
            pixelBuffer.put(bytes[byteIndex]);
            if (pixelBuffer.position() == 2) {
                pixelBuffer.flip();
                pixelValue = pixelBuffer.getShort() & 0xFFFF;
                dicomData.getPixelData()[row][col] = pixelValue;
                col++;
                if (col == dicomData.getColumns()) {
                    col = 0;
                    row++;
                }
                pixelBuffer.clear();
            }
        }
    }

    private static short getShort(byte[] bytes, int index) {
        return ByteBuffer.wrap(bytes, index, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    private static int getInt(byte[] bytes, int index) {
        return ByteBuffer.wrap(bytes, index, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    private static boolean isInArray(String value, String[] array) {
        for (String item : array) {
            if (item.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public String readPattern(int start, int end, byte[]bytes) {
        StringBuilder buffer = new StringBuilder();
        for (int i = start; i < end; i++) {
            buffer.append((char) bytes[i]);
        }
        return buffer.toString();
    }
}
