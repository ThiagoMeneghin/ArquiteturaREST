package br.edu.utfpr.apresentacao;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClienteController {

    @GetMapping("/cliente")
    public String inicial(Model data) throws JsonSyntaxException, UnirestException {

        ClienteModel arrayClientes[] = new Gson()
                    .fromJson(
                        Unirest
                            .get("http://localhost:8081/servico/cliente")
                            .asJson()
                            .getBody()
                            .toString(), 
                        ClienteModel[].class
                    );

        data.addAttribute("clientes", arrayClientes);

        return "cliente-view";
    }

    @PostMapping ("/cliente/criar")
    public String criar(ClienteModel cliente) throws UnirestException {

            Unirest.post("http://localhost:8081/servico/cliente")
                .header("Content-type", "application/json")
                .header("accept", "application/json")
                .body(new Gson().toJson(cliente, ClienteModel.class))
                .asJson();

        return "redirect:/cliente";
    }

    @GetMapping ("/cliente/excluir")
    public String excluir (@RequestParam int id) throws UnirestException {

        Unirest
            .delete("http://localhost:8081/servico/cliente/{id}")
            .routeParam("id", String.valueOf(id))
            .asJson();

        return "redirect:/cliente";
    }

    @GetMapping ("/cliente/prepara-alterar")
    public String preparaAlterar (@RequestParam int id, Model data) throws JsonSyntaxException, UnirestException {

        ClienteModel clienteExistente = new Gson()
            .fromJson(
                Unirest
                    .get("http://localhost:8081/servico/cliente/{id}")
                    .routeParam("id", String.valueOf(id))
                    .asJson()
                    .getBody()
                    .toString(),
                ClienteModel.class
            );

        data.addAttribute("clienteAtual", clienteExistente);

        ClienteModel arrayClientes[] = new Gson()
        .fromJson(
            Unirest
                .get("http://localhost:8081/servico/cliente")
                .asJson()
                .getBody()
                .toString(), 
            ClienteModel[].class
        );

        data.addAttribute("clientes", arrayClientes);

        return "cliente-view-alterar";
    }

    @PostMapping ("/cliente/alterar")
    public String alterar (ClienteModel clienteAlterado) throws UnirestException {

        Unirest
            .put("http://localhost:8081/servico/cliente/{id}")
            .routeParam("id", String.valueOf(clienteAlterado.getId()))
            .header("Content-type", "application/json")
            .header("accept", "application/json")
            .body(new Gson().toJson(clienteAlterado, ClienteModel.class))
            .asJson();

        return "redirect:/cliente";
    }
}