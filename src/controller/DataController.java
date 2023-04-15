package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import model.Book;
import model.BookReaderManagement;
import model.Reader;

public class DataController {
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private PrintWriter printWriter;
    private Scanner scanner;

    // phương thức ghi
    public void openFileToWrite(String filename) {
        try {
            fileWriter = new FileWriter(filename, true); // tạo file để ghi vào, 
            //append: true là để bổ sung dữ liệu vào cuối mà không làm mất dữ liệu cũ
            bufferedWriter = new BufferedWriter(fileWriter); // truyền cái vừa ghi vào bộ đệm
            printWriter = new PrintWriter(bufferedWriter); // truyền từ bộ đệm vào file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeBookToFile(Book book, String fileName) {
        openFileToWrite(fileName);
        printWriter.println(book.getBookID() + "|" + book.getBookName() + "|"
                + book.getAuthor() + "|" + book.getSpecialization() + "|"
                + book.getPublishYear() + "|" + book.getQuantity());
        closeFileAfterWrite(fileName);
    }

    public void writeReaderToFile(Reader reader, String fileName) {
        openFileToWrite(fileName);
        printWriter.println(reader.getReaderID() + "|" + reader.getFullName() + "|"
                + reader.getAddress() + "|" + reader.getPhoneNumber());
        closeFileAfterWrite(fileName);
    }

    public void writeBRMToFile(BookReaderManagement brm, String fileName) {
        openFileToWrite(fileName);
        printWriter.println(brm.getReader().getReaderID() + "|" + brm.getBooks().getBookID() + "|"
                + brm.getNumberOfBorrow() + "|" + brm.getState());
        closeFileAfterWrite(fileName);
    }

    public void closeFileAfterWrite(String filename) {
        try {
            printWriter.close();// đóng từ dưới lên
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    // phương thức đọc
    public void openFileToRead (String fileName){
        try {
            File file = new File(fileName);
            if(!file.exists()){
                file.createNewFile();
            }
            scanner = new Scanner(Paths.get(fileName), "UTF-8");
            /*Use Paths.get() method to convert the string into a Path obj,
            which can be used to interact with files and directories on the file system*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeFileAfterRead(String fileName){
        try {
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Reader> readReadersFromFile (String fileName){
        openFileToRead(fileName);
        ArrayList<Reader> readers = new ArrayList<>();
        while (scanner.hasNextLine()){
            String data = scanner.nextLine();
            Reader reader = createReaderFromData(data);
            readers.add(reader);
            
        }
        closeFileAfterRead(fileName);
        return readers;
    }

    public Reader createReaderFromData(String data) {
        String[] datas = data.split("\\|");
        /*Kí tự `|` là một kí tự đặc biệt trong biểu thức chính quy, 
        do đó để sử dụng `|` như một kí tự thông thường, 
        chúng ta cần dùng hai dấu gạch chéo liên tiếp `\\|` 
        để "escape" nó - tức là đánh dấu nó như một kí tự bình thường để 
        sử dụng được trong phép tách chuỗi. */

        /*printWriter.println(reader.getReaderID() + " | " + reader.getFullName() + " | "
                + reader.getAddress() + " | " + reader.getPhoneNumber());
        public Reader(int readerID, String fullName, String address, 
        String phoneNumber) */

        Reader reader = new Reader(Integer.parseInt(datas[0]), datas[1], datas[2], datas[3]);
        return reader;
    }

    public ArrayList<Book> readBooksFromFile (String fileName){
        openFileToRead(fileName);
        ArrayList<Book> books = new ArrayList<>();
        while(scanner.hasNextLine()){
            String data = scanner.nextLine();
            Book book = createBookFromData(data);
            books.add(book);
        }
        closeFileAfterRead(fileName);
        return books;
    }

    public Book createBookFromData(String data) {
        String[] datas = data.split("\\|");
        /*printWriter.println(book.getBookID() + " | " + book.getBookName() + " | "
                + book.getAuthor() + " | " + book.getSpecialization() + " | "
                + book.getPublishYear() + " | " + book.getQuantity());
        public Book(int bookID, String bookName, String author, 
        String specialization, int publishYear, int quantity) */
        Book book = new Book(Integer.parseInt(datas[0]), datas[1], datas[2], datas[3],
         Integer.parseInt(datas[4]), Integer.parseInt(datas[5]));
        return book;
    }
    public ArrayList<BookReaderManagement> readBRMsFromFile (String fileName){
        ArrayList<Book> books = readBooksFromFile("BOOK.DAT");
        ArrayList<Reader> readers = readReadersFromFile("READER.DAT");
        openFileToRead(fileName);
        ArrayList<BookReaderManagement> brms = new ArrayList<>();
        while (scanner.hasNextLine()){
            String data = scanner.nextLine();
            BookReaderManagement brm = createBRMFromData(data, readers, books);
            brms.add(brm);
        }
        closeFileAfterRead(fileName);
        return brms;
    }

    public BookReaderManagement createBRMFromData(String data, ArrayList<Reader> readers, ArrayList<Book> books) {
        String[] datas = data.split("\\|");
        /*printWriter.println(brm.getReader().getReaderID() + " | " + brm.getBooks().getBookID() + " | "
                + brm.getNumberOfBorrow() + " | " + brm.getState());
        public BookReaderManagement(Reader reader, Book books, int numberOfBorrow, String state, int total) */
        BookReaderManagement brm = new BookReaderManagement(getReader(readers, Integer.parseInt(datas[0])),
        getBooK(books, Integer.parseInt(datas[1])), Integer.parseInt(datas[2]), datas[3], 0);
        return brm;
    }
    public void updateBRMFile(ArrayList<BookReaderManagement> list, String fileName){
        // xoa bo file cu
        File file = new File(fileName);
        if(file.exists()){
            file.delete();
        }
        //ghi moi file nay
        openFileToWrite(fileName);
        for(BookReaderManagement brm: list){
            printWriter.println(brm.getReader().getReaderID() + "|" + brm.getBooks().getBookID() + "|"
                + brm.getNumberOfBorrow() + "|" + brm.getState());
        }
        closeFileAfterWrite(fileName);
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
} 
