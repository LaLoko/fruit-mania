package com.company;

import javax.print.attribute.standard.Media;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;

class Game extends JFrame{
    Game(){
        add(new MAIN_WINDOW());
        setBounds(0,0,1920,830);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}

class MAIN_WINDOW extends JPanel implements ActionListener {

    private boolean leftDirection = false;
    private boolean rightDirection = false;
    private boolean inGame = true;
    private boolean fruit_fell = true;
    private Timer timer;
    final private int DELAY = 60;
    private int bowl_x = 600;
    final private  int bowl_y = 730;
    private int fruit_x = 0;
    private int fruit_y = 20;
    final private int fruit_size =60;
    private int czas = 0;
    private int wynik = 0;
    private  Image bowl;
    private  Image fruit;
    private  Image apple;
    private  Image pinapple;
    private  Image grape;
    JTextField score;
    JTextField time;
    private Random random = new Random();

    MAIN_WINDOW(){
        setBounds(0,0,1920,830);
        setBackground(Color.black);
        score_panel();
        game_panel();
        try {
            music();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        setFocusable(true);
        load_graphics();
        setVisible(true);
    }
    private void score_panel(){
        JPanel scoreboard = new JPanel(new GridLayout());
        scoreboard.setBackground(Color.gray);
        scoreboard.setBounds(0,0,1920,20);

        score = new JTextField("SCORE :"+wynik);
        time = new JTextField("TIME :"+czas);

        Font f = new Font("Arial",Font.ITALIC,20);

        score.setBackground(Color.gray);
        score.setFont(f);
        score.setForeground(Color.yellow);
        score.setSize(100,20);
        score.setEditable(false);
        time.setBackground(Color.gray);
        time.setFont(f);
        time.setForeground(Color.yellow);
        time.setEditable(false);

        scoreboard.add(score);
        scoreboard.add(time);
        add(BorderLayout.SOUTH,scoreboard);
    }
    private void game_panel(){
        JPanel game_panel = new JPanel();
        game_panel.setBackground(Color.black);
        addKeyListener(new TAdapter());
        timer = new Timer(DELAY, this);
        timer.start();
        add(BorderLayout.CENTER,game_panel);
    }
    private void music() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream player = AudioSystem.getAudioInputStream(new File("music.wav"));
        Clip clip = AudioSystem.getClip();
        clip.open(player);
        clip.start();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw_game(g);
        //repaint(wynik);
    }
    private void load_graphics(){
        ImageIcon bi = new ImageIcon("bowl.png");
        ImageIcon ap = new ImageIcon("apple.png");
        ImageIcon gr = new ImageIcon("grape.png");
        ImageIcon pn = new ImageIcon("pinapple.png");
        bowl = bi.getImage();
        apple = ap.getImage();
        grape = gr.getImage();
        pinapple = pn.getImage();
        apple = apple.getScaledInstance(fruit_size,fruit_size,Image.SCALE_DEFAULT);
        grape = grape.getScaledInstance(fruit_size,fruit_size,Image.SCALE_DEFAULT);
        pinapple = pinapple.getScaledInstance(fruit_size,fruit_size,Image.SCALE_DEFAULT);
        bowl = bowl.getScaledInstance(200,60,Image.SCALE_DEFAULT);
    }
    private void draw_game(Graphics g){
        if (inGame) {
            fruit_fall();
            g.drawImage(bowl, bowl_x, bowl_y, this);
            g.drawImage(fruit, fruit_x, fruit_y, this);
            score.setText("SCORE :"+wynik);
            time.setText("TIME :"+czas);
            Toolkit.getDefaultToolkit().sync();
        }
    }
    private void fruit_fall(){
        if(fruit_fell && inGame){
            fruit_x = generate_fruit_x();
            genetate_fruit();
            fruit_fell = false;
        }
        if( still_in_game()){
            fruit_fell = true;
            fruit_y = 40;
            wynik++;
        }
        if (fruit_y > 750) inGame = false;
        fruit_y+=5;
    }
    private boolean still_in_game(){
        return fruit_y + 30 == bowl_y && ((fruit_x >= bowl_x-20) && (fruit_x <= bowl_x + 155));
    }

    private int generate_fruit_x(){
        int generated = 200;
        generated = generated + random.nextInt(1200);
        return generated;
    }
    private void genetate_fruit(){
        int which = random.nextInt(3);
        if (which == 0) fruit = apple;
        if (which == 1) fruit = pinapple;
        if (which == 2) fruit = grape;
    }

    private void move(){
        if (leftDirection )bowl_x-=20;
        if (rightDirection ) bowl_x+=20;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame){
            if(bowl_x > 0 && bowl_x < 1320)  move();
            if(bowl_x == 0) bowl_x +=20;
            if(bowl_x == 1320) bowl_x-=20;
            czas++;
        }
        repaint();
    }
    private class TAdapter extends KeyAdapter {
        int key ;

        @Override
        public void keyPressed(KeyEvent e) {

             key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                rightDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                leftDirection = false;
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {
            key = e.getKeyCode();

            //super.keyReleased(e);
            if (key == KeyEvent.VK_LEFT) {
                leftDirection = false;
            }
            if (key == KeyEvent.VK_RIGHT){
                rightDirection = false;
            }

        }
    }
}


public class Main {

    public static void main(String[] args) {
	new Game();
    }
}
