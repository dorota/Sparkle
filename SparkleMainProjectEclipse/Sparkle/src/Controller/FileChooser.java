package Controller;

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Helpers.EnvSettings;
import Model.World;

public class FileChooser
{
    JFileChooser _fileChooser = new JFileChooser();
    JDialog _frame = new JDialog();

    public FileChooser( Component com, World world )
    {
        // _frame.setVisible( true );
        // _frame.add( _fileChooser );
        // _frame.setMinimumSize( new Dimension( 300, 300 ) );
        // _frame.setPreferredSize( new Dimension( 500, 500 ) );
        // _frame.setDefaultCloseOperation( JDialog.HIDE_ON_CLOSE );
        // _frame.add( _fileChooser );
        int result = _fileChooser.showOpenDialog( com );
        if( result == JFileChooser.APPROVE_OPTION )
        {
            File file = _fileChooser.getSelectedFile();
            try
            {
                BufferedWriter bw = new BufferedWriter( new FileWriter( file ) );
                for( int y = 0; y < Helpers.EnvSettings.getMAX_Y(); ++y )
                {
                    bw.write( "Floor nr: " + y + "\n" );
                    for( int z = 0; z < EnvSettings.getMAX_Z(); ++z )
                    {
                        for( int x = 0; x < EnvSettings.getMAX_X(); ++x )
                        {
                            bw.write( Double.toString( world._worldCurrentValues[ x ][ y ][ z ]
                                    .get_temp() ) + "  " );
                        }
                        bw.write( "\n\n" );
                    }
                }
                bw.close();
            }
            catch( Exception e )
            {
                JOptionPane.showMessageDialog( _frame, e.getMessage(), "File Error",
                    JOptionPane.ERROR_MESSAGE );
            }
        }
        // _fileChooser.addActionListener( new ActionListener()
        // {
        // public void actionPerformed( ActionEvent e )
        // {
        // System.out.println( e.getActionCommand() );
        // if( e.getActionCommand().equals( JFileChooser.APPROVE_OPTION ) )
        // {
        // System.out.println( "acceptu button pressed" );
        // }
        // }
        // } );
        // _frame.getContentPane().add( _fileChooser );
    }
}
