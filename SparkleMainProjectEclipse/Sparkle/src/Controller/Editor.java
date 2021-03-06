/*
 * Editor.java
 *
 * Created on 2010-11-25, 22:06:30
 */
package Controller;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import Interfaces.IEditor;
import Interfaces.IEditorParser;
import Model.World;
import View.Scene3D;

/**
 * @author Dorota
 */
public class Editor extends javax.swing.JPanel implements IEditor
{
    private World _editedWorld;
    // Variables declaration - do not modify
    private java.awt.TextArea _editorTextArea;
    // End of variables declaration
    private String _textAreaContent;
    private IEditorParser _parser = new EditorParser();

    public Editor( World world )
    {
        _editedWorld = world;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    public void initComponents( int width, int height )
    {
        _editorTextArea = new java.awt.TextArea();
        this.setSize( width, height );
        BorderLayout layout = new BorderLayout();
        this.setLayout( layout );
        this.add( _editorTextArea, BorderLayout.CENTER );
        createTitledBorder();
        _editorTextArea.addKeyListener( new java.awt.event.KeyAdapter()
        {
            @Override
            public void keyPressed( java.awt.event.KeyEvent evt )
            {
                editorKeyTyped( evt );
            }
        } );
    }

    private void editorKeyTyped( java.awt.event.KeyEvent evt )
    {
        _textAreaContent = _editorTextArea.getText();
        if( evt.getKeyCode() == KeyEvent.VK_ENTER )
        {
            System.out.println( "enter pressed" );
            _editedWorld.clearMaterials( Scene3D.getScene( MainWindow._sceneCanvas ) );
            System.out.println( "ENTER PRESSED IN EDITOR\nTextbox contents:\n" + _textAreaContent );
            _parser.parseWholeBuilding( new String( _textAreaContent ), _editedWorld );
        }
    }

    private void createTitledBorder()
    {
        TitledBorder title = BorderFactory.createTitledBorder( "Building editor" );
        this.setBorder( title );
    }

    public void setText( String text )
    {
        _editorTextArea.setText( text );
    }
}
