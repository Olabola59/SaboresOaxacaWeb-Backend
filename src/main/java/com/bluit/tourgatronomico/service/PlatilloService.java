package com.bluit.tourgatronomico.service;

import com.bluit.tourgatronomico.dto.PlatilloDTO;
import com.bluit.tourgatronomico.model.Platillo;
import com.bluit.tourgatronomico.model.Gusto;
import com.bluit.tourgatronomico.model.Respuesta;
import com.bluit.tourgatronomico.model.Pregunta;
import com.bluit.tourgatronomico.repository.PlatilloRepository;
import com.bluit.tourgatronomico.repository.GustoRepository;
import com.bluit.tourgatronomico.repository.RespuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlatilloService {
    private static final Logger logger = LoggerFactory.getLogger(PlatilloService.class);

    private final PlatilloRepository platilloRepository;
    private final GustoRepository gustoRepository;
    private final RespuestaRepository respuestaRepository;

    @Autowired
    public PlatilloService(PlatilloRepository platilloRepository,
            GustoRepository gustoRepository,
            RespuestaRepository respuestaRepository) {
        this.platilloRepository = platilloRepository;
        this.gustoRepository = gustoRepository;
        this.respuestaRepository = respuestaRepository;
    }

    public List<Platillo> findAll() {
        return platilloRepository.findAll();
    }

    public Optional<Platillo> findById(Long id) {
        return platilloRepository.findById(id);
    }

    public Platillo save(Platillo platillo) {
        return platilloRepository.save(platillo);
    }

    public void delete(Long id) {
        platilloRepository.deleteById(id);
    }

    public List<PlatilloDTO> getPlatillosRecomendadosParaUsuario(Long userId) {
        logger.info("Obteniendo recomendaciones para usuario: {}", userId);

        // 1. Obtener las preferencias del usuario
        List<Gusto> gustos = gustoRepository.findByUsuarioId(userId);
        logger.info("Gustos encontrados: {}", gustos.size());

        if (gustos.isEmpty()) {
            logger.warn("Usuario sin preferencias, devolviendo todos los platillos");
            return platilloRepository.findAll().stream()
                    .map(PlatilloDTO::new)
                    .collect(Collectors.toList());
        }

        // 2. Extraer valores de preferencias
        String tipoPreferido = obtenerValorGusto(gustos, "tipo");
        String presupuestoPreferido = obtenerValorGusto(gustos, "presupuesto");

        logger.debug("Tipo preferido: {}", tipoPreferido);
        logger.debug("Presupuesto preferido: {}", presupuestoPreferido);

        // 3. Determinar rango de precio según presupuesto
        double precioMaximo = determinarPrecioMaximo(presupuestoPreferido);
        logger.debug("Precio máximo: {}", precioMaximo);

        // 4. Obtener todos los platillos
        List<Platillo> todosPlatillos = platilloRepository.findAll();
        logger.info("Total de platillos en BD: {}", todosPlatillos.size());

        // 5. Filtrar platillos según preferencias
        List<Platillo> platillosFiltrados = todosPlatillos.stream()
                .filter(p -> {
                    boolean coincideTipo = coincideTipo(p.getCategoria(), tipoPreferido);
                    boolean coincidePresupuesto = coincidePresupuesto(p.getPrecio(), precioMaximo);

                    logger.debug("Platillo '{}' - Categoría: '{}', Precio: {} - CoinTipo: {}, CoinPresup: {}",
                            p.getNombre(), p.getCategoria(), p.getPrecio(), coincideTipo, coincidePresupuesto);

                    return coincideTipo && coincidePresupuesto;
                })
                .collect(Collectors.toList());

        logger.info("Platillos filtrados: {}", platillosFiltrados.size());

        // Si no hay platillos que coincidan exactamente, devolver todos
        if (platillosFiltrados.isEmpty()) {
            logger.warn("No hay coincidencias exactas, devolviendo todos");
            return todosPlatillos.stream()
                    .map(PlatilloDTO::new)
                    .collect(Collectors.toList());
        }

        // 6. Convertir a DTO
        return platillosFiltrados.stream()
                .map(PlatilloDTO::new)
                .collect(Collectors.toList());
    }

    public List<PlatilloDTO> getPlatillosPorCategoria(String categoria) {
        return platilloRepository.findByCategoriaIgnoreCase(categoria)
                .stream()
                .map(PlatilloDTO::new)
                .collect(Collectors.toList());
    }

    private String obtenerValorGusto(List<Gusto> gustos, String campo) {
        if (gustos == null || campo == null) {
            return "";
        }

        for (Gusto gusto : gustos) {
            if (gusto != null && gusto.getRespuestaId() != null) {
                // Obtener la respuesta
                Optional<Respuesta> respuestaOpt = respuestaRepository.findById(gusto.getRespuestaId());

                if (respuestaOpt.isPresent()) {
                    Respuesta respuesta = respuestaOpt.get();
                    Pregunta pregunta = respuesta.getPregunta();

                    // Verificar si el campo coincide
                    if (pregunta != null && pregunta.getCampo() != null &&
                            campo.equalsIgnoreCase(pregunta.getCampo())) {
                        String valor = respuesta.getValor();
                        return valor != null ? valor : "";
                    }
                }
            }
        }

        return "";
    }

    private double determinarPrecioMaximo(String presupuesto) {
        if (presupuesto == null || presupuesto.isEmpty()) {
            logger.debug("Sin presupuesto especificado, precio máximo ilimitado");
            return Double.MAX_VALUE;
        }

        String presupuestoNormalizado = presupuesto.toLowerCase().trim();
        logger.debug("Analizando presupuesto: '{}'", presupuestoNormalizado);

        // "Menos de $150 (económico)"
        if (presupuestoNormalizado.contains("menos de") ||
                presupuestoNormalizado.contains("económico") ||
                presupuestoNormalizado.contains("150")) {
            logger.debug("Presupuesto detectado: BAJO (máx $150)");
            return 150.0;
        }
        // "$150–$400 (medio)"
        else if ((presupuestoNormalizado.contains("150") && presupuestoNormalizado.contains("400")) ||
                presupuestoNormalizado.contains("medio")) {
            logger.debug("Presupuesto detectado: MEDIO (máx $400)");
            return 400.0;
        }
        // "Más de $400 (alto)"
        else if (presupuestoNormalizado.contains("más de") ||
                presupuestoNormalizado.contains("alto") ||
                presupuestoNormalizado.contains("400")) {
            logger.debug("Presupuesto detectado: ALTO (sin límite)");
            return Double.MAX_VALUE;
        }

        logger.debug("Presupuesto no reconocido, sin límite");
        return Double.MAX_VALUE;
    }

    private boolean coincideTipo(String categoriaPlato, String tipoPreferido) {
        if (categoriaPlato == null || tipoPreferido == null || tipoPreferido.isEmpty()) {
            logger.debug("coincideTipo: Sin filtro (categoría o preferencia vacía)");
            return true; // Si no hay categoría o preferencia, aceptar todo
        }

        // Normalizar strings para comparación
        String categoriaNormalizada = categoriaPlato.toLowerCase().trim();
        String tipoNormalizado = tipoPreferido.toLowerCase().trim();

        logger.debug("Comparando - Categoría BD: '{}' vs Preferencia usuario: '{}'",
                categoriaNormalizada, tipoNormalizado);

        // Mapeo de preferencias a categorías
        // "Tradicional oaxaqueña (moles, tlayudas, tamales)"
        if (tipoNormalizado.contains("tradicional")) {
            boolean coincide = categoriaNormalizada.contains("tradicional");
            logger.debug("Filtro TRADICIONAL: {}", coincide);
            return coincide;
        }

        // "Callejera auténtica (antojitos, mercados)"
        if (tipoNormalizado.contains("callejera")) {
            boolean coincide = categoriaNormalizada.contains("callejera");
            logger.debug("Filtro CALLEJERA: {}", coincide);
            return coincide;
        }

        // "Fusión / moderna (reinterpretaciones, chefs nuevos)"
        if (tipoNormalizado.contains("fusión") ||
                tipoNormalizado.contains("fusion") ||
                tipoNormalizado.contains("moderna")) {
            boolean coincide = categoriaNormalizada.contains("fusión") ||
                    categoriaNormalizada.contains("fusion") ||
                    categoriaNormalizada.contains("moderna");
            logger.debug("Filtro FUSIÓN/MODERNA: {}", coincide);
            return coincide;
        }

        // "Gourmet / alta cocina"
        if (tipoNormalizado.contains("gourmet") ||
                tipoNormalizado.contains("alta cocina")) {
            boolean coincide = categoriaNormalizada.contains("gourmet") ||
                    categoriaNormalizada.contains("alta cocina");
            logger.debug("Filtro GOURMET: {}", coincide);
            return coincide;
        }

        // "Vegetariana / vegana / saludable"
        if (tipoNormalizado.contains("vegetariana") ||
                tipoNormalizado.contains("vegana") ||
                tipoNormalizado.contains("saludable")) {
            boolean coincide = categoriaNormalizada.contains("vegetariana") ||
                    categoriaNormalizada.contains("vegana") ||
                    categoriaNormalizada.contains("saludable");
            logger.debug("Filtro VEGETARIANA/VEGANA: {}", coincide);
            return coincide;
        }

        logger.warn("⚠️ Tipo '{}' no coincide con ningún patrón conocido", tipoNormalizado);
        return false;
    }

    private boolean coincidePresupuesto(Double precio, double precioMaximo) {
        if (precio == null) {
            return true; // Si el platillo no tiene precio, lo incluimos
        }
        return precio <= precioMaximo;
    }
}