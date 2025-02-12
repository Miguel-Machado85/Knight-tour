import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.*;

public class ClosedKnightsTour {

    public static boolean resolverCaballo(int[][] tablero, int fila, int columna, int contador, int inicioX, int inicioY) {
        int[] movimientoX = {2, 1, -1, -2, -2, -1, 1, 2};
        int[] movimientoY = {1, 2, 2, 1, -1, -2, -2, -1};

        // Si ya hemos recorrido todas las casillas
        if (contador == 64) { // Todas las 64 casillas han sido visitadas
            // Verificamos si el caballo puede volver a la casilla inicial
            for (int i = 0; i < movimientoX.length; i++) {
                int nuevoX = fila + movimientoX[i];
                int nuevoY = columna + movimientoY[i];
                if (nuevoX == inicioX && nuevoY == inicioY) {
                    return true; // Si puede volver a la casilla de inicio
                }
            }
            return false; // No es un tour cerrado
        }

        // Ordenar movimientos posibles por la cantidad de opciones futuras (heurística de Warnsdorff)
        List<int[]> movimientosOrdenados = ordenarMovimientos(tablero, fila, columna, movimientoX, movimientoY);

        for (int[] movimiento : movimientosOrdenados) {
            int nuevoX = movimiento[0];
            int nuevoY = movimiento[1];

            if (tablero[nuevoX][nuevoY] == 0) { // Si no ha sido visitado
                tablero[nuevoX][nuevoY] = contador + 1; // Marcar casilla visitada
                if (resolverCaballo(tablero, nuevoX, nuevoY, contador + 1, inicioX, inicioY)) {
                    return true;
                } else {
                    // Si no se encuentra una solución, desmarcamos la casilla
                    tablero[nuevoX][nuevoY] = 0;
                }
            }
        }
        return false;
    }

    // Heurística de Warnsdorff: Ordenar los movimientos en función del número de movimientos posibles desde la siguiente posición
    public static List<int[]> ordenarMovimientos(int[][] tablero, int fila, int columna, int[] movimientoX, int[] movimientoY) {
        // PriorityQueue con un comparador que ordena según el grado (el tercer valor en el array)
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return Integer.compare(a[2], b[2]); // Comparar por el tercer elemento, que es el grado
            }
        });

        // Recorremos los movimientos posibles
        for (int i = 0; i < movimientoX.length; i++) {
            int nuevoX = fila + movimientoX[i];
            int nuevoY = columna + movimientoY[i];
            // Verificamos si el movimiento es dentro del tablero y la celda está libre
            if (nuevoX >= 0 && nuevoY >= 0 && nuevoX < 8 && nuevoY < 8 && tablero[nuevoX][nuevoY] == 0) {
                int grado = calcularGrado(tablero, nuevoX, nuevoY, movimientoX, movimientoY);
                // Agregar el movimiento con su grado a la PriorityQueue
                pq.add(new int[]{nuevoX, nuevoY, grado});
            }
        }

        // Extraer todos los movimientos ordenados de la PriorityQueue
        List<int[]> movimientosOrdenados = new ArrayList<>();
        while (!pq.isEmpty()) {
            movimientosOrdenados.add(pq.poll());
        }

        return movimientosOrdenados; // Retorna la lista de movimientos ordenados
    }

    // Método auxiliar para calcular el grado (esto ya deberías tenerlo implementado)
    public static int calcularGrado(int[][] tablero, int x, int y, int[] movimientoX, int[] movimientoY) {
        // Implementación del cálculo del grado para los movimientos
        int grado = 0;
        for (int i = 0; i < movimientoX.length; i++) {
            int nuevoX = x + movimientoX[i];
            int nuevoY = y + movimientoY[i];
            if (nuevoX >= 0 && nuevoY >= 0 && nuevoX < 8 && nuevoY < 8 && tablero[nuevoX][nuevoY] == 0) {
                grado++;
            }
        }
        return grado;
    }

    public static void main(String[] args) {
        int[][] tablero = new int[8][8];
        int inicioX =4, inicioY = 0; // Posición inicial del caballo

        // Inicializar el tablero a 0
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                tablero[i][j] = 0;
            }
        }

        // Colocar el caballo en la posición inicial
        tablero[inicioX][inicioY] = 1; // El primer movimiento

        // Intentar resolver el problema
        if (resolverCaballo(tablero, inicioX, inicioY, 1, inicioX, inicioY)) {
            // Mostrar el tablero si se encuentra una solución
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    System.out.printf(" %2d ", tablero[i][j]);
                }
                System.out.println();
            }
        } else {
            System.out.println("No se encontró una solución.");
        }
    }
}
