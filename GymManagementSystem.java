import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class GymManagementSystem extends JFrame {
    // Database Connection
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gym_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    // UI Components
    private JTextField memberNameField, memberAgeField, scheduleField;
    private JTextArea displayArea;

    public GymManagementSystem() {
        // Frame Setup
        setTitle("Gym Management System");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        formPanel.add(new JLabel("Member Name:"));
        memberNameField = new JTextField();
        formPanel.add(memberNameField);

        formPanel.add(new JLabel("Member Age:"));
        memberAgeField = new JTextField();
        formPanel.add(memberAgeField);

        formPanel.add(new JLabel("Schedule (e.g., Morning/Evening):"));
        scheduleField = new JTextField();
        formPanel.add(scheduleField);

        JButton registerButton = new JButton("Register Member");
        registerButton.addActionListener(e -> registerMember());
        formPanel.add(registerButton);

        JButton displayButton = new JButton("Display Members");
        displayButton.addActionListener(e -> displayMembers());
        formPanel.add(displayButton);

        add(formPanel, BorderLayout.NORTH);

        // Display Area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
    }

    private void registerMember() {
        String name = memberNameField.getText();
        String age = memberAgeField.getText();
        String schedule = scheduleField.getText();

        if (name.isEmpty() || age.isEmpty() || schedule.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO members (name, age, schedule) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setInt(2, Integer.parseInt(age));
            stmt.setString(3, schedule);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Member registered successfully!");
            memberNameField.setText("");
            memberAgeField.setText("");
            scheduleField.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void displayMembers() {
        displayArea.setText("ID\tName\tAge\tSchedule\n");
        displayArea.append("===================================\n");

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM members";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                displayArea.append(rs.getInt("id") + "\t" +
                                   rs.getString("name") + "\t" +
                                   rs.getInt("age") + "\t" +
                                   rs.getString("schedule") + "\n");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GymManagementSystem app = new GymManagementSystem();
            app.setVisible(true);
        });
    }
}
