package application;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import bdd.CoSQL;
import javafx.embed.swing.SwingNode;
import obj_affichage.TableColumnAdjuster;

public class Model {
	private class Logininformation {
		private String log, pass;

		public Logininformation(String s, String t) {
			log = s;
			pass = t;
		}
	}

	public Logininformation login_frame(JFrame frame) {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));

		label.add(new JLabel("ID", SwingConstants.RIGHT));
		label.add(new JLabel("Password", SwingConstants.RIGHT));
		panel.add(label, BorderLayout.WEST);
		JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
		JTextField username = new JTextField();

		controls.add(username);
		JPasswordField password = new JPasswordField();

		controls.add(password);
		panel.add(controls, BorderLayout.CENTER);
		JOptionPane.showConfirmDialog(frame, panel, "login", JOptionPane.OK_CANCEL_OPTION);
		//
		return new Logininformation(username.getText(), new String(password.getPassword()));
	}

	public void login() {
		Logininformation li = login_frame(new JFrame());
		CoSQL.set_id(li.log);
		CoSQL.set_pass(li.pass);
	}

	public void error_log() {
		JOptionPane.showMessageDialog(null, "Fonction Non Implémentée encore");
	}

	public Object[][] getTableData(JTable table) {
		DefaultTableModel dtm = (DefaultTableModel) table.getModel();
		int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount();
		Object[][] tableData = new Object[nRow][nCol];

		for (int i = 0; i < nRow; i++)
			for (int j = 0; j < nCol; j++)
				tableData[i][j] = dtm.getValueAt(i, j);
		return tableData;
	}
	//

	public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {

		ResultSetMetaData metaData = rs.getMetaData();

		// names of columns
		Vector<String> columnNames = new Vector<String>();
		int columnCount = metaData.getColumnCount();

		for (int column = 1; column <= columnCount; column++) {
			columnNames.add(metaData.getColumnName(column));
		}

		// data of the table
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		while (rs.next()) {
			Vector<Object> vector = new Vector<Object>();
			for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				vector.add(rs.getObject(columnIndex));
			}
			data.add(vector);
		}
		DefaultTableModel bob = new DefaultTableModel(data, columnNames);
		return bob;
	}

	public void createAndSetSwingContent(SwingNode swingNode, JTable jt) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				jt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				TableColumnAdjuster tca = new TableColumnAdjuster(jt);
				tca.adjustColumns();
				JScrollPane pane = new JScrollPane(jt);
				swingNode.setContent(pane);
			}
		});
	}

	public void SaveFile(String content, File file) {
		try {
			FileWriter fileWriter;

			fileWriter = new FileWriter(file);
			fileWriter.write(content);
			fileWriter.close();
		} catch (IOException ex) {
			System.out.println("erreur sauvegarde du fichier amsql");
		}

	}

	public String LoadFile(File file) {
		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader bufferedReader = null;

		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			String text;
			while ((text = bufferedReader.readLine()) != null) {
				stringBuffer.append(text);
			}
		} catch (FileNotFoundException ex) {
			System.out.println("file not found");
			;
		} catch (IOException ex) {
			System.out.println("fatal erro loading file");
			;
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException ex) {
				System.out.println("thread/textReader fail");
				;
			}
		}
		return stringBuffer.toString();
	}
}
