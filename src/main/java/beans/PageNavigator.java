package beans;

import java.util.ArrayList;

public class PageNavigator {
    private int selectedPageNumber;
    private int objOnPage;
    private ArrayList<Integer> pageNumbers;

    public PageNavigator(int selectedPageNumber, int objOnPage) {
        this.selectedPageNumber = selectedPageNumber;
        this.objOnPage = objOnPage;
    }

    public PageNavigator(int selectedPageNumber, int objOnPage, int totalObjCount) {
        this.selectedPageNumber = selectedPageNumber;
        this.objOnPage = objOnPage;
        this.pageNumbers = new ArrayList<>();
        fillPageNumbers(totalObjCount);
    }

    public int getSelectedPageNumber() {
        return selectedPageNumber;
    }

    public void setSelectedPageNumber(int selectedPageNumber) {
        this.selectedPageNumber = selectedPageNumber;
    }

    public int getObjOnPage() {
        return objOnPage;
    }

    public void setObjOnPage(int objOnPage) {
        this.objOnPage = objOnPage;
    }

    public ArrayList<Integer> getPageNumbers() {
        return pageNumbers;
    }

    public void setPageNumbers(ArrayList<Integer> pageNumbers) {
        this.pageNumbers = pageNumbers;
    }

    public void fillPageNumbers(int totalObjCount) {
        int pageCount = 0;
        if (objOnPage < totalObjCount) {
            float countPageFloat = (float) (totalObjCount) / (float) (objOnPage);
            int countPageInteger = totalObjCount / objOnPage;
            pageCount = countPageFloat > countPageInteger ? countPageInteger + 1 : countPageInteger;
        } else {
            pageCount = 0;
        }
        pageNumbers.clear();
        for (int i = 1; i <= pageCount; i++) {
            pageNumbers.add(i);
        }
    }

    public void prevPage() {
        selectedPageNumber = (selectedPageNumber != 1) ? selectedPageNumber - 1 : selectedPageNumber;
    }

    public void nextPage() {
        selectedPageNumber = (selectedPageNumber < pageNumbers.size()) ? selectedPageNumber + 1 : selectedPageNumber;
    }
}


