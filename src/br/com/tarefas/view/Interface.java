package br.com.tarefas.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;

import javax.swing.JButton;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFormattedTextField;
import java.awt.Color;

import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import br.com.tarefas.model.Tarefa;
import br.com.tarefas.service.Acao;
import br.com.tarefas.service.ControlTarefa;

import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;

import javax.swing.JScrollPane;

public class Interface extends JFrame {

	private ControlTarefa controleTarefa;
	private Acao acao;
	private int indexSelected;
	private static final long serialVersionUID = 1L;

	private JTextField tf_tarefa_add;
	private JFormattedTextField tf_data_add;

	private JPanel pn_principal;
	private JPanel pn_cadastro;
	private JPanel pn_listagem;

	private JButton btn_cancelar;
	private JButton btn_confirmar;
	private JButton btn_editar;
	private JButton btn_adicionar;
	private JButton btn_excluir;

	private JLabel lbl_add_tarefa;
	private JLabel lbl_data_tarefa;

	private JTable table_list;
	private MaskFormatter dataMask;

	// monta a janela do software
	public Interface() {
		indexSelected = 0;

		controleTarefa = new ControlTarefa();
		lbl_add_tarefa = new JLabel("Descrição da tarefa:");
		lbl_data_tarefa = new JLabel("Data de conclusão:");
		btn_adicionar = new JButton("Adicionar");
		pn_listagem = new JPanel();
		pn_principal = new JPanel();
		pn_cadastro = new JPanel();

		setTitle("Gerenciador de tarefas.");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 670, 600);

		pn_principal.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(pn_principal);
		pn_principal.setLayout(null);

		// No painel pn_cadastro são dispostos os componentes para cadastrar novas
		// tarefas
		pn_cadastro
				.setBorder(new TitledBorder(null, "Formulário.", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pn_cadastro.setBounds(61, 340, 500, 200);
		pn_principal.add(pn_cadastro);
		pn_cadastro.setLayout(null);

		pn_listagem.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Lista de Tarefas.", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pn_listagem.setBounds(61, 20, 500, 300);
		pn_principal.add(pn_listagem);
		pn_listagem.setLayout(null);

		this.setLocationRelativeTo(null);

		tfTarefa();
		btnAdicionar();
		btnCancelar();
		btnConfirmar();
		dataTarefa();
		btnEditar();
		btnExcluir();
		lbAddTarefa();
		lbAddData();
		tableList();
		disableCadastro();
		disableOperationTarefa();

		/*
		 * #################################### Metodo para carregar lista de teste,pode
		 * ser comentado para iciar uma aplicacao limpa #######################
		 */
		controleTarefa.CadastroAmostral();

		insertTable();
		acao = Acao.ADICIONAR;
	}

	// Faz a verificacao dos dados e modela para inserir na tabela
	@SuppressWarnings("serial")
	private void tableList() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(21, 21, 450, 224);
		pn_listagem.add(scrollPane);

		table_list = new JTable();
		scrollPane.setViewportView(table_list);
		table_list.setFont(new Font("Tahoma", Font.PLAIN, 12));

		table_list.setCellSelectionEnabled(true);
		table_list.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Descrição das tarefas.", "Datas de conclusão." }) {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] { String.class, String.class };

			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table_list.getColumnModel().getColumn(0).setPreferredWidth(300);

		table_list.getColumnModel().getColumn(1).setResizable(false);
		table_list.getColumnModel().getColumn(1).setPreferredWidth(80);

		table_list.setToolTipText("");

		table_list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				indexSelected = table_list.getSelectedRow();

				if (indexSelected >= 0 && indexSelected < table_list.getRowCount()) {
					Tarefa tarefa = new Tarefa();
					enableOperationTarefa();
					try {
						tarefa = controleTarefa.buscaTarefa(indexSelected);
						tarefaSelecionada(tarefa.getTarefas(), tarefa.getData());
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(rootPane, e2.getMessage(), "Erro.", JOptionPane.ERROR_MESSAGE);
					}

				}
			}
		});

	}

	private void tfTarefa() {
		tf_tarefa_add = new JTextField();
		tf_tarefa_add.setBounds(28, 74, 150, 25);
		pn_cadastro.add(tf_tarefa_add);
		tf_tarefa_add.setColumns(10);
	}

	private void dataTarefa() {
		/****** formatando data *************/

		try {
			dataMask = new MaskFormatter("##/##/####");
			dataMask.setPlaceholder("/");
			dataMask.setValidCharacters("0123456789");
			tf_data_add = new JFormattedTextField(dataMask);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		tf_data_add.setBounds(225, 74, 113, 25);
		pn_cadastro.add(tf_data_add);
		tf_data_add.setColumns(10);

	}

	private void btnAdicionar() {
		btn_adicionar.setBounds(21, 256, 90, 25);
		pn_listagem.add(btn_adicionar);
		btn_adicionar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				enableCadastro();
				disableOperationTarefa();

			}
		});
	}

	private void btnCancelar() {
		btn_cancelar = new JButton("Cancelar");
		btn_cancelar.setBounds(225, 121, 100, 25);
		pn_cadastro.add(btn_cancelar);
		btn_cancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				disableCadastro();
				disableOperationTarefa();
				acao = Acao.ADICIONAR;
			}
		});
	}

	private void btnConfirmar() {
		btn_confirmar = new JButton("Confirmar");
		btn_confirmar.setBounds(28, 121, 100, 25);
		pn_cadastro.add(btn_confirmar);
		btn_confirmar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				switch (acao) {
				case ADICIONAR: {
					cadastraTarefa();
					break;
				}
				case EDITAR: {
					editorTarefa();
					insertTable();
					break;
				}
				case EXCLUIR: {

					if (JOptionPane.showConfirmDialog(null, "Deseja excluir?", "Confirmação",
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
						excluirTarefa();
					}
					break;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + acao);
				}

				acao = Acao.ADICIONAR;
				disableCadastro();
				disableOperationTarefa();
			}
		});
	}

	private void btnEditar() {
		btn_editar = new JButton("Editar");
		btn_editar.setBounds(212, 256, 90, 25);
		pn_listagem.add(btn_editar);
		btn_editar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				enableCadastro();
				acao = Acao.EDITAR;
			}
		});
	}

	private void btnExcluir() {
		btn_excluir = new JButton("Excluir");
		btn_excluir.setBounds(395, 256, 90, 25);
		pn_listagem.add(btn_excluir);
		btn_excluir.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				acao = Acao.EXCLUIR;
				// enableCadastro();
				enableDeleteOperation();
			}
		});
	}

	private void lbAddTarefa() {
		lbl_add_tarefa.setBounds(28, 43, 150, 25);
		pn_cadastro.add(lbl_add_tarefa);

	}

	private void lbAddData() {
		lbl_data_tarefa.setBounds(225, 43, 150, 25);
		pn_cadastro.add(lbl_data_tarefa);
	}

	private void tarefaSelecionada(String tarefa, String data) {
		tf_tarefa_add.setText(tarefa);
		tf_data_add.setText(data);
	}

	private void disableOperationTarefa() {
		btn_editar.setEnabled(false);
		btn_excluir.setEnabled(false);
	}

	private void enableOperationTarefa() {
		btn_editar.setEnabled(true);
		btn_excluir.setEnabled(true);
	}

	private void enableCadastro() {
		btn_confirmar.setEnabled(true);
		btn_cancelar.setEnabled(true);
		tf_tarefa_add.setEnabled(true);
		tf_data_add.setEnabled(true);
	}

	private void enableDeleteOperation() {
		btn_confirmar.setEnabled(true);
		btn_cancelar.setEnabled(true);
	}

	private void disableCadastro() {
		btn_confirmar.setEnabled(false);
		btn_cancelar.setEnabled(false);
		tf_tarefa_add.setEnabled(false);
		tf_data_add.setEnabled(false);
		tf_tarefa_add.setText("");
		tf_data_add.setText("");
	}

	private void cadastraTarefa() {
		try {
			controleTarefa.cadastraTarefas(tf_tarefa_add.getText(), tf_data_add.getText());
			disableCadastro(); /* desabilita campos apos salvar os dados */
			insertTable();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(rootPane, e.getMessage(), "Erro.", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void editorTarefa() {
		try {
			controleTarefa.editaTarefa(indexSelected, tf_tarefa_add.getText(), tf_data_add.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(rootPane, e.getMessage(), "Erro.", JOptionPane.ERROR_MESSAGE);
		}
		//if (!controleTarefa.editaTarefa(indexSelected, tf_tarefa_add.getText(), tf_data_add.getText())) {
		//	JOptionPane.showMessageDialog(null, "Preencha os campos Tarefa e Data corretamente!", "Erro!",
		//			JOptionPane.ERROR_MESSAGE);
		//}
		indexSelected = 0;
	}

	private void excluirTarefa() {
		try {
			controleTarefa.deletaTarefa(indexSelected);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(rootPane, e.getMessage(), "Erro.", JOptionPane.ERROR_MESSAGE);
		}
		
		indexSelected = 0;
		insertTable();

	}

//  Atualiza a tabela para listagem dos dados cadastrados
	private void insertTable() {
		DefaultTableModel modelo = (DefaultTableModel) table_list.getModel();
		modelo.setRowCount(0);
		List<Boolean> dimencionar = new ArrayList<>();

		// if (!controleTarefa.listaCadastros().isEmpty()) {
		try {
			for (Tarefa t : controleTarefa.listaCadastros()) {
				String tar = t.getTarefas();
				String linha1;
				String linha2;
				StringBuilder str = new StringBuilder();
				if (tar.length() > 48) {
					linha1 = tar.substring(0, 48);
					linha2 = tar.substring(49, tar.length());

					str.append("<html>");
					str.append(linha1);
					str.append("<br>");
					str.append(linha2);
					str.append("</html>");
					dimencionar.add(true);
				} else {
					str.append(tar);
					dimencionar.add(false);
				}
				Object[] row = { str.toString(), t.getData() };
				modelo.addRow(row);

			}
		} catch (Exception e3) {
			JOptionPane.showMessageDialog(rootPane, e3.getMessage(), "Erro.", JOptionPane.ERROR_MESSAGE);
		}

		// }

		for (int i = 0; i < modelo.getRowCount(); i++) {

			if (dimencionar.get(i) == true) {
				table_list.setRowHeight(i, 40);
			} else {
				table_list.setRowHeight(i, 20);
			}

		}
		table_list.setModel(modelo);

	}
}
