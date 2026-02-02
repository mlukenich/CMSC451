
/**
 * File: BenchmarkReport.java
 * Author: Matthew Lukenich
 * Project: CMSC451 Project 1
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * BenchmarkReport class that displays the benchmark results in a GUI
 */
public class BenchmarkReport extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    /**
     * Constructor for BenchmarkReport
     */
    public BenchmarkReport() {
        setTitle("Benchmark Report");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table Setup
        String[] columnNames = {
                "Size", "Avg Critical Count", "Coeff Var Count (%)", "Avg Time (ns)", "Coeff Var Time (%)"
        };
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Button Setup
        JButton openButton = new JButton("Open Benchmark File");
        openButton.addActionListener(this::openFile);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /*
     * Opens the benchmark file
     * 
     * @param e The action event
     */
    private void openFile(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(".")); // Start in current directory
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            processFile(selectedFile);
        }
    }

    /*
     * Processes the benchmark file
     * 
     * @param file The benchmark file
     */
    private void processFile(File file) {
        tableModel.setRowCount(0); // Clear existing data
        setTitle("Benchmark Report - " + file.getName());

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty())
                    continue;

                String[] parts = line.trim().split("\\s+");

                // First part is size
                // Check if line format is valid size + 40 pairs = 81 tokens
                if (parts.length < 2)
                    continue;

                int size = Integer.parseInt(parts[0]);

                List<Long> counts = new ArrayList<>();
                List<Long> times = new ArrayList<>();

                // Remaining parts are count/time pairs
                for (int i = 1; i < parts.length; i += 2) {
                    if (i + 1 < parts.length) {
                        counts.add(Long.parseLong(parts[i]));
                        times.add(Long.parseLong(parts[i + 1]));
                    }
                }

                double avgCount = calculateMean(counts);
                double cvCount = calculateCV(counts, avgCount);

                double avgTime = calculateMean(times);
                double cvTime = calculateCV(times, avgTime);

                tableModel.addRow(new Object[] {
                        size,
                        String.format("%.2f", avgCount),
                        String.format("%.2f", cvCount),
                        String.format("%.2f", avgTime),
                        String.format("%.2f", cvTime)
                });
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "File not found: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error parsing file: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * Calculates the mean of a list of values
     * 
     * @param values The list of values
     * 
     * @return The mean of the values
     */
    private double calculateMean(List<Long> values) {
        if (values.isEmpty())
            return 0;
        double sum = 0;
        for (long v : values)
            sum += v;
        return sum / values.size();
    }

    /*
     * Calculates the coefficient of variation of a list of values
     * 
     * @param values The list of values
     * 
     * @param mean The mean of the values
     * 
     * @return The coefficient of variation of the values
     */
    private double calculateCV(List<Long> values, double mean) {
        if (values.isEmpty() || mean == 0)
            return 0;
        double sumSqDiff = 0;
        for (long v : values) {
            sumSqDiff += Math.pow(v - mean, 2);
        }
        double stdDev = Math.sqrt(sumSqDiff / (values.size() - 1));
        return (stdDev / mean) * 100;
    }

    /**
     * Main method to run the benchmark report
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BenchmarkReport report = new BenchmarkReport();
            report.setVisible(true);
        });
    }
}
