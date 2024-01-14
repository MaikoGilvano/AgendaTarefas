package br.com.tarefas.model;

public class Tarefa {

	private String tarefa;
	private String dataConclusao;

	public Tarefa() {

	}

	public void setTarefa(String tarefa) {		
		this.tarefa = tarefa;	
	}

	public void setDate(String data) {
		this.dataConclusao = data;
	}

	public String getTarefas() {
		return tarefa;
	}

	public String getData() {
		return dataConclusao;
	}
		
}
