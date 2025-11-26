Práctica 6 – Sensores y Autenticación Biométrica en Android

Desarrollo de Aplicaciones Móviles Nativas – ESCOM / IPN

Este proyecto implementa una aplicación Android que combina sensores del dispositivo, autenticación biométrica y un pequeño gestor de archivos protegido, siguiendo las instrucciones de la práctica 6.

La app integra:

Lectura de sensores (proximidad y luz)

Autenticación biométrica (huella digital o patrón/PIN del sistema)

Acceso seguro a carpetas usando el Storage Access Framework

Temas personalizables (Guinda IPN / Azul ESCOM)

Modo oscuro seleccionable mediante un switch manual

El objetivo es mostrar cómo aprovechar el hardware del dispositivo de una forma funcional, segura y con una interfaz personalizable.

Características principales
1. Autenticación biométrica para acceder a carpetas protegidas

Antes de acceder al gestor de archivos, se solicita autenticación mediante:

Huella digital

Reconocimiento facial (si el dispositivo lo soporta)

Patrón o PIN del sistema

Esto se implementa mediante la API moderna BiometricPrompt, lo que garantiza seguridad y compatibilidad con Android actual.

Una vez autenticado, el usuario puede navegar carpetas reales del dispositivo a través del Storage Access Framework, sin permisos invasivos.

2. Sensores del dispositivo con visualización en tiempo real
Sensor de proximidad

Detecta si un objeto está cerca del dispositivo.

Muestra la lectura en centímetros.

Sensor de luz ambiental

Detecta el nivel de iluminación.

Muestra la lectura en lux.

Cada sensor incluye un switch independiente para activarlo o desactivarlo según sea necesario.

Los sensores solo se registran cuando están activos y se desregistran en onPause(), optimizando el consumo de energía.

3. Temas personalizables (IPN / ESCOM)

El usuario puede seleccionar entre dos temas basados en los colores institucionales:

Tema Guinda (IPN)

Tema Azul (ESCOM)

Los colores afectan el fondo, botones y textos principales.
La elección se guarda con SharedPreferences para mantenerse entre reinicios.

4. Modo oscuro manual

La aplicación incluye un switch que permite activar un modo oscuro diseñado especialmente para combinar con cada tema.

En modo oscuro + Guinda → los textos se muestran con un tono rosado.

En modo oscuro + Azul → los textos se muestran en tonos azules.

Esta combinación genera una apariencia visual más coherente con el tema seleccionado.

5. Gestión eficiente del uso de batería

Los sensores se activan únicamente bajo demanda del usuario.




Tecnologías y APIs utilizadas

Kotlin

AndroidX Biometric (BiometricPrompt)

SensorManager y SensorEventListener

Storage Access Framework (SAF)

SharedPreferences

ConstraintLayout

Material Components

Instalación y ejecución

Clonar el repositorio:

https://github.com/JaredFs123456/practica6.git


Abrir el proyecto en Android Studio (Giraffe o superior).

Ejecutar en un dispositivo físico o emulador con Android 7.0 o superior.
Si la actividad se pausa, los sensores se desregistran para evitar consumo innecesario.

La navegación de carpetas utiliza el Storage Access Framework, que no requiere permisos costosos ni procesamiento extra.
