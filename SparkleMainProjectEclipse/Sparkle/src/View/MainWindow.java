package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.media.j3d.Canvas3D;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Controller.Editor;
import Controller.MenuPanel;
import Model.World;

import com.sun.j3d.utils.universe.SimpleUniverse;

class MainWindow
{
    private JFrame _frame;
    private Canvas3D _sceneCanvas;
    private MenuPanel _menuPanel;
    private Scene3D _scene;
    private Editor _editor;
    Dimension _screenDimension;
    private World _world;

    public MainWindow()
    {
        setScreenDimensiosn();
        _frame = new JFrame();
        _sceneCanvas = new Canvas3D( SimpleUniverse.getPreferredConfiguration() );
        _scene = new Scene3D( _sceneCanvas );
        _world = new World( _scene );
        _world.initWorld( _scene );
        _menuPanel = new MenuPanel( _scene );
        _editor = new Editor();
        initWindow();
    }

    private void setScreenDimensiosn()
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        _screenDimension = toolkit.getScreenSize();
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
        _frame.setLayout( layout );
        _frame.setResizable( true );
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout( new BorderLayout() );
        mainPanel.add( _sceneCanvas, BorderLayout.CENTER );
        _editor.initComponents( _frame.getWidth() - _menuPanel.getWidth(), 200 );
        mainPanel.add( _editor, BorderLayout.SOUTH );
        // _frame.add( _editor, BorderLayout.SOUTH );
        _frame.add( _menuPanel, BorderLayout.EAST );
        _frame.add( mainPanel, BorderLayout.CENTER );
        _frame.pack();
        _frame.setVisible( true );
    }

    public static void main( String[] args )
    {
        MainWindow mainWindow = new MainWindow();
    }
}