package com.Proyecto_Sena.Efi_Entregas.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orden_envio")
public class OrdenEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrden;

    private String nombreCliente;
    private String direccion;
    private String telefono;
    private String listaProductos;
    private Double valor;
    private String estado;
    private String motivoCancelacion;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public void setIdOrden(Long idOrden) {
        this.idOrden = idOrden;
    }

    @ManyToOne
    @JoinColumn(name = "idPlanTrabajo")
    private PlanTrabajo planTrabajo;
}