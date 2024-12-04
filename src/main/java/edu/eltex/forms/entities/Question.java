package edu.eltex.forms.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    private String imageUrl;

    public enum Type {
        NUMERIC,
        SINGLE_CHOICE,
        MULTIPLE_CHOICE,
        TEXT
    }

    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}

