package JDBC第七题;

import java.util.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class home2 extends JFrame {
	Vector columnNames;
	JTable jt = null;
	JScrollPane jsp = null;
	Connection conn = null;
	private JButton add_one = new JButton("单行插入");
	private JButton add_more = new JButton("多行插入");
	private JButton dele = new JButton("删除");
	private JButton upd = new JButton("更改");
	private JButton sub=new JButton("子查询操作");
	Container contentPane = getContentPane();
	JPanel buttonPanel = new JPanel();
	Statement stmt = null;
	ResultSet rs = null;
	DefaultTableModel model = new DefaultTableModel();

	public static void main(String[] args) {
		home2 show = new home2();
	}

	public void getList() {

		try {
			// 加载驱动
			Class.forName("com.mysql.jdbc.Driver");
			// 得到连接
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/text", "root", "");

			stmt = conn.createStatement();
			String sql = "select * from staff";
			rs = stmt.executeQuery(sql);
			model.setRowCount(0);
			while (rs.next()) {
				// rowData可以存放多行
				Vector hang = new Vector();
				hang.add(rs.getInt(1));
				if (rs.wasNull()) {
					hang.setElementAt("", hang.size() - 1);
				}
				hang.add(rs.getString(2));
				if (rs.wasNull()) {
					hang.setElementAt("", hang.size() - 1);
				}
				hang.add(rs.getString(3));
				if (rs.wasNull()) {
					hang.setElementAt("", hang.size() - 1);
				}
				hang.add(String.valueOf(rs.getString(4)));
				if (rs.wasNull()) {
					hang.setElementAt("", hang.size() - 1);
				}
				hang.add(String.valueOf(rs.getInt(5)));
				if (rs.wasNull()) {
					hang.setElementAt("", hang.size() - 1);
				}
				hang.add(rs.getString(6));
				if (rs.wasNull()) {
					hang.setElementAt("", hang.size() - 1);
				}
				hang.add(rs.getString(7));
				if (rs.wasNull()) {
					hang.setElementAt("", hang.size() - 1);
				}
				hang.add(rs.getString(8));
				if (rs.wasNull()) {
					hang.setElementAt("", hang.size() - 1);
				}
				hang.add(rs.getString(9));
				if (rs.wasNull()) {
					hang.setElementAt("", hang.size() - 1);
				}
				model.addRow(hang);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 构造函数；
	public home2() {
		super("增、删、改、子查询功能菜单");
		columnNames = new Vector();
		columnNames.add("编号");
		columnNames.add("姓名");
		columnNames.add("性别");
		columnNames.add("职务");
		columnNames.add("工资");
		columnNames.add("联系方式");
		columnNames.add("地址");
		columnNames.add("身份证号");
		columnNames.add("备注");
		model.setColumnIdentifiers(columnNames); 

		// 初始化Jtable
		jt = new JTable(model);
		jt.setDefaultRenderer(Object.class, new MyTableCellRenderer());
		getList();
		// 初始化 jsp
		jsp = new JScrollPane(jt);
		contentPane.add(jsp, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.add(add_one);
		buttonPanel.add(add_more);
		buttonPanel.add(dele);
		buttonPanel.add(upd);
		buttonPanel.add(sub);
		// 把jsp放入到jframe
		this.setSize(600, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		// 单行插入功能，实例化单行插入功能类；
		add_one.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton button = (JButton) e.getSource();
				new add_one7(home2.this);
			}
		});
		// 多行插入功能，实例化多行插入功能类；
		add_more.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton button = (JButton) e.getSource();
				new add_more7(home2.this);
			}
		});
		// 删除功能：能进行单行删除和多行删除；
		dele.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton button = (JButton) e.getSource();
				int option = JOptionPane.showConfirmDialog(home2.this, "是否删除选定的行？");
				if (option == JOptionPane.YES_OPTION) {
					int[] rows = jt.getSelectedRows();
					for (int i = 0; i < rows.length; ++i) {
						int number = (int) jt.getValueAt(rows[i], 0);
						try {
							Class.forName("com.mysql.jdbc.Driver");
							Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/text", "root",
									"");
							String sql = "delete from staff where id=?";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setInt(1, number);
							ps.executeUpdate();
							rs.close();
							ps.close();
							conn.close();
						} catch (Exception e1) {
							e1.printStackTrace();
							int SQLCode;
							String SQLState;
							SQLCode = ((SQLException) e1).getErrorCode();
							SQLState = ((SQLException) e1).getSQLState();
							String Message = e1.getMessage();
							JOptionPane.showMessageDialog(null,
									"\nSQLCODE:  " + SQLCode + "\nSQLSTATE: " + SQLState + "\nSQLERRM:  " + Message, "报错内容",
									JOptionPane.ERROR_MESSAGE);
						}
					}
					getList();
				}
			}
		});
		// 修改功能：能对除了主键之外所有的属性值进行修改，单行修改或者多行修改；
		upd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton button = (JButton) e.getSource();
				int option = JOptionPane.showConfirmDialog(home2.this, "是否更改信息");
				if (option == JOptionPane.YES_OPTION) {
					int[] rows = jt.getSelectedRows();
					for (int i = 0; i < rows.length; ++i) {
						if(jt.getValueAt(rows[i], 0).equals("")){
							JOptionPane.showMessageDialog(null, "员工编号不能为空！", "修改失败！", JOptionPane.ERROR_MESSAGE);
							return;
						}
						int number = (int)jt.getValueAt(rows[i], 0);
						try {
							Class.forName("com.mysql.jdbc.Driver");
							Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/text", "root",
									"");
							String sql = "update staff "
									+ "set name=?,sex=?,work=?,salary=?,phone=?,address=?,num=?,note=? " + "where id=?";
							PreparedStatement ps = conn.prepareStatement(sql);
							if (!(jt.getValueAt(rows[i], 1)).equals("")) {
								ps.setString(1, (String) jt.getValueAt(rows[i], 1));
							} else {
								ps.setNull(1, Types.VARCHAR);
							}

							if (!(jt.getValueAt(rows[i], 2)).equals("")) {
								ps.setString(2, (String) jt.getValueAt(rows[i], 2));
							} else {
								ps.setNull(2, Types.VARCHAR);
							}

							if (!(jt.getValueAt(rows[i], 3)).equals("")) {
								ps.setString(3, (String) jt.getValueAt(rows[i], 3));
							} else {
								ps.setNull(3, Types.VARCHAR);
							}

							if (!(jt.getValueAt(rows[i], 4)).equals("")) {
								if(Integer.parseInt((String)jt.getValueAt(rows[i], 4))<=0){
									JOptionPane.showMessageDialog(null,"工资必须为大于等于0的整数！","修改失败",JOptionPane.ERROR_MESSAGE);return;
								}
								ps.setInt(4, Integer.parseInt((String)jt.getValueAt(rows[i], 4)));
							} else {
								ps.setNull(4, Types.INTEGER);
							}

							if (!(jt.getValueAt(rows[i], 5)).equals("")) {
								ps.setString(5, (String) jt.getValueAt(rows[i], 5));
							} else {
								ps.setNull(5, Types.VARCHAR);
							}
							
							if(!(jt.getValueAt(rows[i],6)).equals("")){
								ps.setString(6, (String) jt.getValueAt(rows[i], 6));
								
							}else{
								ps.setNull(6, Types.VARCHAR );
							}
							
							if(!(jt.getValueAt(rows[i], 7)).equals("")){
								ps.setString(7,(String) jt.getValueAt(rows[i], 7));
							}else{
								ps.setNull(7, Types.VARCHAR);
							}
							
							if(!(jt.getValueAt(rows[i], 8)).equals("")){
								ps.setString(8, (String) jt.getValueAt(rows[i], 8));
							}else{
								ps.setNull(8, Types.VARCHAR);
							}
							ps.setInt(9, number);
							ps.executeUpdate();
						}catch(NumberFormatException e2){
							JOptionPane.showMessageDialog(null,"员工编号和工资必须为正整数", "报错内容",JOptionPane.ERROR_MESSAGE);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}

				}
				getList();

			}
		});
		sub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton jb = (JButton) e.getSource();
				new subquery(home2.this);

			}
		});
		// 如果没有选中行，则不能进行删除；
		jt.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				dele.setEnabled(jt.getSelectedRowCount() != 0);
			}

		});
		dele.setEnabled(false);
		// 如果没有选中更新行，则不能进行修改
		jt.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				upd.setEnabled(jt.getSelectedRowCount() != 0);
			}

		});
		upd.setEnabled(false);
	}
}
class MyTableCellRenderer extends DefaultTableCellRenderer {
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if(!value.equals("")) {
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		} else {
			return super.getTableCellRendererComponent(table, hasFocus ? "" : "空", isSelected, hasFocus, row, column);
		}
	}
}