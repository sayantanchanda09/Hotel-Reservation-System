# Hotel Reservation Application

A Java-based command-line application for managing hotel bookings, customers, and room reservations.

## Features

- **Guest Booking**:
  - Find and book available rooms.
  - Recommended rooms (if requested dates are unavailable).
  - View personal reservations.
  - Create a customer account.
- **Administrative Tools**:
  - View all customers and rooms.
  - Add new rooms to the system.
  - View all reservations in the system.
  - See test data.

## Project Structure

- `src/`: Java source files.
- `src/model/`: Data models (Customer, Room, Reservation).
- `src/api/`: Resources for UI interaction.
- `src/service/`: Business logic services.
- `src/ui/`: Command-line interface menus.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher.

### Running the Application

1. Compile the Java files:
   ```bash
   javac -d bin src/*.java src/**/*.java
   ```
2. Run the application:
   ```bash
   java -cp bin HotelApplication
   ```

## Author
[Your Name/GitHub Profile]
