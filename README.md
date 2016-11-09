# Lenguaje TAQ

### Repertorio de instrucciones
| Instruccion |                      Descripcion                       |              Argumentos             | Uso        |
|-------------|:------------------------------------------------------:|:-----------------------------------:|-----------:|
| **var**     | Comienza la definicion de variables                    | -                                   | var:       |
| **endvar**  | Finaliza la definicion de variables                    | -                                   | endvar     |
| **programa**| Indica el comienzo de las instrucciones                | -                                   | programa:  |
| **end**     | Indica la finalizacion del programa                    | -                                   | end        |
| **LOAD**    | Lee un dato en memoria                                 | El nombre de la variable a utilizar | LOAD VAR1  |
| **STORE**   | Almacena un dato en la memoria                         | El nombre de la variable a utilizar | STORE VAR2 |
| **ADD**     | Suma un numero con el numero almacenado en el registro | Variable a sumar                    | ADD VAR1   |
| **AND**     | Operacion AND entre el registro y el parametro         | Variable a utilizar                 | AND VAR2   |
| **SUB**     | Resta un numero con el almacenado en el registro       | Variable a restar                   | SUB VAR1   |
| **JZ**      | Salta si el valor del registro es zero                 | Direccion a la que se quiere saltar | JZ salta   |
| **JUMP**    | Salta incodicionalmente                                | Direccion a la que se quiere saltar | JUMP salta |
| **NOP**     | Instruccion NOP, no hace nada por un pulso de reloj    | -                                   | NOP        |
| **HALT**    | Halt al programa                                       | -                                   | HALT       |

