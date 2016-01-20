package com.sproutlife.panel;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.sproutlife.GameController;
import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.step.GameStep;

public class GameMenu extends JMenuBar implements ActionListener {
    PanelController controller;
    
    //private JMenuBar mb_menu;
    private JMenu m_file, m_game, m_help;
    private JMenuItem mi_file_options, mi_file_exit;
    private JMenuItem mi_game_autofill, mi_game_play, mi_game_step, mi_game_stop, mi_game_reset;
    private JMenuItem mi_help_about, mi_help_source;
    private Action enableMutationAction;
    
    private int i_movesPerSecond = 200;
    
    public GameMenu(PanelController controller) {
        this.controller = controller;
        initActions();
        initMenu();
    }
    
    private GameModel getGameModel() {
        return controller.getGameModel();
    }    
    
    public GamePanel getGamePanel() {
        return controller.getGamePanel();
    }

    private void initMenu() {
        // Setup menu
        //mb_menu = new JMenuBar();
        
        m_file = new JMenu("File");
        this.add(m_file);
        m_game = new JMenu("Game");
        this.add(m_game);
        //m_help = new JMenu("Help");
        //this.add(m_help);
        mi_file_options = new JMenuItem("Options");
        mi_file_options.addActionListener(this);
        mi_file_exit = new JMenuItem("Exit");
        mi_file_exit.addActionListener(this);
        m_file.add(mi_file_options);
        m_file.add(new JSeparator());
        m_file.add(mi_file_exit);
        mi_game_autofill = new JMenuItem("Autofill");
        mi_game_autofill.addActionListener(this);
        mi_game_play = new JMenuItem("Play");
        mi_game_play.addActionListener(this);                        
        mi_game_step = new JMenuItem("Step");
        mi_game_step.addActionListener(this);
        mi_game_stop = new JMenuItem("Stop");
        mi_game_stop.setEnabled(false);
        mi_game_stop.addActionListener(this);
        mi_game_reset = new JMenuItem("Reset");
        mi_game_reset.addActionListener(this);
        m_game.add(mi_game_autofill);
        m_game.add(new JSeparator());
        m_game.add(mi_game_play);
        m_game.add(mi_game_step);
        m_game.add(mi_game_stop);
        m_game.add(new JMenuItem(this.enableMutationAction));
        m_game.add(mi_game_reset);
        /*
        mi_help_about = new JMenuItem("About");
        mi_help_about.addActionListener(this);
        mi_help_source = new JMenuItem("Source");
        mi_help_source.addActionListener(this);
        m_help.add(mi_help_about);
        m_help.add(mi_help_source);
        */
        // Setup game board
    }
    
    public void setPlayGame(boolean playGame) {
        getGameModel().setPlayGame(playGame);
        
        if (playGame) {
            mi_game_play.setEnabled(false);
            mi_game_stop.setEnabled(true);                        
 
        } else {
            mi_game_play.setEnabled(true);
            mi_game_stop.setEnabled(false);        
        }
    }
        
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(mi_file_exit)) {
            // Exit the game
            System.exit(0);
        } else if (ae.getSource().equals(mi_file_options)) {
            // Put up an options panel to change the number of moves per second
            final JFrame f_options = new JFrame();
            f_options.setTitle("Options");
            f_options.setSize(300,60);
            f_options.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - f_options.getWidth())/2, 
                (Toolkit.getDefaultToolkit().getScreenSize().height - f_options.getHeight())/2);
            f_options.setResizable(false);
            JPanel p_options = new JPanel();
            p_options.setOpaque(false);
            f_options.add(p_options);
            p_options.add(new JLabel("Number of moves per second:"));
            Integer[] secondOptions = {1,2,3,4,5,10,15,20};
            final JComboBox cb_seconds = new JComboBox(secondOptions);
            p_options.add(cb_seconds);
            cb_seconds.setSelectedItem(i_movesPerSecond);
            cb_seconds.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    i_movesPerSecond = (Integer)cb_seconds.getSelectedItem();
                    f_options.dispose();
                }
            });
            f_options.setVisible(true);
        } else if (ae.getSource().equals(mi_game_autofill)) {
            final JFrame f_autoFill = new JFrame();            
            f_autoFill.setTitle("Autofill");
            f_autoFill.setSize(360, 60);
            f_autoFill.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - f_autoFill.getWidth())/2, 
                (Toolkit.getDefaultToolkit().getScreenSize().height - f_autoFill.getHeight())/2);
            f_autoFill.setResizable(false);
            JPanel p_autoFill = new JPanel();
            p_autoFill.setOpaque(false);
            f_autoFill.add(p_autoFill);
            p_autoFill.add(new JLabel("What percentage should be filled? "));
            Object[] percentageOptions = {"Select",0,1,10,15,20,25,30,40,50,60,70,80,90,95};
            final JComboBox cb_percent = new JComboBox(percentageOptions);
            p_autoFill.add(cb_percent);
            cb_percent.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (cb_percent.getSelectedIndex() > 0) {
                        getGameModel().getEchosystem().resetCells();
                        getGameModel().getEchosystem().randomlyFillBoard((Integer)cb_percent.getSelectedItem());
                        getGamePanel().repaint();
                        f_autoFill.dispose();
                    }
                }
            });
            f_autoFill.setVisible(true);
        } else if (ae.getSource().equals(mi_game_reset)) {
            getGameModel().getBoard().resetBoard();
            getGamePanel().repaint();
        } else if (ae.getSource().equals(mi_game_play)) {
            setPlayGame(true);
        } else if (ae.getSource().equals(mi_game_step)) {
            getGameModel().performGameStep();
            getGamePanel().repaint();
        } else if (ae.getSource().equals(mi_game_stop)) {
            setPlayGame(false);
        } else if (ae.getSource().equals(mi_help_source)) {
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            try {
                desktop.browse(new URI("https://github.com/Burke9077/Conway-s-Game-of-Life"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Source is available on GitHub at:\nhttps://github.com/Burke9077/Conway-s-Game-of-Life", "Source", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (ae.getSource().equals(mi_help_about)) {
            JOptionPane.showMessageDialog(null, "Conway's game of life was a cellular animation devised by the mathematician John Conway.\nThis Java, swing based implementation was created by Matthew Burke.\n\nhttp://burke9077.com\nBurke9077@gmail.com\n@burke9077\n\nCreative Commons Attribution 4.0 International");
        }
    }
    
    private void initActions() {
        this.enableMutationAction = new AbstractAction("Disable Mutations") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean enabled = getGameModel().getSettings().getBoolean(Settings.MUTATION_ENABLED);               
                getGameModel().set(Settings.MUTATION_ENABLED,!enabled);
                enabled=!enabled;
                if (enabled) {
                    this.putValue(NAME, "Disable Mutations");                    
                }
                else {
                    this.putValue(NAME, "Enable Mutations");  
                }
            }
        };
    }
}
