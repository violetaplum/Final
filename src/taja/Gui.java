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
	public static int speed = 500;// 글자가 내려오는 속도를 정적으로 정수형 변수로 선언한다. 초기값은 1000
	private JLabel tajaLabel,askNickname,askName,resultNickname,resultName, resultAc, resultTime;
	private JButton startButton, quitButton; // JButton
	// 정의
	private JTextField insertDap, inputNickname,inputName; // JTextField 정의
	private Random myRandom = new Random(); // 랜덤함수 정의
	totalPlayTime total_play_time = new totalPlayTime(); // 시간 타이머 쓰레드 클래스 생성
	Rain data_rain = new Rain(); // 글자가 떨어지게하는 쓰레드 클래스 생성
	WordData word_create = new WordData(); // 문제를 생성하는 클래스 생성
	private float tryCount = 0; // 시도 횟수 변수
	private float correctCount = 0; // 맞은 횟수 정수
	private int correctPercent; // 명중률 변수
	public static ImageIcon icon, icon2, buttonIcon, buttonOnclick; // ImageIcon 정의
	private String studentNickname,studentName; // 학번과 , 학생이름 변수 String으로 정의
	Connection con1 = null;
	private TextArea ta1 = new TextArea();



	public Gui() {

		setSize(800, 596); // 패널 사이즈를 800x600으로 지정한다
		setLayout(null); // 위치를 절대값 위치로 지정하기 때문에, 레이아웃을 null로 지정한다
		icon = new ImageIcon("img/sky.jpg");
		icon2 = new ImageIcon("img/222.jpg");
		//this.setBackground(Color.lightGray);
		tajaLabel = new JLabel();
		tajaLabel.setText("타자연습게임");
		tajaLabel.setFont(new Font("굴림",Font.BOLD,30));
		tajaLabel.setBounds(300, 80, 250, 40);
		tajaLabel.setForeground(Color.white);

		add(tajaLabel);


		startButton = new JButton("시작");
		startButton.setForeground(Color.blue);
		startButton.setBounds(620, 235, 100, 43); // strartButton의 좌표와,범위 지정
		add(startButton); // Gui JPanel에 startButton을 추가한다.
		startButton.addActionListener(this); // startButton에 ActionListener 추가



		askNickname = new JLabel();
		askNickname.setText("닉네임: ");
		askNickname.setFont(new Font("굴림",Font.BOLD,25));
		askNickname.setBounds(100, 200, 180, 40);
		askNickname.setForeground(Color.white);
		askNickname.setBackground(Color.white);
		askNickname.setLocation(150,200);
		add(askNickname);
		askName = new JLabel();
		askName.setText("이름: ");
		askName.setFont(new Font("굴림",Font.BOLD,25));
		askName.setBounds(100, 280, 180, 40);
		askName.setForeground(Color.white);
		askName.setBackground(Color.white);
		askName.setLocation(150,280);
		add(askName);


		inputNickname = new JTextField(10);
		inputNickname.setFont(new Font("굴림",Font.BOLD,32));
		inputNickname.setBounds(300, 200, 180, 40);
		add(inputNickname);

		inputName = new JTextField(10);
		inputName.setFont(new Font("굴림",Font.BOLD,32));
		inputName.setBounds(300, 280, 180, 40);
		add(inputName);


		total_play_time.playTime.setBounds(720, 480, 200, 50);
		total_play_time.playTime.setFont(new Font("Dialog", Font.BOLD, 30));
		add(total_play_time.playTime);
		total_play_time.playTime.setVisible(false); // 타이머추가하고, 안보이게 한다
		word_create.create();// word_create클래스에서 단어를 생성하는 create메소드를 실행한다

	}

	@Override
	public void actionPerformed(ActionEvent e) { // 버튼이벤트 정의
		if (e.getSource() == startButton) { // startButton을 클릭하게 되면
			studentNickname = inputNickname.getText(); // studentNickname은 inputNickname텍스트필드에
			studentName=inputName.getText();

			firstStart(); // firstStart 메소드 실행

		}else if (e.getSource() == quitButton) { // quit버튼을 누르면
			int flag=JOptionPane.showConfirmDialog(null,"종료하시겠습니까?","confirm",JOptionPane.OK_CANCEL_OPTION);
			if(JOptionPane.CLOSED_OPTION==flag){
				System.exit(0);
			}else if(JOptionPane.YES_OPTION==flag) {
				System.exit(0); // 프로그램을 종료한다
			}
		}
	}


	@Override
	public void keyPressed(KeyEvent e) { // 키이벤트 정의
		// TODO Auto-generated method stub
		if (e.getKeyCode() == 10) { // 엔터가 눌리면
			tryCount++; // 시도 회수 1 증가
			removeAnswer(); // 정답처리 메소드 실행
			try {
				endAnswer2(); // 모두 정답후 종료 메소드 실행
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

	private void removeAnswer() { // 정답처리 메소드
		for (int i = 0; i < 9; i++) {
			if (insertDap.getText().equals(
					word_create.arr.get(i))) { /* 입력된 값이 어레이리스트의 어느 값과 일치하면 */
				arrJlabel[i].setVisible(false);
				correctCount++;
			} // 그 JLabel을 안보이게 하고, correctCount(맞은 수)를 1 증가시킨다
		}
		insertDap.setText("");
		/*
		 * JLabel Visible False 후, TextLabel을 빈칸으로 두어서 바로 입력할수 있게 한다.
		 */

	}
	//--------------------------------------------------------------start DB
	private void endAnswer2() throws Exception
	{



		// 게임이 끝났을 때의 메소드(정답을 모두 맞췄을 때)
		if (arrJlabel[0].isVisible() == false && arrJlabel[1].isVisible() == false && arrJlabel[2].isVisible() == false
				&& arrJlabel[3].isVisible() == false && arrJlabel[4].isVisible() == false
				&& arrJlabel[5].isVisible() == false && arrJlabel[6].isVisible() == false
				&& arrJlabel[7].isVisible() == false && arrJlabel[8].isVisible() == false) { // 모든
			/* JLabel이 보이지 않으면(즉,완료되면) */

// 문제로 나왔던 단어들을 게임상단에 표시해 준다

			data_rain.stop(); // 산성비 쓰레드 멈춤
			total_play_time.stop(); // 시간 타이머 쓰레드 멈춤.
			correctPercent = Math.round((correctCount / tryCount)* 100); /*
			 * 명중률을 계산함. 맞은회수/총횟수 * 100해서 소수점 버림
			 */
			icon = new ImageIcon("img/award2.jpg"); // 배경을 background3으로
			this.repaint();
			// 바꾼다
			/*
			 */
			int flag=JOptionPane.showConfirmDialog(null, "닉네임:  "+
					studentNickname+"\n"+
					"이름:  "+studentName+"\n"+"시간:  "+
					Integer.toString(total_play_time.gamePlayTime-1) + "초\n정확도:  "+
					Integer.toString(correctPercent) + "%\n","결과",JOptionPane.YES_OPTION);
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
				//선언들--------------------------------------------------------------------------------------end
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


						JLabel jj=new JLabel((c+1)+"위:  "+showName);
						JLabel jj2=new JLabel(showTime+"초");

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


					printAllJlabel2[cc].setBounds(0, 0, 500, 30); // printAllJlabel2의 범위 지정
					printAllJlabel2[cc].setFont(new Font("굴림", Font.BOLD, 25)); // printAllJlabel2의 폰트 지정
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


					printAllJlabel2[cc].setBounds(0, 0, 500, 30); // printAllJlabel2의 범위 지정
					printAllJlabel2[cc].setFont(new Font("바탕", Font.BOLD, 25)); // printAllJlabel2의 폰트 지정
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
				lb.setFont(new Font("바탕",Font.ITALIC,40));
				lb.setForeground(Color.WHITE);
				add(lb);
				this.repaint();

				quitButton = new JButton("확인");
				quitButton.setBounds(340, 480, 80, 30);
				add(quitButton);
				quitButton.addActionListener(this);

			}// 종료하기 버튼을 만들어서 투명으로 설정한후
			con1.close();
			// ActionListener 추가한다
			//--------------------------------------------------------------------DB start



			//------------------------------------------------------------------DB end

		}
	}



//--------------------------------------------------------------end DB



	private void firstStart() { // 시작하기 버튼을 눌렀을 때의 메소드(게임시작)

		/*new JFrame(studentName+"의 게임!");*/
		insertDap = new JTextField(2);
		insertDap.addKeyListener(this); // 텍스트필드에 키 이벤트 추가(엔터)
		insertDap.setFont(new Font("바탕",Font.BOLD,15));
		insertDap.setBounds(320, 493, 150, 30);
		add(insertDap); // 답을 입력하는 텍스트필드를 투명으로 만들어서 추가한다

		tajaLabel.setVisible(false);
		askNickname.setVisible(false);
		askName.setVisible(false);

		startButton.setVisible(false); // 시작 버튼 안보이게
		inputNickname.setVisible(false);
		inputName.setVisible(false);
		insertDap.setVisible(true); // 답 입력창 보이게 함

		// 배경 이미지를 두번째 이미지로 바꿈

		word_create.shuffle();// word_create클래스에서 단어순서를 섞는 shuffle메소드를 실행한다.
		for (int i = 0; i < arrJlabel.length; i++) {
			arrJlabel[i] = new JLabel(
					word_create.arr.get(i)); /*
			 * JLabel을 차례대로 랜덥값이 삽입된 어레이리스트
			 * 키값으로 하는 해쉬맵의 내용을 이름으로 정의
			 */

			arrJlabel[i].setBounds(0, 0, 150, 30); // arrJLabel의 범위 지정
			arrJlabel[i].setFont(new Font("바탕", Font.BOLD, 15)); // arrJLabel의 폰트 지정
			arrJlabel[i].setForeground(Color.WHITE);
			int num=myRandom.nextInt(300) -200;
			int num2=(i * 90)+15;
			arrJlabel[i].setLocation(num2, num); // arrJLabel의
			// 위치를
			// 지정한다.
			add(arrJlabel[i]); // arrJLabel을 패널에 추가한다

		}


		total_play_time.playTime.setVisible(true); // 시간타이머를 보이게 함
		total_play_time.start(); // 시간 타이머 쓰레드 시작
		data_rain.start(); // 산성비 쓰레드 시작

	}
	public void paintComponent(Graphics g) {
		g.drawImage(icon.getImage(), 0, 0, null); // 0,0좌표부터 이미지를 뿌림
		setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
		super.paintComponent(g);
	}



}

