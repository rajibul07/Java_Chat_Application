import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class Client extends JFrame{
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    //declare components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto", Font.PLAIN,20);
        //constructor...
    public Client()
    {
        try {
            System.out.println("Sending request to server...");
            socket=new Socket("127.0.0.1",7777);
            System.out.println("Connection done");
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            //startWriting();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void createGUI()
    {
        this.setTitle("Client Messager");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //coding for components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("chat3.jfif"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        //frame for layout
        this.setLayout(new BorderLayout());
        //adding components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jscrollpane=new JScrollPane(messageArea);
        this.add(jscrollpane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        this.setVisible(true);
    }
    public void handleEvents()
    {
        messageInput.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub        
            }
            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
            }
            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                //System.out.println("Key released"+e.getKeyCode());
                if(e.getKeyCode()==10)
                {
                    //System.out.println("You have pressed enter button");
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me:"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }          
        });
    }
    public void startReading()
    {
        //Thread: Reads the data
        Runnable r1=()->{
            System.out.println("Reading started...");
            try{
                while (true)
                {
                        String msg=br.readLine();
                        if(msg.equals("exit"))
                        {
                            System.out.println("Server terminated the chat");
                            JOptionPane.showMessageDialog(this,"Server terminated the chat");
                            messageInput.setEnabled(false);
                            socket.close();
                            break;
                        }
                        //System.out.println("Server :"+msg);
                        messageArea.append("Server:"+msg+"\n");
                }
            }catch(Exception e)
            {
                System.out.println("Connection terminated");
            }
        };
        new Thread(r1).start();
    }
    public void startWriting()
    {
        //Thread: Writes the data and sends the data
        Runnable r2=()->{
            try{
                while(true&& !socket.isClosed())
                {
                    System.out.println("Writing started...");
                    BufferedReader br1= new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();
                    out.println(content);
                    out.flush();
                }
            }catch(Exception e)
            {
                System.out.println("Connection terminated");
            }
        };
        new Thread(r2).start();
    }
    public static void main(String[] args) {
        System.out.println("Client started...");
        new Client();
    }
}
