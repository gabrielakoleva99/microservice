package com.microservices.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usertable")
public class User {

    @Column(name="id")
    private int id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="userid")
    private String userId;
    @Column(name="firstname")
    private String firstName;
    @Column(name="lastname")
    private String lastName;
    @Column(name="password")
    private String password;
    @Column(name="token")
    private String token;
    @ElementCollection
    @CollectionTable(name = "user_songlists", joinColumns = @JoinColumn(name = "userid"))
    @Column(name = "songlist_id")
    private List<Integer> songlists;
}
