import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class PaymentPage extends JFrame {

    private String username, movie, theater, showtime;
    private List<String> selectedSeats;

    public PaymentPage(String username, String movie, String theater, String showtime, List<String> selectedSeats) {
        this.username = username;
        this.movie = movie;
        this.theater = theater;
        this.showtime = showtime;
        this.selectedSeats = selectedSeats;

        setTitle("Book My Ticket - Pay for Happiness");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Proceed with payment to confirm your booking:");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userLabel = new JLabel("User: " + username);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel amountLabel = new JLabel("Total Amount: ₹" + (selectedSeats.size() * 100));
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton payButton = new JButton("Pay Now");
        payButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(userLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(amountLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(payButton);

        add(panel);
        setVisible(true);

        payButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveReceipt();
                showConfirmation();
            }
        });
    }

    private void saveReceipt() {
        try (FileWriter fw = new FileWriter("receipt.csv", true)) {
            String record = username + "," + movie + "," + theater + "," + showtime + "," +
                    String.join(" ", selectedSeats) + "," + (selectedSeats.size() * 150) + "," +
                    LocalDateTime.now();
            fw.write(record + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showConfirmation() {
        String seatList = String.join(", ", selectedSeats);
        String message = "Booking Confirmed!\n\n" +
                         "User: " + username + "\n" +
                         "Movie: " + movie + "\n" +
                         "Theater: " + theater + "\n" +
                         "Showtime: " + showtime + "\n" +
                         "Seats: " + seatList + "\n" +
                         "Amount Paid: ₹" + (selectedSeats.size() * 150) + "\n\n" +
                         "Enjoy your movie!";

        int option = JOptionPane.showOptionDialog(null, message, "Ticket Confirmation",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                new String[]{"Book Another Ticket", "Exit"}, "Book Another Ticket");

        if (option == 0) {
            dispose();
            new MovieSelectionPage(username);
        } else {
            JOptionPane.showMessageDialog(null, "Thank you for using Movie Booking App!!!");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new PaymentPage("testuser", "Dune", "PVR", "6 PM", List.of("3A", "3B"));
    }
}
