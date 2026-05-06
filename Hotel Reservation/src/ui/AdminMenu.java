package ui;

import api.AdminResource;
import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private static final AdminResource adminResource = AdminResource.getInstance();

    public static void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean keepRunning = true;

        while (keepRunning) {
            try {
                if (!scanner.hasNextLine()) {
                    break;
                }
                System.out.println("\nAdmin Menu");
                System.out.println("--------------------------------------------");
                System.out.println("1. See all Customers");
                System.out.println("2. See all Rooms");
                System.out.println("3. See all Reservations");
                System.out.println("4. Add a Room");
                System.out.println("5. Back to Main Menu");
                System.out.println("--------------------------------------------");
                System.out.print("Please select a number for the menu option: ");

                String input = scanner.nextLine();
                switch (input) {
                    case "1":
                        seeAllCustomers();
                        break;
                    case "2":
                        seeAllRooms();
                        break;
                    case "3":
                        seeAllReservations();
                        break;
                    case "4":
                        addRooms(scanner);
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

    private static void seeAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            for (Customer customer : customers) {
                System.out.println(customer);
            }
        }
    }

    private static void seeAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            for (IRoom room : rooms) {
                System.out.println(room);
            }
        }
    }

    private static void seeAllReservations() {
        adminResource.displayAllReservations();
    }

    private static void addRooms(Scanner scanner) {
        boolean addingRooms = true;
        List<IRoom> newRooms = new ArrayList<>();

        while (addingRooms) {
            System.out.print("Enter room number: ");
            String roomNumber = scanner.nextLine();
            
            System.out.print("Enter price per night: ");
            Double price = null;
            try {
                price = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid price. Defaulting to 0.0");
                price = 0.0;
            }

            System.out.print("Enter room type: 1 for single bed, 2 for double bed: ");
            RoomType type = null;
            String typeInput = scanner.nextLine();
            if (typeInput.equals("1")) {
                type = RoomType.SINGLE;
            } else if (typeInput.equals("2")) {
                type = RoomType.DOUBLE;
            } else {
                System.out.println("Error: Invalid room type. Defaulting to SINGLE.");
                type = RoomType.SINGLE;
            }

            IRoom room;
            if (price == 0.0) {
                room = new FreeRoom(roomNumber, type);
            } else {
                room = new Room(roomNumber, price, type);
            }
            newRooms.add(room);

            System.out.print("Would you like to add another room? y/n: ");
            String another = scanner.nextLine();
            if (another.equalsIgnoreCase("n")) {
                addingRooms = false;
            }
        }

        adminResource.addRoom(newRooms);
        System.out.println("Rooms added successfully!");
    }
}
