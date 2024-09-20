package com.example.sumsub.entity;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Applicant {

    @Id
    @Column(name = "external_user_id")
    private String externalUserId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "dob")
    private Date dateOfBirth;

    @Column(name = "level_name")
    private String levelName;
}
