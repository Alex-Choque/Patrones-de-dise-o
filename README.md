# Tienda de Videojuegos Digitales: Patrones de diseño en Java

Programa de consola que simula los pedidos en línea de una tienda de videojuegos digitales, y que demuestra un patrón **creacional**, uno **estructural** y uno de **comportamiento**.

## Caso de uso

El cliente elige un juego del catálogo, la plataforma en la que lo quiere, los extras que desea agregar y el método de pago. Al final, el programa muestra el resumen del pedido con el subtotal, el total con comisión y la licencia digital (clave de activación o código de canje, según la plataforma).

Todo está en un solo archivo: las clases e interfaces están anidadas dentro de `TiendaVideojuegos`.

## Requisitos

- JDK 17 o superior

Para comprobarlo:

```
java -version
javac -version
```

## Cómo ejecutar

Desde la carpeta del proyecto:

```
javac TiendaVideojuegos.java
java TiendaVideojuegos
```

También se puede ejecutar desde Visual Studio Code con la extensión *Extension Pack for Java*, usando el botón *Run* sobre el método `main`.
