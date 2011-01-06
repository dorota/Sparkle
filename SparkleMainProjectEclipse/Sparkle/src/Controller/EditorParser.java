package Controller;

import java.util.regex.Pattern;

import javax.vecmath.Point3d;

import Interfaces.IEditorParser;
import Model.World;
import View.Scene3D;

public class EditorParser implements IEditorParser
{
    public void parseLine( String lineOfText, World world )
    {
        if( lineOfText.equals( "" ) )
        {
            return; // sometimes we get empty string
        }
        if( lineOfText.startsWith( COMMENT_CHAR ) || lineOfText.substring( 0, 1 ).equals( "#" ) )
        {
            // do nothing; it is a comment
        }
        else
        {
            try
            {
                lineOfText = lineOfText.trim();
                Pattern pattern = Pattern.compile( "[()\\[\\]:,]" );
                String[] lineParts = pattern.split( lineOfText );
                int leftBottomBackCornerX = Integer.valueOf( lineParts[ 1 ] );
                int leftBottomBackCornerY = Integer.valueOf( lineParts[ 2 ] );
                int leftBottomBackCornerZ = Integer.valueOf( lineParts[ 3 ] );
                int sizeX = Integer.valueOf( lineParts[ 5 ] );
                int sizeY = Integer.valueOf( lineParts[ 6 ] );
                int sizeZ = Integer.valueOf( lineParts[ 7 ] );
                String material = new String( lineParts[ 9 ] );
                Scene3D scene = Scene3D.getScene( MainWindow._sceneCanvas );
                world.addBuildingPart( new Point3d( leftBottomBackCornerX, leftBottomBackCornerY,
                    leftBottomBackCornerZ ), new Point3d( sizeX, sizeY, sizeZ ), material.trim(),
                    scene );
            }
            catch( Exception e )
            {
                // stuff happens...
                //
            }
        }
    }

    public void deleteBlocks( String toDelete, World world ) throws ArrayIndexOutOfBoundsException
    {
        if( toDelete.startsWith( COMMENT_CHAR ) )
        {
            // do nothing deleting comment
        }
        else
        {
            toDelete = toDelete.trim();
            toDelete = toDelete.trim();
            Pattern pattern = Pattern.compile( "[()\\[\\]:,]" );
            String[] lineParts = pattern.split( toDelete );
            int leftBottomBackCornerX = Integer.valueOf( lineParts[ 1 ] );
            int leftBottomBackCornerY = Integer.valueOf( lineParts[ 2 ] );
            int leftBottomBackCornerZ = Integer.valueOf( lineParts[ 3 ] );
            int sizeX = Integer.valueOf( lineParts[ 5 ] );
            int sizeY = Integer.valueOf( lineParts[ 6 ] );
            int sizeZ = Integer.valueOf( lineParts[ 7 ] );
            String material = "Air";
            Scene3D scene = Scene3D.getScene( MainWindow._sceneCanvas );
            world.addBuildingPart( new Point3d( leftBottomBackCornerX, leftBottomBackCornerY,
                leftBottomBackCornerZ ), new Point3d( sizeX, sizeY, sizeZ ), material, scene );
        }
    }

    public void parseWholeBuilding( String text, World world )
    {
        // String textAreaContentWithoutWhiteSpaces = text.trim();
        String lines[] = text.split( "[\\r\\n]+" );
        for( int i = 0; i < lines.length; ++i )
        {
            parseLine( lines[ i ], world );
        }
    }

    public static String getLastLine( String textAreaContent )
    {
        String textAreaContentWithoutWhiteSpaces = textAreaContent.trim();
        String[] lines = textAreaContentWithoutWhiteSpaces.split( "\n" );
        int linesLength = lines.length;
        return lines[ linesLength - 1 ];
    }

    private final static String COMMENT_CHAR = "#";
}
