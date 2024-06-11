package com.stormstars.stellastone.model.atelier;

import java.beans.Transient;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.stormstars.stellastone.model.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Fusee {

    private String nomFusee, infos, propriete, historique;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Lob
    private byte[] image;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="myuserfusee",referencedColumnName = "id")
    private User user;

    @Transient
    public String getIdIconFusee() {
        return "/home/modeRealiste/fusee/" + getId() + "/icon";
    }

}
