package com.example.condorapp.data.injection

/**
 * Configuración centralizada para conectar la aplicación a los emuladores locales de Firebase
 * y al backend local (Express).
 */
object EmulatorConfig {
    /**
     * Determina si se deben usar los emuladores locales de Firebase en compilaciones debug.
     * Si es false, se conectará a los servicios reales de producción de Firebase.
     */
    const val USE_EMULATOR = false

    /**
     * IP del host donde corren los emuladores de Firebase y el backend local en tu máquina de desarrollo.
     *
     * - Para pruebas en el emulador de Android (AVD) de forma local: usa "10.0.2.2"
     * - Para pruebas en un dispositivo físico conectado a la misma red Wi-Fi: usa la IP de tu PC, ej: "10.195.42.20"
     */
    const val HOST_IP = "192.168.1.20"
}
