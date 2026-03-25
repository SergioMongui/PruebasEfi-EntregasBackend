package com.Proyecto_Sena.Efi_Entregas.service;

import com.Proyecto_Sena.Efi_Entregas.model.ConexOrdenPlanTrabajo;
import com.Proyecto_Sena.Efi_Entregas.model.OrdenEnvio;
import com.Proyecto_Sena.Efi_Entregas.model.PlanTrabajo;
import com.Proyecto_Sena.Efi_Entregas.model.PlanTrabajoDTO;
import com.Proyecto_Sena.Efi_Entregas.repository.PlanTrabajoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Proyecto_Sena.Efi_Entregas.repository.ConexOrdenPlanTrabajoRepository;
import java.util.List;

@Service
public class PlanTrabajoService {

    @Autowired
    private PlanTrabajoRepository planTrabajoRepository;

    @Autowired
private ConexOrdenPlanTrabajoRepository conexionRepository;

    public List<PlanTrabajoDTO> obtenerTodos() {

        List<PlanTrabajo> planes = planTrabajoRepository.findAll();

        return planes.stream().map(plan -> {

            PlanTrabajoDTO dto = new PlanTrabajoDTO();

            dto.setIdPlanTrabajo(plan.getIdPlanTrabajo());
            dto.setEstado(plan.getEstadoPT());

            List<ConexOrdenPlanTrabajo> conexiones = conexionRepository.findByPlanTrabajo(plan);

            ConexOrdenPlanTrabajo conexion = conexiones.stream().findFirst().orElse(null);

            if (conexion != null) {
                dto.setFecha(conexion.getFechaCreacion());
            }

            // toma el id usuario de la orden
            OrdenEnvio orden = conexion != null ? conexion.getOrdenEnvio() : null;

            if (orden != null && orden.getUsuario() != null) {
                dto.setNombre(orden.getUsuario().getNombre());
                dto.setTelefono(orden.getUsuario().getTelefono());
                dto.setEmail(orden.getUsuario().getEmail());
            }

            return dto;

        }).toList();
    }
}
