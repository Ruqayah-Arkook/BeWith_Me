package com.app.cardfeature7;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public class PDFGenerator {
//    static void createPDF(List<Map<String,String>> data, String reportName, Context context) {
//        Document document = new Document();
//        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + reportName;
//        int reportNumber = 1;
//        String finalFilePath = filePath + ".pdf";
//        while (new File(finalFilePath).exists()) {
//            finalFilePath = filePath + "_" + reportNumber + ".pdf";
//            reportNumber++;
//        }
//        try {
//            PdfWriter.getInstance(document, new FileOutputStream(finalFilePath));
//            document.open();
//            Font font = new Font();
//            font.setSize(12);
//
//            for (Map<String, String> item : data) {
//                String entry = "Date: " + item.get("date") +
//                        ", Emotion: " + item.get("emotion") +
//                        ", Time: " + item.get("time");
//                document.add(new Paragraph(entry, font));
//            }
//            document.close();
//            Toast.makeText(context, "PDF generated and saved to Download directory.", Toast.LENGTH_SHORT).show();
//        } catch (DocumentException | FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    static void createPDF(List<Map<String, String>> data, String reportName, Context context) {
        Document document = new Document();
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + reportName;

        int reportNumber = 1;
        String finalFilePath = filePath + ".pdf";

        while (new File(finalFilePath).exists()) {
            finalFilePath = filePath + "_" + reportNumber + ".pdf";
            reportNumber++;
        }

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(finalFilePath));

            // Add page border
            writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));

            document.open();

            Font headingFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font entryFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            Font labelFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            // Add heading at the top center
            Paragraph heading = new Paragraph(reportName, headingFont);
            heading.setAlignment(Element.ALIGN_CENTER);
            document.add(heading);

            int entryNumber = 1;
            for (Map<String, String> item : data) {
                // Add entry number
                Paragraph entryNumberParagraph = new Paragraph("Entry " + entryNumber, entryFont);

                // Add data in the desired format
                Paragraph dataParagraph = new Paragraph();
                dataParagraph.add(new Phrase("   Emotion : ", labelFont));
                dataParagraph.add(new Phrase(item.get("emotion"), entryFont));
                dataParagraph.add(new Phrase("\n   Time : ", labelFont));
                dataParagraph.add(new Phrase(item.get("time"), entryFont));
                dataParagraph.add(new Phrase("\n   Date : ", labelFont));
                dataParagraph.add(new Phrase(item.get("date"), entryFont));
                // Add space between entries
                Paragraph space = new Paragraph("\n");

                document.add(entryNumberParagraph);
                document.add(dataParagraph);
                document.add(space);

                entryNumber++;
            }

            document.close();
            // Display a message indicating where the PDF was saved
            Toast.makeText(context, "PDF generated and saved to: " + finalFilePath, Toast.LENGTH_SHORT).show();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }



}