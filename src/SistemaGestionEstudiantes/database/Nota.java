package SistemaGestionEstudiantes.database;

public class Nota {

    private int id;
    private int idEstudiante;
    private int idMateria;
    private double nota;

    public Nota(int id, int idEstudiante, int idMateria, double nota) {
        this.id = id;
        this.idEstudiante = idEstudiante;
        this.idMateria = idMateria;
        this.nota = nota;
    }

    public int getId() {
        return id;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public int getIdMateria() {
        return idMateria;
    }

    public double getNota() {
        return nota;
    }
}
