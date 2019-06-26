package br.edu.utfpr.servico;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.dto.ClienteDTO;
import io.micrometer.core.ipc.http.HttpSender.Response;

@RestController
public class ServicoCliente {

    private List<ClienteDTO> clientes;

    public ServicoCliente() {
        clientes = Stream.of(
            ClienteDTO.builder().id(1).nome("Pedro").idade(15).telefone("+55 11 99999-8888").limiteCredito(200).pais(PaisDTO.builder().id(1).nome("Brasil").sigla("BR").codigoTelefone(55).build()).build(),
            ClienteDTO.builder().id(1).nome("Marcos").idade(17).telefone("+55 11 97777-8888").limiteCredito(600).pais(PaisDTO.builder().id(1).nome("Portugal").sigla("PT").codigoTelefone(351).build()).build()
        ).collect(Collectors.toList());
    }

    @GetMapping ("/servico/cliente")
    public ResponseEntity<List<ClienteDTO>> listar() {
        return ResponseEntity.ok(clientes);
    }

    @GetMapping ("/servico/cliente/{id}")
    public ResponseEntity<ClienteDTO> listarPorId(@PathVariable int id) {
        Optional<ClienteDTO> clienteEncontrado = clientes.stream().filter(p -> p.getId() == id).findAny();

        return ResponseEntity.of(clienteEncontrado);
    }

    @PostMapping ("/servico/cliente")
    public ResponseEntity<ClienteDTO> criar (@RequestBody ClienteDTO cliente) {

        cliente.setId(clientes.size() + 1);
        clientes.add(cliente);

        return ResponseEntity.status(201).body(cliente);
    }

    @DeleteMapping ("/servico/cliente/{id}")
    public ResponseEntity excluir (@PathVariable int id) {
        
        if (clientes.removeIf(cliente -> cliente.getId() == id))
            return ResponseEntity.noContent().build();

        else
            return ResponseEntity.notFound().build();
    }

    @PutMapping ("/servico/cliente/{id}")
    public ResponseEntity<ClienteDTO> alterar (@PathVariable int id, @RequestBody ClienteDTO cliente) {
        Optional<ClienteDTO> clienteExistente = clientes.stream().filter(c -> c.getId() == id).findAny();

        clienteExistente.ifPresent(c -> {
            c.setNome(cliente.getNome());
            c.setIdade(cliente.getIdade());
            c.setTelefone(cliente.getTelefone());
            c.setLimiteCredito(cliente.getLimiteCredito());
            c.setPais(cliente.getPais());
        });

        return ResponseEntity.of(clienteExistente);
    }
}