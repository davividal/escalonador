package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import cpu.CPU;
import cpu.Process;


public class Escalonador {
	private CPU cpu;
	private JFrame frame;
	private JTable table;
	private Integer x = 0;
	private DefaultTableModel processTable = new DefaultTableModel();

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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);

		JLabel lblCpu = new JLabel("Cores: " + cpu.getCores());
		panel.add(lblCpu);

		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.WEST);

		JPanel panel_2 = new JPanel();
		frame.getContentPane().add(panel_2, BorderLayout.SOUTH);

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

					processTable.setValueAt(process.getStatus(status), (process.getPid() - 1), x);
				}

				Enumeration<TableColumn> columns = table.getColumnModel().getColumns();
				while (columns.hasMoreElements()) {
					TableColumn column = columns.nextElement();
					column.setCellRenderer(new ProcessCellRenderer());
				}
				x++;
			}
		});
		btnprximaRodada.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		panel_2.add(btnprximaRodada);

		JPanel panel_3 = new JPanel();
		frame.getContentPane().add(panel_3, BorderLayout.EAST);

		JPanel panel_4 = new JPanel();
		frame.getContentPane().add(panel_4, BorderLayout.CENTER);

		table = new JTable();
		table.setModel(this.processTable);
		panel_4.add(table);
	}

}
