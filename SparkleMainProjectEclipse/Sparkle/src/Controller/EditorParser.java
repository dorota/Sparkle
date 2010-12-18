package Controller;

import java.util.regex.Pattern;

import javax.vecmath.Point3d;

import Model.World;
import View.Scene3D;

public class EditorParser
{
    private static EditorParser _instance = new EditorParser();

    private EditorParser()
    {
    }

    public static EditorParser getInstance()
    {
        return _instance;
    }

    public static void parseLine( String lineOfText, World world )
            throws ArrayIndexOutOfBoundsException
    {
        if( lineOfText.startsWith( COMMENT_CHAR ) )
        {
            // do nothing; it is a comment
        }
        else
        {
            lineOfText.trim();
            Pattern pattern = Pattern.compile( "[()\\[\\]:,]" );
            String[] lineParts = pattern.split( lineOfText );
            // for( String s: lineParts )
            // {
            // System.out.println( s );
            // }
            int leftBottomBackCornerX = Integer.valueOf( lineParts[ 1 ] );
            int leftBottomBackCornerY = Integer.valueOf( lineParts[ 2 ] );
            int leftBottomBackCornerZ = Integer.valueOf( lineParts[ 3 ] );
            int sizeX = Integer.valueOf( lineParts[ 5 ] );
            int sizeY = Integer.valueOf( lineParts[ 6 ] );
            int sizeZ = Integer.valueOf( lineParts[ 7 ] );
            String material = lineParts[ 9 ];
            Scene3D scene = Scene3D.getScene( MainWindow._sceneCanvas );
            System.out.println( "x y z " + leftBottomBackCornerX + " " + leftBottomBackCornerY
                    + " " + leftBottomBackCornerZ );
            world.addBuildingPart( new Point3d( leftBottomBackCornerX, leftBottomBackCornerY,
                leftBottomBackCornerZ ), new Point3d( sizeX, sizeY, sizeZ ), material, scene );
        }
    }

    public static void deleteBlocks( String toDelete, World world )
            throws ArrayIndexOutOfBoundsException
    {
        if( toDelete.startsWith( COMMENT_CHAR ) )
        {
            // do nothing deleting comment
        }
        else
        {
            toDelete.trim();
            toDelete.trim();
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

    public static void parseWholeBuilding( String text, World world )
    {
        String lines[] = text.split( "\n" );
        // world.cleanWorld();
        for( String line: lines )
        {
            parseLine( line, world );
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
