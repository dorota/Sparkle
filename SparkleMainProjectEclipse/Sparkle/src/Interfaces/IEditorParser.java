package Interfaces;


public interface IEditorParser
{
    void parseLine( String lineOfText, IWorld world );

    void deleteBlocks( String toDelete, IWorld world ) throws ArrayIndexOutOfBoundsException;

    void parseWholeBuilding( String text, IWorld world );
}
