package Interface;

public class EditorParser
{
    public static void parseLine( String lineOfText )
    {
        if( lineOfText.startsWith( COMMENT_CHAR ) )
        {
            // do nothing; it is a comment
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
