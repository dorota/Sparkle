package Controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.media.j3d.Canvas3D;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import Model.World;
import View.Scene3D;

import com.sun.j3d.utils.universe.SimpleUniverse;

public class MainWindow
{
    private JFrame _frame;
    // FIXME refactor this: _sceneCanvas
    public static Canvas3D _sceneCanvas;
    private MenuPanel _menuPanel;
    private Scene3D _scene;
    private Editor _editor;
    Dimension _screenDimension;
    private World _world;
    private static MainWindow _instance = new MainWindow();
    private JMenuBar menuBar;
    private JMenu File_menu;
    private JMenuItem save_temps_menu_item;

    public static MainWindow getMainWindow()
    {
        return _instance;
    }

    /**
     * @wbp.parser.entryPoint
     */
    private MainWindow()
    {
        setScreenDimensiosn();
        _frame = new JFrame();
        _sceneCanvas = new Canvas3D( SimpleUniverse.getPreferredConfiguration() );
        _scene = Scene3D.getScene( _sceneCanvas );
        _world = World.getWorld( _scene );
        _menuPanel = new MenuPanel( _scene );
        _editor = new Editor( _world );
        menuBar = new JMenuBar();
        _frame.setJMenuBar( menuBar );
        File_menu = new JMenu( "File" );
        menuBar.add( File_menu );
        JPopupMenu.setDefaultLightWeightPopupEnabled( false );
        save_temps_menu_item = new JMenuItem( "Save temperatures" );
        save_temps_menu_item.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent arg0 )
            {
                saveCurrentTempToFileActionPerformed();
            }
        } );
        File_menu.add( save_temps_menu_item );
        initWindow();
    }

    private void setScreenDimensiosn()
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        _screenDimension = toolkit.getScreenSize();
    }

    private void saveCurrentTempToFileActionPerformed()
    {
        FileChooser fc = new FileChooser( _frame, _world );
    }

    private void initWindow()
    {
        _frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        int minWindowWidth = 1500;
        int minWindowHeight = 600;
        _frame.setMinimumSize( new Dimension( minWindowWidth, minWindowHeight ) );
        _frame.setPreferredSize( _screenDimension );
        _frame.setExtendedState( JFrame.MAXIMIZED_BOTH );
        _sceneCanvas.setBackground( Color.black );
        BorderLayout layout = new BorderLayout();
        _frame.getContentPane().setLayout( layout );
        _frame.setResizable( true );
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout( new BorderLayout() );
        mainPanel.add( _sceneCanvas, BorderLayout.CENTER );
        _editor.initComponents( _frame.getWidth() - _menuPanel.getWidth(), 200 );
        mainPanel.add( _editor, BorderLayout.SOUTH );
        // _frame.add( _editor, BorderLayout.SOUTH );
        _frame.getContentPane().add( _menuPanel, BorderLayout.EAST );
        _frame.getContentPane().add( mainPanel, BorderLayout.CENTER );
        _frame.pack();
        _frame.setVisible( true );
    }

    /**
     * @wbp.parser.entryPoint
     */
    public static void main( String[] args )
    {
        getMainWindow();
    }
}