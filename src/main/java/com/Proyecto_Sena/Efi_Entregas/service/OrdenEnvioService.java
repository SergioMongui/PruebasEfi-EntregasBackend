package com.Proyecto_Sena.Efi_Entregas.service;

import com.Proyecto_Sena.Efi_Entregas.model.OrdenEnvio;
import com.Proyecto_Sena.Efi_Entregas.model.Usuario;
import com.Proyecto_Sena.Efi_Entregas.repository.OrdenEnvioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdenEnvioService {

    @Autowired
    private OrdenEnvioRepository ordenEnvioRepository;

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

    public void asignarOrdenes(Long idUsuario, List<Integer> ordenesIds) {

    Usuario usuario = new Usuario();
    usuario.setIdUsuario(idUsuario);

    for (Integer id : ordenesIds) {

        OrdenEnvio orden = ordenEnvioRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        orden.setUsuario(usuario);
        orden.setEstado("ASIGNADA");

        ordenEnvioRepository.save(orden);
    }
}
}
