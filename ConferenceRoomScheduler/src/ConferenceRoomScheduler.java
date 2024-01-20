import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ConferenceRoomScheduler {
    private Map<String, ConferenceRoom> conferenceRooms;
    private List<Reservation> reservations;
    private Scanner scanner;

    public ConferenceRoomScheduler() {
        conferenceRooms = new HashMap<>();
        reservations = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void addConferenceRoom(String roomId, String name, int capacity) {
        ConferenceRoom room = new ConferenceRoom(roomId, name, capacity);
        conferenceRooms.put(roomId, room);
        System.out.println("Conference room added successfully!");
    }

    public void makeReservation(String reservationId, String roomId, Date startTime, Date endTime) {
        ConferenceRoom room = conferenceRooms.get(roomId);
        if (room == null) {
            System.out.println("Invalid room ID");
            return;
        }

        for (Reservation reservation : reservations) {
            if (roomId.equals(reservation.getRoomId()) && isOverlapping(reservation, startTime, endTime)) {
                System.out.println("Reservation conflicts with an existing reservation");
                return;
            }
        }

        if (room.getCapacity() < getReservationCapacity(startTime, endTime)) {
            System.out.println("Reservation exceeds room capacity");
            return;
        }

        Reservation reservation = new Reservation(reservationId, roomId, startTime, endTime);
        reservations.add(reservation);
        System.out.println("Reservation made successfully!");
    }

    public boolean checkAvailability(String roomId, Date startTime, Date endTime) {
        ConferenceRoom room = conferenceRooms.get(roomId);
        if (room == null) {
            System.out.println("Invalid room ID");
            return false;
        }

        for (Reservation reservation : reservations) {
            if (roomId.equals(reservation.getRoomId()) && isOverlapping(reservation, startTime, endTime)) {
                return false;
            }
        }

        return true;
    }

    public void retrieveReservations() {
        System.out.println("All reservations:");
        for (Reservation reservation : reservations) {
            System.out.println(reservation);
        }
    }

    public void closeConnection() {
        scanner.close();
    }

    public static void main(String[] args) {
        ConferenceRoomScheduler scheduler = new ConferenceRoomScheduler();

        System.out.println("Welcome to the Conference Room Scheduler!");

        while (true) {
            System.out.println("\nPlease select an option:");
            System.out.println("1. Add Conference Room");
            System.out.println("2. Make Reservation");
            System.out.println("3. Check Availability");
            System.out.println("4. Retrieve Reservations");
            System.out.println("5. Exit");

            int choice = scheduler.getUserChoice();

            switch (choice) {
                case 1:
                    scheduler.addConferenceRoomFromInput();
                    break;
                case 2:
                    scheduler.makeReservationFromInput();
                    break;
                case 3:
                    scheduler.checkAvailabilityFromInput();
                    break;
                case 4:
                    scheduler.retrieveReservations();
                    break;
                case 5:
                    scheduler.closeConnection();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private int getUserChoice() {
        System.out.print("Enter your choice: ");
        return scanner.nextInt();
    }

    private void addConferenceRoomFromInput() {
        System.out.print("Enter Room ID: ");
        String roomId = scanner.next();
        scanner.nextLine(); // Consume the newline character

        System.out.print("Enter Room Name: ");
        String roomName = scanner.nextLine();

        System.out.print("Enter Room Capacity: ");
        int capacity = scanner.nextInt();

        addConferenceRoom(roomId, roomName, capacity);
    }

    private void makeReservationFromInput() {
        System.out.print("Enter Reservation ID: ");
        String reservationId = scanner.next();
        System.out.print("Enter Room ID: ");
        String roomId = scanner.next();
        System.out.print("Enter Start Time (yyyy-MM-dd HH:mm): ");
        String startTimeStr = scanner.next();
        scanner.nextLine(); // Consume the newline character
        System.out.print("Enter End Time (yyyy-MM-dd HH:mm): ");
        String endTimeStr = scanner.nextLine();

        try {
            Date startTime = parseDateTime(startTimeStr);
            Date endTime = parseDateTime(endTimeStr);
            makeReservation(reservationId, roomId, startTime, endTime);
        } catch (ParseException e) {
            System.out.println("Invalid date/time format");
        }
    }

    private void checkAvailabilityFromInput() {
        System.out.print("Enter Room ID: ");
        String roomId = scanner.next();
        System.out.print("Enter Start Time (yyyy-MM-dd HH:mm): ");
        String startTimeStr = scanner.next();
        scanner.nextLine();
        System.out.print("Enter End Time (yyyy-MM-dd HH:mm): ");
        String endTimeStr = scanner.nextLine();

        try {
            Date startTime = parseDateTime(startTimeStr);
            Date endTime = parseDateTime(endTimeStr);
            boolean available = checkAvailability(roomId, startTime, endTime);
            System.out.println("Room " + roomId + " is " + (available ? "available" : "not available") + " during the specified time");
        } catch (ParseException e) {
            System.out.println("Invalid date/time format");
        }
    }

    private Date parseDateTime(String dateTimeStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        format.setLenient(false);
        return format.parse(dateTimeStr);
    }

    private boolean isOverlapping(Reservation reservation, Date startTime, Date endTime) {
        return (startTime.before(reservation.getEndTime()) || startTime.equals(reservation.getEndTime())) &&
                (endTime.after(reservation.getStartTime()) || endTime.equals(reservation.getStartTime()));
    }

    private int getReservationCapacity(Date startTime, Date endTime) {
        int capacity = 0;
        for (Reservation reservation : reservations) {
            if (isOverlapping(reservation, startTime, endTime)) {
                capacity += conferenceRooms.get(reservation.getRoomId()).getCapacity();
            }
        }
        return capacity;
    }
    class ConferenceRoom {
        private String roomId;
        private String roomName;
        private int capacity;

        public ConferenceRoom(String roomId, String roomName, int capacity) {
            this.roomId = roomId;
            this.roomName = roomName;
            this.capacity = capacity;
        }

        public String getRoomId() {
            return roomId;
        }

        public String getRoomName() {
            return roomName;
        }

        public int getCapacity() {
            return capacity;
        }
    }

    class Reservation {
        private String reservationId;
        private String roomId;
        private Date startTime;
        private Date endTime;

        public Reservation(String reservationId, String roomId, Date startTime, Date endTime) {
            this.reservationId = reservationId;
            this.roomId = roomId;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public String getReservationId() {
            return reservationId;
        }

        public String getRoomId() {
            return roomId;
        }

        public Date getStartTime() {
            return startTime;
        }

        public Date getEndTime() {
            return endTime;
        }

        @Override
        public String toString() {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return "Reservation ID: " + reservationId + ", Room ID: " + roomId +
                    ", Start Time: " + format.format(startTime) + ", End Time: " + format.format(endTime);
        }
    }
}