import java.time.LocalDate;
import java.util.*;

class Book {
    String title;
    String author;
    String isbn;
    boolean isBorrowed;
    LocalDate dueDate;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isBorrowed = false;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author + ", ISBN: " + isbn + ", Borrowed: " + (isBorrowed ? "Yes" : "No") + ", Due Date: " + (isBorrowed ? dueDate.toString() : "Not borrowed");
    }
}

class Member {
    String name;
    String memberId;
    List<Book> borrowedBooks;

    public Member(String name, String memberId) {
        this.name = name;
        this.memberId = memberId;
        this.borrowedBooks = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Member ID: " + memberId + ", Borrowed Books: " + borrowedBooks.size();
    }
}

