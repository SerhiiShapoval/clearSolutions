package com.shapoval.clearsolution.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@EqualsAndHashCode
@ToString(exclude = "id")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "lastName",  nullable = false)
    private String lastName;

    @Embedded
    private Address address;

    @ElementCollection
    @CollectionTable(name = "user_phones",
            joinColumns = @JoinColumn (name = "user_id"),
    indexes = @Index(name = "users_phones_id_idx", columnList = "user_id"))
    private List<String> phoneNumbers = new ArrayList<>();
}
