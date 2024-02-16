import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PlagiarismDetectionApp {
    private JButton selectPdfButton1;
    private JButton selectPdfButton2;
    private JButton detectPlagiarismButton;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JTextArea resultTextArea;
    private File selectedFile1;
    private File selectedFile2;

    public PlagiarismDetectionApp() {

        textArea1 = new JTextArea();
        textArea2 = new JTextArea();
        resultTextArea = new JTextArea();

        selectPdfButton1 = new JButton("Select PDF 1");
        selectPdfButton2 = new JButton("Select PDF 2");
        detectPlagiarismButton = new JButton("Detect Plagiarism");

        textArea1 = new JTextArea(10, 40);
        textArea2 = new JTextArea(10, 40);
        resultTextArea = new JTextArea(10, 40);
        resultTextArea.setEditable(false);

        JFrame frame = new JFrame("Plagiarism Detection App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(selectPdfButton1, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(selectPdfButton2, constraints);

        constraints.gridy = 1;
        constraints.gridwidth = 2;
        panel.add(detectPlagiarismButton, constraints);

        constraints.gridy = 2;
        constraints.gridheight = 2;
        panel.add(new JScrollPane(textArea1), constraints);

        constraints.gridx = 2;
        constraints.gridy = 2;
        panel.add(new JScrollPane(textArea2), constraints);

        constraints.gridx = 3;
        constraints.gridy = 2;
        constraints.gridheight = 2;
        panel.add(new JScrollPane(resultTextArea), constraints);

        frame.add(panel);
        frame.setVisible(true);

        selectPdfButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select PDF 1");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setMultiSelectionEnabled(false);
                int userAction = fileChooser.showOpenDialog(frame);
                if (userAction == JFileChooser.APPROVE_OPTION) {
                    selectedFile1 = fileChooser.getSelectedFile();
                    try {
                        String text = extractTextFromPDF(selectedFile1);
                        textArea1.setText(text);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        selectPdfButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select PDF 2");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setMultiSelectionEnabled(false);
                int userAction = fileChooser.showOpenDialog(frame);
                if (userAction == JFileChooser.APPROVE_OPTION) {
                    selectedFile2 = fileChooser.getSelectedFile();
                    try {
                        String text = extractTextFromPDF(selectedFile2);
                        textArea2.setText(text);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        detectPlagiarismButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedFile1 != null && selectedFile2 != null) {
                    String text1 = textArea1.getText();
                    String text2 = textArea2.getText();
                    double similarity = calculateSimilarity(text1, text2);
                    resultTextArea.setText("Similarity: " + similarity + "%");
                }
            }
        });
    }

    private String extractTextFromPDF(File file) throws IOException {
        PDDocument document = Loader.loadPDF(file);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        document.close();
        return text;
    }

    private double calculateSimilarity(String text1, String text2) {
        int distance = calculateEditDistance(text1, text2);
        double similarity = (1.0 - ((double) distance / Math.max(text1.length(), text2.length()))) * 100;
        return similarity;
    }

    private int calculateEditDistance(String text1, String text2) {
        int[][] dp = new int[text1.length() + 1][text2.length() + 1];
        for (int i = 0; i <= text1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= text2.length(); j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= text1.length(); i++) {
            for (int j = 1; j <= text2.length(); j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i][j - 1], dp[i - 1][j])) + 1;
                }
            }
        }
        return dp[text1.length()][text2.length()];
    }
}
