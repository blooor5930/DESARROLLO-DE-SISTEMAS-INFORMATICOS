import java.util.*;

public class Main {
    interface Observador { void actualizar(CasoRescate caso); }
    static class Voluntario implements Observador {
        private final String nombre;
        Voluntario(String nombre) { this.nombre = nombre; }
        public void actualizar(CasoRescate caso) {
            System.out.println("Aviso a " + nombre + ": nuevo rescate de " + caso.expediente.tipo());
        }
    }
    interface Expediente { String tipo(); }
    static class Perro implements Expediente { public String tipo() { return "perro"; } }
    static class Gato implements Expediente { public String tipo() { return "gato"; } }
    static class FaunaSilvestre implements Expediente { public String tipo() { return "fauna silvestre"; } }
    static abstract class FabricaExpediente {
        abstract Expediente crear();
    }
    static class FabricaPerro extends FabricaExpediente { Expediente crear() { return new Perro(); } }
    static class FabricaGato extends FabricaExpediente { Expediente crear() { return new Gato(); } }
    static class FabricaFauna extends FabricaExpediente { Expediente crear() { return new FaunaSilvestre(); } }
    static class CasoRescate {
        final int id;
        final Expediente expediente;
        String estado = "Rescatado";
        CasoRescate(int id, Expediente expediente) { this.id = id; this.expediente = expediente; }
    }
    static class GestorCasos {
        private static final GestorCasos INSTANCIA = new GestorCasos();
        private final List<CasoRescate> casos = new ArrayList<>();
        private final List<Observador> observadores = new ArrayList<>();
        private GestorCasos() {}
        static GestorCasos obtenerInstancia() { return INSTANCIA; }
        void suscribir(Observador observador) { observadores.add(observador); }
        CasoRescate registrar(Expediente expediente) {
            CasoRescate caso = new CasoRescate(casos.size() + 1, expediente);
            casos.add(caso);
            for (Observador observador : observadores) observador.actualizar(caso);
            return caso;
        }
        List<CasoRescate> consultar() { return Collections.unmodifiableList(casos); }
    }
    static class VistaConsola {
        void mostrar(CasoRescate caso) {
            System.out.println("Caso #" + caso.id + " | " + caso.expediente.tipo() + " | " + caso.estado);
        }
    }
    static class ControladorRescate {
        private final GestorCasos modelo;
        private final VistaConsola vista;
        ControladorRescate(GestorCasos modelo, VistaConsola vista) { this.modelo = modelo; this.vista = vista; }
        void nuevoRescate(FabricaExpediente fabrica) { vista.mostrar(modelo.registrar(fabrica.crear())); }
    }
    public static void main(String[] args) {
        GestorCasos gestor = GestorCasos.obtenerInstancia();
        gestor.suscribir(new Voluntario("Ana"));
        gestor.suscribir(new Voluntario("Luis"));
        ControladorRescate controlador = new ControladorRescate(gestor, new VistaConsola());
        controlador.nuevoRescate(new FabricaPerro());
        controlador.nuevoRescate(new FabricaGato());
        controlador.nuevoRescate(new FabricaFauna());
        System.out.println("Total de casos: " + gestor.consultar().size());
    }
}
