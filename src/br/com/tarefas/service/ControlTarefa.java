package br.com.tarefas.service;

import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.management.RuntimeErrorException;

import br.com.tarefas.model.Tarefa;
import br.com.tarefas.repository.TarefaRepository;

public class ControlTarefa {

	private TarefaRepository repositorioTarefas;

	public ControlTarefa() {
		repositorioTarefas = new TarefaRepository();
	}

	public void CadastroAmostral() {
		cadastraTarefas("Criar calculadoura IMC", "28/01/2024");
		cadastraTarefas("Terminar curso flluter", "08/02/2024");
	}

	private String validaTarefa(String tarefa) {
		if (!tarefa.isEmpty()) {			
			return tarefa;
		}
		throw new RuntimeErrorException(null,"Descrição inválida!");		
	}

	private String validaData(String dataConclusao) {
		SimpleDateFormat formato = new SimpleDateFormat("dd/mm/yyyy");
		try {
			Date data = formato.parse(dataConclusao);			
			return formato.format(data);
		} catch (ParseException e) {			
			throw new RuntimeErrorException(null,"Data inválida!");
		}		
	}

	public void cadastraTarefas(String tarefaDescrita, String dataConclusao) {
		String  tarefaSalvar = validaTarefa(tarefaDescrita);
		String dataSalvar = validaData(dataConclusao);
		
		repositorioTarefas.cadastraTarefa( tarefaSalvar, dataSalvar);					
	}

	// busca uma tarefa na lista com base no index selecionado na tabela
	public Tarefa buscaTarefa(int index) {		
			return repositorioTarefas.buscaLista(index);
	}

	// retorna a lista de tarefas cadastrada
	public List<Tarefa> listaCadastros() {
		List<Tarefa> lt = new ArrayList<Tarefa>();
		lt = repositorioTarefas.showLista();
		if(!lt.isEmpty()) {
		   return lt;
		}
		throw new RuntimeErrorException(null,"Lista de tarefas vazia");
	}

	// edita um valor selecionado
	public void editaTarefa(int index, String tarefaEditada, String dataEditada) {
		String tarefaSalvar = validaTarefa(tarefaEditada);
		String dataSalvar = validaData(dataEditada);
		if (tarefaSalvar != null && dataSalvar != null) {			
			repositorioTarefas.editaTarefa(index, tarefaSalvar, dataSalvar);						
		}else {		
		   throw new RuntimeErrorException(null,"Preencha a Tarefa e a Data corretamente!");
		}
	}

	public void deletaTarefa(int index) {
		repositorioTarefas.deletaTarefa(index);
	}
}
