# CSE110_Object_Oriented_Programming_Project
HridoyPriyoBooking is an online ticket booking platform. You can book any kind of event, movie, concert, party etc. by using our project.

# HridoyPriyoBooking - Online Ticket Booking System

Pure File-I/O (no database) Java Swing desktop application built for CSE110 Object Oriented Programme project.

## Folder structure

```
HridoyPriyoBooking/
├── src/
│   ├── app/
│   │   └── Main.java              (entry point)
│   ├── model/
│   │   ├── BookingItem.java       (abstract base class)
│   │   ├── Movie.java             (extends BookingItem)
│   │   ├── Concert.java           (extends BookingItem)
│   │   ├── Transport.java         (extends BookingItem)
│   │   ├── GeneralEvent.java      (extends BookingItem)
│   │   ├── User.java
│   │   └── Booking.java
│   ├── util/
│   │   └── FileManager.java       (all File I/O, try/catch everywhere)
│   └── ui/
│       ├── LoginFrame.java
│       ├── RegisterDialog.java
│       ├── UserDashboard.java
│       └── AdminDashboard.java
└── data/
    ├── users.txt
    ├── events.txt
    └── bookings.txt
```

## Import into IntelliJ IDEA

1. Open IntelliJ IDEA → **File → Open** → select the `HridoyPriyoBooking` folder.
2. Right-click `src` → **Mark Directory as → Sources Root**.
3. Go to **File → Project Structure → Project** and set the SDK to **JDK 17+**.
4. Right-click `app/Main.java` → **Run 'Main.main()'**.

**Important:** IntelliJ's default run configuration uses the project root
(`HridoyPriyoBooking/`) as the working directory, so the relative path
`data/...` used by `FileManager` will resolve correctly. If you ever see a
"file not found" issue, check **Run → Edit Configurations → Working
directory** and point it at the project root.

## Data file formats

**data/users.txt**
```
id,username,password,role
1,admin,admin123,ADMIN
2,rahim,rahim123,USER
```

**data/events.txt**
```
id,category,title,extraInfo,location,date,basePrice,totalSeats,availableSeats
1,Movie,Interstellar,Sci-Fi,Blockbuster Cinemas Dhanmondi,15-12-2024,350.0,100,100
2,Concert,Coke Studio Bangla Live,James,Army Stadium Dhaka,20-12-2024,1200.0,500,500
3,Transport,Dhaka to Cox's Bazar,Bus (AC),Saint Martin Paribahan,18-12-2024,1800.0,40,40
4,Event,Dhaka Tech Summit 2024,Basis,Bangabandhu International Conference Center,25-12-2024,500.0,300,300
```
`extraInfo` holds a category-specific value: Movie → genre, Concert → artist,
Transport → vehicle type, Event → organizer.

**data/bookings.txt** (empty at first run, filled as users book tickets)
```
bookingId,userId,username,eventId,eventTitle,category,seatCount,totalPrice,bookingDate,status
```

## Default login credentials

| Role  | Username | Password  |
|-------|----------|-----------|
| Admin | admin    | admin123  |
| User  | rahim    | rahim123  |

## OOP design notes

- **Encapsulation** — every field in every model class is `private` with
  public getters/setters; behaviour (`bookSeats`, `releaseSeats`) lives on
  the object itself.
- **Abstraction / Inheritance** — `BookingItem` is `abstract` and declares
  `calculateTotalPrice()`, `getCategory()`, `getDetails()`, `getSubField()`,
  `toFileString()` as abstract; `Movie`, `Concert`, `Transport`, and
  `GeneralEvent` each extend it.
- **Polymorphism** — `UserDashboard` and `AdminDashboard` work only with the
  `BookingItem` reference type; the actual price formula and details string
  used at runtime depend on the concrete subclass (Concert adds a 10%
  service charge, GeneralEvent adds a flat Tk 20 booking fee, Movie and
  Transport charge base price × seats).
- **Persistence Helper** — `FileManager` is the single place that touches
  `BufferedReader`/`BufferedWriter`; every method catches `IOException`
  internally and returns a safe default (empty list / `false`) instead of
  crashing the UI.

## Notes / assumptions

- Dates are plain strings in `dd-MM-yyyy` format (matches the 2024 sample
  data) — no date-picker validation is enforced, to keep pure file parsing
  simple; you can tighten this with `SimpleDateFormat` parsing in
  `AdminDashboard.validateForm()` if your evaluator wants stricter checks.
- Titles/locations should avoid commas since the storage format is
  comma-separated; swap to a different delimiter (e.g. `|`) if you need
  comma-safe titles.
- `bookings.txt` is append-only for new bookings and fully rewritten only
  when a status changes (cancellation), which keeps most writes cheap.
