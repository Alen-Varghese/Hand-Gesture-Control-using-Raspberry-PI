import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PlagiarismDetection{
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Plagiarism Detection");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel pdf1Label = new JLabel("Insert PDF 1:");
        pdf1Label.setBounds(10, 20, 80, 25);
        panel.add(pdf1Label);

        JTextField pdf1TextField = new JTextField(20);
        pdf1TextField.setBounds(100, 20, 200, 25);
        panel.add(pdf1TextField);

        JButton pdf1Button = new JButton("Browse");
        pdf1Button.setBounds(310, 20, 80, 25);
        panel.add(pdf1Button);

        JLabel pdf2Label = new JLabel("Insert PDF 2:");
        pdf2Label.setBounds(10, 50, 80, 25);
        panel.add(pdf2Label);

        JTextField pdf2TextField = new JTextField(20);
        pdf2TextField.setBounds(100, 50, 200, 25);
        panel.add(pdf2TextField);

        JButton pdf2Button = new JButton("Browse");
        pdf2Button.setBounds(310, 50, 80, 25);
        panel.add(pdf2Button);

        JButton goButton = new JButton("Go");
        goButton.setBounds(10, 80, 80, 25);
        panel.add(goButton);

        goButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String pdf1Path = pdf1TextField.getText();
                String pdf2Path = pdf2TextField.getText();

                try {
                    String pdf1Text = extractText(pdf1Path);
                    String pdf2Text = extractText(pdf2Path);

                    String[] pdf1Words = pdf1Text.split("\\s+");
                    String[] pdf2Words = pdf2Text.split("\\s+");

                    int sameWords = 0;
                    for (String word : pdf1Words) {
                        if (isContained(word, pdf2Words)) {
                            sameWords++;
                        }
                    }

                    double accuracy = (double) sameWords / pdf1Words.length * 100;

                    JOptionPane.showMessageDialog(null, "The accuracy of both the PDFs is: " + accuracy + "%");

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        pdf1Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Specify a PDF 1 file");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int userSelection = fileChooser.showOpenDialog(null);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    pdf1TextField.setText(file.getAbsolutePath());
                }
            }
        });

        pdf2Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Specify a PDF 2 file");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int userSelection = fileChooser.showOpenDialog(null);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    pdf2TextField.setText(file.getAbsolutePath());
                }
            }
        });
    }

    private static String extractText(String path) throws IOException {
        PDDocument document = PDDocument.load(new File(path));
        PDFTextStripper textStripper = new PDFTextStripper();
        String text = textStripper.getText(document);
        document.close();
        return text;
    }

    private static boolean isContained(String word, String[] words) {
        for (String w : words) {
            if (w.equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }
}