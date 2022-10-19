package com.challenge.tenpo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "session_data")
@Getter
@Setter
public class SessionData {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonProperty(access = WRITE_ONLY)
    @JsonIgnore
    private String token;

    public SessionData() {

    }

    public SessionData(Long userId, String username, String token) {
        this.userId = userId;
        this.username = username;
        this.token = token;
    }

}