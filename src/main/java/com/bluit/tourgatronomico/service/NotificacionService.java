package com.bluit.tourgatronomico.service;

import com.bluit.tourgatronomico.model.*;
import com.bluit.tourgatronomico.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NotificacionService {

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private NotificacionDispositivoRepository notificacionDispositivoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ========= Dispositivos =========
    @Transactional
    public Dispositivo registrarToken(Integer usuarioId, String token, String plataforma) {
        Dispositivo d = dispositivoRepository.findByTokenFcm(token).orElse(new Dispositivo());
        d.setTokenFcm(token);
        d.setPlataforma(plataforma);
        d.setActivo(true);
        if (usuarioId != null) {
            Usuario usuario = usuarioRepository.findById(usuarioId.longValue())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            d.setUsuario(usuario);
        }
        return dispositivoRepository.save(d);
    }

    public Optional<Dispositivo> dispositivosDeUsuario(Integer usuarioId) {
        return dispositivoRepository.findById(usuarioId.longValue());
    }

    public void desactivar(Long dispositivoId) {
        dispositivoRepository.findById(dispositivoId).ifPresent(d -> {
            d.setActivo(false);
            dispositivoRepository.save(d);
        });
    }

    // ========= Envío (simulado; listo para integrar FCM) =========
    @Transactional
    public Notificacion enviarAUsuario(Integer usuarioId, String titulo, String mensaje, String dataJsonNoUsado) {
        Notificacion n = new Notificacion();
        n.setTitulo(titulo);
        n.setMensaje(mensaje);
        Usuario usuario = usuarioRepository.findById(usuarioId.longValue())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        n.setUsuario(usuario);
        n = notificacionRepository.save(n);

        List<Dispositivo> dispositivos = dispositivoRepository.findByUsuarioId(usuarioId.longValue());
        enlazarComoEnviado(n, dispositivos);
        return n;
    }

    @Transactional
    public Notificacion enviarBroadcast(String titulo, String mensaje, String dataJsonNoUsado) {
        Notificacion n = new Notificacion();
        n.setTitulo(titulo);
        n.setMensaje(mensaje);
        n = notificacionRepository.save(n);

        List<Dispositivo> dispositivos = dispositivoRepository.findAll();
        enlazarComoEnviado(n, dispositivos);
        return n;
    }

    private void enlazarComoEnviado(Notificacion n, List<Dispositivo> dispositivos) {
        for (Dispositivo d : dispositivos) {
            NotificacionDispositivo link = new NotificacionDispositivo();
            link.setNotificacion(n);
            link.setDispositivo(d);
            link.setEstadoEnvio("SENT"); // simulado
            notificacionDispositivoRepository.save(link);
        }
    }

    // ========= Lectura / listado =========
    public List<Notificacion> listarPorUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioIdOrderByFechaEnvioDesc(usuarioId);
    }

    @Transactional
    public Notificacion marcarComoLeida(Long notificacionId) {
        Notificacion n = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        n.setLeido(true);
        return notificacionRepository.save(n);
    }

    @Transactional
    public void marcarTodasComoLeidas(Long usuarioId) {
        List<Notificacion> list = notificacionRepository.findByUsuarioIdOrderByFechaEnvioDesc(usuarioId);
        for (Notificacion n : list) {
            if (n.getLeido() == null || !n.getLeido()) {
                n.setLeido(true);
            }
        }
        notificacionRepository.saveAll(list);
    }

}