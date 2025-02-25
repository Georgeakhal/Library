import org.example.Borrowings.Borrowing;
import org.example.Borrowings.Service.BorrowingsDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class BorrowingsTest extends Assertions {
    private final BorrowingsDao borrowingsDao = new BorrowingsDao();

    public BorrowingsTest() throws SQLException {
    }

    @Test
    public void testAddBorrowing() throws SQLException {
        Borrowing borrowing = new Borrowing(102, "a", LocalDate.of(2020, 1, 7));
        borrowingsDao.addBorrowing(borrowing);
        ArrayList<Borrowing> borrowings = borrowingsDao.getBorrowings();

        assertNotNull(borrowings);
    }

    @Test
    public void testReturnAndBorrow() throws SQLException {

        borrowingsDao.returnBook(100, LocalDate.now());

        assertNotNull(borrowingsDao.getLastBorrowingByBookCode(100).getReturnDate());
    }
}

