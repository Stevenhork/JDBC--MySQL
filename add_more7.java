package JDBC第七题;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class add_more7 extends JFrame {// 给单行插入增加监听器；
	// 定义该图形中所需的组件的引用
	private JButton submit = new JButton("提交");
	Container contentPane = getContentPane();
	JPanel buttonpanel = new JPanel();
	JTextArea area = new JTextArea();
	JScrollPane js = new JScrollPane(area);

	add_more7(home2 aduc)// 构造方法
	{
		super("多行插入");
		contentPane.add(js, BorderLayout.CENTER);
		contentPane.add(buttonpanel, BorderLayout.SOUTH);
		buttonpanel.add(submit);
		this.setSize(400, 300);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton button = (JButton) e.getSource();
				run();
				aduc.getList();
				add_more7.this.dispose();
			}
		});

	}

	public void run() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection coon = DriverManager.getConnection("jdbc:mysql://localhost:3306/text", "root", "");
			String sql = "insert into staff "
					+ "(id,name,sex,work,salary,phone,address,num,note) values(?,?,?,?,?,?,?,?,?)";
			String sq = "select * from staff";
			PreparedStatement stmt = (PreparedStatement) coon.prepareStatement(sql);
			Statement st = coon.createStatement();
			ResultSet rs = st.executeQuery(sq);
			ArrayList<Integer> index = new ArrayList<Integer>();
			int count = 0;
			while (rs.next()) {
				index.add(rs.getInt(1));
				count++;
			}
			String[] lines = area.getText().split("\n");
			String[][] values = new String[lines.length][];
			for (int i = 0; i < lines.length; ++i) {
				values[i] = lines[i].split("[ \t]");
				if (!values[i][0].equals("")) {
					if(Integer.parseInt(values[i][0])<=0){
						JOptionPane.showMessageDialog(null, "编号必须为正整数" , "单行插入失败",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					for (int j = 0; j < index.size(); j++) {
						if (Integer.parseInt(values[i][0]) == index.get(j)) {
							JOptionPane.showMessageDialog(null, "员工编号" + index.get(j) + "已重复", "插入失败",
									JOptionPane.ERROR_MESSAGE);
							return;
						}

					}
					stmt.setInt(1, Integer.parseInt(values[i][0]));
				} else {
					JOptionPane.showMessageDialog(null, "员工编号不能为空！不符合条件的插入失败！", "单行插入", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!values[i][1].equals("")) {
					stmt.setString(2, values[i][1]);
				} else {
					stmt.setNull(2, Types.VARCHAR);
				}

				if (!values[i][2].equals("")) {
					stmt.setString(3, values[i][2]);
				} else {
					stmt.setNull(3, Types.VARCHAR);
				}

				if (!values[i][3].equals("")) {
					stmt.setString(4, values[i][3]);
				} else {
					stmt.setNull(4, Types.VARCHAR);
				}

				if (!values[i][4].equals("")) {
					if (Integer.parseInt(values[i][4]) <= 0) {
						JOptionPane.showMessageDialog(null, "工资必须为大于零的整数,不符合条件的插入失败！", "单行插入",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					stmt.setInt(5, Integer.parseInt(values[i][4]));

				} else {
					stmt.setNull(5, Types.INTEGER);
				}

				if (!values[i][5].equals("")) {
					stmt.setString(6, values[i][5]);
				} else {
					stmt.setNull(6, Types.VARCHAR);
				}

				if (!values[i][6].equals("")) {
					stmt.setString(7, values[i][6]);
				} else {
					stmt.setNull(7, Types.VARCHAR);
				}

				if (!values[i][7].equals("")) {
					stmt.setString(8, values[i][7]);
				} else {
					stmt.setNull(8, Types.VARCHAR);
				}

				if (!values[i][8].equals("")) {
					stmt.setString(9, values[i][8]);
				} else {
					stmt.setNull(9, Types.VARCHAR);
				}

				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			int SQLCode;
			String SQLState;
			SQLCode =e.getErrorCode();
			SQLState =  e.getSQLState();
			String Message = e.getMessage();
			JOptionPane.showMessageDialog(null,
					"\nSQLCODE:  " + SQLCode + "\nSQLSTATE: " + SQLState + "\nSQLERRM:  " + Message, "报错内容",
					JOptionPane.ERROR_MESSAGE);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null,"员工编号和工资必须为正整数","插入失败",JOptionPane.ERROR_MESSAGE);
		}

		
	}

}