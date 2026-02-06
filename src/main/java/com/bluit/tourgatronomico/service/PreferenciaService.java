package com.bluit.tourgatronomico.service;

import com.bluit.tourgatronomico.model.*;
import com.bluit.tourgatronomico.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PreferenciaService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private LugarRepository lugarRepo;

    @Autowired
    private PalabraClaveRepository palabraRepo;

    @Autowired
    private UsuarioPalabraClaveRepository usuarioPalRepo;

    @Autowired
    private LugarPalabraClaveRepository lugarPalRepo;

    @Autowired
    private PlatilloRepository platilloRepo;

    @Autowired
    private PlatilloPalabraClaveRepository platilloPalRepo;

    @Autowired
    private NotificacionMatchEmailService matchEmailService;


    // --- Usuario ---
    public List<UsuarioPalabraClave> listarPorUsuario(Long usuarioId) {
        return usuarioPalRepo.findByUsuarioId(usuarioId);
    }

    @Transactional
    public void agregarAUsuario(Long usuarioId, Long palabraId) {
        if (!usuarioPalRepo.existsByUsuarioIdAndPalabraId(usuarioId, palabraId)) {
            Usuario u = usuarioRepo.findById(usuarioId).orElseThrow();
            PalabraClave p = palabraRepo.findById(palabraId).orElseThrow();
            UsuarioPalabraClave up = new UsuarioPalabraClave();
            up.setUsuario(u);
            up.setPalabra(p);
            usuarioPalRepo.save(up);
        }
    }

    @Transactional
    public void quitarDeUsuario(Long usuarioId, Long palabraId) {
        usuarioPalRepo.deleteByUsuarioIdAndPalabraId(usuarioId, palabraId);
    }

    // --- Lugar ---
    public List<LugarPalabraClave> listarPorLugar(Long lugarId) {
        return lugarPalRepo.findByLugarId(lugarId);
    }

    /*@Transactional
    public void agregarALugar(Long lugarId, Long palabraId) {
        if (!lugarPalRepo.existsByLugarIdAndPalabraId(lugarId, palabraId)) {
            Lugar l = lugarRepo.findById(lugarId).orElseThrow();
            PalabraClave p = palabraRepo.findById(palabraId).orElseThrow();
            LugarPalabraClave lp = new LugarPalabraClave();
            lp.setLugar(l);
            lp.setPalabra(p);
            lugarPalRepo.save(lp);
        }
    }*/

    @Transactional
    public void agregarALugar(Long lugarId, Long palabraId) {
        if (!lugarPalRepo.existsByLugarIdAndPalabraId(lugarId, palabraId)) {
            Lugar l = lugarRepo.findById(lugarId).orElseThrow();
            PalabraClave p = palabraRepo.findById(palabraId).orElseThrow();
            LugarPalabraClave lp = new LugarPalabraClave();
            lp.setLugar(l);
            lp.setPalabra(p);
            lugarPalRepo.save(lp);

            // ✅ Disparar notificación + correo por match
            matchEmailService.notificarNuevoLugarPorPalabra(l, p);
        }
    }

    @Transactional
    public void quitarDeLugar(Long lugarId, Long palabraId) {
        lugarPalRepo.deleteByLugarIdAndPalabraId(lugarId, palabraId);
    }

    // --- Platillo ---
    public List<PlatilloPalabraClave> listarPorPlatillo(Long platilloId) {
        return platilloPalRepo.findByPlatilloId(platilloId);
    }

    @Transactional
    public void agregarAPlatillo(Long platilloId, Long palabraId) {
        if (!platilloPalRepo.existsByPlatilloIdAndPalabraId(platilloId, palabraId)) {
            Platillo pl = platilloRepo.findById(platilloId).orElseThrow();
            PalabraClave p = palabraRepo.findById(palabraId).orElseThrow();

            PlatilloPalabraClave ppc = new PlatilloPalabraClave();
            ppc.setPlatillo(pl);
            ppc.setPalabra(p);
            platilloPalRepo.save(ppc);

            // ✅ Disparar notificación + correo por match
            matchEmailService.notificarNuevoPlatilloPorPalabra(pl, p);
        }
    }

    @Transactional
    public void quitarDePlatillo(Long platilloId, Long palabraId) {
        platilloPalRepo.deleteByPlatilloIdAndPalabraId(platilloId, palabraId);
    }

}