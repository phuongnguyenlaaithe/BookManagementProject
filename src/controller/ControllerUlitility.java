package controller;

import java.util.ArrayList;

import model.BookReaderManagement;

/**
 * ControllerUlitility
 */
public class ControllerUlitility {

    public ArrayList<BookReaderManagement> updateBRMInfo (ArrayList<BookReaderManagement> list,
    BookReaderManagement brm){
        boolean isUpdated = false;

        for(int i=0; i<list.size(); i++){
            BookReaderManagement b = list.get(i);
            if(b.getBooks().getBookID() == brm.getBooks().getBookID()
            && b.getReader().getReaderID() == brm.getReader().getReaderID()){
                list.set(i,brm); // cap nhat lai doi tuong quan li muon
                isUpdated = true;
            }
        }
        if(!isUpdated){
            list.add(brm);
        }
        return list;
    }

    public ArrayList<BookReaderManagement> updatetotalBorrowed (ArrayList<BookReaderManagement> list){
        for(int i=0; i<list.size(); i++){
            BookReaderManagement b = list.get(i);
            int count = 0;
            for(int j=0; j<list.size(); j++){
                if(list.get(j).getReader().getReaderID() == b.getReader().getReaderID()){
                    count += list.get(j).getNumberOfBorrow();
                }
            }
            b.setTotal(count); // update totalBorrowed
            list.set(i, b);
        }
        return list;
    }

    public ArrayList<BookReaderManagement> sortByReaderName (ArrayList<BookReaderManagement> list){
        for(int i = 0; i<list.size(); i++){
            for(int j=list.size()-1; j>i; j--){
                BookReaderManagement bj = list.get(j);
                BookReaderManagement bbj = list.get(j-1);
                if(bj.getReader().getFullName().substring(bj.getReader().getFullName().lastIndexOf(" ")).
                compareTo(bbj.getReader().getFullName().substring(bbj.getReader().getFullName().lastIndexOf(" "))) < 0){
                    //doi cho:
                    list.set(j, bbj);
                    list.set(j-1, bj);
                }
            }
        }
        return list;
    }

    public ArrayList<BookReaderManagement> sortByNumberOfBorrowed (ArrayList<BookReaderManagement> list){
        for(int i = 0; i<list.size(); i++){
            for(int j=list.size()-1; j>i; j--){
                BookReaderManagement bj = list.get(j);
                BookReaderManagement bbj = list.get(j-1);
                if(bj.getTotal() > bbj.getTotal()){
                    //doi cho:
                    list.set(j, bbj);
                    list.set(j-1, bj);
                }
            }
        }
        return list;
    }

    public ArrayList<BookReaderManagement> searchByReaderName (ArrayList<BookReaderManagement> list, String key){
        ArrayList<BookReaderManagement> result = new ArrayList<>();
        String pattern = ".*"+ key.toLowerCase() + ".*";
        for (int i =0; i<list.size(); i++){
            BookReaderManagement b = list.get(i);
            if(b.getReader().getFullName().toLowerCase().matches(pattern)){
                result.add(b);
            }
        }
        return result;
    }
}