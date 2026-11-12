package com.example.calculadoracarlosredondoumb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Calculadora()
        }
    }
}

//funciones para las operaciones matematicas
fun suma(num1: Double, num2: Double): Double{
    return num1 + num2
}

fun resta(num1: Double, num2: Double): Double{
    return num1 - num2
}

fun producto(num1: Double, num2: Double): Double{
    return num1 * num2
}

fun division(num1: Double, num2: Double): Double{
    return num1 / num2
}


@Composable
//funcion de calculo
fun Calculadora() {


    //variable para guardar el numero actual
    var numeroActual by remember {
        mutableStateOf("0")
    }

    //variable para guardar el numero anterior
    var primerNumero by remember {
        mutableStateOf(0.0)
    }

    //variable para operaciones
    var operacion by remember {
        mutableStateOf("")
    }

    //variable para el texto del operador
    var operacionTexto by remember {
        mutableStateOf("")
    }

    //variable logica para mostrar el resultado
    var resultadoMostrado by remember {
        mutableStateOf(false)
    }

    //variable para el control de errores
    var mensajeError by remember { mutableStateOf("") }

    //variable para detener la aplicacion
    val activity = LocalActivity.current

    //variable para verificar el estado del modo nocturno
    var modoNocturno by remember { mutableStateOf(false) }

    //variable para el redondeo a tres digitos
    var redondearResultado by remember {
        mutableStateOf(false)
    }

    //variable que guarda el resultado sin redondear
    var resultadoSinRedondear by remember {
        mutableStateOf(0.0)
    }



    //variable para el cambio de modo
    val esquemaColores = if (modoNocturno) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }


    //funcion para agregar numeros a la derecha del numero actual
    fun agregarNumero(numero: String) {

        //primero revisamos el estado actual si tenemos un resultado en pantalla
        if (resultadoMostrado) {
            numeroActual = numero
            resultadoMostrado = false
        } else if (numeroActual == "0") {
            numeroActual = numero
        } else {
            numeroActual += numero
        }

        //mostramos variables como string en pantalla
        if (operacion != "") {
            operacionTexto = "$primerNumero $operacion $numeroActual"
        }
    }

    //funcion para limpiar la pantalla
    fun limpiar() {
        numeroActual = "0"
        primerNumero = 0.0
        operacion = ""
        operacionTexto = ""
        resultadoMostrado = false
        mensajeError = ""
    }


    //funcion para guardar el numero en pantalla luego reinicia su valor a cero y para seleccionar operacion
    fun seleccionarOperacion(op: String) {
        primerNumero = numeroActual.toDouble()
        operacion = op
        operacionTexto = "$primerNumero $op"
        numeroActual = "0"
        resultadoMostrado = false
    }


    //funcion encargada de llamar las funciones secundarias de operaciones matematicas
    fun calcular() {
        val segundoNumero = numeroActual.toDouble()

        //control de error
        if (operacion == "/" && segundoNumero == 0.0) {
            mensajeError = "No se puede dividir entre cero"
            return
        }

        //Operacion llamando las funciones de operaciones matematicas
        val resultado = when (operacion) {
            "+" -> suma(primerNumero, segundoNumero)
            "-" -> resta(primerNumero, segundoNumero)
            "*" -> producto(primerNumero, segundoNumero)
            "/" -> division(primerNumero, segundoNumero)
            else -> return
        }

        resultadoSinRedondear = resultado

        //muestra resultado segun checkbox
        numeroActual = if (redondearResultado) {
            String.format(
                java.util.Locale.US,
                "%.3f",
                resultado
            )
        } else {
            resultado.toString()
        }

        //limpiar operacion
        operacionTexto = ""
        operacion = ""

        //Mostrar resultado
        resultadoMostrado = true
    }


    MaterialTheme(colorScheme = esquemaColores) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                //columna global de objetos
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    //fila de opciones superiores
                    Row(
                        modifier = Modifier.padding(
                            top = 24.dp,
                            start = 24.dp
                        )
                    ) {

                        //Boton para salir
                        Button(
                            onClick = { activity?.finish() },
                        ) {
                            Text("Salir")
                        }

                        //Boton radial para el cambio de modo nocturno y diurno
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = !modoNocturno,
                                onClick = { modoNocturno = false }
                            )
                            Text("Diurno")

                            RadioButton(
                                selected = modoNocturno,
                                onClick = { modoNocturno = true }
                            )
                            Text("Nocturno")
                        }
                    }


                    //Columna para organizar el cuadro de texto y el teclado
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        //texto para mostrar la operacion actual o el numero resultado de la operacion
                        Text(
                            text = if (operacionTexto != "") operacionTexto else numeroActual,
                            style = MaterialTheme.typography.headlineLarge
                        )

                        //Mensaje de error
                        if (mensajeError != "") {
                            Text(
                                text = mensajeError,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        //Teclado de la calculadora
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                            //teclado numerico
                            Column() {
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Button(onClick = { agregarNumero("1") }) {
                                        Text("1")
                                    }

                                    Button(onClick = { agregarNumero("2") }) {
                                        Text("2")
                                    }

                                    Button(onClick = { agregarNumero("3") }) {
                                        Text("3")
                                    }

                                }


                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Button(onClick = { agregarNumero("4") }) {
                                        Text("4")
                                    }

                                    Button(onClick = { agregarNumero("5") }) {
                                        Text("5")
                                    }

                                    Button(onClick = { agregarNumero("6") }) {
                                        Text("6")
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Button(onClick = { agregarNumero("7") }) {
                                        Text("7")
                                    }

                                    Button(onClick = { agregarNumero("8") }) {
                                        Text("8")
                                    }

                                    Button(onClick = { agregarNumero("9") }) {
                                        Text("9")
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Button(onClick = { calcular() }) {
                                        Text("=")
                                    }

                                    Button(onClick = { agregarNumero("0") }) {
                                        Text("0")
                                    }
                                    Button(onClick = { limpiar() }) {
                                        Text("C")
                                    }

                                }
                            }

                            //seccion de operaciones del teclado
                            Column() {
                                Button(onClick = {
                                    seleccionarOperacion("+")
                                }) {
                                    Text("+")
                                }

                                Button(onClick = {
                                    seleccionarOperacion("-")
                                }) {
                                    Text("-")
                                }

                                Button(onClick = {
                                    seleccionarOperacion("*")
                                }) {
                                    Text("×")
                                }

                                Button(onClick = {
                                    seleccionarOperacion("/")
                                }) {
                                    Text("÷")
                                }
                            }
                        }

                        //checkbox que cambia el resultado segun su estado redondeando a 3 decimaless
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = redondearResultado,
                                onCheckedChange = { nuevoEstado ->

                                    redondearResultado = nuevoEstado

                                    if (resultadoMostrado) {
                                        //numero con formato
                                        if (nuevoEstado) {
                                            numeroActual = String.format(
                                                java.util.Locale.US,
                                                "%.3f",
                                                resultadoSinRedondear
                                            )
                                        //numero sin formato
                                        } else {
                                            numeroActual = resultadoSinRedondear.toString()
                                        }
                                    }
                                }
                            )

                            Text("Redondear a 3 decimales")
                        }
                    }
                }
            }
        }
    }
}
