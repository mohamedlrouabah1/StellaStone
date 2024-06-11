package com.stormstars.stellastone.model.user;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.stormstars.stellastone.model.atelier.Fusee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String lastname, firstname, password,bio;

    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private byte[] avatar;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Fusee> fusees = new ArrayList<>();
   
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Transient
    public String getUserAvatar() {
        return "/home/settings/user/" + getId() + "/avatar";
    }

    @Override
    public String toString() {
        return "User{" +
                "lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", password='" + password + '\'' +
                ", bio='" + bio + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", resetPasswordToken='" + resetPasswordToken + '\'' +
                ", id=" + id +
                ", roles=" + roles +
                '}';
    }
}
