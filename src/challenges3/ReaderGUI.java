package challenges3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class ReaderGUI extends JFrame {
    private JTextField folderLocationField;
    private JTextField amountOfWeeksField;
    private JTextArea outputArea;

    public ReaderGUI() {
        setTitle("Reader GUI");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab 1: Original Input Form
        JPanel tab1Panel = new JPanel(new BorderLayout());

        // Panel for input fields
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));

        inputPanel.add(new JLabel("Folder Location:"));
        folderLocationField = new JTextField(30);
        inputPanel.add(folderLocationField);

        inputPanel.add(new JLabel("Amount of Weeks:"));
        amountOfWeeksField = new JTextField(5);
        inputPanel.add(amountOfWeeksField);

        JButton processButton = new JButton("Process Files");
        processButton.addActionListener(new ProcessButtonListener());
        inputPanel.add(processButton);

        // Output area
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Add input panel and output area to tab 1
        tab1Panel.add(inputPanel, BorderLayout.NORTH);
        tab1Panel.add(scrollPane, BorderLayout.CENTER);

        // Add tab 1 to the tabbed pane
        tabbedPane.addTab("Input & Output", tab1Panel);

        // Tab 2: Empty Panel (You can customize it as needed)
        JPanel generationPane = new JPanel();
        generationPane.add(new JLabel("Here will be some buttons to generate en"));
        tabbedPane.addTab("Generate new Challenge", generationPane);
        
        // Tab3: Explanation
        JPanel infoPanel = new JPanel();
        infoPanel.add(new JLabel("Here will be information later."));
        tabbedPane.addTab("Information", infoPanel);

        // Add the tabbed pane to the main frame
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    private class ProcessButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String folderLocation = folderLocationField.getText();
            folderLocation = ensureTrailingBackslash(folderLocation);
            
            int amountOfWeeks;
            try {
                amountOfWeeks = Integer.parseInt(amountOfWeeksField.getText());
            } catch (NumberFormatException ex) {
                outputArea.setText("Invalid number for weeks.");
                return;
            }
            
            // Call the reader's main logic
            processFiles(folderLocation, amountOfWeeks);
        }
    }

    private void processFiles(String folderLocation, int amountOfWeeks) {
        int week = 0;
        ArrayList<BufferedReader> br_week_list = new ArrayList<>();

        while (week <= amountOfWeeks) {
            String file_name = folderLocation + "chatW" + week + ".txt";
            File file = new File(file_name);
            if (file.exists() && isFileValid(file)) {
                try {
                    br_week_list.add(new BufferedReader(new FileReader(file)));
                } catch (FileNotFoundException e) {
                    outputArea.append("File not found: " + file_name + "\n");
                    return;
                }
            }
            week++;
        }

        try {
            new Process(getBufReader(folderLocation + "promise.txt"),
                        getBufReader(folderLocation + "done.txt"),
                        getBufReader(folderLocation + "bonus.txt"),
                        br_week_list,
                        outputArea);
        } catch (FileNotFoundException e) {
            outputArea.append("Required file not found: " + e.getMessage() + "\n");
        } catch (Exception e) {
            outputArea.append("An error occurred: " + e.getMessage() + "\n");
        }
    }

    private boolean isFileValid(File file) {
        return file.isFile() && file.canRead();
    }

    private BufferedReader getBufReader(String fileLocation) throws FileNotFoundException {
        return new BufferedReader(new FileReader(new File(fileLocation)));
    }

    private String ensureTrailingBackslash(String path) {
        if (path.endsWith("\\")) {
            return path;
        } else {
            return path + "\\";
        }
    }

    private String checkForChars(String naam2) {
        // Define special characters that need to be escaped
        String specialChars = "_*";
        
        // Use a regular expression to replace each special character with an escaped version
        for (char c : specialChars.toCharArray()) {
            naam2 = naam2.replace(Character.toString(c), "\\" + c);
        }
        
        return naam2;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ReaderGUI gui = new ReaderGUI();
            gui.setVisible(true);
        });
    }
}