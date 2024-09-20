package com.example.sumsub.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Webhook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "webhook_name")
    private String webhookName;

    @Column(name = "webhook_time")
    private LocalDateTime webhookTime;

    @Column(name = "external_user_id")
    private String externalUserId;
}
