package OnetoN;

// AWT �� ����, ArrayList, �Ҹ�, Ÿ�̸� ���� Ŭ���� ��� ����
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;

// �Ҹ� ���
import java.io.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

// Ÿ�̸�
import java.util.Timer;
import java.util.TimerTask;

public class Game extends Frame implements ActionListener{
	public final static int X = 5; // ���� ���� ����
	public final static int Y = 5; // ���� ���� ����
	
	public final static int N = 25; // ������ ��ȣ ����, 5X5 �̹Ƿ� N�� �ּҰ��� 25�̴�.

	
	Timer timer;
	int time_s = 0, time_ms = -1; // Ÿ�̸� �ð�, �ٷ� +1 �ǹǷ� �ʱⰪ�� 0
	int currentNum = 1;
	int wrongCount = 0; // Ʋ�� Ƚ���� �����ϱ� ���� ����
	Random ran = new Random();
	ArrayList<Integer> N_box = new ArrayList(); // �ߺ����� �ʴ� ���� ���ڸ� ���ϰ� ���� �� �����ϱ� ���� ArrayList�� ����Ѵ�.
	Button btns[]; // ��ư �迭 ������Ʈ ����
	
	Game(){
		setTitle("1 TO " + N);
		setLayout(new GridLayout(X,Y)); // ��ư�� 5x5 �� ��ġ�ϱ� ���� GridLayout ���
		
		btns = new Button[N]; // ��ư ������Ʈ���� �迭�� �����Ѵ�.
		
		for(int i=0; i < X*Y; i++){ // 5x5 �� 25�� ��ư�� �����ϱ� ���� for ��
			btns[i] = new Button("");
			btns[i].setFont(new Font("���� ���", Font.BOLD, 20));
			btns[i].addActionListener(this);
			add(btns[i]);
		}
		btns[0].setLabel("����"); // ù��° ��ư�� ���۹�ư���� �����Ѵ�.
				
		addWindowListener(new btn_X()); // Ÿ��Ʋ���� �����ư�� ���� ��� ���α׷� ����
		
		pack(); // â ũ�� ������ ������Ʈ �ڵ� ��ġ�� ����.
		setSize(700,700);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("����") || e.getActionCommand().equals("�ٽ� ����")){
			setTitle("1 TO " + N + " - 0s");
			
			for(int i = 0; i < X*Y; i++) // N_box ArrayList�� 1���� 25������ ���� �Է�
				N_box.add(i+1);
			for(int i=0; i < X*Y; i++) // 5x5 �� 25�� ��ư�� ArrayList ���� �������� ���� ���ڸ� �̸����� ����
				btns[i].setLabel(N_box.remove(ran.nextInt(N_box.size())).toString()); // ArrayList�� Remove �޼ҵ�� ������ ���ÿ� ���� ��ȯ�Ѵ�. ��ȯ�� ���� ��ư �̸����� �����Ѵ�.
			
			// �����ġ Ÿ�̸� ���� �κ�
			timer = new Timer();
			StopWatch t = new StopWatch(); // StopWatch Ŭ���� ��ü ����
			timer.schedule(t,  0, 100); // StopWatch ��ü�� Ÿ�̸� �����ٷ� ����, ����� 0�� ����ϸ�, Ÿ�̸��� interval�� 0.1�ʷ� ����
		}
		else if(e.getActionCommand().equals("") == false) { // ��ư �̸��� ������츸, ������� for���� �����ʿ䰡 ����.
			for(int i=0;i < X*Y;i++){
				if(e.getSource()==btns[i])
				{
					if(btns[i].getLabel().equals(Integer.toString(currentNum))){ // �ùٸ� ��ȣ�� ���
						Play("Sounds/Windows Notify.wav");
						btns[i].setLabel(""); // �ش� ��ư�� ��ȣ�� �����.
						
						if (currentNum + X*Y <= N)
							// �����ȣ�� +25(5X5) ���� N�� ���� ���� ��쿡�� ���� ��ȣ�� �����Ѵ�.
							// �����ȣ�� +25 ���� N�� �Ѿ���ȴµ��� ���� ��ȣ�� �����Ѵٸ� ��������ȣ�� N���� ū ��ư�� �����Ǳ� �����̴�.
							btns[i].setLabel(Integer.toString(currentNum + X*Y));
						
						if(currentNum == N){ // ���� ��ư�� ������ ��ȣ�� ��� (���� ����)
							timer.cancel(); // �����ġ ����
							setTitle("1 TO " + N + "  - ���� ����, ����ð� : " + time_s + "." + time_ms + "s, " + "Ʋ�� Ƚ�� : " + wrongCount);
							btns[0].setLabel("�ٽ� ����"); // ù��° ��ư�� ���۹�ư���� �����Ѵ�.
							currentNum = 0; wrongCount = 0; // ���� �ʱ�ȭ
							time_s = 0; time_ms = 0; // ���� �ʱ�ȭ
						}
						currentNum = currentNum + 1; // ������ �������� ��ư, ���� ���ڸ� �������ش�. (+1)
						break; // for���� �������´�. 
					}
					// �ùٸ� ��ȣ�� �ƴҰ�� (�� �������� break�� ����߱� ������ else �� �ʿ����.)
					if(currentNum < N + 1){ // ������ ������ �ʾ��� ��쿡�� ����� ���
						Play("Sounds/Windows Hardware Fail.wav");
						wrongCount = wrongCount + 1;
					}
				} 
			} // for ����
		} // if - else ����
	}
	
	public static void main(String[] args) {
		Game Main = new Game();
		Main.setSize(700, 700);
		Main.setVisible(true);
	}
	
	// �����ư ó��
	class btn_X extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			System.exit(0);
		}
	}
	
	// �����ġ Ÿ�̸� �޼ҵ�
	class StopWatch extends TimerTask{
		@Override // TimerTask �� ��ӹ޾����� run �޼ҵ带 Override ���Ѿ� ��밡��
		public void run(){
			if (time_ms == 9)
			{
				time_ms = -1; // 0.1�� ����, �ٷ� +1 �ǹǷ� �ʱⰪ�� -1
				time_s = time_s + 1; // 1�� ����
			}
			time_ms = time_ms + 1;
			setTitle("1 TO " + N + " - " + time_s + "." + time_ms + "s"); // Ÿ��Ʋ�� �ð� �ݿ�
		}
	}
	void Play(String file){
		// Play �޼ҵ� :
		/* File �� ���� ����� ��Ʈ���� �����´� (ais ��ü)
			������ ����� ��Ʈ���� clip ���� ����Ѵ�.
			(File ��ü ���ÿ� try - catch ������ �ʼ������� �ʿ���. */
		
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