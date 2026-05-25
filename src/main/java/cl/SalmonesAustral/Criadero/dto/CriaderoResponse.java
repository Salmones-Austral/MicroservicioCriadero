package cl.SalmonesAustral.Criadero.dto;

public class CriaderoResponse {
    private Integer id;
    private String nombre;
    private String ubicacion;
    private String region;
    private int capacidadJaulas;
    private String estado;
    private String fechaCreacion;

    //Campo calculado (lógica de negocio)
    private String estadoOperacion;

    //Otro ejemplo
    private String clasificacion;

    // GETTERS & SETTERS

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

    public int getcapacidadJaulas() {
        return capacidadJaulas;
    }

    public void setcapacidadJaulas(int capacidadJaulas) {
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
//fin modificaciones de valores faltantes###################
    public String getEstadoOperacion() {
        return estadoOperacion;
    }

    public void setEstadoOperacion(String estadoOperacion) {
        this.estadoOperacion = estadoOperacion;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }
}
