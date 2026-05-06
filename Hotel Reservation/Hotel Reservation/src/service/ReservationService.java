package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.*;

public class ReservationService {
    private static final ReservationService instance = new ReservationService();
    private final Map<String, IRoom> rooms = new HashMap<>();
    private final Set<Reservation> reservations = new HashSet<>();

    private ReservationService() {
    }

    public static ReservationService getInstance() {
        return instance;
    }

    public void addRoom(IRoom room) {
        rooms.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomId) {
        return rooms.get(roomId);
    }

    public Collection<IRoom> getAllRooms() {
        return rooms.values();
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation newReservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(newReservation);
        return newReservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        return findAvailableRooms(checkInDate, checkOutDate);
    }

    public Collection<IRoom> findRecommendedRooms(Date checkInDate, Date checkOutDate) {
        Calendar calendar = Calendar.getInstance();
        
        calendar.setTime(checkInDate);
        calendar.add(Calendar.DATE, 7);
        Date recommendedCheckIn = calendar.getTime();

        calendar.setTime(checkOutDate);
        calendar.add(Calendar.DATE, 7);
        Date recommendedCheckOut = calendar.getTime();

        return findAvailableRooms(recommendedCheckIn, recommendedCheckOut);
    }

    private Collection<IRoom> findAvailableRooms(Date checkInDate, Date checkOutDate) {
        Set<IRoom> availableRooms = new HashSet<>(rooms.values());
        
        for (Reservation reservation : reservations) {
            boolean isOverlapping = checkInDate.before(reservation.getCheckOutDate()) && 
                                    checkOutDate.after(reservation.getCheckInDate());
            if (isOverlapping) {
                availableRooms.remove(reservation.getRoom());
            }
        }
        return availableRooms;
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        Set<Reservation> customerReservations = new HashSet<>();
        for (Reservation reservation : reservations) {
            if (reservation.getCustomer().equals(customer)) {
                customerReservations.add(reservation);
            }
        }
        return customerReservations;
    }

    public void printAllReservation() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
                System.out.println("-------------------------");
            }
        }
    }
}
