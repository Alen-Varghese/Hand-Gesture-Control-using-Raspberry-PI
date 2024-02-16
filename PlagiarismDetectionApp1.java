import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class PlagiarismDetectionApp1 {
    private JFrame frame;
    private JButton selectPdfButton1;
    private JButton selectPdfButton2;
    private JButton detectPlagiarismButton;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JTextArea resultTextArea;

    private File selectedFile1;
    private File selectedFile2;

    public PlagiarismDetectionApp1() {
        frame = new JFrame("Plagiarism Detection App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        selectPdfButton1 = new JButton("Select PDF 1");
        selectPdfButton2 = new JButton("Select PDF 2");
        detectPlagiarismButton = new JButton("Detect Plagiarism");

        textArea1 = new JTextArea(10, 40);
        textArea2 = new JTextArea(10, 40);
        resultTextArea = new JTextArea(10, 40);
        resultTextArea.setEditable(false);

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
        constraints.gridx = 0;
        panel.add(textArea1, constraints);

        constraints.gridy = 2;
        panel.add(textArea2, constraints);

        constraints.gridy = 3;
        panel.add(detectPlagiarismButton, constraints);

        constraints.gridy = 4;
        panel.add(resultTextArea, constraints);

        frame.add(panel);
        frame.setVisible(true);

        selectPdfButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile1 = fileChooser.getSelectedFile();
                }
            }
        });

        selectPdfButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile2 = fileChooser.getSelectedFile();
                }
            }
        });

        detectPlagiarismButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedFile1 != null && selectedFile2 != null) {
                    try {
                        String text1 = extractTextFromPDF(selectedFile1);
                        String text2 = extractTextFromPDF(selectedFile2);
        
                        // Implement plagiarism detection algorithm here
                        // Compare text1 and text2, and display results in resultTextArea
                        Set<String> words1 = new HashSet<>(Arrays.asList(text1.split("\\s+")));
                        Set<String> words2 = new HashSet<>(Arrays.asList(text2.split("\\s+")));
        
                        Set<String> intersection = new HashSet<>(words1);
                        intersection.retainAll(words2);
        
                        Set<String> union = new HashSet<>(words1);
                        union.addAll(words2);
        
                        double jaccardSimilarity = (double) intersection.size() / union.size();
                        String result = "Jaccard similarity index: " + jaccardSimilarity;
                        resultTextArea.setText(result);
        
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private String extractTextFromPDF(File file) throws IOException {
        PDDocument document = PDDocument.load(file);
        PDFTextStripper pdfStripper = new PDFTextStripper();
        return pdfStripper.getText(document);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PlagiarismDetectionApp1());
    }
}
