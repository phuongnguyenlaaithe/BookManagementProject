package view;


import java.util.ArrayList;
import java.util.Scanner;

import controller.ControllerUlitility;
import controller.DataController;
import model.Book;
import model.Reader;
import model.BookReaderManagement;
/**
 * View
 */
public class View {
    public static void main(String[] args) {
        int choice =0;
        Scanner scanner = new Scanner(System.in);

        String booksFileName = "BOOK.DAT";
        String readersFileName = "READER.DAT";
        String brmFileName = "BRM.DAT";

        DataController controller = new DataController();
        ControllerUlitility ulitility = new ControllerUlitility();

        ArrayList<Book> books = new ArrayList<>();
        ArrayList<Reader> readers = new ArrayList<>();
        ArrayList<BookReaderManagement> brms = new ArrayList<>();

        boolean isReaderChecked = false;
        boolean isBookChecked = false;

        do{
            System.out.println("_______________MENU______________");
            System.out.println("1.Add a book to file");
            System.out.println("2.Display books in file");
            System.out.println("3.Add a reader to file");
            System.out.println("4.Display readers in file");
            System.out.println("5.Enter book reader management");
            System.out.println("6.Sorting");
            System.out.println("7. Search reader by name");
            System.out.println("0.Exit");
            System.out.println("Your choice?");

            choice = scanner.nextInt();
            scanner.nextLine(); // Bỏ qua đọc dòng trên

            switch(choice){
                case 0:
                    System.out.println("Thank you for using our service");
                    break;
                
                case 1:
                    if (!isBookChecked) {
                        checkBookID(controller, booksFileName);
                        isBookChecked =true;
                    }
                    
                /*public Book(int bookID, String bookName, String author, 
                String specialization, int publishYear, int quantity) */
                    String bookName, author, spec;
                    int year, quan, sp;
                    String[] specs = {"Science", "Art", "Economic", "IT"};

                    System.out.println("Enter book: ");
                    bookName = scanner.nextLine();

                    System.out.println("Enter author: ");
                    author = scanner.nextLine();

                    do {
                        System.out.println("Enter specialization");
                        System.out.println("1.Science\n2.Art\n3.Economic\n4.IT");
                        sp = scanner.nextInt();
                    } while (sp < 1|| sp > 4);

                    spec = specs[sp-1];

                    System.out.println("Enter publish year:");
                    year = scanner.nextInt();

                    System.out.println("Enter quantity:");
                    quan = scanner.nextInt();

                    /*public Book(int bookID, String bookName, String author, 
                    String specialization, int publishYear, int quantity) */
                    Book book = new Book(0, bookName, author, spec, year, quan);
                    controller.writeBookToFile(book, booksFileName);
                    break;

                case 2:
                    books = controller.readBooksFromFile(booksFileName);
                    showBookInfo(books);
                    break;
                
                case 3:
                    if(!isReaderChecked){
                        checkReaderID(controller, readersFileName);
                        isReaderChecked = true;
                    }
                    String fullName, address, phoneNum;
                    System.out.println("Enter name: ");
                    fullName = scanner.nextLine();

                    System.out.println("Enter address: ");
                    address = scanner.nextLine();

                    do {
                        System.out.println("Enter phone number:");
                        phoneNum = scanner.nextLine();
                    } while (!phoneNum.matches("\\d{10}"));//  kiểm tra xem chuỗi có đúng 10 chữ số không
                    //Reader(int readerID, String fullName, String address, String phoneNumber)

                    Reader reader = new Reader(0, fullName, address, phoneNum);
                    controller.writeReaderToFile(reader, readersFileName);
                    break;

                case 4:
                    readers = controller.readReadersFromFile(readersFileName);
                    showReadersInfo(readers);
                    break;
                
                case 5:
                    //B0: Khoi tao danh sach
                    readers = controller.readReadersFromFile(readersFileName);
                    books = controller.readBooksFromFile(booksFileName);
                    brms = controller.readBRMsFromFile(brmFileName);

                    //B1: 
                    int readerID, bookID;
                    boolean isBorrowable = false;
                    do {
                        showReadersInfo(readers);
                        System.out.println("_________________________________");
                        System.out.println("Enter reader ID, enter 0 to pass: ");
                        readerID = scanner.nextInt();
                        if(readerID == 0){
                            break;// tat ca ban doc da duoc muon du sach 
                        }
                        isBorrowable = checkBorrowed(brms, readerID);
                        if(isBorrowable){
                            break;
                        }else{
                            System.out.println("You have borrowed enough allowed quantity");
                        }
                    } while (true);

                    //B2:
                    boolean isFull = false;
                    do {
                        showBookInfo(books);
                        System.out.println("___________________________________");
                        System.out.println("Enter book ID, enter 0 to pass");
                        bookID = scanner.nextInt();

                        if(bookID == 0){
                            break;
                        }else{
                            isFull = checkFull(brms, readerID, bookID);
                            if(isFull){
                                System.out.println("Please choose another book");
                            }else{
                                break;
                            }
                        }
                    } while (true);

                    //B3:
                    int numberborrowed = getTotal(brms, readerID, bookID);
                    do {
                        System.out.println("Enter quantity want to borrow, max is 3 (already borrowed:" + numberborrowed + "): ");
                        int x = scanner.nextInt();
                        if(x+numberborrowed >=1 && x+numberborrowed<=3){
                            numberborrowed += x;
                            break;
                        }else{
                            System.out.println("Over allowed quantity! Please enter quantity again");
                        }
                    } while (true);
                    scanner.nextLine();

                    System.out.println("Enter state:");
                    String status = scanner.nextLine();

                    //B4:
                    //public BookReaderManagement(Reader reader, Book books, 
                    //int numberOfBorrow, String state, int total)
                   
                    Book currentBook = getBooK(books, bookID);
                    Reader currenReader = getReader(readers, readerID);
                    BookReaderManagement b = new BookReaderManagement(currenReader,  currentBook,
                    numberborrowed, status, 0);

                    //B5:
                    brms = ulitility.updateBRMInfo(brms, b); // cap nhat danh sach quan li muon
                    controller.updateBRMFile(brms, brmFileName);// cap nhat file du lieu

                    //B6:
                    showBRMInfo(brms);
                    break;
                case 6:
                    brms = controller.readBRMsFromFile(brmFileName); // doc ra danh sach quan li
                    // update tong so luong muon
                    brms = ulitility.updatetotalBorrowed(brms);
                    do {
                        System.out.println("_______________________________________");
                        System.out.println("_________Sorting option_____________");
                        int x = 0;
                        System.out.println("1. Sort by name(A-Z)");
                        System.out.println("2. Sort by total of borrowings in descending order");
                        System.out.println("0. Back to menu");
                        System.out.println("Your choice?");
                        x= scanner.nextInt();
                        if(x==0){
                        break;
                        }
                        switch(x){
                            case 1:
                                // sap xep theo ten
                                brms = ulitility.sortByReaderName(brms);
                                showBRMInfo(brms);
                                break;
                            
                            case 2:
                                //sap xep theo so luong muon
                                brms = ulitility.sortByNumberOfBorrowed(brms);
                                showBRMInfo(brms);
                                break;
                            }
                    } while (true);
                    break;
                case 7:
                    brms = controller.readBRMsFromFile(brmFileName);
                    System.out.println("Enter name need to find: ");
                    String key = scanner.nextLine();
                    ArrayList<BookReaderManagement> result = ulitility.searchByReaderName(brms, key);
                    if(result.size()==0){
                        System.out.println("Can't find reader");
                    }else{
                        showBRMInfo(result);
                    }
                    break;
            }
        }while(choice !=0);
    }

    private static void showBRMInfo(ArrayList<BookReaderManagement> brms) {
        for (BookReaderManagement b : brms) {
            System.out.println(b);
        }
    }

    private static Reader getReader(ArrayList<Reader> readers, int readerID) {
        for( int i =0; i<readers.size(); i++){
            if(readers.get(i).getReaderID() == readerID){
                return readers.get(i);
            }
        }
        return null;
    }

    private static Book getBooK(ArrayList<Book> books, int bookID) {
        for( int i =0; i<books.size(); i++){
            if(books.get(i).getBookID() == bookID){
                return books.get(i);
            }
        }
        return null;
    }

    private static int getTotal(ArrayList<BookReaderManagement> brms, int readerID, int bookID) {
        for (BookReaderManagement r : brms) {
            if(r.getReader().getReaderID() == readerID
            && r.getBooks().getBookID() == bookID){
                return r.getNumberOfBorrow();
            }
        }
        return 0;
    }

    private static boolean checkFull(ArrayList<BookReaderManagement> brms, int readerID, int bookID) {
        for (BookReaderManagement r : brms) {
            if(r.getReader().getReaderID() == readerID
            && r.getBooks().getBookID() == bookID && r.getNumberOfBorrow() == 3){
                return true;
            }
        }
        return false;
    }

    private static boolean checkBorrowed(ArrayList<BookReaderManagement> brms, int readerID) {
        int count =0;
        for (BookReaderManagement r : brms) {
            if(r.getReader().getReaderID() == readerID){
                count += r.getNumberOfBorrow();
            }
        }
        if(count == 15) return false;
        else return true;
    }

    private static void showReadersInfo(ArrayList<Reader> readers) {
        System.out.println("___________Information of Readers______________");
        for (Reader r : readers) {
            System.out.println(r);
        }
    }

    private static void checkReaderID(DataController controller, String readersFileName) {
        ArrayList<Reader> readers = controller.readReadersFromFile(readersFileName);
        if(readers.size() == 0){
            //do nothing
        }else{
            Reader.setId(readers.get(readers.size()-1).getReaderID() +1);
        }
    }

    private static void checkBookID(DataController controller, String fileName) {
        ArrayList<Book> lisBooks = controller.readBooksFromFile(fileName);
        if (lisBooks.size() ==0) {
            //do nothing
        } else {
            Book.setId(lisBooks.get(lisBooks.size()-1).getBookID() + 1);
        }
    }

    public static void showBookInfo(ArrayList<Book> books) {
        System.out.println("________________Information of books______________");
        for( Book b: books){
            System.out.println(b);
        }
    }   
}
