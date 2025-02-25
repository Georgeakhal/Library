import org.example.Books.Book;
import org.example.Books.Service.BooksDao;
import org.example.Members.Member;
import org.example.Members.Service.MembersDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class MembersTest extends Assertions {
    MembersDao membersDao = new MembersDao();

    public MembersTest() throws SQLException {
    }

    @Test
    public void testGetAll_Add() throws SQLException {
        Member member = new Member("a", "aa", "b@b", LocalDate.now());
        membersDao.addMember(member);
        ArrayList<Member> array = membersDao.getMembers();

        assertNotNull(array);
    }

    @Test
    public void delete() throws SQLException {
        Member member = new Member("a1", "aa", "b@b", LocalDate.now());
        membersDao.addMember(member);
        membersDao.deleteMember("a1");
        Member newMember = membersDao.getMember("a1");

        assertNull(newMember);
    }

    @Test
    public void update() throws SQLException {
        Member member = new Member("a", "a1", "c@b", LocalDate.now());;

        Member oldMember = membersDao.getMember("a");

        membersDao.updateMember(member);

        Member newMember = membersDao.getMember("a");

        assertNotEquals(oldMember.getEmail(), newMember.getEmail());
    }
}


