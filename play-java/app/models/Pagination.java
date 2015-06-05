package models;

public class Pagination {
    private int elements;
    private int elementsPerPage;
    private int currentPageIndex;

    public Pagination(int elements, int elementsPerPage, int currentPageIndex) {
        this.elements = elements;
        this.elementsPerPage = elementsPerPage;
        this.currentPageIndex = currentPageIndex;
    }

    public int getElements() {
        return elements;
    }

    public int getElementsPerPage() {
        return elementsPerPage;
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public long getStartPageIndex() {
        return (currentPageIndex-1)*elementsPerPage+1L;
    }

    public long getEndPageIndex() {
        return getStartPageIndex()+elementsPerPage-1L;
    }

    public int getMaxPageIndex() {
        return elements/elementsPerPage+1;
    }
}
