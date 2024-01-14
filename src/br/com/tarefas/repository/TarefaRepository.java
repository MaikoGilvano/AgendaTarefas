package br.com.tarefas.repository;

import java.util.ArrayList;
import java.util.List;
import javax.management.RuntimeErrorException;

import br.com.tarefas.model.Tarefa;

public class TarefaRepository {
	private List<Tarefa> listaTarefas;

	public TarefaRepository() {
		listaTarefas = new ArrayList<Tarefa>();
	}

	public void cadastraTarefa(String t, String d) {
		Tarefa tarefa = new Tarefa();
		tarefa.setTarefa(t);
		tarefa.setDate(d);
		listaTarefas.add(tarefa);
	}

	public Tarefa buscaLista(int index) {
		int tamanho = listaTarefas.size();
		if (index >= 0 && index <= tamanho) {
			return listaTarefas.get(index);
		}		
		throw new RuntimeErrorException(null,"Não foi encontrado o item!");
	}

	/*public Boolean tarefaExiste(int index) {
		if (listaTarefas.size() > 0 && listaTarefas.size() <= index) {
			return true;
		}
		return false;
	}*/

	public List<Tarefa> showLista() {
		return listaTarefas;
	}

	public void deletaTarefa(int index) {
		if (index >= 0 && index <= listaTarefas.size()) {			
			listaTarefas.remove(index);	
		}else {
			throw new RuntimeErrorException(null,"Falha na exclusão!");
		}
	}

	public void editaTarefa(int index, String tarefaEditada, String dataEditada) {
		Tarefa tarefaEdit = new Tarefa();
		tarefaEdit.setTarefa(tarefaEditada);
		tarefaEdit.setDate(dataEditada);
	    listaTarefas.set(index, tarefaEdit);	   
	}
}
