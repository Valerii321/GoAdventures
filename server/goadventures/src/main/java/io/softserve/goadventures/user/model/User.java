package io.softserve.goadventures.user.model;

import javax.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "location")
    private String location;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private int phone;

    @Column(name = "role")
    private String role;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "status_id")
    private int statusId;

    public User() {
    }

    public User(String fullname, String email, String password) {
        setFullname(fullname);
        setEmail(email);
        setPassword(password);
    }

    public int getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {

        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

            byte[] bytes = messageDigest.digest(fullname.getBytes());

            StringBuilder str = new StringBuilder();

            for (byte b : bytes) {
                str.append(b);
            }

            this.password = str.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
}
