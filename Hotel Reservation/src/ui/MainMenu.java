package ui;

import api.HotelResource;
import model.IRoom;
import model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {
    private static final HotelResource hotelResource = HotelResource.getInstance();
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    public static void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean keepRunning = true;

        while (keepRunning) {
            try {
                if (!scanner.hasNextLine()) {
                    break;
                }
                System.out.println("\nWelcome to the Hotel Reservation Application");
                System.out.println("--------------------------------------------");
                System.out.println("1. Find and book a room");
                System.out.println("2. See my reservations");
                System.out.println("3. Create an account");
                System.out.println("4. Admin");
                System.out.println("5. Exit");
                System.out.println("--------------------------------------------");
                System.out.print("Please select a number for the menu option: ");

                String input = scanner.nextLine();
                switch (input) {
                    case "1":
                        findAndBookRoom(scanner);
                        break;
                    case "2":
                        seeMyReservations(scanner);
                        break;
                    case "3":
                        createAccount(scanner);
                        break;
                    case "4":
                        AdminMenu.displayMenu();
                        break;
                    case "5":
                        keepRunning = false;
                        break;
                    default:
                        System.out.println("Error: Invalid input. Please select a number from 1 to 5.");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getLocalizedMessage());
            }
        }
    }

    private static void findAndBookRoom(Scanner scanner) {
        System.out.println("Enter Check-In Date (" + DATE_FORMAT + "): ");
        Date checkIn = parseDate(scanner);
        System.out.println("Enter Check-Out Date (" + DATE_FORMAT + "): ");
        Date checkOut = parseDate(scanner);

        if (checkIn != null && checkOut != null) {
            if (checkOut.before(checkIn)) {
                System.out.println("Error: Check-out date must be after check-in date.");
                return;
            }
            if (checkIn.before(new Date())) {
                System.out.println("Error: Reservations cannot be made in the past.");
                return;
            }

            Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn, checkOut);

            if (availableRooms.isEmpty()) {
                System.out.println("No rooms available for those dates. Checking for recommended rooms...");
                Collection<IRoom> recommendedRooms = hotelResource.findRecommendedRooms(checkIn, checkOut);
                
                if (recommendedRooms.isEmpty()) {
                    System.out.println("No recommended rooms found.");
                } else {
                    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
                    Date recCheckIn = addDays(checkIn, 7);
                    Date recCheckOut = addDays(checkOut, 7);
                    System.out.println("Recommended rooms for " + formatter.format(recCheckIn) + " to " + formatter.format(recCheckOut) + ":");
                    printRooms(recommendedRooms);
                    bookingFlow(scanner, recommendedRooms, recCheckIn, recCheckOut);
                }
            } else {
                System.out.println("Available rooms:");
                printRooms(availableRooms);
                bookingFlow(scanner, availableRooms, checkIn, checkOut);
            }
        }
    }

    private static void bookingFlow(Scanner scanner, Collection<IRoom> rooms, Date checkIn, Date checkOut) {
        System.out.print("Would you like to book a room? y/n: ");
        String wantToBook = scanner.nextLine();

        if (wantToBook.equalsIgnoreCase("y")) {
            System.out.print("Do you have an account with us? y/n: ");
            String hasAccount = scanner.nextLine();

            if (hasAccount.equalsIgnoreCase("y")) {
                System.out.print("Enter Email format: name@domain.com: ");
                String email = scanner.nextLine();

                if (hotelResource.getCustomer(email) == null) {
                    System.out.println("Customer not found. You need to create an account first.");
                } else {
                    System.out.print("What room number would you like to reserve? ");
                    String roomNumber = scanner.nextLine();

                    IRoom selectedRoom = null;
                    for (IRoom room : rooms) {
                        if (room.getRoomNumber().equals(roomNumber)) {
                            selectedRoom = room;
                            break;
                        }
                    }

                    if (selectedRoom == null) {
                        System.out.println("Error: That room is not available.");
                    } else {
                        Reservation reservation = hotelResource.bookARoom(email, selectedRoom, checkIn, checkOut);
                        System.out.println("Reservation successful!");
                        System.out.println(reservation);
                    }
                }
            } else {
                System.out.println("Please create an account first.");
            }
        }
    }

    private static void seeMyReservations(Scanner scanner) {
        System.out.print("Enter Email format: name@domain.com: ");
        String email = scanner.nextLine();
        Collection<Reservation> reservations = hotelResource.getCustomersReservations(email);

        if (reservations == null || reservations.isEmpty()) {
            System.out.println("No reservations found for this email.");
        } else {
            for (Reservation res : reservations) {
                System.out.println(res);
                System.out.println("-------------------------");
            }
        }
    }

    private static void createAccount(Scanner scanner) {
        System.out.print("Enter Email format: name@domain.com: ");
        String email = scanner.nextLine();
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        try {
            hotelResource.createACustomer(email, firstName, lastName);
            System.out.println("Account created successfully!");
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getLocalizedMessage());
        }
    }

    private static Date parseDate(Scanner scanner) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(scanner.nextLine());
        } catch (ParseException e) {
            System.out.println("Error: Invalid date format. Please use " + DATE_FORMAT);
            return null;
        }
    }

    private static Date addDays(Date date, int days) {
        long time = date.getTime();
        time += (long) days * 24 * 60 * 60 * 1000;
        return new Date(time);
    }

    private static void printRooms(Collection<IRoom> rooms) {
        if (rooms.isEmpty()) {
            System.out.println("No rooms available.");
        } else {
            for (IRoom room : rooms) {
                System.out.println(room);
            }
        }
    }
}
