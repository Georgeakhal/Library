package org.example.Members;

import java.time.LocalDate;

public class Member {
    private String id;
    private String name;
    private String email;
    private LocalDate join_date;

    public Member(String id, String name, String email, LocalDate date) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.join_date = date;
    }

    public Member(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.join_date = LocalDate.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getJoin_date() {
        return join_date;
    }

    public void setJoin_date(LocalDate join_date) {
        this.join_date = join_date;
    }
}
