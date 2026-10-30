import java.util.Scanner;
import java.util.UUID;

public class TiendaVideojuegos {

    // Producto: interfaz común. Es el "Producto" del Factory Method y el "Componente" del Decorator.
    interface Producto {
        String descripcion();
        double precio();
        String licencia();
    }

    static abstract class JuegoDigital implements Producto {
        protected final String titulo;
        protected final double precio;

        JuegoDigital(String titulo, double precio) {
            this.titulo = titulo;
            this.precio = precio;
        }

        public double precio() { return precio; }

        protected String codigo() {
            return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }

    // Productos concretos (también son los "Componentes concretos" del Decorator).
    static class JuegoPC extends JuegoDigital {
        JuegoPC(String t, double p) { super(t, p); }
        public String descripcion() { return titulo + " [PC]"; }
        public String licencia() { return "Clave de activación: " + codigo(); }
    }

    static class JuegoPlayStation extends JuegoDigital {
        JuegoPlayStation(String t, double p) { super(t, p); }
        public String descripcion() { return titulo + " [PlayStation]"; }
        public String licencia() { return "Código de canje PSN: " + codigo(); }
    }

    static class JuegoXbox extends JuegoDigital {
        JuegoXbox(String t, double p) { super(t, p); }
        public String descripcion() { return titulo + " [Xbox]"; }
        public String licencia() { return "Código de canje Xbox: " + codigo(); }
    }

    /* ===== PATRÓN CREACIONAL: FACTORY METHOD =====
     * TiendaPlataforma es la clase Creadora: declara el método fábrica crearJuego().
     * Cada subclase (Creador concreto) lo sobrescribe y devuelve el producto de su
     * plataforma. El código cliente solo trabaja con TiendaPlataforma, sin conocer
     * la clase concreta del juego que se crea. */
    static abstract class TiendaPlataforma {
        abstract Producto crearJuego(String titulo, double precio);
    }

    static class TiendaPC extends TiendaPlataforma {
        Producto crearJuego(String titulo, double precio) { return new JuegoPC(titulo, precio); }
    }

    static class TiendaPlayStation extends TiendaPlataforma {
        Producto crearJuego(String titulo, double precio) { return new JuegoPlayStation(titulo, precio); }
    }

    static class TiendaXbox extends TiendaPlataforma {
        Producto crearJuego(String titulo, double precio) { return new JuegoXbox(titulo, precio); }
    }

    /* ===== PATRÓN ESTRUCTURAL: DECORATOR =====
     * Extra es la Decoradora base: implementa Producto, guarda un Producto envuelto y
     * le delega todas las operaciones. Cada decorador concreto sobrescribe los métodos,
     * llama al padre (que delega) y le suma su propio comportamiento. Se pueden apilar. */
    static abstract class Extra implements Producto {
        protected final Producto envuelto;

        Extra(Producto envuelto) { this.envuelto = envuelto; }

        public String descripcion() { return envuelto.descripcion(); }
        public double precio() { return envuelto.precio(); }
        public String licencia() { return envuelto.licencia(); }
    }

    static class EdicionDeluxe extends Extra {
        EdicionDeluxe(Producto p) { super(p); }
        public String descripcion() { return super.descripcion() + " + edición deluxe"; }
        public double precio() { return super.precio() + 20; }
    }

    static class PackDLC extends Extra {
        PackDLC(Producto p) { super(p); }
        public String descripcion() { return super.descripcion() + " + DLC (pack de contenido)"; }
        public double precio() { return super.precio() + 35; }
    }

    /* ===== PATRÓN DE COMPORTAMIENTO: STRATEGY =====
     * MetodoPago es la interfaz Estrategia y cada método de pago es una Estrategia concreta
     * (un algoritmo distinto para calcular el total). Pedido es el Contexto: guarda una
     * referencia a la estrategia, la usa solo por la interfaz y permite cambiarla con
     * setMetodoPago(). El cliente (main) es quien elige y le pasa la estrategia. */
    interface MetodoPago {
        String nombre();
        double calcularTotal(double monto);
    }

    static class PagoTarjeta implements MetodoPago {
        public String nombre() { return "Tarjeta (+3% comisión)"; }
        public double calcularTotal(double monto) { return monto * 1.03; }
    }

    static class PagoPayPal implements MetodoPago {
        public String nombre() { return "PayPal (+5% comisión)"; }
        public double calcularTotal(double monto) { return monto * 1.05; }
    }

    static class Pedido {
        private final Producto producto;
        private MetodoPago metodoPago;

        Pedido(Producto producto) { this.producto = producto; }

        void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }

        void imprimirResumen() {
            System.out.println("\n--- Resumen del pedido ---");
            System.out.println("Producto : " + producto.descripcion());
            System.out.println("Subtotal : Bs " + producto.precio());
            System.out.println("Pago     : " + metodoPago.nombre());
            System.out.printf("Total    : Bs %.2f%n", metodoPago.calcularTotal(producto.precio()));
            System.out.println(producto.licencia());
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String[] titulos = {"Aventura Cósmica", "Racing Legends", "Dungeon Quest"};
        double[] precios = {250, 180, 120};

        System.out.println("=== Tienda de Videojuegos Digitales ===");
        for (int i = 0; i < titulos.length; i++) {
            System.out.println((i + 1) + ") " + titulos[i] + " - Bs " + precios[i]);
        }
        System.out.print("Elige un juego: ");
        int juego = Integer.parseInt(sc.nextLine()) - 1;

        System.out.print("Plataforma: 1) PC  2) PlayStation  3) Xbox: ");
        String plataforma = sc.nextLine();

        // Factory Method: el cliente elige el creador concreto y este crea el producto.
        TiendaPlataforma tienda;
        switch (plataforma) {
            case "1": tienda = new TiendaPC(); break;
            case "2": tienda = new TiendaPlayStation(); break;
            case "3": tienda = new TiendaXbox(); break;
            default: throw new IllegalArgumentException("Plataforma no válida");
        }
        Producto producto = tienda.crearJuego(titulos[juego], precios[juego]);

        // Decorator: cada extra envuelve al producto anterior.
        String op;
        do {
            System.out.print("Extra: 1) Deluxe  2) DLC  0) Terminar: ");
            op = sc.nextLine();
            switch (op) {
                case "1": producto = new EdicionDeluxe(producto); break;
                case "2": producto = new PackDLC(producto); break;
            }
        } while (!op.equals("0"));

        // Strategy: el cliente crea la estrategia y se la pasa al contexto (Pedido).
        Pedido pedido = new Pedido(producto);
        System.out.print("Pago: 1) Tarjeta  2) PayPal: ");
        String opPago = sc.nextLine();
        switch (opPago) {
            case "2": pedido.setMetodoPago(new PagoPayPal()); break;
            default:  pedido.setMetodoPago(new PagoTarjeta());
        }

        pedido.imprimirResumen();
        sc.close();
    }
}