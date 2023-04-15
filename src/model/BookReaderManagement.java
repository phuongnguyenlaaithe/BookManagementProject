package model;

public class BookReaderManagement {
    private Reader reader;
    private Book books;
    private int numberOfBorrow;  // so  luong muon
    private String state; //trang thai khi muon
    private int total;

    
    public BookReaderManagement() {
    }

    public BookReaderManagement(Reader reader, Book books, int numberOfBorrow, String state, int total) {
        this.reader = reader;
        this.books = books;
        this.numberOfBorrow = numberOfBorrow;
        this.state = state;
        this.total = total;
    }

    public Reader getReader() {
        return reader;
    }
    public void setReader(Reader reader) {
        this.reader = reader;
    }
    public Book getBooks() {
        return books;
    }
    public void setBooks(Book books) {
        this.books = books;
    }
    public int getNumberOfBorrow() {
        return numberOfBorrow;
    }
    public void setNumberOfBorrow(int numberOfBorrow) {
        this.numberOfBorrow = numberOfBorrow;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "BookReaderManagement[" +
        "readerID=" + reader.getReaderID() + 
        ", readerName=" + reader.getFullName() +
        ", bookID=" + books.getBookID() +
        ",bookName=" + books.getBookName() + 
        ", numberOfBorrow=" + numberOfBorrow+ 
        ", state=" + state + 
        ", total=" + total + 
        "]";
    }

    
}


