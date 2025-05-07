package vde.dev.garage.modele;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "Car")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    @Id
    private String immatriculation;
    @Column(length = 50)
    private String marque;
    @Column(length = 50)
    private String modele;
    @Column(length = 50)
    private String etat ;

    @Enumerated(EnumType.STRING)
    private StatutName statut;

    private Date estimatedCompletionDate;

    public Car(String immatriculation, String marque, String modele, String etat){
        this.immatriculation=immatriculation;
        this.marque=marque;
        this.modele=modele;
        this.etat=etat;
    }

    @ManyToOne
    private AppUser user;

}
