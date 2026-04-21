import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class LibrarySystem {

    static List<Book> books = new ArrayList<>();
    static List<User> users = new ArrayList<>();
    static List<IssueRecord> records = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    static Book findBookById(int id) {
        for (Book book : books) {
            if (book.id == id) {
                return book;
            }
        }
        return null;
    }

    static User findUserById(int id) {
        for (User user : users) {
            if (user.id == id) {
                return user;
            }
        }
        return null;
    }

    static void pauseForMenu() {
        System.out.print("Type anything and press Enter to see the options again: ");
        sc.nextLine();
    }

    static void showError(String message) {
        System.out.println("Error: " + message);
        pauseForMenu();
    }

    static void loadDefaultBooks() {
        books.add(new Book(101, "Java Basics", "James Gosling", 3));
        books.add(new Book(102, "Python Fundamentals", "Guido van Rossum", 2));
        books.add(new Book(103, "C Programming", "Dennis Ritchie", 4));
    }

    static void listBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available");
            pauseForMenu();
            return;
        }

        System.out.println("\nBooks available:");
        for (Book book : books) {
            System.out.println("Book ID: " + book.id + " | Title: " + book.title + " | Author: " + book.author + " | Copies: " + book.copies);
        }
        pauseForMenu();
    }

    static void addBook(int id, String title, String author, int copies) {
        if (findBookById(id) != null) {
            showError("Book id already exists");
            return;
        }
        if (copies <= 0) {
            showError("Copies must be greater than 0");
            return;
        }

        books.add(new Book(id, title, author, copies));
        System.out.println("Book added successfully");
    }

    static void addUser(int id, String name) {
        if (findUserById(id) != null) {
            showError("User id already exists");
            return;
        }

        users.add(new User(id, name));
        System.out.println("User registered");
    }

    static void searchBook(String title) {
        boolean found = false;

        for (Book book : books) {
            if (book.title.equalsIgnoreCase(title)) {
                System.out.println("Book Found: " + book.title + " by " + book.author + " | Copies: " + book.copies);
                found = true;
            }
        }

        if (!found) {
            showError("Book not found");
        }
    }

    static void issueBook(int bookId, int userId) {
        Book book = findBookById(bookId);
        if (book == null) {
            showError("No such book id");
            return;
        }

        User user = findUserById(userId);
        if (user == null) {
            showError("User not found");
            return;
        }

        for (IssueRecord record : records) {
            if (record.bookId == bookId && record.userId == userId) {
                showError("This user already has this book");
                return;
            }
        }

        if (book.copies <= 0) {
            showError("No copies available");
            return;
        }

        book.copies--;
        LocalDate dueDate = LocalDate.now().plusDays(7);
        records.add(new IssueRecord(bookId, userId, dueDate));

        System.out.println("Book issued. Due date: " + dueDate);
    }

    static void returnBook(int bookId, int userId) {
        Iterator<IssueRecord> iterator = records.iterator();

        while (iterator.hasNext()) {
            IssueRecord record = iterator.next();
            if (record.bookId == bookId && record.userId == userId) {
                long lateDays = ChronoUnit.DAYS.between(record.dueDate, LocalDate.now());
                double fine = 0;

                if (lateDays > 0) {
                    fine = lateDays * 5;
                }

                Book book = findBookById(bookId);
                if (book != null) {
                    book.copies++;
                }

                iterator.remove();

                System.out.println("Book returned");
                System.out.println("Fine: Rs." + fine);
                return;
            }
        }

        showError("Record not found");
    }

    static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                int value = sc.nextInt();
                sc.nextLine();
                return value;
            }
            System.out.println("Error: Please enter a valid number");
            sc.nextLine();
        }
    }

    public static void main(String[] args) {
        loadDefaultBooks();

        while (true) {
            System.out.println("\n1.Add Book");
            System.out.println("2.Add User");
            System.out.println("3.Search Book");
            System.out.println("4.Issue Book");
            System.out.println("5.Return Book");
            System.out.println("6.List Books");
            System.out.println("7.Exit");

            int choice = readInt(sc, "Enter choice: ");

            switch (choice) {
                case 1:
                    int id = readInt(sc, "Enter book id: ");
                    System.out.print("Enter title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter author: ");
                    String author = sc.nextLine();
                    int copies = readInt(sc, "Enter copies: ");
                    addBook(id, title, author, copies);
                    break;

                case 2:
                    int uid = readInt(sc, "Enter user id: ");
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();
                    addUser(uid, name);
                    break;

                case 3:
                    System.out.print("Enter title: ");
                    String search = sc.nextLine();
                    searchBook(search);
                    break;

                case 4:
                    int bid = readInt(sc, "Enter book id: ");
                    int issueUserId = readInt(sc, "Enter user id: ");
                    issueBook(bid, issueUserId);
                    break;

                case 5:
                    int returnBookId = readInt(sc, "Enter book id: ");
                    int returnUserId = readInt(sc, "Enter user id: ");
                    returnBook(returnBookId, returnUserId);
                    break;

                case 6:
                    listBooks();
                    break;

                case 7:
                    System.out.println("Exiting...");
                    sc.close();
                    return;

                default:
                    showError("Invalid choice");
            }
        }
    }
}
