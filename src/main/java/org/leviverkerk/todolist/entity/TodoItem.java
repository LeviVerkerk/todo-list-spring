package org.leviverkerk.todolist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@ToString
@Entity
@Table(name = "items")
public class TodoItem {

    //  == fields ==
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "details")
    private String details;

    @Column(name = "deadline")
    private LocalDate deadline;

    public TodoItem(String title, String details, LocalDate deadline) {
        this.title = title;
        this.details = details;
        this.deadline = deadline;
    }

    public TodoItem() {

    }
}
