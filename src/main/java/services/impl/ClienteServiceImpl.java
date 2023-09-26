package services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.Cliente;
import model.ClienteRepository;
import model.Endereco;
import model.EnderecoRepository;
import services.ClienteService;
import services.ViaCepService;

@Service
public class ClienteServiceImpl implements ClienteService{
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ViaCepService viaCepService;

	@Override
	public Iterable<Cliente> buscarTodos() {
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		Optional<Cliente>cliente= clienteRepository.findById(id);
		return cliente.get();
	}

	@Override
	public void inserir(Cliente cliente) {
		 clienteRepository.save(cliente);
	}

	@Override
	public void deletar(Long id) {
		 clienteRepository.deleteById(id);
	}

	@Override
	public void atualizar(Long id, Cliente cliente) {
		Optional<Cliente>clienteBd= clienteRepository.findById(id);
		
		if(clienteBd.isPresent()) {
			clienteRepository.save(cliente);
		}
	}
		
		private void salvarClienteComCep(Cliente cliente) {
			// Verificar se o Endereco do Cliente já existe (pelo CEP).
			String cep = cliente.getEndereco().getCep();
			Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
				// Caso não exista, integrar com o ViaCEP e persistir o retorno.
				Endereco novoEndereco = viaCepService.consultarCep(cep);
				enderecoRepository.save(novoEndereco);
				return novoEndereco;
			});
			cliente.setEndereco(endereco);
			// Inserir Cliente, vinculando o Endereco (novo ou existente).
			clienteRepository.save(cliente);
		
		
	}
	

}
