package OnetoN;

// AWT 및 랜덤, ArrayList, 소리, 타이머 관련 클래스 사용 선언
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;

// 소리 재생
import java.io.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

// 타이머
import java.util.Timer;
import java.util.TimerTask;

public class Game extends Frame implements ActionListener{
	public final static int X = 5; // 가로 길이 설정
	public final static int Y = 5; // 세로 길이 설정
	
	public final static int N = 25; // 마지막 번호 설정, 5X5 이므로 N의 최소값은 25이다.

	
	Timer timer;
	int time_s = 0, time_ms = -1; // 타이머 시간, 바로 +1 되므로 초기값은 0
	int currentNum = 1;
	int wrongCount = 0; // 틀린 횟수를 누적하기 위한 변수
	Random ran = new Random();
	ArrayList<Integer> N_box = new ArrayList(); // 중복되지 않는 랜덤 숫자를 편리하게 생성 및 관리하기 위해 ArrayList를 사용한다.
	Button btns[]; // 버튼 배열 컴포넌트 선언
	
	Game(){
		setTitle("1 TO " + N);
		setLayout(new GridLayout(X,Y)); // 버튼을 5x5 로 배치하기 위해 GridLayout 사용
		
		btns = new Button[N]; // 버튼 컴포턴트들을 배열로 생성한다.
		
		for(int i=0; i < X*Y; i++){ // 5x5 의 25개 버튼을 생성하기 위한 for 문
			btns[i] = new Button("");
			btns[i].setFont(new Font("맑은 고딕", Font.BOLD, 20));
			btns[i].addActionListener(this);
			add(btns[i]);
		}
		btns[0].setLabel("시작"); // 첫번째 버튼을 시작버튼으로 변경한다.
				
		addWindowListener(new btn_X()); // 타이틀바의 종료버튼을 누를 경우 프로그램 종료
		
		pack(); // 창 크기 조절시 컴포넌트 자동 배치를 위함.
		setSize(700,700);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("시작") || e.getActionCommand().equals("다시 시작")){
			setTitle("1 TO " + N + " - 0s");
			
			for(int i = 0; i < X*Y; i++) // N_box ArrayList에 1부터 25까지의 숫자 입력
				N_box.add(i+1);
			for(int i=0; i < X*Y; i++) // 5x5 의 25개 버튼에 ArrayList 에서 랜덤으로 꺼낸 숫자를 이름으로 지정
				btns[i].setLabel(N_box.remove(ran.nextInt(N_box.size())).toString()); // ArrayList의 Remove 메소드는 삭제와 동시에 값을 반환한다. 반환한 값을 버튼 이름으로 지정한다.
			
			// 스톱워치 타이머 실행 부분
			timer = new Timer();
			StopWatch t = new StopWatch(); // StopWatch 클래스 객체 생성
			timer.schedule(t,  0, 100); // StopWatch 객체를 타이머 스케줄로 지정, 실행시 0초 대기하며, 타이머의 interval은 0.1초로 지정
		}
		else if(e.getActionCommand().equals("") == false) { // 버튼 이름이 있을경우만, 없을경우 for문을 돌릴필요가 없음.
			for(int i=0;i < X*Y;i++){
				if(e.getSource()==btns[i])
				{
					if(btns[i].getLabel().equals(Integer.toString(currentNum))){ // 올바른 번호일 경우
						Play("Sounds/Windows Notify.wav");
						btns[i].setLabel(""); // 해당 버튼의 번호를 지운다.
						
						if (currentNum + X*Y <= N)
							// 현재번호의 +25(5X5) 값이 N을 넘지 않을 경우에만 다음 번호를 지정한다.
							// 현재번호의 +25 값이 N을 넘어버렸는데도 다음 번호를 지정한다면 마지막번호인 N보다 큰 버튼이 생성되기 때문이다.
							btns[i].setLabel(Integer.toString(currentNum + X*Y));
						
						if(currentNum == N){ // 누른 버튼이 마지막 번호일 경우 (게임 종료)
							timer.cancel(); // 스톱워치 중지
							setTitle("1 TO " + N + "  - 게임 종료, 경과시간 : " + time_s + "." + time_ms + "s, " + "틀린 횟수 : " + wrongCount);
							btns[0].setLabel("다시 시작"); // 첫번째 버튼을 시작버튼으로 변경한다.
							currentNum = 0; wrongCount = 0; // 변수 초기화
							time_s = 0; time_ms = 0; // 변수 초기화
						}
						currentNum = currentNum + 1; // 다음에 눌러야할 버튼, 현재 숫자를 지정해준다. (+1)
						break; // for문을 빠져나온다. 
					}
					// 올바른 번호가 아닐경우 (위 구문에서 break를 사용했기 때문에 else 는 필요없다.)
					if(currentNum < N + 1){ // 게임이 끝나지 않았을 경우에만 경고음 재생
						Play("Sounds/Windows Hardware Fail.wav");
						wrongCount = wrongCount + 1;
					}
				} 
			} // for 구문
		} // if - else 구문
	}
	
	public static void main(String[] args) {
		Game Main = new Game();
		Main.setSize(700, 700);
		Main.setVisible(true);
	}
	
	// 종료버튼 처리
	class btn_X extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			System.exit(0);
		}
	}
	
	// 스톱워치 타이머 메소드
	class StopWatch extends TimerTask{
		@Override // TimerTask 를 상속받았으면 run 메소드를 Override 시켜야 사용가능
		public void run(){
			if (time_ms == 9)
			{
				time_ms = -1; // 0.1초 단위, 바로 +1 되므로 초기값은 -1
				time_s = time_s + 1; // 1초 단위
			}
			time_ms = time_ms + 1;
			setTitle("1 TO " + N + " - " + time_s + "." + time_ms + "s"); // 타이틀에 시간 반영
		}
	}
	void Play(String file){
		// Play 메소드 :
		/* File 로 부터 오디오 스트림을 가져온다 (ais 객체)
			가져온 오디오 스트림을 clip 으로 재생한다.
			(File 객체 사용시에 try - catch 구문이 필수적으로 필요함. */
		
        try {
    		AudioInputStream ais = AudioSystem.getAudioInputStream(new File(file));
            Clip clip = AudioSystem.getClip();
            clip.stop();
            clip.open(ais);
            clip.start();
        }
        catch (Exception ex)
        {
        	System.out.println(ex.toString());
        }
	}
}