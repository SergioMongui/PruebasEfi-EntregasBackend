package com.Proyecto_Sena.Efi_Entregas.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
public class PlanTrabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlanTrabajo;

    @Column(nullable = false)
    private String estadoPT;

    @OneToMany(mappedBy = "planTrabajo")
    @JsonIgnore
    private List<ConexAsignacion> asignaciones;

    @OneToMany(mappedBy = "planTrabajo")
    @JsonIgnore
    private List<ConexOrdenPlanTrabajo> conexionesOrdenPlan;

    
}
