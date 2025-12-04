package SistemaGestionEstudiantes.controllers;

public class NotaMateria {
    private String materia;
    private String notas;
    private String promedio;
    private String estado;

    public NotaMateria(String materia, String notas, String promedio, String estado) {
        this.materia = materia;
        this.notas = notas;
        this.promedio = promedio;
        this.estado = estado;
    }

    public String getMateria() { return materia; }
    public void setMateria(String value) { materia = value; }

    public String getNotas() { return notas; }
    public void setNotas(String value) { notas = value; }

    public String getPromedio() { return promedio; }
    public void setPromedio(String value) { promedio = value; }

    public String getEstado() { return estado; }
    public void setEstado(String value) { estado = value; }
}