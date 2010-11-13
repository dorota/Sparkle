package Interface;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.media.j3d.Canvas3D;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.j3d.utils.universe.SimpleUniverse;

class MainWindow
{
    JFrame _frame;
    Canvas3D _sceneCanvas;
    JPanel _menuPanel;
    Scene3D _scene;

    public MainWindow()
    {
        _frame = new JFrame();
        _sceneCanvas = new Canvas3D( SimpleUniverse.getPreferredConfiguration() );
        _menuPanel = new JPanel();
        _scene = new Scene3D( _sceneCanvas );
    }

    public void init()
    {
        _frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        _frame.setMinimumSize( new Dimension( 1000, 300 ) );
        _frame.setExtendedState( JFrame.MAXIMIZED_BOTH );
        _frame.addComponentListener( new ComponentListener()
        {
            @SuppressWarnings( "deprecation" )
            public void componentResized( ComponentEvent e )
            {
                int frameWidth = _frame.getWidth();
                int frameHeight = _frame.getHeight();
                _sceneCanvas.resize( new Dimension( (int)( 0.75 * frameWidth ), frameHeight ) );
                _menuPanel.resize( new Dimension( (int)( 0.25 * frameWidth ), frameHeight ) );
            }

            public void componentMoved( ComponentEvent e )
            {
            }

            public void componentHidden( ComponentEvent e )
            {
            }

            public void componentShown( ComponentEvent e )
            {
            }
        } );
        _sceneCanvas.setBackground( Color.black );
        int frameWidth = _frame.getWidth();
        int frameHeight = _frame.getHeight();
        FlowLayout layout = new FlowLayout( FlowLayout.LEADING );
        _frame.setLayout( layout );
        _frame.setComponentOrientation( ComponentOrientation.LEFT_TO_RIGHT );
        _frame.setResizable( true );
        _sceneCanvas.setSize( new Dimension( (int)( 0.75 * frameWidth ), frameHeight ) );
        _menuPanel.setSize( new Dimension( (int)( 0.25 * frameWidth ), frameHeight ) );
        _frame.getContentPane().add( _sceneCanvas );
        _frame.pack();
        _frame.setVisible( true );
    }

    public static void main( String[] args )
    {
        MainWindow mainWindow = new MainWindow();
        mainWindow.init();
    }
}