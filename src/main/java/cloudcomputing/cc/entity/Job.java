package cloudcomputing.cc.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="jobs")
@Data
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String city;
    @Column(length = 5000, nullable = false)
    private String description;
    @Column(length = 5000, nullable = false)
    private String skills;
    @Column(nullable = false)
    private String experience;
    @Column(nullable = false)
    private String url;

}
