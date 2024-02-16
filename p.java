import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PlagiarismDetection {
    private static String file1;
    private static String file2;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PlagiarismDetection().createUI();
            }
        });
    }

    public void createUI() {
        JFrame frame = new JFrame("Plagiarism Detection");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Enter File 1:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField userInput = new JTextField();
        userInput.setBounds(100, 20, 165, 25);
        panel.add(userInput);

        JButton userButton = new JButton("Browse");
        userButton.setBounds(275, 20, 80, 25);
        userButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.showOpenDialog(null);
                userInput.setText(chooser.getSelectedFile().getAbsolutePath());
                file1 = userInput.getText();
            }
        });
        panel.add(userButton);

        JLabel userLabel2 = new JLabel("Enter File 2:");
        userLabel2.setBounds(10, 60, 80, 25);
        panel.add(userLabel2);

        JTextField userInput2 = new JTextField();
        userInput2.setBounds(100, 60, 165, 25);
        panel.add(userInput2);

        JButton userButton2 = new JButton("Browse");
        userButton2.setBounds(275, 60, 80, 25);
        userButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.showOpenDialog(null);
                userInput2.setText(chooser.getSelectedFile().getAbsolutePath());
                file2 = userInput2.getText();
            }
        });
        panel.add(userButton2);

        JButton goButton = new JButton("Compare");
        goButton.setBounds(100, 100, 80, 25);
        goButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (file1 != null && file2 != null) {
                    try {
                        compareFiles(file1, file2);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        panel.add(goButton);
    }

    private void compareFiles(String file1, String file2) throws IOException {
        String text1 = extractText(file1);
        String text2 = extractText(file2);

        int similarities = countSimilarities(text1, text2);

        JFrame resultFrame = new JFrame("Comparison Result");
        resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        resultFrame.setSize(800, 600);

        JPanel resultPanel = new JPanel();
        resultFrame.add(resultPanel);

        JLabel resultLabel = new JLabel("Similarities found: " + similarities);
        resultLabel.setBounds(350, 275, 300, 25);
        resultPanel.add(resultLabel);

        resultFrame.setVisible(true);
    }

    private String extractText(String file) throws IOException {
        PDDocument document = PDDocument.load(file);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        document.close();
        return text;
    }

    private int countSimilarities(String text1, String text2) {
        String[] words1 = text1.split(" ");
        String[] words2 = text2.split(" ");

        int similarities = 0;
        for (String word1 : words1) {
            for (String word2 : words2) {
                if (word1.equalsIgnoreCase(word2)) {
                    similarities++;
                }
            }
        }
        return similarities;
    }
}
