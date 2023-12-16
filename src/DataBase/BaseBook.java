// BaseBook sınıfı
package DataBase;

public abstract class BaseBook {
    private int id;
    private String title;
    private String author;
    private String subject;
    private int status;

    public BaseBook(int id, String title, String author, String subject, int status) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.subject = subject;
        this.status = status;
    }

    public BaseBook(){

    }

    public int getId() {
        return id;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getStatus() { return status; }

    public void setStatus(int status) {
        this.status = status;
    }


}