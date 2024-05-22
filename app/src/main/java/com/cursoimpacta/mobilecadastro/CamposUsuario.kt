package com.cursoimpacta.mobilecadastro

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import kotlinx.coroutines.launch
import retrofit2.Response

//@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CamposUsuario() {
    val api = remember { ViaCepService.create() }
    val coroutineScope = rememberCoroutineScope()
    var dados by remember {
        mutableStateOf(DadosPessoais("", "", "", "9", "", ""))
    }

    var infoCep by remember {
        mutableStateOf(Endereco("", "", "", "", "", false))
    }

    var msgStatus by remember {
        mutableStateOf("")
    }

    val colorWhite = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        disabledContainerColor = Color.White,
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .width(280.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        TextField(
            value = dados.nome,
            { dados = dados.copy(nome = it) },
            Modifier.clip(RoundedCornerShape(8.dp)),
            label = { Text(text = "Nome") },

            colors = colorWhite
        )
        if (dados.nome.isNotEmpty()) {
            Text(
                text = validaNome(dados.nome), color = Color.Red, modifier = Modifier
                    .align(Alignment.Start)
            )
        }

        TextField(
            value = dados.email,
            onValueChange = { dados = dados.copy(email = it) },
            label = { Text(text = "Email") },

            modifier = Modifier.clip(RoundedCornerShape(8.dp)),
            colors = colorWhite
        )
        if (dados.email.isNotEmpty()) {
            Text(
                text = validaEmail(dados.email), color = Color.Red, modifier = Modifier
                    .align(Alignment.Start)
            )
        }

        Text(
            text = "Endereço:",
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.Start)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            TextField(value = dados.cep, onValueChange = {
                if (it.isDigitsOnly()) {
                    dados = dados.copy(cep = it)
                }
            }, label = { Text(text = "CEP") },
                modifier = Modifier
                    .width(195.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = colorWhite,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(
                onClick = {
                    if (dados.cep.length == 8) {
                        coroutineScope.launch {
                            msgStatus = "Aguarde..."
                            lateinit var response: Response<Endereco>
                            response = api.procuraCep(dados.cep)
                            if (response.isSuccessful && !response.body()!!.erro) {
                                infoCep = response.body()!!
                                msgStatus = ""
                            } else {
                                infoCep = Endereco("", "", "", "", "", false)
                                msgStatus = "CEP inválido!"
                            }
                        }
                    }

                }, modifier = Modifier
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF809CBE))
            ) {
                Text(text = "⌕", fontSize = 26.sp)
            }
        }
        if (msgStatus != "") {
            Text(text = msgStatus)
        }



        TextField(
            value = infoCep.logradouro,
            onValueChange = {
                infoCep = infoCep.copy(logradouro = it)
            },
            label = {
                Text(
                    text = "Logradouro"
                )
            },
            modifier = Modifier.clip(RoundedCornerShape(8.dp)),
            colors = colorWhite
        )

        TextField(
            value = infoCep.bairro,
            onValueChange = { infoCep = infoCep.copy(bairro = it) },
            label = {
                Text(
                    text = "Bairro"
                )
            },
            modifier = Modifier.clip(RoundedCornerShape(8.dp)),
            colors = colorWhite
        )
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            TextField(
                value = dados.numero,
                onValueChange = {
                    if (it.isDigitsOnly()) {
                        dados = dados.copy(numero = it)
                    }
                },
                label = {
                    Text(
                        text = "Número"
                    )
                },
                modifier = Modifier
                    .width(110.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = colorWhite,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            TextField(
                value = dados.complemento,
                onValueChange = { dados = dados.copy(complemento = it) },
                label = {
                    Text(
                        text = "Complemento"
                    )
                },
                modifier = Modifier
                    .width(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = colorWhite,

                )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            TextField(
                value = infoCep.localidade,
                onValueChange = { infoCep = infoCep.copy(localidade = it) },
                label = {
                    Text(
                        text = "Cidade"
                    )
                },
                modifier = Modifier
                    .width(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = colorWhite
            )
            TextField(
                value = infoCep.uf,
                onValueChange = { infoCep = infoCep.copy(uf = it) },
                label = {
                    Text(
                        text = "Estado"
                    )
                },
                modifier = Modifier
                    .width(110.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = colorWhite
            )
        }
        Text(text = "Contato:", modifier = Modifier.align(Alignment.Start), fontSize = 18.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            TextField(
                value = infoCep.ddd,
                onValueChange = {
                    if (it.isDigitsOnly()) {
                        infoCep = infoCep.copy(ddd = it)
                    }
                },
                label = { Text(text = "DDD") },
                modifier = Modifier
                    .width(70.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = colorWhite,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            TextField(
                value = dados.telefone,
                onValueChange = {
                    if (it.isDigitsOnly()) {
                        dados = dados.copy(telefone = it)
                    }
                },
                label = { Text(text = "Telefone") },
                modifier = Modifier
                    .width(190.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = colorWhite,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        if (dados.telefone.isNotEmpty()) {
            Text(
                text = validaTelefone(infoCep.ddd, dados.telefone),
                modifier = Modifier.align(Alignment.Start),
                color = Color.Red,
            )
        }
        Button(
            onClick = {
                getDados(dados, infoCep)
            },
            modifier = Modifier
                .width(280.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF809CBE))
        ) {
            Text(text = "CADASTRAR")
        }
    }
}
fun getDados(/*db: Database, */dados: DadosPessoais, infoCep: Endereco){
    println(dados.nome)
    println(infoCep.bairro)
//    db.addUser(dados, infoCep)
}
