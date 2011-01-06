package Controller;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Helpers.EnvSettings;
import Helpers.EnvSettings.FileChooserAction;
import Interfaces.IEditor;
import Interfaces.IEditorParser;
import Model.World;

public class FileChooser
{
    JFileChooser _fileChooser = new JFileChooser();
    JDialog _frame = new JDialog();
    IEditorParser _parser = new EditorParser();

    public FileChooser( Component com, World world, EnvSettings.FileChooserAction fileChooseAction,
            IEditor editor )
    {
        int result = _fileChooser.showOpenDialog( com );
        if( result == JFileChooser.APPROVE_OPTION )
        {
            File file = _fileChooser.getSelectedFile();
            try
            {
                if( fileChooseAction.equals( FileChooserAction.SAVE_TEMPERATURE )
                        || fileChooseAction.equals( FileChooserAction.SAVE_STATES ) )
                { // save sth to file
                    BufferedWriter bw = new BufferedWriter( new FileWriter( file ) );
                    for( int y = 0; y < Helpers.EnvSettings.getMAX_Y(); ++y )
                    {
                        bw.write( "Floor nr: " + y + "\n" );
                        for( int z = 0; z < EnvSettings.getMAX_Z(); ++z )
                        {
                            for( int x = 0; x < EnvSettings.getMAX_X(); ++x )
                            {
                                if( fileChooseAction.equals( FileChooserAction.SAVE_TEMPERATURE ) )
                                {
                                    bw.write( Double
                                            .toString( world._worldCurrentValues[ x ][ y ][ z ]
                                                    .get_temp() )
                                            + "  " );
                                }
                                else if( fileChooseAction.equals( FileChooserAction.SAVE_STATES ) )
                                {
                                    bw.write( world._worldCurrentValues[ x ][ y ][ z ]
                                            .get_cellState().toString() );
                                }
                            }
                            bw.write( "\n\n" );
                        }
                    }
                    bw.close();
                }
                else
                // read building from file
                {
                    BufferedReader br = new BufferedReader( new FileReader( file ) );
                    String line = "";
                    String fileContent = "";
                    while( ( line = br.readLine() ) != null )
                    {
                        fileContent += line + "\n";
                    }
                    br.close();
                    _parser.parseWholeBuilding( fileContent, world );
                    editor.setText( fileContent );
                }
            }
            catch( IOException e )
            {
                JOptionPane.showMessageDialog( _frame, e.getMessage(), "File Error",
                    JOptionPane.ERROR_MESSAGE );
            }
        }
    }
}
