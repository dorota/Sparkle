import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.swing.Timer;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.universe.SimpleUniverse;

class Cell
{
    public Cell( int id )
    {
        this.id = id;
    }

    float temp = 20.0f;
    int id;
    float heatCapacity;
    Material material = new Material();
    double DONT_TOUCH_PART_OF_RO_FOR_OXYGEN = 36.85; // czesæ wzoru na geœtosc
    // obliczone dla powietrza
    // nie ruszaæ
    float cellLength = 0.25f;
    float mass = 0.02f;
}

class Material
{
    String name = "oxygen";
    float specificHeat = 916;
}

public class Demo extends Behavior
{
    private final float brickImageLength = 0.05f;
    private final float brickLength = 50f;
    public int roomHeight = 200;
    public int roomX = 2000;
    public int roomZ = 2000;
    Color3f blue = new Color3f( 0f, 0.9f, 0.9f );
    Color3f yellow = new Color3f( 1f, 1f, 0f );
    Color3f orange = new Color3f( 1f, 0.2f, 0f );
    Color3f red = new Color3f( 1f, 0f, 0f );
    final float WOOD_FIRE_TEMP = 200;
    BranchGroup gr;
    BoundingSphere bounds = new BoundingSphere( new Point3d( 0.0, 0.0, 0.0 ), 100.0 );
    SimpleUniverse univ;
    long SimTime;
    int DeltaT;
    WakeupCriterion yawn;

    @Override
    public void initialize()
    {
        DeltaT = 100; // milliseconds
        SimTime = 0;
        yawn = new WakeupOnElapsedTime( DeltaT );
        wakeupOn( yawn );
    }

    @Override
    public void processStimulus( Enumeration e )
    {
        SimTime += DeltaT;
        wakeupOn( yawn );
        // updateScene();
    }

    public Demo()
    {
        try
        {
            _fw = new FileWriter( "log.txt" );
        }
        catch( IOException e )
        {
            System.out.println( "cannot open the log file" );
        }
        univ = new SimpleUniverse();
        gr = new BranchGroup();
        Appearance app = new Appearance();
        app.setTransparencyAttributes( new TransparencyAttributes( TransparencyAttributes.FASTEST,
            0.9f ) );
        // gr.addChild(new Box((float)(0.1), (float)(0.1), (float)(0.1),app));
        AmbientLight lightA = new AmbientLight();
        lightA.setColor( new Color3f( 255, 0, 100 ) );
        gr.addChild( lightA );
        Background background = new Background();
        background.setColor( new Color3f( 0.1f, 0.2f, 0.1f ) );
        background.setApplicationBounds( bounds );
        gr.addChild( background );
        // System.out.println(roomHeight/br)
        int l_howManyBricksInX = (int)( roomX / brickLength );
        int l_howManyBricksInY = (int)( roomHeight / brickLength );
        int l_howManyBricksInZ = (int)( roomZ / brickLength );
        System.out.println( "how many bricks " + l_howManyBricksInX * l_howManyBricksInY
                * l_howManyBricksInZ );
        for( int i = 1; i <= roomHeight / brickLength; ++i )
        {
            for( int j = 1; j <= roomZ / brickLength; ++j )
            {
                for( int k = 1; k <= roomX / brickLength; ++k )
                {
                    // gr.addChild( addObject( ( k - 1 ) * brickImageLength, ( i
                    // - 1 )
                    // * brickImageLength, ( j - 1 ) * brickImageLength, blue )
                    // );
                    // building.add( new Cell( i * j + k + 2 ) );
                    building.add( new Cell(
                        ( ( i - 1 ) * ( l_howManyBricksInX * l_howManyBricksInZ ) ) + ( j - 1 )
                                * l_howManyBricksInX + k - 1 ) );
                    oldValues.add( new Cell(
                        ( ( i - 1 ) * ( l_howManyBricksInX * l_howManyBricksInZ ) ) + ( j - 1 )
                                * l_howManyBricksInX + k - 1 ) );
                }
            }
        }
        System.out.println( building.size() );
        // System.out.println("buillding ids: ");
        // for(int i=0; i<building.size();++i)
        // {
        // System.out.println(building.get(i).id + i);
        // }
        // System.out.println("The end of building ids");
        univ.getViewingPlatform().setNominalViewingTransform();
        univ.addBranchGraph( gr );
    }

    private int getRandomPosition()
    {
        Random rn = new Random( new Date().getTime() );
        return rn.nextInt( gr.numChildren() - 3 );
    }

    public void updateScene()
    {
        // int firePosition=getRandomPosition();
        // setCellColor(red,(Box)(((TransformGroup)(gr.getChild(firePosition))).getChild(0)));
        for( int i = 0; i < building.size(); ++i )
        {
            // System.out.println(building.get(i).temp);
            if( building.get( i ).temp >= WOOD_FIRE_TEMP )
            {
                setCellColor( building.get( i ).id );
            }
        }
    }

    private TransformGroup addObject( float xLoc, float yLoc, float zLoc, Color3f cellColor )
    {
        TransformGroup tg = new TransformGroup();
        Transform3D transform = new Transform3D();
        Vector3f vector = new Vector3f( xLoc, yLoc, zLoc );
        transform.setTranslation( vector );
        tg.setTransform( transform );
        Appearance app = new Appearance();
        ColoringAttributes coloringAttributes = new ColoringAttributes( cellColor,
            ColoringAttributes.NICEST );
        app.setColoringAttributes( coloringAttributes );
        app.setTransparencyAttributes( new TransparencyAttributes( TransparencyAttributes.FASTEST,
            0.9f ) );
        // tg.addChild(new Box((scale*50), scale*50, scale*50, app));
        tg.addChild( new Box( brickImageLength / 2, brickImageLength / 2, brickImageLength / 2,
            Box.ENABLE_APPEARANCE_MODIFY, app ) );
        tg.getChild( 0 ).setCapability( Box.ENABLE_APPEARANCE_MODIFY );
        return tg;
    }

    public void setCellColor( int boxId )
    {
        setCellColor( red, (Box)( ( (TransformGroup)( gr.getChild( boxId + 2 ) ) ).getChild( 0 ) ) );
    }

    private void setCellColor( Color3f cellColor, Box cell )
    {
        Appearance app = new Appearance();
        ColoringAttributes coloringAttributes = new ColoringAttributes( cellColor,
            ColoringAttributes.NICEST );
        app.setCapability( Appearance.ALLOW_COLORING_ATTRIBUTES_READ );
        app.setCapability( Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE );
        coloringAttributes.setCapability( ColoringAttributes.ALLOW_COLOR_WRITE );
        coloringAttributes.setCapability( ColoringAttributes.ALLOW_COLOR_READ );
        app.setColoringAttributes( coloringAttributes );
        app.setTransparencyAttributes( new TransparencyAttributes( TransparencyAttributes.FASTEST,
            0.9f ) );
        cell.setAppearance( app );
    }

    public int initFire()
    {
        // int startOfFire=getRandomPosition();
        // int startOfFire=159;
        int startOfFire = 0;
        building.get( startOfFire ).temp = 200;
        oldValues.get( startOfFire ).temp = 200;
        // if(getRightNeigh(startOfFire)!=NO_SUCH_NEIGH)
        // {
        // building.get(getRightNeigh(startOfFire)).temp=1000;
        // oldValues.get(getRightNeigh(startOfFire)).temp=1000;
        // }
        // if(getTopNeigh(startOfFire)!=NO_SUCH_NEIGH)
        // {
        // building.get(getTopNeigh(startOfFire)).temp=1000;
        // oldValues.get(getTopNeigh(startOfFire)).temp=1000;
        // }
        return startOfFire;
    }

    private void showCellAndNeighbours( int startId )
    {
        System.out.println( "temperatura startu " + this.building.get( startId ).temp );
        if( this.getLeftNeigh( startId ) != this.NO_SUCH_NEIGH )
        {
            System.out.println( "left " + this.building.get( this.getLeftNeigh( startId ) ).temp );
        }
        if( this.getRightNeigh( startId ) != this.NO_SUCH_NEIGH )
        {
            System.out.println( "right " + this.building.get( this.getRightNeigh( startId ) ).temp );
        }
        if( this.getBackNeigh( startId ) != this.NO_SUCH_NEIGH )
        {
            System.out.println( "back " + this.building.get( this.getBackNeigh( startId ) ).temp );
        }
        if( this.getFrontNeigh( startId ) != this.NO_SUCH_NEIGH )
        {
            System.out.println( "front " + this.building.get( this.getFrontNeigh( startId ) ).temp );
        }
        if( this.getTopNeigh( startId ) != this.NO_SUCH_NEIGH )
        {
            System.out.println( "top" + this.building.get( this.getTopNeigh( startId ) ).temp );
        }
        if( this.getBottomNeigh( startId ) != this.NO_SUCH_NEIGH )
        {
            System.out.println( "bottom "
                    + this.building.get( this.getBottomNeigh( startId ) ).temp );
        }
    }

    public static void main( String[] args )
    {
        // Get the jvm heap size.
        long heapSize = Runtime.getRuntime().totalMemory();
        // Print the jvm heap size.
        System.out.println( "Heap Size = " + heapSize );
        // int sampleTime=100;
        int timeDelay = 500;
        Demo d2 = new Demo();
        Timer samplingTimer;
        try
        {
            samplingTimer = new Timer( timeDelay, new ActionListener()
            {
                Demo d = new Demo();
                final int FIRST_TIME_ACTION_PERFORMED = 0;
                int whichTimeActionPerformed = 0;
                int startId = 2;
                BufferedWriter writer = new BufferedWriter( new FileWriter( "plik.txt" ) );

                public void actionPerformed( ActionEvent e )
                {
                    try
                    {
                        System.out.println( "building size: " + d.building.size() );
                        if( whichTimeActionPerformed == FIRST_TIME_ACTION_PERFORMED )
                        {
                            try
                            {
                                // System.out.println( "init" );
                                startId = d.initFire();
                                // d.showCellAndNeighbours( startId );
                                // System.out.println( startId );
                                // d.setCellColor( startId );
                                // d.updateScene();
                                Thread.sleep( 500 );
                            }
                            catch( InterruptedException exception )
                            {
                            }
                        }
                        else
                        {
                            for( int i = 0; i < d.building.size(); ++i )
                            {
                                d.conductHeat( d.building.get( i ) );
                                // d.conductHeatByAvarage(d.building.get(i));
                            }
                            // d.updateScene();
                            d.showCellAndNeighbours( startId );
                            _fw.close();
                        }
                        ++whichTimeActionPerformed;
                    }
                    catch( IOException exc )
                    {
                    }
                }
            } );
            samplingTimer.start();
        }
        catch( IOException e )
        {
        }
    }

    private final int NO_SUCH_NEIGH = -1;
    List<Cell> building = new ArrayList<Cell>();
    List<Cell> oldValues = new ArrayList<Cell>();

    public int getLeftNeigh( int cellId )
    {
        int howManyBrickInX = (int)( roomX / brickLength );
        if( cellId % howManyBrickInX == 0 ) // no left neigh
        {
            return NO_SUCH_NEIGH;
        }
        else
        {
            return cellId - 1;
        }
    }

    public int getRightNeigh( int cellId )
    {
        int howManyBrickInX = (int)( roomX / brickLength );
        // System.out.println("int right neigh"+cellId%howManyBrickInX + " "+
        // (howManyBrickInX-1));
        if( cellId % howManyBrickInX == ( howManyBrickInX - 1 ) ) // no right
        // neigh
        {
            return NO_SUCH_NEIGH;
        }
        else
        {
            return cellId + 1;
        }
    }

    public int getTopNeigh( int cellId )
    {
        int howManyBrickInY = (int)( roomHeight / brickLength );
        int howManyBricksInX = (int)( roomX / brickLength );
        int howManyBricksInZ = (int)( roomZ / brickLength );
        if( cellId >= ( ( howManyBrickInY - 1 ) * howManyBricksInX * howManyBricksInZ ) ) // no
        // top
        // neigh
        {
            return NO_SUCH_NEIGH;
        }
        else
        {
            return cellId + ( howManyBricksInX * howManyBricksInZ );
        }
    }

    public int getBottomNeigh( int cellId )
    {
        int howManyBrickInY = (int)( roomHeight / brickLength );
        int howManyBricksInX = (int)( roomX / brickLength );
        int howManyBricksInZ = (int)( roomZ / brickLength );
        if( cellId < ( howManyBricksInX * howManyBricksInZ ) ) // no left neigh
        {
            return NO_SUCH_NEIGH;
        }
        else
        {
            return cellId - ( howManyBricksInX * howManyBricksInZ );
        }
    }

    public int getFrontNeigh( int cellId )
    {
        int howManyBricksInX = (int)( roomX / brickLength );
        int howManyBricksInZ = (int)( roomZ / brickLength );
        if( ( cellId % ( howManyBricksInX * howManyBricksInZ ) ) >= ( ( howManyBricksInZ - 1 ) * howManyBricksInX ) )
        {
            return NO_SUCH_NEIGH;
        }
        else
        {
            return cellId + howManyBricksInX;
        }
    }

    public int getBackNeigh( int cellId )
    {
        int howManyBricksInX = (int)( roomX / brickLength );
        int howManyBricksInZ = (int)( roomZ / brickLength );
        if( ( cellId % ( howManyBricksInX * howManyBricksInZ ) ) < howManyBricksInX )
        {
            return NO_SUCH_NEIGH;
        }
        else
        {
            return cellId - howManyBricksInX;
        }
    }

    private void addToNeighbours( int neighId, List<Cell> neighbours )
    {
        if( neighId != NO_SUCH_NEIGH )
        {
            neighbours.add( building.get( neighId ) );
        }
    }

    private List<Cell> makeCellNeighborsList( Cell cell )
    {
        List<Cell> neighbours = new ArrayList<Cell>();
        int leftNeigh = getLeftNeigh( cell.id );
        int rightNeigh = getRightNeigh( cell.id );
        int frontNeigh = getFrontNeigh( cell.id );
        int backNeigh = getBackNeigh( cell.id );
        int topNeigh = getTopNeigh( cell.id );
        int bottomNeigh = getBottomNeigh( cell.id );
        addToNeighbours( leftNeigh, neighbours );
        addToNeighbours( rightNeigh, neighbours );
        addToNeighbours( bottomNeigh, neighbours );
        addToNeighbours( topNeigh, neighbours );
        addToNeighbours( frontNeigh, neighbours );
        addToNeighbours( backNeigh, neighbours );
        return neighbours;
    }

    public void conductHeatByAvarage( Cell cell )
    {
        // float topFactor=0.25f;
        // float bottomFactor=0.75f;
        // float sidesFactor=0.5f;
        int topFactor = 1;
        int bottomFactor = 3;
        int sidesFactor = 2;
        int howManyNeigh = 0;
        oldValues.get( cell.id ).temp = cell.temp;
        cell.temp = 0;
        if( getTopNeigh( cell.id ) != NO_SUCH_NEIGH )
        {
            cell.temp = topFactor * oldValues.get( getTopNeigh( cell.id ) ).temp;
            // System.out.println(cell.temp);
            ++howManyNeigh;
        }
        if( getBottomNeigh( cell.id ) != NO_SUCH_NEIGH )
        {
            cell.temp += bottomFactor * oldValues.get( getBottomNeigh( cell.id ) ).temp;
            howManyNeigh += bottomFactor;
        }
        if( getLeftNeigh( cell.id ) != NO_SUCH_NEIGH )
        {
            cell.temp += sidesFactor * oldValues.get( getLeftNeigh( cell.id ) ).temp;
            howManyNeigh += sidesFactor;
        }
        if( getRightNeigh( cell.id ) != NO_SUCH_NEIGH )
        {
            cell.temp += sidesFactor * oldValues.get( getRightNeigh( cell.id ) ).temp;
            // System.out.println(sidesFactor*oldValues.get(getRightNeigh(cell.id)).temp);
            howManyNeigh += sidesFactor;
        }
        if( getBackNeigh( cell.id ) != NO_SUCH_NEIGH )
        {
            cell.temp += sidesFactor * oldValues.get( getBackNeigh( cell.id ) ).temp;
            howManyNeigh += sidesFactor;
        }
        if( getFrontNeigh( cell.id ) != NO_SUCH_NEIGH )
        {
            cell.temp += sidesFactor * oldValues.get( getFrontNeigh( cell.id ) ).temp;
            howManyNeigh += sidesFactor;
        }
        // System.out.println("new building");
        // for(int i=0; i<building.size(); ++i)
        // {
        // System.out.print(building.get(i).temp);
        // }
        // System.out.println("\n old values");
        // for(int i=0; i<oldValues.size(); ++i)
        // {
        // System.out.print(oldValues.get(i).temp);
        // }
        // System.out.println(cell.id +"     "+howManyNeigh);
        cell.temp /= howManyNeigh;
        // System.out.println("srednia: "+ cell.temp);
    }

    // oblicza na podstawie algo z pere³ek nowe wartoœci temp w siatce ca
    private void updateOldValues()
    {
        for( int i = 0; i < oldValues.size(); ++i )
        {
            oldValues.get( i ).temp = building.get( i ).temp;
        }
    }

    public void conductHeat( Cell cell )
    {
        int TopConstantEnergyFlowFactor = 3;
        int BottomConstantEnergyFlowFactor = 1;
        int SidesConstantEnergyFlowFactor = 2;
        List<Cell> neighbours = makeCellNeighborsList( cell );
        updateOldValues();
        for( int i = 0; i < neighbours.size(); ++i )
        {
            // cell.calculateMass(); //mass depends on temperature; need to be
            // calculated each time
            cell.heatCapacity = cell.material.specificHeat * cell.mass;
            Cell neigh = neighbours.get( i );
            neigh.heatCapacity = neigh.material.specificHeat * neigh.mass;
            float l_energyFlow = oldValues.get( neigh.id ).temp - oldValues.get( cell.id ).temp;
            // float l_energyFlow=neigh.temp-cell.temp;
            // System.out.println(l_energyFlow);
            if( l_energyFlow > 0.0 ) // s¹siad ma wiêksz¹ temp
            {
                l_energyFlow *= neigh.heatCapacity;
            }
            else
            // ja mam wiêksz¹ temp
            {
                l_energyFlow *= cell.heatCapacity;
            }
            // ja przekazuje ciep³o do góry, lub biorê ciep³o z do³u
            // wspó³czynnik wiêkszy
            if( ( getTopNeigh( cell.id ) == neigh.id && l_energyFlow < 0 )
                    || ( getBottomNeigh( cell.id ) == neigh.id && l_energyFlow > 0 ) ) // top
            // neighbour
            {
                l_energyFlow *= TopConstantEnergyFlowFactor;
                // System.out.println("top factor");
            }
            // s¹siad z góry przekazuje mi ciep³o lub ja przekazujê na dó³
            else if( ( getBottomNeigh( cell.id ) == neigh.id && l_energyFlow < 0 )
                    || ( getTopNeigh( cell.id ) == neigh.id && l_energyFlow > 0 ) )
            {
                l_energyFlow *= BottomConstantEnergyFlowFactor;
                // System.out.println("bottom factor");
            }
            else
            {
                l_energyFlow *= SidesConstantEnergyFlowFactor;
                // System.out.println("sides factor");
            }
            System.out.println( l_energyFlow );
            neigh.temp -= l_energyFlow / neigh.heatCapacity;
            cell.temp += l_energyFlow / cell.heatCapacity;
            if( ( l_energyFlow > 0 && neigh.temp < cell.temp )
                    || ( l_energyFlow <= 0 && neigh.temp > cell.temp ) )
            {
                float l_totalEnergy = cell.heatCapacity * oldValues.get( cell.id ).temp
                        + neigh.heatCapacity * oldValues.get( cell.id ).temp;
                // float
                //
                l_totalEnergy = cell.heatCapacity * cell.temp + neigh.heatCapacity * neigh.temp;
                float l_avarageTemp = l_totalEnergy / ( cell.heatCapacity + neigh.heatCapacity );
                cell.temp = l_avarageTemp;
                neigh.temp = l_avarageTemp;
            }
        }
    }

    static FileWriter _fw;
}

class CellCoordinates
{
    int x;
    int y;
    int z;
}