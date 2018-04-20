package com.amandeep.booksentering;

/**
 * Created by Amandeep on 24/02/18.
 */

public class Books {
    public String subject;
    public String title;
    public String author;
    public String status;
    public int numberOfCopies;

    public Books(String subject, String title, String author, int numberOfCopies, String status) {
        this.subject = subject;
        this.title = title;
        this.author = author;
        this.numberOfCopies = numberOfCopies;
        this.status = status;
    }
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
