package taja;

import com.sun.deploy.util.ArrayUtil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Gui extends JPanel implements ActionListener, KeyListener {
	public static JLabel[] arrJlabel = new JLabel[9];
	public static JLabel[] printAllJlabel2;
	public static int speed = 500;// ���ڰ� �������� �ӵ��� �������� ������ ������ �����Ѵ�. �ʱⰪ�� 1000
	private JLabel tajaLabel,askNickname,askName,resultNickname,resultName, resultAc, resultTime;
	private JButton startButton, quitButton; // JButton
	// ����
	private JTextField insertDap, inputNickname,inputName; // JTextField ����
	private Random myRandom = new Random(); // �����Լ� ����
	totalPlayTime total_play_time = new totalPlayTime(); // �ð� Ÿ�̸� ������ Ŭ���� ����
	Rain data_rain = new Rain(); // ���ڰ� ���������ϴ� ������ Ŭ���� ����
	WordData word_create = new WordData(); // ������ �����ϴ� Ŭ���� ����
	private float tryCount = 0; // �õ� Ƚ�� ����
	private float correctCount = 0; // ���� Ƚ�� ����
	private int correctPercent; // ���߷� ����
	public static ImageIcon icon, icon2, buttonIcon, buttonOnclick; // ImageIcon ����
	private String studentNickname,studentName; // �й��� , �л��̸� ���� String���� ����
	Connection con1 = null;
	private TextArea ta1 = new TextArea();



	public Gui() {

		setSize(800, 596); // �г� ����� 800x600���� �����Ѵ�
		setLayout(null); // ��ġ�� ���밪 ��ġ�� �����ϱ� ������, ���̾ƿ��� null�� �����Ѵ�
		icon = new ImageIcon("img/sky.jpg");
		icon2 = new ImageIcon("img/222.jpg");
		//this.setBackground(Color.lightGray);
		tajaLabel = new JLabel();
		tajaLabel.setText("Ÿ�ڿ�������");
		tajaLabel.setFont(new Font("����",Font.BOLD,30));
		tajaLabel.setBounds(300, 80, 250, 40);
		tajaLabel.setForeground(Color.white);

		add(tajaLabel);


		startButton = new JButton("����");
		startButton.setForeground(Color.blue);
		startButton.setBounds(620, 235, 100, 43); // strartButton�� ��ǥ��,���� ����
		add(startButton); // Gui JPanel�� startButton�� �߰��Ѵ�.
		startButton.addActionListener(this); // startButton�� ActionListener �߰�



		askNickname = new JLabel();
		askNickname.setText("�г���: ");
		askNickname.setFont(new Font("����",Font.BOLD,25));
		askNickname.setBounds(100, 200, 180, 40);
		askNickname.setForeground(Color.white);
		askNickname.setBackground(Color.white);
		askNickname.setLocation(150,200);
		add(askNickname);
		askName = new JLabel();
		askName.setText("�̸�: ");
		askName.setFont(new Font("����",Font.BOLD,25));
		askName.setBounds(100, 280, 180, 40);
		askName.setForeground(Color.white);
		askName.setBackground(Color.white);
		askName.setLocation(150,280);
		add(askName);


		inputNickname = new JTextField(10);
		inputNickname.setFont(new Font("����",Font.BOLD,32));
		inputNickname.setBounds(300, 200, 180, 40);
		add(inputNickname);

		inputName = new JTextField(10);
		inputName.setFont(new Font("����",Font.BOLD,32));
		inputName.setBounds(300, 280, 180, 40);
		add(inputName);


		total_play_time.playTime.setBounds(720, 480, 200, 50);
		total_play_time.playTime.setFont(new Font("Dialog", Font.BOLD, 30));
		add(total_play_time.playTime);
		total_play_time.playTime.setVisible(false); // Ÿ�̸��߰��ϰ�, �Ⱥ��̰� �Ѵ�
		word_create.create();// word_createŬ�������� �ܾ �����ϴ� create�޼ҵ带 �����Ѵ�

	}

	@Override
	public void actionPerformed(ActionEvent e) { // ��ư�̺�Ʈ ����
		if (e.getSource() == startButton) { // startButton�� Ŭ���ϰ� �Ǹ�
			studentNickname = inputNickname.getText(); // studentNickname�� inputNickname�ؽ�Ʈ�ʵ忡
			studentName=inputName.getText();

			firstStart(); // firstStart �޼ҵ� ����

		}else if (e.getSource() == quitButton) { // quit��ư�� ������
			int flag=JOptionPane.showConfirmDialog(null,"�����Ͻðڽ��ϱ�?","confirm",JOptionPane.OK_CANCEL_OPTION);
			if(JOptionPane.CLOSED_OPTION==flag){
				System.exit(0);
			}else if(JOptionPane.YES_OPTION==flag) {
				System.exit(0); // ���α׷��� �����Ѵ�
			}
		}
	}


	@Override
	public void keyPressed(KeyEvent e) { // Ű�̺�Ʈ ����
		// TODO Auto-generated method stub
		if (e.getKeyCode() == 10) { // ���Ͱ� ������
			tryCount++; // �õ� ȸ�� 1 ����
			removeAnswer(); // ����ó�� �޼ҵ� ����
			try {
				endAnswer2(); // ��� ������ ���� �޼ҵ� ����
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			//endAnswer();

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	private void removeAnswer() { // ����ó�� �޼ҵ�
		for (int i = 0; i < 9; i++) {
			if (insertDap.getText().equals(
					word_create.arr.get(i))) { /* �Էµ� ���� ��̸���Ʈ�� ��� ���� ��ġ�ϸ� */
				arrJlabel[i].setVisible(false);
				correctCount++;
			} // �� JLabel�� �Ⱥ��̰� �ϰ�, correctCount(���� ��)�� 1 ������Ų��
		}
		insertDap.setText("");
		/*
		 * JLabel Visible False ��, TextLabel�� ��ĭ���� �ξ �ٷ� �Է��Ҽ� �ְ� �Ѵ�.
		 */

	}
	//--------------------------------------------------------------start DB
	private void endAnswer2() throws Exception
	{



		// ������ ������ ���� �޼ҵ�(������ ��� ������ ��)
		if (arrJlabel[0].isVisible() == false && arrJlabel[1].isVisible() == false && arrJlabel[2].isVisible() == false
				&& arrJlabel[3].isVisible() == false && arrJlabel[4].isVisible() == false
				&& arrJlabel[5].isVisible() == false && arrJlabel[6].isVisible() == false
				&& arrJlabel[7].isVisible() == false && arrJlabel[8].isVisible() == false) { // ���
			/* JLabel�� ������ ������(��,�Ϸ�Ǹ�) */

// ������ ���Դ� �ܾ���� ���ӻ�ܿ� ǥ���� �ش�

			data_rain.stop(); // �꼺�� ������ ����
			total_play_time.stop(); // �ð� Ÿ�̸� ������ ����.
			correctPercent = Math.round((correctCount / tryCount)* 100); /*
			 * ���߷��� �����. ����ȸ��/��Ƚ�� * 100�ؼ� �Ҽ��� ����
			 */
			icon = new ImageIcon("img/award2.jpg"); // ����� background3����
			this.repaint();
			// �ٲ۴�
			/*
			 */
			int flag=JOptionPane.showConfirmDialog(null, "�г���:  "+
					studentNickname+"\n"+
					"�̸�:  "+studentName+"\n"+"�ð�:  "+
					Integer.toString(total_play_time.gamePlayTime-1) + "��\n��Ȯ��:  "+
					Integer.toString(correctPercent) + "%\n","���",JOptionPane.YES_OPTION);
			if(JOptionPane.YES_OPTION==flag) {
				insertDap.setVisible(false);
				total_play_time.playTime.setVisible(false);


				//--------------------------------------------------------------------DB start

				Class.forName("oracle.jdbc.driver.OracleDriver");
				con1 = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe","hr","hr");
				System.out.println("Connected");
				String check = "select table_name from user_tables where table_name like 'SCORE'";
				String create = "create table score(id varchar2(30), name varchar(30), time number(30))";
				String insert = "insert into score(id,name,time) values(?,?,?)";
				String id = studentNickname;
				String name = studentName;
				int time = total_play_time.gamePlayTime-1;
				//�����--------------------------------------------------------------------------------------end
				PreparedStatement checkP = con1.prepareStatement(check);
				ResultSet chrs = checkP.executeQuery();
				String scoreString="";
/*
				if(chrs.next())
				{
					while(true)
					{
						String score = chrs.getString(1)
					}
				}*/

						/*while(chrs.next())
						{
							String score = chrs.getString("TABLE_NAME");
							scoreString += score+"\n";
						}*/

/*

					String[] scoreArray = scoreString.split("\n");
					if(scoreString.contains("SCORE")!=true)
					{
						con1.prepareStatement(create).executeUpdate();
					}
*/



				PreparedStatement inps = con1.prepareStatement(insert);
				inps.setString(1,id);
				inps.setString(2,name);
				inps.setInt(3,time);
				int rs = inps.executeUpdate();
				System.out.println("I Inserted "+rs+"!!");

				String sql = "select name,time from score order by time asc";
				PreparedStatement ps = con1.prepareStatement(sql);
				ResultSet rss = ps.executeQuery();

				String s[] = null;

				ArrayList<JLabel> arr=new ArrayList<JLabel>();
				ArrayList<JLabel> arr2=new ArrayList<JLabel>();
				int c=0;
				
				if(rss.next()) {
					do{
						String showName = rss.getString(1);
						int showTime = rss.getInt(2);


						JLabel jj=new JLabel((c+1)+"��:  "+showName);
						JLabel jj2=new JLabel(showTime+"��");

						arr.add(jj);
						arr2.add(jj2);

						c++;
						if(c==5)
							break;
					}
					while(rss.next());
				}

				printAllJlabel2=new JLabel[5];
				System.out.println(printAllJlabel2.length);
				int cc=0;
				for(JLabel j2:arr) {
					printAllJlabel2[cc]=j2;


					printAllJlabel2[cc].setBounds(0, 0, 500, 30); // printAllJlabel2�� ���� ����
					printAllJlabel2[cc].setFont(new Font("����", Font.BOLD, 25)); // printAllJlabel2�� ��Ʈ ����
					printAllJlabel2[cc].setForeground(Color.black);
					printAllJlabel2[cc].setLocation(180,(cc+3)*50);
					add(printAllJlabel2[cc]);
					cc++;
					this.repaint();
				}
				cc=0;
				printAllJlabel2=new JLabel[5];
				for(JLabel j3:arr2) {
					printAllJlabel2[cc]=j3;


					printAllJlabel2[cc].setBounds(0, 0, 500, 30); // printAllJlabel2�� ���� ����
					printAllJlabel2[cc].setFont(new Font("����", Font.BOLD, 25)); // printAllJlabel2�� ��Ʈ ����
					printAllJlabel2[cc].setForeground(Color.black);

					printAllJlabel2[cc].setLocation(580,(cc+3)*50);
					add(printAllJlabel2[cc]);
					cc++;
					this.repaint();
				}
				icon = new ImageIcon("img/award.jpg");
				this.repaint();

				JLabel lb = new JLabel("Ranking");
				lb.setBounds(180,30,200,50);
				lb.setFont(new Font("����",Font.ITALIC,40));
				lb.setForeground(Color.WHITE);
				add(lb);
				this.repaint();

				quitButton = new JButton("Ȯ��");
				quitButton.setBounds(340, 480, 80, 30);
				add(quitButton);
				quitButton.addActionListener(this);

			}// �����ϱ� ��ư�� ���� �������� ��������
			con1.close();
			// ActionListener �߰��Ѵ�
			//--------------------------------------------------------------------DB start



			//------------------------------------------------------------------DB end

		}
	}



//--------------------------------------------------------------end DB



	private void firstStart() { // �����ϱ� ��ư�� ������ ���� �޼ҵ�(���ӽ���)

		/*new JFrame(studentName+"�� ����!");*/
		insertDap = new JTextField(2);
		insertDap.addKeyListener(this); // �ؽ�Ʈ�ʵ忡 Ű �̺�Ʈ �߰�(����)
		insertDap.setFont(new Font("����",Font.BOLD,15));
		insertDap.setBounds(320, 493, 150, 30);
		add(insertDap); // ���� �Է��ϴ� �ؽ�Ʈ�ʵ带 �������� ���� �߰��Ѵ�

		tajaLabel.setVisible(false);
		askNickname.setVisible(false);
		askName.setVisible(false);

		startButton.setVisible(false); // ���� ��ư �Ⱥ��̰�
		inputNickname.setVisible(false);
		inputName.setVisible(false);
		insertDap.setVisible(true); // �� �Է�â ���̰� ��

		// ��� �̹����� �ι�° �̹����� �ٲ�

		word_create.shuffle();// word_createŬ�������� �ܾ������ ���� shuffle�޼ҵ带 �����Ѵ�.
		for (int i = 0; i < arrJlabel.length; i++) {
			arrJlabel[i] = new JLabel(
					word_create.arr.get(i)); /*
			 * JLabel�� ���ʴ�� �������� ���Ե� ��̸���Ʈ
			 * Ű������ �ϴ� �ؽ����� ������ �̸����� ����
			 */

			arrJlabel[i].setBounds(0, 0, 150, 30); // arrJLabel�� ���� ����
			arrJlabel[i].setFont(new Font("����", Font.BOLD, 15)); // arrJLabel�� ��Ʈ ����
			arrJlabel[i].setForeground(Color.WHITE);
			int num=myRandom.nextInt(300) -200;
			int num2=(i * 90)+15;
			arrJlabel[i].setLocation(num2, num); // arrJLabel��
			// ��ġ��
			// �����Ѵ�.
			add(arrJlabel[i]); // arrJLabel�� �гο� �߰��Ѵ�

		}


		total_play_time.playTime.setVisible(true); // �ð�Ÿ�̸Ӹ� ���̰� ��
		total_play_time.start(); // �ð� Ÿ�̸� ������ ����
		data_rain.start(); // �꼺�� ������ ����

	}
	public void paintComponent(Graphics g) {
		g.drawImage(icon.getImage(), 0, 0, null); // 0,0��ǥ���� �̹����� �Ѹ�
		setOpaque(false); // �׸��� ǥ���ϰ� ����,�����ϰ� ����
		super.paintComponent(g);
	}



}

