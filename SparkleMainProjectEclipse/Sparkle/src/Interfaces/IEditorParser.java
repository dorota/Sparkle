package Interfaces;

import Model.World;

public interface IEditorParser
{
    void parseLine( String lineOfText, World world );

    void deleteBlocks( String toDelete, World world ) throws ArrayIndexOutOfBoundsException;

    void parseWholeBuilding( String text, World world );
}
