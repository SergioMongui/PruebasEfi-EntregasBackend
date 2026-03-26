package com.Proyecto_Sena.Efi_Entregas.service;

import com.Proyecto_Sena.Efi_Entregas.model.ConexOrdenPlanTrabajo;
import com.Proyecto_Sena.Efi_Entregas.model.OrdenEnvio;
import com.Proyecto_Sena.Efi_Entregas.model.Usuario;
import com.Proyecto_Sena.Efi_Entregas.repository.ConexOrdenPlanTrabajoRepository;
import com.Proyecto_Sena.Efi_Entregas.repository.OrdenEnvioRepository;
import com.Proyecto_Sena.Efi_Entregas.repository.PlanTrabajoRepository;
import com.Proyecto_Sena.Efi_Entregas.repository.UsuarioRepository;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Proyecto_Sena.Efi_Entregas.model.PlanTrabajo;

import java.util.List;

@Service
public class OrdenEnvioService {

    @Autowired
    private OrdenEnvioRepository ordenEnvioRepository;

    @Autowired
    private PlanTrabajoRepository planTrabajoRepository;

    @Autowired
    private ConexOrdenPlanTrabajoRepository conexionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public OrdenEnvio save(OrdenEnvio orden) {
        if (orden.getEstado() == null || orden.getEstado().isEmpty()) {
            orden.setEstado("ACTIVA");
        }
        return ordenEnvioRepository.save(orden);
    }

    public List<OrdenEnvio> getAll() {
        return ordenEnvioRepository.findAll();
    }

    public void eliminar(Long id) {
        ordenEnvioRepository.deleteById(id);
    }

    public OrdenEnvio cancelarOrden(Long id, String motivo) {
        OrdenEnvio orden = ordenEnvioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        orden.setEstado("CANCELADA");
        orden.setMotivoCancelacion(motivo);

        return ordenEnvioRepository.save(orden);
    }

    @Transactional
    public void asignarOrdenes(Long idUsuario, List<Integer> ordenesIds) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        PlanTrabajo plan = new PlanTrabajo();// determina un id de plan de trabajo a una orden y cambia es estado
        plan.setEstadoPT("ACTIVO");

        plan = planTrabajoRepository.save(plan);

        for (Integer id : ordenesIds) {

            OrdenEnvio orden = ordenEnvioRepository.findById(Long.valueOf(id))
                    .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

            orden.setUsuario(usuario);
            orden.setEstado("ASIGNADA");
            orden.setPlanTrabajo(plan);
            ordenEnvioRepository.save(orden); //guarda la orden orden

            ConexOrdenPlanTrabajo conexion = new ConexOrdenPlanTrabajo();
            conexion.setPlanTrabajo(plan);
            conexion.setOrdenEnvio(orden);

            conexionRepository.save(conexion);
        }
    }

    public OrdenEnvio actualizarEstado(Long id, String nuevoEstado, String comentario) {
    OrdenEnvio orden = ordenEnvioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

    orden.setEstado(nuevoEstado);

    orden.setMotivoCancelacion(comentario);

    return ordenEnvioRepository.save(orden);
}
}
