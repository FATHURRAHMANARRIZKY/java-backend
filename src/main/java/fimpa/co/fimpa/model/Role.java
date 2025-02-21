package fimpa.co.fimpa.model;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.*;

@Document(collection = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    // Default constructor
    public Role() {}

    // Parameterized constructor
    public Role(String name) {
        this.name = name;
    }

    // Getters and setters
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
}