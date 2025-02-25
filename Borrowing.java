package org.example.Borrowings;

import java.time.LocalDate;

public class Borrowing {
    private int code;
    private String id;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    public Borrowing(int code, String id, LocalDate borrowDate) {
        this.code = code;
        this.id = id;
        this.borrowDate = borrowDate;
    }
    public Borrowing(int code, String id, LocalDate borrowDate, LocalDate returnDate) {
        this.code = code;
        this.id = id;
        this.borrowDate = LocalDate.now();
        this.returnDate = returnDate;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
