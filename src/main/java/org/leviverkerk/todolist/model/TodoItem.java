package org.leviverkerk.todolist.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.swing.text.html.HTML;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
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

    @Column(name = "tags")
    @Enumerated
    @ElementCollection(targetClass = Tags.class)
    private List<Tags> tags;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Transient
    private boolean due;

    public TodoItem(String title, String details, LocalDate deadline, Tags[] tags) {
        this.title = title;
        this.details = details;
        this.deadline = deadline;
        this.tags = Arrays.stream(tags).collect(Collectors.toList());

        this.due = deadline.isBefore(LocalDate.now());
    }

    public TodoItem() {
    }

    public boolean isDue() {

        this.due = deadline.isBefore(LocalDate.now());

        return due;
    }

    public void addTag(Tags tag){
        tags.add(tag);
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", deadline=" + deadline +
                ", tags=" + tags +
                ", due=" + due +
                '}';
    }
}
