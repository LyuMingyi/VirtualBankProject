package AccountType;

import javax.swing.*;
import java.io.IOException;
import java.awt.*;


/**
 * A JFrame subclass that visualizes account data over a series of years.
 * This frame displays financial data through both a chart and a text area for detailed account logs.
 */
public class AccountVisualizationFrame extends JFrame {
    private static final int YEARS_COUNT = 5;
    private static final int DATA_TYPES_COUNT = 6;
    private static final int MONTHS_COUNT = 12;
    private double[][][] data;
    private int year;
    private ChartPanel chartPanel;
    private JTextArea totalAmountsArea;
    private JPanel monthPanel;
    private String filePath;
    private String logFilePath;

    /**
     * Constructs a new frame to display financial data for a specified account.
     *
     * @param email         the email associated with the account
     * @param accountNumber the account number for the data to be visualized
     */
    public AccountVisualizationFrame(String email, String accountNumber) {
        AccountBillInfo.parseFinancialLogs(email, accountNumber);
        filePath = "./registerTable/" + email + "/AccountName&Password/" + accountNumber;
        logFilePath = "./registerTable/" + email + "/AccountFile/" + accountNumber;
        try {
            //data = AccountBillInfo.createFakeArrayFromFile();
            data = AccountBillInfo.readArrayFromFile(filePath);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load data: " + e.getMessage(), e);
        }
        setTitle("Financial Data Viewer");
        setSize(1200, 800);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new FlowLayout());
        for (int year = 2020; year <= 2024; year++) {
            JButton yearButton = new JButton(String.valueOf(year));
            int finalYear = year - 2020;
            yearButton.addActionListener(e -> displayYearData(finalYear));
            topPanel.add(yearButton);
        }
        add(topPanel, BorderLayout.NORTH);

        monthPanel = new JPanel(new FlowLayout());
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (int month = 1; month <= MONTHS_COUNT; month++) {
            JButton monthButton = new JButton(monthName[month - 1]);
            int finalMonth = month - 1;
            monthButton.addActionListener(e -> updateMonthlyTotals(year,finalMonth));  // Modified action listener
            monthPanel.add(monthButton);
        }
        add(monthPanel, BorderLayout.SOUTH);
        add(monthPanel, BorderLayout.SOUTH);

        chartPanel = new ChartPanel();
        add(chartPanel, BorderLayout.CENTER);

        totalAmountsArea = new JTextArea(5, 30);
        totalAmountsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(totalAmountsArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.EAST);

        displayYearData(0);
    }

    /**
     * Updates the display to show totals for a specific month and year.
     *
     * @param currentYearIndex the index of the year to display data for
     * @param monthIndex       the index of the month to display data for
     */
    private void updateMonthlyTotals(int currentYearIndex,int monthIndex) {
        StringBuilder sb = new StringBuilder();
        String[] dataLabels = {"Task", "Receiver", "Sender", "Deposit", "Withdraw", "SumUp"};

        // Header for the specific month
        sb.append("Month ").append(monthIndex + 1).append(" Totals:\n");

        // Append monthly totals for each data type
        for (int dataType = 0; dataType < DATA_TYPES_COUNT; dataType++) {
            double monthlyTotal = data[currentYearIndex][dataType][monthIndex];
            sb.append(dataLabels[dataType]).append(": ").append(String.format("%.2f", monthlyTotal)).append("\n");
        }

        String[] monthLog = AccountBillInfo.readLogsForMonth(currentYearIndex + 2020, monthIndex + 1, logFilePath);
        String[] modifiedLog = AccountBillInfo.logTransfer(monthLog);
        sb.append("\n");

        for (String log : modifiedLog) {
            sb.append(log);
            System.out.println(log);
        }
        totalAmountsArea.setText(sb.toString());
        chartPanel.displayMonthData(monthIndex);

    }
    /**
     * Displays data for a specific year.
     *
     * @param yearIndex the index of the year to display
     */
        private void displayYearData ( int yearIndex){
            year = yearIndex;
            chartPanel.displayYearData(yearIndex);
            updateTotalAmounts(yearIndex);
        }
    /**
     * Updates the display to show yearly totals for all data types.
     *
     * @param yearIndex the index of the year to display totals for
     */
        private void updateTotalAmounts ( int yearIndex){
            StringBuilder sb = new StringBuilder();
            sb.append("Yearly Totals for ").append(2020 + yearIndex).append(":\n");
            String[] dataLabels = {"Task", "Receiver", "Sender", "Deposit", "Withdraw", "SumUp"};
            double[] yearlyTotals = new double[DATA_TYPES_COUNT];  // Array to store the yearly totals for each data type

            // Initialize monthly totals for each data type
            double[][] monthlyTotals = new double[MONTHS_COUNT][DATA_TYPES_COUNT];

            // Calculate monthly and yearly totals
            for (int month = 0; month < MONTHS_COUNT; month++) {
                for (int dataType = 0; dataType < DATA_TYPES_COUNT; dataType++) {
                    double value = data[yearIndex][dataType][month];
                    if (dataType == 0 || dataType == 1 || dataType == 3) {
                        monthlyTotals[month][dataType] += value;
                        yearlyTotals[dataType] += value;
                    } else {
                        monthlyTotals[month][dataType] -= value;
                        yearlyTotals[dataType] -= value;
                    }
                }
            }

            // Append yearly totals to the StringBuilder
            for (int dataType = 0; dataType < DATA_TYPES_COUNT; dataType++) {
                sb.append(dataLabels[dataType]).append(" Yearly Total: ").append(String.format("%.2f", yearlyTotals[dataType])).append("\n");
            }

            // Append monthly totals for each data type
            for (int month = 0; month < MONTHS_COUNT; month++) {
                sb.append("\nMonth ").append(month + 1).append(" Totals:\n");
                for (int dataType = 0; dataType < DATA_TYPES_COUNT; dataType++) {
                    sb.append(dataLabels[dataType]).append(": ").append(String.format("%.2f", monthlyTotals[month][dataType])).append("\n");
                }
            }

            totalAmountsArea.setText(sb.toString());
        }


    /**
     * Inner class that functions as a custom JPanel for drawing charts related to financial data.
     */
    class ChartPanel extends JPanel {
            private int currentYearIndex = 0;
            private int currentMonthIndex = -1;

        /**
         * Displays data for a selected year on the chart.
         *
         * @param yearIndex the index of the year whose data should be displayed
         */
        public void displayYearData(int yearIndex) {
                this.currentYearIndex = yearIndex;
                this.currentMonthIndex = -1;  // Reset the month index when changing the year
                repaint();
            }

        /**
         * Updates the current month index and repaints the chart to display data for the selected month.
         * This method allows for dynamic updates to the chart display based on user interaction, such as
         * selecting different months to visualize data.
         *
         * @param monthIndex the index of the month whose data should be displayed, zero-based.
         */
        public void displayMonthData(int monthIndex) {
                this.currentMonthIndex = monthIndex;
                repaint();// Triggers the paintComponent to redraw the chart with new data
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Checks if data is available and the indices are within the expected range before drawing
                if (data == null || currentYearIndex < 0 || currentYearIndex >= data.length) {
                    return; // Prevents drawing if data is not set or indices are out of bounds
                }

                int panelWidth = getWidth();
                int panelHeight = getHeight();
                int margin = 80;
                int chartWidth = panelWidth - 2 * margin;
                int chartHeight = panelHeight - 2 * margin;
                double maxValue = findMaxValue();
                // Conditional drawing based on whether a specific month is selected or not
                if (currentMonthIndex == -1) {  // Draw bar chart only if no month is selected
                    drawBarChart(g, panelWidth, panelHeight, margin, chartWidth, chartHeight, maxValue);
                } else {
                    // Draw a line graph for the selected month
                    drawLineGraph(g, panelWidth, panelHeight, margin, chartWidth, chartHeight);
                }
            }

        /**
         * Calculates the maximum value from the dataset to scale the chart correctly.
         * This method scans all relevant data points to establish the upper bound for chart rendering.
         *
         * @return the maximum value found in the dataset.
         */
            private double findMaxValue() {
                double maxValue = 0.0;
                for (int month = 0; month < MONTHS_COUNT; month++) {
                    for (int dataType = 0; dataType < DATA_TYPES_COUNT; dataType++) {
                        if (dataType < data[currentYearIndex].length && month < data[currentYearIndex][dataType].length) {
                            maxValue = Math.max(maxValue, data[currentYearIndex][dataType][month]);
                        }
                    }
                }
                return maxValue;
            }
        /**
         * Draws a bar chart on the provided Graphics context.
         * This method visualizes the entire dataset as a bar chart, with separate bars for each data type across all months.
         *
         * @param g            the Graphics context to draw on.
         * @param panelWidth   the width of the panel.
         * @param panelHeight  the height of the panel.
         * @param margin       the margin around the chart.
         * @param chartWidth   the width of the chart area.
         * @param chartHeight  the height of the chart area.
         * @param maxValue     the maximum value in the dataset for scaling purposes.
         */
            private void drawBarChart(Graphics g, int panelWidth, int panelHeight, int margin, int chartWidth, int chartHeight, double maxValue) {
                int barWidth = chartWidth / (MONTHS_COUNT * DATA_TYPES_COUNT); // reduce the widith and enlarge the gaps
                int maxBarHeight = chartHeight - 150; // More space for legends and labels

                g.setColor(Color.BLACK);
                g.drawLine(margin, panelHeight - margin, margin, margin);  // Vertical axis
                g.drawLine(margin, panelHeight - margin, panelWidth - margin, panelHeight - margin);  // Horizontal axis

                // Month labels
                String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

                for (int i = 0; i < MONTHS_COUNT; i++) {
                    g.drawString(months[i], margin + i * (chartWidth / MONTHS_COUNT), panelHeight - margin + 20);
                }

                // Draw bars with spacing between them
                Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.CYAN};
                String[] dataLabels = {"Task", "Receiver", "Sender", "Deposit", "Withdraw", "SumUp"};
                for (int month = 0; month < MONTHS_COUNT; month++) {
                    for (int dataType = 0; dataType < DATA_TYPES_COUNT; dataType++) {
                        int x = margin + month * barWidth * DATA_TYPES_COUNT + dataType * barWidth + (barWidth / 10); // add gaps
                        int y = panelHeight - margin;
                        double value = (dataType < data[currentYearIndex].length && month < data[currentYearIndex][dataType].length) ?
                                data[currentYearIndex][dataType][month] : 0;
                        int barHeight = (int) ((value / maxValue) * maxBarHeight);
                        g.setColor(colors[dataType]);
                        g.fillRect(x, y - barHeight, barWidth - (barWidth / 5), barHeight); // keeps gaps between bar char
                    }
                }

                // Draw vertical axis ticks and labels
                g.setColor(Color.BLACK);
                int numTicks = 10; // Number of ticks on the vertical axis
                for (int i = 0; i <= numTicks; i++) {
                    int y = panelHeight - margin - (i * maxBarHeight / numTicks);
                    g.drawLine(margin - 5, y, margin, y); // Tick marks
                    String label = String.format("%.1f", (maxValue / numTicks) * i);
                    g.drawString(label, margin - 50, y + 5); // Label each tick with a value
                }

                // Data type labels on the side
                int legendY = 30;  // Start position for legend
                for (int i = 0; i < dataLabels.length; i++) {
                    g.setColor(colors[i]);
                    g.fillRect(750, legendY, 20, 20);
                    g.setColor(Color.BLACK);
                    g.drawString(dataLabels[i], 775, legendY + 15);
                    legendY += 30;
                }
            }

        /**
         * Draws a line graph on the provided Graphics context.
         * This method visualizes data for a specific month as a line graph, detailing daily fluctuations across various data types.
         *
         * @param g            the Graphics context to draw on.
         * @param panelWidth   the width of the panel.
         * @param panelHeight  the height of the panel.
         * @param margin       the margin around the chart.
         * @param chartWidth   the width of the chart area.
         * @param chartHeight  the height of the chart area.
         */
            private void drawLineGraph(Graphics g, int panelWidth, int panelHeight, int margin, int chartWidth, int chartHeight) {
                g.clearRect(margin, margin, chartWidth, chartHeight);  // Clear the area for drawing graphs

                double[][][] dailyValues = new double[4][DATA_TYPES_COUNT][31];  // Updated to 4 graphs
                double[] maxValues = new double[4];  // Updated to 4 max values
                double[] minValues = new double[4];  // Updated to 4 max values
                double[][] monthData = AccountBillInfo.createLogMatrix(AccountBillInfo.readLogsForMonth(currentYearIndex + 2020, currentMonthIndex + 1, logFilePath));
                AccountBillInfo.printArray2D(monthData);
                // Assuming sample data initialization and other setup
                for (int graph = 0; graph < 4; graph++) {  // Updated loop to include the new graph
                    for (int dataType = 0; dataType < DATA_TYPES_COUNT; dataType++) {
                        for (int day = 0; day < 31; day++) {
//                        dailyValues[graph][dataType][day] = Math.random() * 100;  // Random sample data
                            dailyValues[graph][dataType][day] = monthData[dataType][day];
                            maxValues[graph] = Math.max(maxValues[graph], dailyValues[graph][dataType][day]);
                            minValues[graph] = Math.min(minValues[graph], dailyValues[graph][dataType][day]);
                        }
                    }
                }

                int[][] graphDataTypes = {{0}, {1, 2}, {3, 4}, {5}};  // Include task at index 0
                String[] sGraphTitles = {"Task", "Receiver", "Sender", "Deposit", "Withdraw", "Financial Summary"};
                Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.CYAN};

                int sectionHeight = (chartHeight - margin) / 4;  // Updated to 4 sections

                g.setColor(Color.BLACK);  // Set color for all axes and labels to black

                for (int graph = 0; graph < 4; graph++) {  // Loop through all 4 graphs now
                    int baseY = margin + graph * sectionHeight;
                    int graphBase = baseY + sectionHeight - 20;
                    g.setColor(Color.BLACK);
                    // Draw vertical and horizontal axes
                    g.drawLine(margin, baseY, margin, graphBase);
                    if (graph == 3) {
                        g.drawLine(margin, baseY, margin, margin + graphBase);
                    }
                    g.drawLine(margin, graphBase, margin + chartWidth - 2 * margin, graphBase);
                    g.setColor(Color.BLACK);
                    // Draw and label vertical axis ticks
                    int numTicks = 5;
                    for (int i = 0; i <= numTicks; i++) {
                        int y = graphBase - (i * (sectionHeight - 40) / numTicks);
                        g.drawLine(margin - 5, y, margin, y);
                        String label = String.format("%.1f", i * (maxValues[graph] / numTicks));
                        g.drawString(label, margin - 50, y + 5);
                    }

                    if (graph == 3) {
                        numTicks = 5;
                        // Flips the position level of the next cycle relative to the end of the first cycle

                        for (int i = 0; i <= numTicks; i++) {
                            int y = graphBase + (5 * (sectionHeight - 40) / numTicks) - (i * (sectionHeight - 40) / numTicks);
                            g.drawLine(margin - 5, y, margin, y);
                            if ((5 - i) * (minValues[graph] / numTicks) != 0.0) {
                                String label = String.format("%.1f", (5 - i) * (minValues[graph] / numTicks));
                                g.drawString(label, margin - 50, y + 5); // The label position is adjusted to the right side of the line
                            }

                        }
                    }

                    // Draw titles and corresponding color blocks
                    for (int i = 0; i < graphDataTypes[graph].length; i++) {
                        int dataType = graphDataTypes[graph][i];
                        g.setColor(colors[dataType % colors.length]);
                        g.fillRect(margin + 600 + i * 100, baseY - 15, 20, 20);
                        g.setColor(Color.BLACK);
                        g.drawString(sGraphTitles[dataType], margin + 625 + i * 100, baseY);
                    }

                    // Axis labels and vertical grid lines every 6 days
                    for (int day = 0; day <= 30; day += 6) {
                        int x = margin + day * (chartWidth - 2 * margin) / 30;
                        g.drawLine(x, graphBase, x, graphBase - 5);
                        g.drawString(String.valueOf(day + 1), x - 5, graphBase + 15);
                    }

                    // Draw lines for data types in each graph
                    for (int dataTypeIndex = 0; dataTypeIndex < graphDataTypes[graph].length; dataTypeIndex++) {
                        int dataType = graphDataTypes[graph][dataTypeIndex];
                        g.setColor(colors[dataType % colors.length]);
                        int prevX = margin, prevY = graphBase - (int) ((dailyValues[graph][dataType][0] / maxValues[graph]) * (sectionHeight - 40));
                        for (int day = 1; day < 31; day++) {
                            int x = margin + day * (chartWidth - 2 * margin) / 30;
                            int y = graphBase - (int) ((dailyValues[graph][dataType][day] / maxValues[graph]) * (sectionHeight - 40));
                            g.drawLine(Math.abs(prevX), Math.abs(prevY), Math.abs(x), Math.abs(y));
                            prevX = x;
                            prevY = y;
                        }
                    }
                }
            }


        }

    /**
     * Main.
     *
     * @param args the args
     */
    public static void main (String[]args){
            SwingUtilities.invokeLater(() -> {
                new AccountVisualizationFrame("kid@qmul.ac.uk", "CUR18525953.txt").setVisible(true);
            });
        }
    }