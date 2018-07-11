package smsAndMailer;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.mail.EmailException;

import keeptoo.KGradientPanel;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class TheUniqueTutorials {
	private JPasswordField passwordField;
	private JTextField textField;
	private PrintStream standardOut;
	private JLabel background_student_info, background;
	private JButton buttonLogin;
	private String sidStore, prevSid;
	private JFrame frame;
	private JPanel panelStudentInfo;
	private JTextField textSid, textSname, textPno, textPemail;
	private JButton buttonAdd, buttonUpdate, buttonRemove, buttonLogout;
	private static Connection con;
	private JButton buttonClearText1;
	private JButton buttonClearText2;
	private JButton buttonClearText3;
	private JButton buttonClearText4;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TheUniqueTutorials window = new TheUniqueTutorials();
					window.frame.setVisible(true);
					window.frame.setResizable(false);
					program();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TheUniqueTutorials() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\The Unique Tutorials\\workspace\\TheUniqueTutorialsSMSandMailer\\assets\\LogoIcon.png"));
		frame.setBounds(10, 10, 1280, 749);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		textField = new JTextField();
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if(textField.getText().equals("Username")) {
					textField.setText("");
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(textField.getText().equals("")) {
					textField.setText("Username");
				}
			}
		});
		frame.getContentPane().setLayout(null);
		textField.setFont(new Font("Nunito", Font.PLAIN, 25));
		textField.setBounds(100, 321, 473, 60);
		textField.setForeground(new Color (112,112,112));
		textField.setText("Username");
		textField.setBorder(null);
		textField.setBackground(new Color(255,255,255));
		textField.setOpaque(false);
		frame.getContentPane().add(textField);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Nunito", Font.PLAIN, 25));
		char passwordChar = passwordField.getEchoChar(); 
		passwordField.setEchoChar ((char) 0); 
		passwordField.setText("Password"); 
		passwordField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				passwordField.setText("");
				passwordField.setEchoChar(passwordChar); 
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(passwordField.equals("")) {
					passwordField.setEchoChar ((char) 0); 
					passwordField.setText("Password"); 
				}
			}
		});
		passwordField.setBounds(100, 431, 473, 60);
		passwordField.setForeground(new Color (112,112,112));
		passwordField.setBorder(null);
		passwordField.setBackground(new Color(255,255,255));
		passwordField.setOpaque(false);
		frame.getContentPane().add(passwordField);
		
		buttonLogin = new JButton("");
		buttonLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String username = textField.getText();
				String password = passwordField.getText();
				if(username.contains("Admin") && password.contains("ut123")) {
					textField.setText(null);
					passwordField.setText(null);
					background_student_info.setVisible(true);
					background.setVisible(false);
					textField.setVisible(false);
					passwordField.setVisible(false);
					buttonLogin.setVisible(false);
					panelStudentInfo.setVisible(true);
//					frame.dispose();
//					Program program = new Program();
//					program.setVisible(true);
//					program.setResizable(false);
				} else {
					JOptionPane.showMessageDialog(null, "Invalid Username or Password " +username +" " +password, "Error", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		buttonLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
		buttonLogin.setBounds(223, 541, 200, 60);
		buttonLogin.setOpaque(false);
		buttonLogin.setContentAreaFilled(false);
		buttonLogin.setBorderPainted(false);
		frame.getContentPane().add(buttonLogin);
		
		JTextArea textArea = new JTextArea();
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setBounds(691, 330, 509, 341);
		scroll. setVerticalScrollBarPolicy( JScrollPane. VERTICAL_SCROLLBAR_ALWAYS );
		scroll.setBorder(null);
		textArea.setEditable(false);
		
		PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
		textArea.setWrapStyleWord(true);
		// keeps reference of standard output stream
        standardOut = System.out;
        // re-assigns standard output stream and error output stream
        System.setOut(printStream);
        System.setErr(printStream);
		frame.getContentPane().add(scroll);
		
		background_student_info = new JLabel("");
		background_student_info.setIcon(new ImageIcon("C:\\Users\\The Unique Tutorials\\workspace\\TheUniqueTutorialsSMSandMailer\\assets\\Main New.png"));
		background_student_info.setBounds(0, 0, 1280, 720);
		background_student_info.setVisible(false);
		
		panelStudentInfo = new JPanel();
		panelStudentInfo.setBounds(0, 0, 638, 720);
		panelStudentInfo.setOpaque(false);
		panelStudentInfo.setVisible(false);
		frame.getContentPane().add(panelStudentInfo);
		panelStudentInfo.setLayout(null);
		
		textSid = new JTextField();
		textSid.setBounds(47, 323, 211, 60);
		textSid.setText("Student ID");
		textSid.setOpaque(false);
		textSid.setForeground(new Color(112, 112, 112));
		textSid.setFont(new Font("Dialog", Font.PLAIN, 25));
		textSid.setColumns(10);
		textSid.setBorder(null);
		textSid.setBackground(Color.BLACK);
		textSid.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if(textSid.getText().equals("Student ID")) {
					textSid.setText("");
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(textSid.getText().equals("")) {
					textSid.setText("Student ID");
				}
			}
		});
		panelStudentInfo.add(textSid);
		
		textSname = new JTextField();
		textSname.setBounds(354, 323, 220, 60);
		textSname.setText("Student name");
		textSname.setOpaque(false);
		textSname.setForeground(new Color(112, 112, 112));
		textSname.setFont(new Font("Dialog", Font.PLAIN, 25));
		textSname.setColumns(10);
		textSname.setBorder(null);
		textSname.setBackground(Color.BLACK);
		textSname.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if(textSname.getText().equals("Student name")) {
					textSname.setText("");
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(textSname.getText().equals("")) {
					textSname.setText("Student name");
				}
			}
		});
		panelStudentInfo.add(textSname);
		
		textPno = new JTextField();
		textPno.setBounds(47, 403, 211, 60);
		textPno.setText("Parent's phone no.");
		textPno.setOpaque(false);
		textPno.setForeground(new Color(112, 112, 112));
		textPno.setFont(new Font("Dialog", Font.PLAIN, 25));
		textPno.setColumns(10);
		textPno.setBorder(null);
		textPno.setBackground(Color.BLACK);
		textPno.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if(textPno.getText().equals("Parent's phone no.")) {
					textPno.setText("");
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(textPno.getText().equals("")) {
					textPno.setText("Parent's phone no.");
				}
			}
		});
		panelStudentInfo.add(textPno);
		
		textPemail = new JTextField();
		textPemail.setBounds(354, 403, 220, 60);
		textPemail.setText("Parent's email");
		textPemail.setOpaque(false);
		textPemail.setForeground(new Color(112, 112, 112));
		textPemail.setFont(new Font("Dialog", Font.PLAIN, 25));
		textPemail.setColumns(10);
		textPemail.setBorder(null);
		textPemail.setBackground(Color.BLACK);
		textPemail.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if(textPemail.getText().equals("Parent's email")) {
					textPemail.setText("");
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(textPemail.getText().equals("")) {
					textPemail.setText("Parent's email");
				}
			}
		});
		panelStudentInfo.add(textPemail);
		
		buttonAdd = new JButton("");
		buttonAdd.setBounds(22, 513, 193, 60);
		buttonAdd.setOpaque(false);
		buttonAdd.setContentAreaFilled(false);
		buttonAdd.setBorderPainted(false);
		buttonAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
		buttonAdd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				databaseConnection();
				if(textSid.getText().equals(null) || textSname.getText().equals(null) 
						|| textPno.getText().equals(null)) {
					System.out.println("Please enter student information to add them to database.");
				} else if (textSid.getText().equals("Student ID") || textSname.getText().equals("Student name") 
						|| textPno.getText().equals("Parent's phone no.")) {
					System.out.println("Please enter student information to add them to database.");
				} else {
					try {
						if(textPemail.getText().equals("") || textPemail.getText().equals("Parent's email")){
							textPemail.setText("wrong@certainly.not.an.email.com");
						}
						PreparedStatement stmt;
						String sqlCheck = "SELECT s_id FROM main_details_1 WHERE s_id =?";
						stmt = con.prepareStatement(sqlCheck);
						stmt.setString(1, textSid.getText());
						ResultSet set = stmt.executeQuery();
						while(set.next()) {
							sidStore = set.getString("s_id");
						}
						if(textSid.getText().equals(sidStore)) {
							System.out.println("Student already exists! Please click UPDATE button.");
						} else {
							String sql = "INSERT INTO main_details_1 (s_id, s_name, p_no, p_email)"
									+ " VALUES (?, ?, ?, ?)";
							stmt = con.prepareStatement(sql);
							stmt.setString(1, textSid.getText());
						      stmt.setString(2, textSname.getText());
						      stmt.setString(3, textPno.getText());
						      stmt.setString(4, textPemail.getText());
						      stmt.executeUpdate();
						      System.out.println("Student add success");
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				      
				}
			}
				
		});
		
		buttonClearText1 = new JButton("");
		buttonClearText1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				textSid.setText("Student ID");
			}
		});
		buttonClearText1.setIcon(new ImageIcon("C:\\Users\\The Unique Tutorials\\workspace\\TheUniqueTutorialsSMSandMailer\\assets\\1x\\baseline_backspace_black_24dp.png"));
		buttonClearText1.setBounds(275, 342, 24, 24);
		buttonClearText1.setOpaque(false);
		buttonClearText1.setContentAreaFilled(false);
		buttonClearText1.setBorderPainted(false);
		buttonClearText1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		panelStudentInfo.add(buttonClearText1);
		panelStudentInfo.add(buttonAdd);
		
		buttonUpdate = new JButton("");
		buttonUpdate.setBounds(226, 513, 193, 60);
		buttonUpdate.setOpaque(false);
		buttonUpdate.setContentAreaFilled(false);
		buttonUpdate.setBorderPainted(false);
		buttonUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
		buttonUpdate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				databaseConnection();
				String sid = textSid.getText();
				if(textSname.getText().equals("Student name") 
						|| textPno.getText().equals("Parent's phone no.") || textPemail.getText().equals("Parent's email")) {
					try {
						String sql = "SELECT s_name, p_no, p_email FROM main_details_1 WHERE s_id =?";
					      PreparedStatement statement = con.prepareStatement(sql);
					      statement.setString(1, sid);
					      //STEP 5: Extract data from result set
					      ResultSet set = statement.executeQuery();
					      while(set.next()){
					         //Retrieve by column name
					    	 textSname.setText(set.getString("s_name"));
					    	 textPno.setText(set.getString("p_no"));
					    	 textPemail.setText(set.getString("p_email"));
					      }
					      prevSid = textSid.getText();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					try {
						Statement statement = con.createStatement();
						String sql = "UPDATE main_details_1 SET s_id='"+textSid.getText()+"',s_name='"+textSname.getText()+
								"',p_no='"+textPno.getText()+"',p_email='"+textPemail.getText()+"' WHERE s_id='"+prevSid+"'";
					      statement.executeUpdate(sql);
					      System.out.println("Record Update success");
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		buttonClearText2 = new JButton("");
		buttonClearText2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textSname.setText("Student name");
			}
		});
		buttonClearText2.setIcon(new ImageIcon("C:\\Users\\The Unique Tutorials\\workspace\\TheUniqueTutorialsSMSandMailer\\assets\\1x\\baseline_backspace_black_24dp.png"));
		buttonClearText2.setOpaque(false);
		buttonClearText2.setContentAreaFilled(false);
		buttonClearText2.setBorderPainted(false);
		buttonClearText2.setCursor(new Cursor(Cursor.HAND_CURSOR));
		buttonClearText2.setBounds(584, 342, 24, 24);
		panelStudentInfo.add(buttonClearText2);
		panelStudentInfo.add(buttonUpdate);
		
		buttonRemove = new JButton("");
		buttonRemove.setBounds(430, 513, 193, 60);
		buttonRemove.setOpaque(false);
		buttonRemove.setContentAreaFilled(false);
		buttonRemove.setBorderPainted(false);
		buttonRemove.setCursor(new Cursor(Cursor.HAND_CURSOR));
		buttonRemove.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				databaseConnection();
				try {
					String sql = "DELETE FROM main_details_1 WHERE s_id =?";
				      PreparedStatement statement = con.prepareStatement(sql);
				      statement.setString(1, textSid.getText());
				      //STEP 5: Extract data from result set
				      statement.execute();
				      System.out.println("Record delete success");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		buttonClearText3 = new JButton("");
		buttonClearText3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textPno.setText("Parent's phone no.");
			}
		});
		buttonClearText3.setIcon(new ImageIcon("C:\\Users\\The Unique Tutorials\\workspace\\TheUniqueTutorialsSMSandMailer\\assets\\1x\\baseline_backspace_black_24dp.png"));
		buttonClearText3.setOpaque(false);
		buttonClearText3.setContentAreaFilled(false);
		buttonClearText3.setBorderPainted(false);
		buttonClearText3.setCursor(new Cursor(Cursor.HAND_CURSOR));
		buttonClearText3.setBounds(275, 422, 24, 24);
		panelStudentInfo.add(buttonClearText3);
		panelStudentInfo.add(buttonRemove);
		
		buttonLogout = new JButton("");
		buttonLogout.setContentAreaFilled(false);
		buttonLogout.setBorderPainted(false);
		buttonLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
		buttonLogout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				background_student_info.setVisible(false);
				background.setVisible(true);
				textField.setVisible(true);
				passwordField.setVisible(true);
				buttonLogin.setVisible(true);
				panelStudentInfo.setVisible(false);
			}
		});
		
		buttonClearText4 = new JButton("");
		buttonClearText4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textPemail.setText("Parent's email");
			}
		});
		buttonClearText4.setIcon(new ImageIcon("C:\\Users\\The Unique Tutorials\\workspace\\TheUniqueTutorialsSMSandMailer\\assets\\1x\\baseline_backspace_black_24dp.png"));
		buttonClearText4.setOpaque(false);
		buttonClearText4.setContentAreaFilled(false);
		buttonClearText4.setBorderPainted(false);
		buttonClearText4.setCursor(new Cursor(Cursor.HAND_CURSOR));
		buttonClearText4.setBounds(584, 422, 24, 24);
		panelStudentInfo.add(buttonClearText4);
		buttonLogout.setBounds(21, 617, 602, 60);
		buttonLogout.setOpaque(false);
		buttonLogout.setContentAreaFilled(false);
		buttonLogout.setBorderPainted(false);
		panelStudentInfo.add(buttonLogout);
		frame.getContentPane().add(background_student_info);
		
		background = new JLabel("");
		background.setIcon(new ImageIcon("C:\\Users\\The Unique Tutorials\\workspace\\TheUniqueTutorialsSMSandMailer\\assets\\Login screen New.png"));
		background.setBounds(0, 0, 1280, 720);
		frame.getContentPane().add(background);
	}
	
	private static void program() {
		class repeat extends TimerTask {
		    public void run() {
		    	try {
					FileRead readAndWrite = new FileRead();
					SendSMSandMail smsAndMail = new SendSMSandMail();
				} catch (IOException | SQLException e) {
					e.printStackTrace();
				} catch (EmailException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
		    }
		}
		
		Timer timer = new Timer();
		timer.schedule(new repeat(), 0, 5000);
	}
	
	private static void databaseConnection() {
		try
			{  
				Class.forName("com.mysql.cj.jdbc.Driver");
				con=DriverManager.getConnection(  
				"jdbc:mysql://IP:PORT/DATABASE","USERNAME","PASSWORD");
				System.out.println("Connection success!"); 
			}
			catch(Exception e)
			{
				System.out.println(e);  
			}
	}
}
