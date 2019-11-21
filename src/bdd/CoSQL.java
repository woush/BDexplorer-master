package bdd;

import java.sql.*;

import javax.swing.JOptionPane;

public class CoSQL {
	private static String _ip, _port, _id, _pass;
	private static String url;
	private static Connection con = null;
	@SuppressWarnings("unused")
	private CoSQL co = new CoSQL();

	public static void set_url() {
		url = new String("jdbc:mysql://" + _ip + ":" + _port + "/?useSSL=false");
	}

	public static String get_url() {
		return url;
	}

	public static String get_ip() {
		return _ip;
	}

	public static void set_ip(String _ip) {
		CoSQL._ip = _ip;
	}

	public static String get_port() {
		return _port;
	}

	public static void set_port(String _port) {
		CoSQL._port = _port;
	}

	public static String get_id() {
		return _id;
	}

	public static void set_id(String _id) {
		CoSQL._id = _id;
	}

	public static String get_pass() {
		return _pass;
	}

	public static void set_pass(String _pass) {
		CoSQL._pass = _pass;
	}

	private CoSQL() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			System.out.println("constructor connection");
		}
	}

	public static Connection getInstance() {
		if (con == null) {
			try {
				con = DriverManager.getConnection(url, _id, _pass);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "ERROR / CANCEL LOG TO THE DATABASE");
			}
		}
		return con;
	}

	public static void close() {
		try {
			if (con != null) {
				con.close();
				System.out.println("connection close");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}