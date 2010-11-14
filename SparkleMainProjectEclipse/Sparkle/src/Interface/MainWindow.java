package Interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.media.j3d.Canvas3D;
import javax.swing.JFrame;

import com.sun.j3d.utils.universe.SimpleUniverse;

class MainWindow
{
    private JFrame _frame;
    private Canvas3D _sceneCanvas;
    private MenuPanel _menuPanel;
    private Scene3D _scene;

    public MainWindow()
    {
        _frame = new JFrame();
        _sceneCanvas = new Canvas3D( SimpleUniverse.getPreferredConfiguration() );
        _menuPanel = new MenuPanel();
        _scene = new Scene3D( _sceneCanvas );
    }

    public void initWindow()
    {
        _frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        int minWindowWidth = 1000;
        int minWindowHeight = 300;
        _frame.setMinimumSize( new Dimension( minWindowWidth, minWindowHeight ) );
        _frame.setExtendedState( JFrame.MAXIMIZED_BOTH );
        _sceneCanvas.setBackground( Color.black );
        BorderLayout layout = new BorderLayout();
        _frame.setLayout( layout );
        _frame.setResizable( true );
        _frame.add( _menuPanel, BorderLayout.EAST );
        _frame.add( _sceneCanvas, BorderLayout.CENTER );
        _frame.pack();
        _frame.setVisible( true );
    }

    public static void main( String[] args )
    {
        MainWindow mainWindow = new MainWindow();
        mainWindow.initWindow();
    }
}