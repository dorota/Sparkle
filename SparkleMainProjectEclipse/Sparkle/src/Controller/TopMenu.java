package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import Helpers.EnvSettings.FileChooserAction;
import Interfaces.IEditor;
import Interfaces.IWorld;
import Model.World;

public class TopMenu
{
    private JMenuBar menuBar;
    private JMenu File_menu;
    private JMenuItem save_temps_menu_item;
    private JMenuItem saveCellStatesMenuItem;
    private JMenuItem _readBuildingFromFileMenuItem;
    private JMenu help_menu;
    private JMenuItem about_program_menu_item;
    private JMenuItem user_guide_menu_item;
    private JFrame _frame;
    private IWorld _world;
    private IEditor _editor;

    private void saveCurrentTempToFileActionPerformed()
    {
        FileChooser fc = new FileChooser( _frame, (World)_world,
            FileChooserAction.SAVE_TEMPERATURE, _editor );
    }

    public TopMenu( JFrame frame, IWorld world, IEditor e )
    {
        _editor = e;
        _world = world;
        _frame = frame;
        menuBar = new JMenuBar();
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
        saveCellStatesMenuItem = new JMenuItem( "Save cell states" );
        saveCellStatesMenuItem.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent arg0 )
            {
                FileChooser fc = new FileChooser( _frame, (World)_world,
                    FileChooserAction.SAVE_STATES, _editor );
            }
        } );
        File_menu.add( saveCellStatesMenuItem );
        File_menu.add( save_temps_menu_item );
        _readBuildingFromFileMenuItem = new JMenuItem( "Read building from file" );
        _readBuildingFromFileMenuItem.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent arg0 )
            {
                FileChooser fc = new FileChooser( _frame, (World)_world,
                    FileChooserAction.READ_BUILDING_FROM_FILE, _editor );
            }
        } );
        File_menu.add( _readBuildingFromFileMenuItem );
        help_menu = new JMenu( "Help" );
        menuBar.add( help_menu );
        user_guide_menu_item = new JMenuItem( "User guide" );
        help_menu.add( user_guide_menu_item );
        about_program_menu_item = new JMenuItem( "About program" );
        help_menu.add( about_program_menu_item );
    }

    public JMenuBar getMenuBar()
    {
        return menuBar;
    }
}
