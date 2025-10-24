#!/bin/bash

# Script para análisis de rendimiento con hilos - Optimizado para 8 núcleos y 12GB RAM
# Uso: ./test_performance_optimized.sh

echo "=== ANÁLISIS DE RENDIMIENTO CON HILOS (8 núcleos, 12GB RAM) ==="
echo "Detectando configuración del sistema..."

# Detectar número de núcleos
NUM_CORES=$(nproc)
echo "Núcleos detectados: $NUM_CORES"

# Detectar memoria disponible (en GB)
MEM_GB=$(free -g | awk '/^Mem:/{print $2}')
echo "Memoria total: ${MEM_GB}GB"

# Parámetros optimizados para tu sistema
# Con 8 núcleos y 12GB, podemos manejar listas más grandes
# Objetivo: ~1 minuto con 1 thread, optimizar hasta 9 threads (n+1)

# Para alcanzar ~1 minuto con 1 thread, necesitamos calibrar
# Comencemos con valores que generen suficiente carga de trabajo
CURPS_POR_LISTA=10000  # n (reducido para evitar usar toda la RAM)
NUM_LISTAS=300         # m (ajustado para balance memoria/CPU)

echo "Configuración de prueba:"
echo "- $NUM_LISTAS listas con $CURPS_POR_LISTA CURPs cada una"
echo "- Total CURPs: $((NUM_LISTAS * CURPS_POR_LISTA))"
echo "- Memoria estimada: ~$((NUM_LISTAS * CURPS_POR_LISTA * 18 / 1000000))MB"

echo ""
echo "Compilando código..."
javac Main2Hilos2.java

if [ $? -ne 0 ]; then
    echo "Error en la compilación"
    exit 1
fi

echo ""
echo "========== TABLA DE RESULTADOS =========="
echo "| Threads | Tiempo (s) | Speedup | Eficiencia | CPU/Thread |"
echo "|---------|------------|---------|------------|------------|"

# Variable para almacenar el tiempo con 1 thread (referencia)
tiempo_1_thread=0

# Probar con 1 hasta n+1 threads (1 a 9 en tu caso)
for threads in $(seq 1 $((NUM_CORES + 1))); do
    echo -n "| $threads       |"
    
    # Ejecutar 3 veces y tomar el mejor tiempo
    mejor_tiempo=999999
    
    for run in {1..3}; do
        # Usar time para medir el tiempo real de ejecución
        # Timeout de 300 segundos (5 minutos) para casos extremos
        tiempo_output=$(timeout 300s /usr/bin/time -f "%e" java Main2Hilos2 $CURPS_POR_LISTA $NUM_LISTAS $threads 2>&1)
        exit_code=$?
        
        # Verificar si timeout ocurrió
        if [ $exit_code -eq 124 ]; then
            mejor_tiempo="TIMEOUT"
            break
        elif [ $exit_code -ne 0 ]; then
            continue  # Error en ejecución, intentar de nuevo
        fi
        
        # Extraer el tiempo de la última línea (time output)
        tiempo=$(echo "$tiempo_output" | grep -o '[0-9]*\.[0-9]*$' | tail -1)
        
        if [ ! -z "$tiempo" ] && (( $(echo "$tiempo < $mejor_tiempo" | bc -l 2>/dev/null || echo "0") )); then
            mejor_tiempo=$tiempo
        fi
    done
    
    if [ "$mejor_tiempo" = "999999" ] || [ "$mejor_tiempo" = "TIMEOUT" ]; then
        echo " TIMEOUT   | N/A     | N/A        | N/A        |"
    else
        # Calcular speedup y eficiencia
        if [ $threads -eq 1 ]; then
            tiempo_1_thread=$mejor_tiempo
            speedup="1.00"
            eficiencia="1.00"
            cpu_por_thread="100.0"
        else
            if (( $(echo "$tiempo_1_thread > 0" | bc -l 2>/dev/null || echo "0") )); then
                speedup=$(echo "scale=2; $tiempo_1_thread / $mejor_tiempo" | bc -l 2>/dev/null || echo "N/A")
                eficiencia=$(echo "scale=2; $speedup / $threads" | bc -l 2>/dev/null || echo "N/A")
                cpu_por_thread=$(echo "scale=1; 100.0 * $eficiencia" | bc -l 2>/dev/null || echo "N/A")
            else
                speedup="N/A"
                eficiencia="N/A"
                cpu_por_thread="N/A"
            fi
        fi
        
        printf " %-8.2f | %-7s | %-10s | %-8s%% |\n" "$mejor_tiempo" "$speedup" "$eficiencia" "$cpu_por_thread"
    fi
    
    # Pequeña pausa para evitar sobrecarga del sistema
    sleep 2
done

echo ""
echo "========== ANÁLISIS Y RECOMENDACIONES =========="
echo "Configuración del sistema:"
echo "- CPU: $NUM_CORES núcleos"
echo "- RAM: ${MEM_GB}GB"
echo "- Configuración de prueba: $NUM_LISTAS listas x $CURPS_POR_LISTA CURPs"
echo ""
echo "Métricas:"
echo "- Speedup = Tiempo_1_thread / Tiempo_N_threads (ideal = N)"
echo "- Eficiencia = Speedup / N_threads (ideal = 1.0)"
echo "- CPU/Thread = Eficiencia * 100% (ideal = 100%)"
echo ""
echo "Interpretación:"
echo "- Speedup > 1: Beneficio del paralelismo"
echo "- Eficiencia > 0.7: Buen aprovechamiento de threads"
echo "- CPU/Thread cerca de 100%: Thread bien aprovechado"
echo ""
echo "Para monitorear CPU en tiempo real, usar:"
echo "  htop"
echo "  o"
echo "  watch -n 1 'cat /proc/loadavg'"
