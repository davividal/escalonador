package gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ProcessCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 575205731439820202L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		this.setForeground(Color.BLACK);
		if (value == null) {
			this.setBackground(Color.WHITE);
		} else {
			switch ((String) value) {
			case "P":
				this.setBackground(Color.YELLOW);
				break;
			case "E":
				this.setBackground(Color.GREEN);
				break;
			case "B":
				this.setBackground(Color.RED);
				break;
			case "F":
				this.setBackground(Color.BLACK);
				this.setForeground(Color.WHITE);
				break;
			}
		}

		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	}

	@Override
	public void validate() {}
	@Override
	public void revalidate() {}
}
