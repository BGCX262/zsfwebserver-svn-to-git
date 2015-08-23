import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.image.VolatileImage;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class CustomGUI extends Canvas {   
    private VolatileImage volatileImg;   
  
    // ...   
  
    public void paint(Graphics g) {   
        // 创建硬件加速图像   
        createBackBuffer();   
  
        // 主渲染循环。VolatileImage对象可能失去内容。    
        // 这个循环会不断渲染（如果需要的话并制造）VolatileImage对象   
        // 直到渲染过程圆满完成。   
        do {   
  
            // 为该控件的Graphics配置验证VolatileImage的有效性。   
            // 如果VolatileImage对象不能匹配GraphicsConfiguration   
            // （换句话说，硬件加速不能应用在新设备上）   
            // 那我们就重建它。   
            GraphicsConfiguration gc = this.getGraphicsConfiguration();   
            int valCode = volatileImg.validate(gc);   
  
            // 以下说明设备不匹配这个硬件加速Image对象。   
            if(valCode==VolatileImage.IMAGE_INCOMPATIBLE){   
                createBackBuffer(); // 重建硬件加速Image对象。   
            }   
  
            Graphics offscreenGraphics = volatileImg.getGraphics();      
            offscreenGraphics.setColor(Color.WHITE);   
            offscreenGraphics.fillRect(0, 0, getWidth(), getHeight());   
            offscreenGraphics.setColor(Color.BLACK);   
            offscreenGraphics.drawLine(0, 0, 10, 10); // 任意的渲染逻辑   
      
            // 把缓冲画回主Graphics对象   
            g.drawImage(volatileImg, 0, 0, this);   
            // 检查内容是否丢失     
        } while(volatileImg.contentsLost());   
    }   
    
    // 以下创建新的VolatileImage对象   
    private void createBackBuffer() {   
        GraphicsConfiguration gc = getGraphicsConfiguration();   
        volatileImg = gc.createCompatibleVolatileImage(getWidth(), getHeight());   
    }   
  
    public void update(Graphics g) {   
        paint(g);   
    }   
    
    public static void main(String[] args) {
		JFrame frame = new JFrame();
		
		frame.setSize(300, 250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.add(new CustomGUI());
		frame.pack();
		frame.setVisible(true);
	}
}   