# Lenguaje TAQ
> El lenguaje ensamblador es un lenguaje de programación de bajo nivel que simula las instrucciones básicas para los procesadores. Como cada arquitectura de procesador tiene sus propias caractersticas que la definen, un lenguaje ensamblador es, por lo tanto, específico de cada tipo de arquitectura de computador física (o virtual).

**TAQ** es un lenguaje ensamblador basado en un procesador sencillo cuya arquitectura cuenta con un registro de direcciones a memoria, un registro que cumple la funcion de acumulador y un registro de salida de datos a memoria.
El unico registro visible al programador del lenguaje TAQ es el acumulador. Se utiliza como almacenamiento y/o operando de ciertas instrucciones.

### Repertorio de instrucciones
| Instruccion |                      Descripcion                                |              Argumentos             | Uso                   |
|:-----------:|:---------------------------------------------------------------:|:-----------------------------------:|:----------------------|
| **var**     | Comienza la definicion de variables                             | -                                   | var:                  |
| **endvar**  | Finaliza la definicion de variables                             | -                                   | endvar                |
| **programa**| Indica el comienzo de las instrucciones                         | -                                   | programa:             |
| **end**     | Indica la finalizacion del programa                             | -                                   | end                   |
| **etiqueta**| Guarda una posicion de programa a la cual luego se puede saltar.| -                                   | etiqueta: instruccion |
| **LOAD**    | Carga un dato de memoria al acumulador                          | El nombre de la variable a utilizar | LOAD VAR1             |
| **STORE**   | Almacena el dato del aculumador en memoria                      | El nombre de la variable a utilizar | STORE VAR2            |
| **ADD**     | Le suma el operando al acumulador                               | Variable a sumar                    | ADD VAR1              |
| **AND**     | Operacion AND entre el acumulador y el operando                 | Variable a utilizar                 | AND VAR2              |
| **SUB**     | Le resta el operando al acumulador                              | Variable a restar                   | SUB VAR1              |
| **JZ**      | Salta si el valor del acumulador es zero                        | Direccion a la que se quiere saltar | JZ salta              |
| **JUMP**    | Salta incodicionalmente                                         | Direccion a la que se quiere saltar | JUMP salta            |
| **NOP**     | Instruccion NOP, no hace nada por un ciclo de cpu               | -                                   | NOP                   |
| **HALT**    | Halt al programa                                                | -                                   | HALT                  |

### Compilador
El compilador interpreta el lenguaje TAQ para convertir las instrucciones en su representación simbólica de los códigos de máquina binarios y otras constantes necesarias para programar la arquitectura de procesador sencillo.
La memoria utiliza por el compilador consta de 256 celdas de 16 bits, usadas para el almacenamineto de las instrucciones, direcciones y operandos(los cuales son de representacion binaria sin signo de 16 bits).
Las variables son apiladas comenzando en la ultima direccion de la memoria, mientras que las instrucciones del programa siguen una secuencia incremental desde la celda cero. Como podemos observar en el siguiente ejemplo:
```
# Un programa que suma dos numeros:
var:
N1: 3                   0000000000000011 -- Celda 255
N2: 2                   0000000000000010 -- Celda 254
N3: 0                   0000000000000000 -- Celda 253
endvar
programa:
LOAD N1                 0000001011111111 -- Celda 0
ADD N2                  0000001111111110 -- Celda 1
STORE N3                0000000111111101 -- Celda 2
HALT                    0000100000000000 -- Celda 3
end
```

```
# Ejemplo de uso de etiquetas, se realiza una multiplicacion entre N1 y N2, cuyo resultado se almacena en N3:
var:
N1: 3
N2: 2
N3: 0
N4: 1
endvar
programa:
LOAD N2
JZ ld
LOAD N1
JZ ld
LOAD N3
dd: ADD N1
STORE N3
LOAD N2
SUB N4
STORE N2
JZ ld
LOAD N3
JUMP dd
ld: LOAD N3
HALT
end
```
#### Extras
El codigo fuente del programa se considera texto plano, cuando se lo pasa por el compilador se genera un archivo `.taq` con las 256 lineas de 16 bits correspondientes a las celdas de la memoria.

Se debe poner un `HALT` que indique la finalizacion de ejecucion del programa, ya que sino el procesador sencillo continuara buscando nuevas instrucciones.

Se recomienda utilizar la siguiente convencion con respecto a las etiquetas:
```
etiqueta: instruccion -- Recomendado
etiqueta:instruccion -- No recomendado
```

# Acerca de este proyecto
Este proyecto se llevo a cabo gracias a la motivacion del proyecto final de la materia Taller de Arquitectura, dictada por Horacio Villagarcia, de la Facultad de Informatica de la Universidad Nacional de La Plata.

# License
OMEGA LICENSE
