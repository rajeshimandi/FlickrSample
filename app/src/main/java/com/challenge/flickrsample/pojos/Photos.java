package com.challenge.flickrsample.pojos;


import java.util.ArrayList;

public class Photos {

    private String total;

    private int page;

    private int pages;

    private ArrayList<PhotoItem> photo;

    private int perpage;

    public String getTotal ()
    {
        return total;
    }

    public void setTotal (String total)
    {
        this.total = total;
    }

    public ArrayList<PhotoItem> getPhoto() {
        return photo;
    }

    public void setPhoto(ArrayList<PhotoItem> photo) {
        this.photo = photo;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }
}
