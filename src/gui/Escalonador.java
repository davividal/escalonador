package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import cpu.CPU;
import cpu.Process;


public class Escalonador {
	private CPU cpu;
	private JFrame frame;
	private Integer x = 0;
	private DefaultTableModel processTable = new DefaultTableModel();
	private JTable table;
	private JTable table_1;
	private JScrollPane scrollPane;
	private Timer autorun;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Escalonador window = new Escalonador();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Escalonador() {
		cpu = new CPU();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 800, 460);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);

		JLabel lblCpu = new JLabel("Cores: " + cpu.getCores());
		panel.add(lblCpu);

		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.WEST);

		JPanel panel_2 = new JPanel();
		frame.getContentPane().add(panel_2, BorderLayout.SOUTH);

		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(300, 50, 1000, 50));

		JToggleButton tglbtnIniciar = new JToggleButton("Iniciar");
		JButton btnReiniciar = new JButton("Reiniciar");

		JButton btnprximaRodada = new JButton("Pr\u00F3xima Rodada");
		btnprximaRodada.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				processTable.addColumn("R" + (x+1));
				if (x == 0) {
					for (int i = 0; i < 20; i++) {
						processTable.addRow(new Object[] {});
					}
				}

				cpu.run();

				Enumeration<Process> processes = cpu.getRoundProcesses().keys();
				while (processes.hasMoreElements()) {
					Process process = processes.nextElement();
					Process.Status status = cpu.getRoundProcesses().get(process);

					String value = process.getStatus(status);

					if (status == Process.Status.BLOCKED && process.roundsBlocked() >= 0) {
						value += " B: " + (process.roundsBlocked() + 1);
					} else if (status != Process.Status.FINISHED) {
						value += " ETA: " + (process.remaning() + 1);
					}

					processTable.setValueAt(value, (process.getPid() - 1), x);
				}

				Enumeration<TableColumn> columns = table.getColumnModel().getColumns();
				while (columns.hasMoreElements()) {
					TableColumn column = columns.nextElement();
					column.setCellRenderer(new ProcessCellRenderer());
				}
				x++;

				scrollPane.getHorizontalScrollBar().setValue(scrollPane.getHorizontalScrollBar().getMaximum() + 1);

				if (cpu.isFinished()) {
					tglbtnIniciar.setSelected(false);
					tglbtnIniciar.setEnabled(false);
					btnprximaRodada.setEnabled(false);
					btnReiniciar.setEnabled(true);
				}
			}
		});
		panel_2.add(btnprximaRodada);

		autorun = new Timer((int) spinner.getValue(), btnprximaRodada.getActionListeners()[0]);

		tglbtnIniciar.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (tglbtnIniciar.isSelected()) {
					tglbtnIniciar.setText("Parar");
					btnprximaRodada.setEnabled(false);
					spinner.setEnabled(false);
					autorun.start();
				} else {
					tglbtnIniciar.setText("Iniciar");
					btnprximaRodada.setEnabled(true);
					spinner.setEnabled(true);
					autorun.stop();
				}
			}
		});

		JLabel lblVelocidade = new JLabel("Velocidade");
		panel_2.add(lblVelocidade);

		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				autorun.setDelay((int) spinner.getValue());
			}
		});
		panel_2.add(spinner);

		JLabel lblMs = new JLabel("ms");
		panel_2.add(lblMs);
		panel_2.add(tglbtnIniciar);

		btnReiniciar.setEnabled(false);
		btnReiniciar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				cpu = new CPU();
				processTable = new DefaultTableModel();
				table.setModel(processTable);
				tglbtnIniciar.setEnabled(true);
				btnprximaRodada.setEnabled(true);
				x = 0;
				lblCpu.setText("Cores: " + cpu.getCores());
				btnReiniciar.setEnabled(false);
			}
		});
		panel_2.add(btnReiniciar);

		JPanel panel_3 = new JPanel();
		frame.getContentPane().add(panel_3, BorderLayout.EAST);

		scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.setRowSelectionAllowed(false);
		table.setModel(this.processTable);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPane.setViewportView(table);

		table_1 = new JTable();
		scrollPane.setColumnHeaderView(table_1);
	}

}
