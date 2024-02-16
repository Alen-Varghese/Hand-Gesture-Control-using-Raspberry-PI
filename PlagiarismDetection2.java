import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class PlagiarismDetection2 {

    // Function to extract text from a PDF file
    public static String extractTextFromPDF(String pdfFile) throws IOException {
        PDDocument document = Loader.loadPDF(new File(pdfFile));
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        document.close();
        return text;
    }

    // Function to calculate similarity between two text documents
    public static double calculateSimilarity(String text1, String text2) {
        String[] words1 = text1.toLowerCase().split("\\W+");
        String[] words2 = text2.toLowerCase().split("\\W+");

        int commonWords = 0;
        int totalWords = 0;

        for (String word1 : words1) {
            for (String word2 : words2) {
                if (word1.equals(word2)) {
                    commonWords++;
                    break;
                }
            }
            totalWords++;
        }

        return (double) commonWords / totalWords;
    }

    // Function to create a report in a Word document
    public static void createReport(String text1, String text2, double similarity) throws IOException {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph heading = document.createParagraph();
            heading.setSpacingAfter(200);
            heading.createRun().setText("Handwriting Plagiarism Detection Report");

            XWPFParagraph pdf1Heading = document.createParagraph();
            pdf1Heading.createRun().setText("PDF 1");
            XWPFParagraph pdf1Content = document.createParagraph();
            pdf1Content.createRun().setText(text1);

            XWPFParagraph pdf2Heading = document.createParagraph();
            pdf2Heading.createRun().setText("PDF 2");
            XWPFParagraph pdf2Content = document.createParagraph();
            pdf2Content.createRun().setText(text2);

            XWPFParagraph plagiarismRatioHeading = document.createParagraph();
            plagiarismRatioHeading.createRun().setText("Plagiarism Ratio");
            XWPFParagraph plagiarismRatioContent = document.createParagraph();
            plagiarismRatioContent.createRun().setText("The plagiarism ratio between the two PDFs is " + (similarity * 100) + "%");

            FileOutputStream out = new FileOutputStream("plagiarism_report.docx");
            document.write(out);
            out.close();
        }
    }

    public static void main(String[] args) {
        try {
            try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
                System.out.print("Enter the first PDF file name: ");
                String pdfFile1 = scanner.nextLine();

                System.out.print("Enter the second PDF file name: ");
                String pdfFile2 = scanner.nextLine();

                String text1 = extractTextFromPDF(pdfFile1);
                String text2 = extractTextFromPDF(pdfFile2);

                double similarity = calculateSimilarity(text1, text2);

                createReport(text1, text2, similarity);
            }

            System.out.println("Plagiarism report has been generated as \"plagiarism_report.docx\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
