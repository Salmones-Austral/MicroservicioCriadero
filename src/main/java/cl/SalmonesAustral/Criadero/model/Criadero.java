package cl.SalmonesAustral.Criadero.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//Entidad JPA para representar un Criadero (Centro de Cultivo)

@Entity
@Table(name = "criaderos")
public class Criadero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "ubicacion", nullable = false, length = 150)
    private String ubicacion;

    @Column(name = "region", nullable = false, length = 100)
    private String region;

    @Column(name = "capacidad_jaulas", nullable = false)
    private Integer capacidadJaulas;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado; // ACTIVO, INACTIVO, EN MANTENCION

    @Column(name = "fecha_creacion", nullable = false)
    private String fechaCreacion;

    // Constructor sin argumentos
    public Criadero() {}

    // Constructor completo
    public Criadero(Integer id, String nombre, String ubicacion, String region, Integer capacidadJaulas,
                    String estado, String fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.region = region;
        this.capacidadJaulas = capacidadJaulas;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getCapacidadJaulas() {
        return capacidadJaulas;
    }

    public void setCapacidadJaulas(Integer capacidadJaulas) {
        this.capacidadJaulas = capacidadJaulas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
