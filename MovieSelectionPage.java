import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

public class MovieSelectionPage extends JFrame {

    private String username;

    private JComboBox<String> movieDropdown;
    private JComboBox<String> theaterDropdown;
    private JTextArea movieDescriptionArea;
    private JPanel seatPanel;
    private JButton[][] seats;
    private final int rows = 5, cols = 5;

    private List<Movie> movieList = new ArrayList<>();
    private Map<String, Set<String>> bookedSeatsMap = new HashMap<>();

    public MovieSelectionPage(String username) {
        this.username = username;

        setTitle("Book My Ticket - Select Your Happiness");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        loadMoviesFromCSV("movies.csv");
        loadBookedSeats();

        JPanel topPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        movieDropdown = new JComboBox<>(getUniqueMovieTitles().toArray(new String[0]));
        theaterDropdown = new JComboBox<>();

        movieDescriptionArea = new JTextArea(3, 30);
        movieDescriptionArea.setEditable(false);
        movieDescriptionArea.setLineWrap(true);
        movieDescriptionArea.setWrapStyleWord(true);

        topPanel.add(new JLabel("Select Movie:"));
        topPanel.add(movieDropdown);
        topPanel.add(new JLabel("Select Theater:"));
        topPanel.add(theaterDropdown);
        topPanel.add(new JLabel("Description:"));
        topPanel.add(new JScrollPane(movieDescriptionArea));
        add(topPanel, BorderLayout.NORTH);

        seatPanel = new JPanel(new GridLayout(rows, cols, 5, 5));
        seats = new JButton[rows][cols];
        add(seatPanel, BorderLayout.CENTER);

        JButton bookButton = new JButton("Book Seats");
        bookButton.setPreferredSize(new Dimension(150, 40));
        add(bookButton, BorderLayout.SOUTH);

        movieDropdown.addActionListener(e -> updateTheaterDropdown());
        theaterDropdown.addActionListener(e -> updateSeatLayout());

        bookButton.addActionListener(e -> handleBooking());

        movieDropdown.setSelectedIndex(0);
        updateTheaterDropdown();

        setVisible(true);
    }

    private void loadMoviesFromCSV(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 4);
                if (parts.length == 4) {
                    movieList.add(new Movie(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading movie data!!!");
        }
    }

    private void loadBookedSeats() {
        File file = new File("booked_seats.csv");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    String key = parts[0];
                    String[] seats = parts[1].split(" ");
                    bookedSeatsMap.putIfAbsent(key, new HashSet<>());
                    bookedSeatsMap.get(key).addAll(Arrays.asList(seats));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Set<String> getUniqueMovieTitles() {
        Set<String> titles = new HashSet<>();
        for (Movie m : movieList) titles.add(m.title);
        return titles;
    }

    private void updateTheaterDropdown() {
        theaterDropdown.removeAllItems();
        String selectedMovie = (String) movieDropdown.getSelectedItem();
        for (Movie m : movieList) {
            if (m.title.equals(selectedMovie)) {
                theaterDropdown.addItem(m.theater + " (" + m.showtime + ")");
            }
        }

        for (Movie m : movieList) {
            if (m.title.equals(selectedMovie)) {
                movieDescriptionArea.setText(m.description);
                break;
            }
        }

        if (theaterDropdown.getItemCount() > 0)
            theaterDropdown.setSelectedIndex(0);
    }

    private void updateSeatLayout() {
        seatPanel.removeAll();

        String movie = (String) movieDropdown.getSelectedItem();
        String theaterInfo = (String) theaterDropdown.getSelectedItem();
        if (movie == null || theaterInfo == null) return;

        String theater = theaterInfo.split(" \\(")[0];
        String showtime = theaterInfo.split("\\(")[1].replace(")", "");
        String bookingKey = movie + "_" + theater + "_" + showtime;

        Set<String> booked = bookedSeatsMap.getOrDefault(bookingKey, new HashSet<>());

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String seatCode = (i + 1) + "" + (char) ('A' + j);
                JButton seat = new JButton(seatCode);
                if (booked.contains(seatCode)) {
                    seat.setBackground(Color.RED);
                    seat.setEnabled(false);
                } else {
                    seat.setBackground(Color.GREEN);
                    seat.addActionListener(new SeatClickHandler(seat));
                }
                seats[i][j] = seat;
                seatPanel.add(seat);
            }
        }

        seatPanel.revalidate();
        seatPanel.repaint();
    }

    private void handleBooking() {
        String movie = (String) movieDropdown.getSelectedItem();
        String theaterInfo = (String) theaterDropdown.getSelectedItem();
        if (movie == null || theaterInfo == null) return;

        String theater = theaterInfo.split(" \\(")[0];
        String showtime = theaterInfo.split("\\(")[1].replace(")", "");
        String bookingKey = movie + "_" + theater + "_" + showtime;

        StringBuilder selectedSeats = new StringBuilder();
        List<String> newBookedSeats = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JButton btn = seats[i][j];
                if (btn.getBackground() == Color.YELLOW) {
                    String seatCode = btn.getText();
                    selectedSeats.append(seatCode).append(" ");
                    newBookedSeats.add(seatCode);
                }
            }
        }

        if (newBookedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No seats selected!");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Selected Seats: " + selectedSeats.toString() + "\nProceed to payment?",
                "Confirm Booking",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE);

        if (choice == JOptionPane.OK_OPTION) {
            try (FileWriter fw = new FileWriter("booked_seats.csv", true)) {
                fw.write(bookingKey + "," + String.join(" ", newBookedSeats) + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

            bookedSeatsMap.putIfAbsent(bookingKey, new HashSet<>());
            bookedSeatsMap.get(bookingKey).addAll(newBookedSeats);

            dispose();
            new PaymentPage(username, movie, theater, showtime, newBookedSeats);
        }
    }

    class SeatClickHandler implements ActionListener {
        private JButton seat;

        public SeatClickHandler(JButton seat) {
            this.seat = seat;
        }

        public void actionPerformed(ActionEvent e) {
            if (seat.getBackground() == Color.GREEN) {
                seat.setBackground(Color.YELLOW);
            } else if (seat.getBackground() == Color.YELLOW) {
                seat.setBackground(Color.GREEN);
            }
        }
    }

    class Movie {
        String title, description, theater, showtime;

        Movie(String title, String description, String theater, String showtime) {
            this.title = title;
            this.description = description;
            this.theater = theater;
            this.showtime = showtime;
        }
    }

    public static void main(String[] args) {
        new MovieSelectionPage("testuser");
    }
}
