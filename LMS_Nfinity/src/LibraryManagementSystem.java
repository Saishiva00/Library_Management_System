import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LibraryManagementSystem {
    List<Book> books = new ArrayList<>();
    List<Member> members = new ArrayList<>();
    int maxBorrowedBooks = 3;
    int daysToBorrow = 14;

    // Initialize with some books
    public LibraryManagementSystem() {
        books.add(new Book("To Kill a Mockingbird", "Harper Lee", "9780060758676"));
        books.add(new Book("1984", "George Orwell", "9780451532535"));
        books.add(new Book("Pride and Prejudice", "Jane Austen", "9780553353364"));
        books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "9780743273565"));
    }

    // Book Management
    public void addBook(String title, String author, String isbn) {
        books.add(new Book(title, author, isbn));
        System.out.println("Book added successfully!");
    }

    public void removeBook(String isbn) {
        books.removeIf(book -> book.isbn.equals(isbn));
        System.out.println("Book removed successfully!");
    }

    public void searchBook(String keyword) {
        for (Book book : books) {
            if (book.title.contains(keyword) || book.author.contains(keyword) || book.isbn.contains(keyword)) {
                System.out.println(book);
            }
        }
    }

    public void displayAllBooks() {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    // Member Management
    public void registerMember(String name, String memberId) {
        members.add(new Member(name, memberId));
        System.out.println("Member registered successfully!");
    }

    public void removeMember(String memberId) {
        members.removeIf(member -> member.memberId.equals(memberId));
        System.out.println("Member removed successfully!");
    }

    public void displayAllMembers() {
        for (Member member : members) {
            System.out.println(member);
        }
    }

    // Borrowing System
    public void borrowBook(String memberId, String isbn) {
        Member member = findMember(memberId);
        if (member != null && member.borrowedBooks.size() < maxBorrowedBooks) {
            for (Book book : books) {
                if (book.isbn.equals(isbn) && !book.isBorrowed) {
                    book.isBorrowed = true;
                    book.dueDate = LocalDate.now().plusDays(daysToBorrow);
                    member.borrowedBooks.add(book);
                    System.out.println("Book borrowed successfully! Due date: " + book.dueDate);
                    return;
                }
            }
            System.out.println("Book not available or already borrowed.");
        } else {
            System.out.println("Member has already borrowed 3 books or member not found.");
        }
    }

    public void returnBook(String memberId, String isbn) {
        Member member = findMember(memberId);
        if (member != null) {
            for (Book book : member.borrowedBooks) {
                if (book.isbn.equals(isbn)) {
                    book.isBorrowed = false;
                    member.borrowedBooks.remove(book);
                    System.out.println("Book returned successfully!");
                    return;
                }
            }
            System.out.println("Book not found in the borrowed list.");
        } else {
            System.out.println("Member not found.");
        }
    }

    private Member findMember(String memberId) {
        for (Member member : members) {
            if (member.memberId.equals(memberId)) {
                return member;
            }
        }
        return null;
    }

    // Main Method
    public static void main(String[] args) {
        LibraryManagementSystem library = new LibraryManagementSystem();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nLibrary Management System");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Book");
            System.out.println("3. Search Book");
            System.out.println("4. Display All Books");
            System.out.println("5. Register Member");
            System.out.println("6. Remove Member");
            System.out.println("7. Display All Members");
            System.out.println("8. Borrow Book");
            System.out.println("9. Return Book");
            System.out.println("10. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter book title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter book author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter book ISBN: ");
                    String isbn = scanner.nextLine();
                    library.addBook(title, author, isbn);
                    break;
                case 2:
                    System.out.print("Enter book ISBN to remove: ");
                    isbn = scanner.nextLine();
                    library.removeBook(isbn);
                    break;
                case 3:
                    System.out.print("Enter keyword to search: ");
                    String keyword = scanner.nextLine();
                    library.searchBook(keyword);
                    break;
                case 4:
                    library.displayAllBooks();
                    break;
                case 5:
                    System.out.print("Enter member name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter member ID: ");
                    String memberId = scanner.nextLine();
                    library.registerMember(name, memberId);
                    break;
                case 6:
                    System.out.print("Enter member ID to remove: ");
                    memberId = scanner.nextLine();
                    library.removeMember(memberId);
                    break;
                case 7:
                    library.displayAllMembers();
                    break;
                case 8:
                    System.out.print("Enter member ID: ");
                    memberId = scanner.nextLine();
                    System.out.print("Enter book ISBN to borrow: ");
                    isbn = scanner.nextLine();
                    library.borrowBook(memberId, isbn);
                    break;
                case 9:
                    System.out.print("Enter member ID: ");
                    memberId = scanner.nextLine();
                    System.out.print("Enter book ISBN to return: ");
                    isbn = scanner.nextLine();
                    library.returnBook(memberId, isbn);
                    break;
                case 10:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 10);

        scanner.close();
    }
}
