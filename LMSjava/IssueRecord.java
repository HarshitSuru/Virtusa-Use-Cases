import java.time.LocalDate;

class IssueRecord {
    int bookId;
    int userId;
    LocalDate dueDate;

    IssueRecord(int bookId, int userId, LocalDate dueDate) {
        this.bookId = bookId;
        this.userId = userId;
        this.dueDate = dueDate;
    }
}