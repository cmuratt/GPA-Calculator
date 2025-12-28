
/**
 * GPA Calculator GUI (Swing UI)
 * The main class that manages the user interface 
 * and all visual interactions.
 * * @author Murat Can IŞIK
 * @version 1.0
 */

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GPACalculatorGUI extends JFrame {

    private GPAManager manager = new GPAManager();
    private JTabbedPane tabbedPane;
    private JLabel lblGlobalGANO;

    private final Color BG_DARK = new Color(18, 18, 18);
    private final Color BG_PANEL = new Color(32, 32, 32);
    private final Color BG_INPUT = new Color(45, 45, 45);
    private final Color TEXT_WHITE = new Color(240, 240, 240);
    private final Color ACCENT_BLUE = new Color(50, 150, 255);
    private final Color SELECTION_COLOR = new Color(0, 100, 200);
    private final Color BTN_BG = new Color(25, 25, 25);
    private final Color BTN_HOVER = new Color(50, 50, 50);
    private final Color BTN_BORDER = new Color(80, 80, 80);
    private final Color BTN_TEXT = new Color(240, 240, 240);

    public GPACalculatorGUI() {
        setupUIManager();
        showStartScreen();
    }

    private void showStartScreen() {
        getContentPane().removeAll();
        setupFrame("GPA Calculator");

        JLabel titleLabel = new JLabel(
                "<html><center>Select Mode<br><br><span style='font-size:10px; color:#999999'>Designed by Murat Can IŞIK</span></center></html>",
                SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_WHITE);

        Object[] options = { "             Saved Profile             ", "Guest Mode" };

        JOptionPane pane = new JOptionPane(titleLabel, JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null,
                options, options[0]);
        JPanel buttonPanel = (JPanel) pane.getComponent(1);
        if (buttonPanel != null)
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JDialog dialog = pane.createDialog("GPA Calculator");
        for (Component component : pane.getComponents()) {
            if (component instanceof JPanel) {
                for (Component subComp : ((JPanel) component).getComponents()) {
                    if (subComp instanceof JButton)
                        styleButton((JButton) subComp, BTN_BG);
                }
            }
        }
        dialog.setVisible(true);
        Object selectedValue = pane.getValue();

        if (selectedValue == null
                || (selectedValue instanceof Integer && (Integer) selectedValue == JOptionPane.CLOSED_OPTION)) {
            System.exit(0);
        }

        if (options[0].equals(selectedValue))
            initProfileUI();
        else
            initManualUI();

        revalidate();
        repaint();
    }

    private void initProfileUI() {
        getContentPane().removeAll();
        setupFrame("GPA Calculator - My Profile");

        JPanel topPanel = new JPanel(new BorderLayout());
        stylePanel(topPanel);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        stylePanel(btnPanel);

        JButton btnBack = new JButton("<");
        styleButton(btnBack, BTN_BG);
        btnBack.setPreferredSize(new Dimension(45, 30));
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton btnAddSemester = new JButton("Add Semester");
        JButton btnRenameSemester = new JButton("Rename Semester");
        JButton btnDelSemester = new JButton("Delete Semester");

        styleButton(btnAddSemester, BTN_BG);
        styleButton(btnRenameSemester, BTN_BG);
        styleButton(btnDelSemester, BTN_BG);

        btnPanel.add(btnBack);
        btnPanel.add(Box.createHorizontalStrut(15));
        btnPanel.add(btnAddSemester);
        btnPanel.add(btnRenameSemester);
        btnPanel.add(btnDelSemester);

        lblGlobalGANO = new JLabel("Overall GPA: 0.00");
        lblGlobalGANO.setForeground(ACCENT_BLUE);
        lblGlobalGANO.setFont(new Font("Segoe UI", Font.BOLD, 22));

        updateGlobalGANO();

        topPanel.add(btnPanel, BorderLayout.WEST);
        topPanel.add(lblGlobalGANO, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabbedPane.setUI(new BasicTabbedPaneUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                highlight = BG_DARK;
                lightHighlight = BG_DARK;
                shadow = BG_DARK;
                darkShadow = BG_DARK;
                focus = BG_DARK;
                contentBorderInsets = new Insets(1, 1, 1, 1);
            }

            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                g.setColor(BG_PANEL);
                g.drawRect(0, 0, tabbedPane.getWidth() - 1, tabbedPane.getHeight() - 1);
            }

            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
                    boolean isSelected) {
                if (isSelected)
                    g.setColor(BG_PANEL);
                else
                    g.setColor(BG_DARK);
                g.fillRect(x, y, w, h);
            }

            @Override
            protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex,
                    String title, Rectangle textRect, boolean isSelected) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                if (isSelected)
                    g.setColor(ACCENT_BLUE);
                else
                    g.setColor(TEXT_WHITE);
                g.setFont(font);
                g.drawString(title, textRect.x, textRect.y + metrics.getAscent());
            }
        });

        add(tabbedPane, BorderLayout.CENTER);

        manager.getAllSemesters().forEach((name, courses) -> {
            tabbedPane.addTab(name, createSemesterPanel(name, courses));
        });

        btnBack.addActionListener(e -> showStartScreen());
        btnAddSemester.addActionListener(e -> {
            JPanel inputPanel = createInputPanel("Semester Name:");
            JTextField txtName = (JTextField) inputPanel.getComponent(1);

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Add Semester", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String name = txtName.getText();
                if (name != null && !name.trim().isEmpty()) {
                    manager.addSemester(name);
                    tabbedPane.addTab(name, createSemesterPanel(name, new ArrayList<>()));
                    tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
                }
            }
        });

        btnRenameSemester.addActionListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex != -1) {
                String oldName = tabbedPane.getTitleAt(selectedIndex);
                JPanel inputPanel = createInputPanel("New Semester Name:");
                JTextField txtName = (JTextField) inputPanel.getComponent(1);
                txtName.setText(oldName);

                int result = JOptionPane.showConfirmDialog(this, inputPanel, "Rename Semester",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    String newName = txtName.getText();
                    if (newName != null && !newName.trim().isEmpty() && !newName.equals(oldName)) {
                        if (manager.renameSemester(oldName, newName)) {
                            tabbedPane.setTitleAt(selectedIndex, newName);
                        } else {
                            JOptionPane.showMessageDialog(this, "Name invalid or already exists!");
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No semester selected!");
            }
        });

        btnDelSemester.addActionListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex != -1) {
                String name = tabbedPane.getTitleAt(selectedIndex);
                int confirm = JOptionPane.showConfirmDialog(this, "Delete '" + name + "'?", "Confirm Delete",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    manager.removeSemester(name);
                    tabbedPane.remove(selectedIndex);
                    updateGlobalGANO();
                }
            }
        });

        revalidate();
        repaint();
    }

    private JPanel createSemesterPanel(String semesterName, ArrayList<Course> courses) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        stylePanel(panel);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = { "Code", "Credit", "Grade", "Value" };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Course c : courses) {
            model.addRow(new Object[] { c.getCode(), c.getCredit(), c.getLetterGrade().name(),
                    c.getLetterGrade().getValue() });
        }

        JTable table = createCustomTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(BG_PANEL);

        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setViewportBorder(null);

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        styleScrollBar(scrollPane);

        panel.add(tableContainer, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        stylePanel(inputPanel);

        JTextField txtCode = new JTextField(5);
        JTextField txtCredit = new JTextField(3);
        JComboBox<LetterGrade> cmbGrade = new JComboBox<>(LetterGrade.values());
        styleComboBox(cmbGrade);
        cmbGrade.setMaximumRowCount(LetterGrade.values().length);
        if (cmbGrade.getItemCount() > 0)
            cmbGrade.setSelectedIndex(0);

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDel = new JButton("Delete");

        JLabel lblYANO = new JLabel("Term GPA: 0.00");

        styleTextField(txtCode);
        styleTextField(txtCredit);
        styleButton(btnAdd, BTN_BG);
        styleButton(btnUpdate, BTN_BG);
        styleButton(btnDel, BTN_BG);

        lblYANO.setForeground(ACCENT_BLUE);
        lblYANO.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblYANO.setText(String.format("Term GPA: %.2f", manager.calculateSemesterGPA(semesterName)));

        JLabel l1 = new JLabel("Code:");
        l1.setForeground(TEXT_WHITE);
        JLabel l2 = new JLabel("Credit:");
        l2.setForeground(TEXT_WHITE);

        inputPanel.add(l1);
        inputPanel.add(txtCode);
        inputPanel.add(l2);
        inputPanel.add(txtCredit);
        inputPanel.add(cmbGrade);
        inputPanel.add(btnAdd);
        inputPanel.add(btnUpdate);
        inputPanel.add(btnDel);
        inputPanel.add(Box.createHorizontalStrut(20));
        inputPanel.add(lblYANO);

        panel.add(inputPanel, BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtCode.setText(model.getValueAt(row, 0).toString());
                txtCredit.setText(model.getValueAt(row, 1).toString());
                try {
                    String gradeName = model.getValueAt(row, 2).toString();
                    cmbGrade.setSelectedItem(LetterGrade.valueOf(gradeName));
                } catch (Exception ex) {
                }
            }
        });

        btnAdd.addActionListener(e -> {
            try {
                String code = txtCode.getText().toUpperCase();
                int credit = Integer.parseInt(txtCredit.getText());
                LetterGrade grade = (LetterGrade) cmbGrade.getSelectedItem();
                if (code.isEmpty())
                    return;

                Course newCourse = new Course(code, credit, grade);
                manager.addCourse(semesterName, newCourse);
                model.addRow(new Object[] { code, credit, grade.name(), grade.getValue() });

                lblYANO.setText(String.format("Term GPA: %.2f", manager.calculateSemesterGPA(semesterName)));
                updateGlobalGANO();
                txtCode.setText("");
                txtCredit.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Credit must be a number!");
            }
        });

        btnUpdate.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    String newCode = txtCode.getText().toUpperCase();
                    int newCredit = Integer.parseInt(txtCredit.getText());
                    LetterGrade newGrade = (LetterGrade) cmbGrade.getSelectedItem();
                    if (newCode.isEmpty())
                        return;

                    manager.updateCourse(semesterName, selectedRow, newCode, newCredit, newGrade);

                    model.setValueAt(newCode, selectedRow, 0);
                    model.setValueAt(newCredit, selectedRow, 1);
                    model.setValueAt(newGrade.name(), selectedRow, 2);
                    model.setValueAt(newGrade.getValue(), selectedRow, 3);

                    lblYANO.setText(String.format("Term GPA: %.2f", manager.calculateSemesterGPA(semesterName)));
                    updateGlobalGANO();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error updating course.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a course to update!");
            }
        });

        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                manager.removeCourse(semesterName, row);
                model.removeRow(row);
                lblYANO.setText(String.format("Term GPA: %.2f", manager.calculateSemesterGPA(semesterName)));
                updateGlobalGANO();
            }
        });

        return panel;
    }

    private void updateGlobalGANO() {
        if (lblGlobalGANO != null) {
            double gano = manager.calculateTotalCGPA();
            lblGlobalGANO.setText(String.format("Overall GPA: %.2f", gano));
        }
    }

    private void initManualUI() {
        getContentPane().removeAll();
        setupFrame("Guest Mode - Scenario Calculator");

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        stylePanel(mainPanel);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topContainer = new JPanel(new BorderLayout());
        stylePanel(topContainer);

        JButton btnBack = new JButton("<");
        styleButton(btnBack, BTN_BG);
        btnBack.setPreferredSize(new Dimension(45, 30));
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        stylePanel(menuPanel);
        menuPanel.add(btnBack);

        topContainer.add(menuPanel, BorderLayout.NORTH);

        JPanel currentStatsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        stylePanel(currentStatsPanel);
        currentStatsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                " Current Academic Status ",
                0, 0, new Font("Segoe UI", Font.BOLD, 14), TEXT_WHITE));

        Font bigFont = new Font("Segoe UI", Font.BOLD, 16);
        JTextField txtCurrentGPA = new JTextField("0.00", 5);
        JTextField txtCurrentCredits = new JTextField("0", 4);
        styleTextField(txtCurrentGPA);
        styleTextField(txtCurrentCredits);
        txtCurrentGPA.setFont(bigFont);
        txtCurrentCredits.setFont(bigFont);

        JLabel l1 = new JLabel("Current CGPA:");
        l1.setForeground(TEXT_WHITE);
        l1.setFont(bigFont);

        JLabel l2 = new JLabel("Total Credits Earned:");
        l2.setForeground(TEXT_WHITE);
        l2.setFont(bigFont);

        currentStatsPanel.add(l1);
        currentStatsPanel.add(txtCurrentGPA);
        currentStatsPanel.add(l2);
        currentStatsPanel.add(txtCurrentCredits);

        topContainer.add(currentStatsPanel, BorderLayout.CENTER);
        mainPanel.add(topContainer, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(new String[] { "Course (Opt)", "Credit", "Grade", "Value" },
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = createCustomTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(BG_PANEL);

        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setViewportBorder(null);

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        styleScrollBar(scrollPane);

        mainPanel.add(tableContainer, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        stylePanel(bottomPanel);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        stylePanel(inputPanel);

        JTextField txtCode = new JTextField(5);
        JTextField txtCredit = new JTextField(3);
        JComboBox<LetterGrade> cmbGrade = new JComboBox<>(LetterGrade.values());
        styleComboBox(cmbGrade);

        styleTextField(txtCode);
        styleTextField(txtCredit);

        JButton btnAdd = new JButton("Add Course");
        JButton btnDel = new JButton("Delete Selected");
        styleButton(btnAdd, BTN_BG);
        styleButton(btnDel, BTN_BG);

        JLabel lCode = new JLabel("Code:");
        lCode.setForeground(TEXT_WHITE);
        JLabel lCred = new JLabel("Credit:");
        lCred.setForeground(TEXT_WHITE);

        inputPanel.add(lCode);
        inputPanel.add(txtCode);
        inputPanel.add(lCred);
        inputPanel.add(txtCredit);
        inputPanel.add(cmbGrade);
        inputPanel.add(btnAdd);
        inputPanel.add(btnDel);

        JLabel lblProjectedGPA = new JLabel("New Projected GPA: 0.00", SwingConstants.CENTER);
        lblProjectedGPA.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblProjectedGPA.setForeground(ACCENT_BLUE);
        lblProjectedGPA.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(lblProjectedGPA, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);

        ArrayList<Course> guestCourses = new ArrayList<>();

        btnBack.addActionListener(e -> showStartScreen());

        Runnable calculate = () -> {
            try {
                String gpaText = txtCurrentGPA.getText().replace(",", ".");
                String creditText = txtCurrentCredits.getText();

                double currentGPA = gpaText.isEmpty() ? 0.0 : Double.parseDouble(gpaText);
                int currentCredits = creditText.isEmpty() ? 0 : Integer.parseInt(creditText);

                double newGPA = manager.calculateProjectedCGPA(currentGPA, currentCredits, guestCourses);
                lblProjectedGPA.setText(String.format("New Projected GPA: %.2f", newGPA));

            } catch (NumberFormatException ex) {
                lblProjectedGPA.setText("Invalid Input!");
            }
        };

        btnAdd.addActionListener(e -> {
            try {
                String code = txtCode.getText().toUpperCase();
                if (code.isEmpty())
                    code = "NEW";

                int credit = Integer.parseInt(txtCredit.getText());
                LetterGrade grade = (LetterGrade) cmbGrade.getSelectedItem();

                Course newCourse = new Course(code, credit, grade);
                guestCourses.add(newCourse);

                model.addRow(new Object[] { code, credit, grade.name(), grade.getValue() });
                calculate.run();

                txtCode.setText("");
                txtCredit.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Credit must be a number!");
            }
        });

        btnDel.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                guestCourses.remove(selectedRow);
                model.removeRow(selectedRow);
                calculate.run();
            }
        });

        revalidate();
        repaint();
    }

    private JTable createCustomTable(DefaultTableModel model) {
        return new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (isRowSelected(row)) {
                    c.setBackground(SELECTION_COLOR);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(BG_PANEL);
                    c.setForeground(TEXT_WHITE);
                }
                return c;
            }
        };
    }

    private JPanel createInputPanel(String labelText) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        stylePanel(panel);
        JTextField txt = new JTextField();
        styleTextField(txt);
        JLabel lbl = new JLabel(labelText);
        lbl.setForeground(TEXT_WHITE);
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(txt, BorderLayout.CENTER);
        return panel;
    }

    private void setupUIManager() {
        try {
            UIManager.put("OptionPane.background", BG_DARK);
            UIManager.put("Panel.background", BG_DARK);
            UIManager.put("OptionPane.messageForeground", TEXT_WHITE);
            UIManager.put("Button.background", BTN_BG);
            UIManager.put("Button.foreground", BTN_TEXT);
            UIManager.put("Button.select", BTN_HOVER);
            UIManager.put("Button.focus", new Color(0, 0, 0, 0));
            UIManager.put("Label.foreground", TEXT_WHITE);
        } catch (Exception e) {
        }
    }

    private void setupFrame(String title) {
        setTitle(title);
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout());
    }

    private void stylePanel(JPanel p) {
        p.setBackground(BG_PANEL);
    }

    private void styleButton(JButton b, Color bgColor) {
        b.setBackground(bgColor);
        b.setForeground(BTN_TEXT);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(bgColor.brighter());
            }

            public void mouseExited(MouseEvent e) {
                b.setBackground(bgColor);
            }
        });
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BTN_BORDER, 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));
    }

    private void styleTextField(JTextField t) {
        t.setBackground(BG_INPUT);
        t.setForeground(TEXT_WHITE);
        t.setCaretColor(TEXT_WHITE);
        t.setBorder(BorderFactory.createLineBorder(BTN_BORDER));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setHorizontalAlignment(JTextField.CENTER);
    }

    private void styleComboBox(JComboBox<LetterGrade> c) {
        c.setUI(new FlatComboBoxUI());
        c.setBackground(BG_INPUT);
        c.setForeground(TEXT_WHITE);
        c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        c.setBorder(BorderFactory.createLineBorder(BTN_BORDER));

        c.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected)
                    setBackground(new Color(80, 80, 80));
                else
                    setBackground(BG_INPUT);
                setForeground(Color.WHITE);
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                if (value instanceof LetterGrade) {
                    LetterGrade lg = (LetterGrade) value;
                    if (index == -1)
                        setText(lg.name());
                    else
                        setText(value.toString());
                }
                return this;
            }
        });
    }

    private void styleTable(JTable t) {
        t.setBackground(BG_PANEL);
        t.setForeground(TEXT_WHITE);
        t.setFillsViewportHeight(true);
        t.setFocusable(true);
        t.setRowSelectionAllowed(true);
        t.setColumnSelectionAllowed(false);
        t.setCellSelectionEnabled(false);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        t.setGridColor(new Color(50, 50, 50));

        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0, 1));

        t.setRowHeight(30);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        t.setSelectionBackground(SELECTION_COLOR);
        t.setSelectionForeground(Color.WHITE);

        JTableHeader h = t.getTableHeader();
        h.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(BG_DARK);
                setForeground(TEXT_WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(80, 80, 80)));
                return this;
            }
        });

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);

        TableColumnModel cm = t.getColumnModel();
        for (int i = 0; i < cm.getColumnCount(); i++)
            cm.getColumn(i).setCellRenderer(center);
    }

    private void styleScrollBar(JScrollPane sp) {
        sp.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(100, 100, 100);
                this.trackColor = BG_DARK;
            }
        });
    }

    private class FlatComboBoxUI extends BasicComboBoxUI {
        @Override
        protected JButton createArrowButton() {
            JButton btn = new JButton() {
                @Override
                public void paint(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(BG_INPUT);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(TEXT_WHITE);
                    int size = 4;
                    int x = (getWidth() - size) / 2;
                    int y = (getHeight() - size) / 2 + 1;
                    g2.fillPolygon(new int[] { x, x + size * 2, x + size }, new int[] { y, y, y + size }, 3);
                    g2.dispose();
                }
            };
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            return btn;
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            g.setColor(BG_INPUT);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup popup = new BasicComboPopup(comboBox) {
                @Override
                protected JScrollPane createScroller() {
                    JScrollPane sp = super.createScroller();
                    sp.getVerticalScrollBar().setPreferredSize(new Dimension(12, Integer.MAX_VALUE));
                    sp.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                        @Override
                        protected void configureScrollBarColors() {
                            this.thumbColor = new Color(100, 100, 100);
                            this.trackColor = BG_DARK;
                        }
                    });
                    return sp;
                }
            };
            popup.setBorder(new LineBorder(BTN_BORDER));
            return popup;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GPACalculatorGUI().setVisible(true));
    }
}