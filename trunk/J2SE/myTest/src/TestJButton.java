import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;



public class TestJButton extends JFrame {
	
	JButton btn = new JButton("button");
	
	public TestJButton() {
		super("test frame");
		
		setLayout(new FlowLayout(FlowLayout.CENTER));
		
		setBounds(100, 100, 300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		btn.setMnemonic(KeyEvent.VK_B);
		btn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				System.out.println("pressed~");
			}
		});
		
		add(btn);
		
	}
	
	public static void main(String[] args) {
		new TestJButton().setVisible(true);
	}

}
