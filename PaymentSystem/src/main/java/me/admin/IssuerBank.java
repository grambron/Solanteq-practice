package me.admin;

import javax.persistence.*;

@Entity
@Table(name = "banks")
public class IssuerBank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "url")
    private String urlString;
    private String bin;

    public IssuerBank() {}

    public IssuerBank(String bin, String url) {
        this.setBin(bin);
        this.setUrlString(url);
    }

    public IssuerBank(int id, String bin, String url) {
        this.setId(id);
        this.setBin(bin);
        this.setUrlString(url);
    }

    public int getId() {
        return id;
    }

    public String getBin() {
        return bin;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public String toCompactString() {
        return String.format("%s :: %s", bin, urlString);
    }

    @Override
    public String toString() {
        return "IssuerBank{" +
                "id=" + id +
                ", bin='" + bin + '\'' +
                ", url='" + urlString + '\'' +
                '}';
    }
}
