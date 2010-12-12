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
    public Cell( int id, float temp, String material, float specificHeat, float mass )
    {
        this._id = id;
        _temp = temp;
        _mass = mass;
        _specificHeat = specificHeat;
        _material = material;
    }

    float _temp;
    int _id;
    float _heatCapacity;
    float _mass;
    float _specificHeat;
    String _material;
}

public class Demo extends Behavior
{
    // simulation parameters
    private final float brickImageLength = 0.05f;
    private final float brickLength = 50f;
    public int roomHeight = 750;
    public int roomX = 750;
    public int roomZ = 750;
    final float FIRE_TEMP = 30;
    // visualization colors
    Color3f blue = new Color3f( 0f, 0.9f, 0.9f );
    Color3f yellow = new Color3f( 1f, 1f, 0f );
    Color3f orange = new Color3f( 1f, 0.2f, 0f );
    Color3f red = new Color3f( 1f, 0f, 0f );
    float transparency = 0.9f;
    // 3d graphics & simulation needed variables
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
        univ = new SimpleUniverse();
        gr = new BranchGroup();
        Appearance app = new Appearance();
        app.setTransparencyAttributes( new TransparencyAttributes( TransparencyAttributes.FASTEST,
            transparency ) );
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
                    gr.addChild( addObject( ( k - 1 ) * brickImageLength, ( i - 1 )
                            * brickImageLength, ( j - 1 ) * brickImageLength, blue ) );
                    // building.add( new Cell( i * j + k + 2 ) );
                    int cellId = ( ( i - 1 ) * ( l_howManyBricksInX * l_howManyBricksInZ ) )
                            + ( j - 1 ) * l_howManyBricksInX + k - 1;
                    // masa i ciep³o w³aœciwe drewna
                    building.add( new Cell( cellId, 20.0f, "oxygen", 2400, 50 ) );
                    oldValues.add( new Cell( cellId, 20.0f, "oxygen", 2400, 50 ) );
                }
            }
        }
        System.out.println( building.size() );
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
            if( building.get( i )._temp >= FIRE_TEMP )
            {
                setCellColor( building.get( i )._id );
            }
            else
            {
                setCellColor( blue, building.get( i )._id );
            }
        }
    }

    // adds 3d object to be rendered
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

    // sets color of the box with given boxid to red
    public void setCellColor( int boxId )
    {
        float temp = building.get( boxId )._temp;
        float scale = clamp( temp / FIRE_TEMP, 0.0f, 1.0f );
        setCellColor( new Color3f( lerp( 0.0f, 1.0f, scale ), // red
            0.0f, // green
            lerp( 1.0f, 0.0f, scale ) ), boxId );
    }

    private float lerp( float from, float to, float scale )
    {
        return from * ( 1.0f - scale ) + to * scale;
    }

    private float clamp( float what, float min, float max )
    {
        return Math.max( Math.min( what, max ), min );
    }

    private void setCellColor( Color3f cellColor, int boxId )
    {
        Box cell = (Box)( ( (TransformGroup)( gr.getChild( boxId + 2 ) ) ).getChild( 0 ) );
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
        building.get( 0 )._temp = 10000;
        // building.get( 0 )._temp = 1500;
        // oldValues.get( 0 )._temp = 1500;
        // for( int i = 200; i < 300; ++i )
        // {
        // building.get( i )._temp = 1500;
        // oldValues.get( i )._temp = 1500;
        // }
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
        System.out.println( "temperatura startu " + this.building.get( startId )._temp );
        if( this.getLeftNeigh( startId ) != this.NO_SUCH_NEIGH )
        {
            System.out.println( "left " + this.building.get( this.getLeftNeigh( startId ) )._temp );
        }
        if( this.getRightNeigh( startId ) != this.NO_SUCH_NEIGH )
        {
            System.out
                    .println( "right " + this.building.get( this.getRightNeigh( startId ) )._temp );
        }
        if( this.getBackNeigh( startId ) != this.NO_SUCH_NEIGH )
        {
            System.out.println( "back " + this.building.get( this.getBackNeigh( startId ) )._temp );
        }
        if( this.getFrontNeigh( startId ) != this.NO_SUCH_NEIGH )
        {
            System.out
                    .println( "front " + this.building.get( this.getFrontNeigh( startId ) )._temp );
        }
        if( this.getTopNeigh( startId ) != this.NO_SUCH_NEIGH )
        {
            System.out.println( "top" + this.building.get( this.getTopNeigh( startId ) )._temp );
        }
        if( this.getBottomNeigh( startId ) != this.NO_SUCH_NEIGH )
        {
            System.out.println( "bottom "
                    + this.building.get( this.getBottomNeigh( startId ) )._temp );
        }
    }

    public static void main( String[] args )
    {
        // Get the jvm heap size.
        long heapSize = Runtime.getRuntime().totalMemory();
        // Print the jvm heap size.
        System.out.println( "Heap Size = " + heapSize );
        // int sampleTime=100;
        int timeDelay = 33;
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
                    System.out.println( "building size: " + d.building.size() );
                    if( whichTimeActionPerformed == FIRST_TIME_ACTION_PERFORMED )
                    {
                        try
                        {
                            startId = d.initFire();
                            d.updateScene();
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
                        }
                        System.out.println();
                        for( int i = 0; i < d.building.size(); ++i )
                        {
                            System.out.print( d.building.get( i )._temp );
                        }
                        System.out.println();
                        d.updateScene();
                    }
                    ++whichTimeActionPerformed;
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
        int leftNeigh = getLeftNeigh( cell._id );
        int rightNeigh = getRightNeigh( cell._id );
        int frontNeigh = getFrontNeigh( cell._id );
        int backNeigh = getBackNeigh( cell._id );
        int topNeigh = getTopNeigh( cell._id );
        int bottomNeigh = getBottomNeigh( cell._id );
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
        oldValues.get( cell._id )._temp = cell._temp;
        cell._temp = 0;
        if( getTopNeigh( cell._id ) != NO_SUCH_NEIGH )
        {
            cell._temp = topFactor * oldValues.get( getTopNeigh( cell._id ) )._temp;
            // System.out.println(cell.temp);
            ++howManyNeigh;
        }
        if( getBottomNeigh( cell._id ) != NO_SUCH_NEIGH )
        {
            cell._temp += bottomFactor * oldValues.get( getBottomNeigh( cell._id ) )._temp;
            howManyNeigh += bottomFactor;
        }
        if( getLeftNeigh( cell._id ) != NO_SUCH_NEIGH )
        {
            cell._temp += sidesFactor * oldValues.get( getLeftNeigh( cell._id ) )._temp;
            howManyNeigh += sidesFactor;
        }
        if( getRightNeigh( cell._id ) != NO_SUCH_NEIGH )
        {
            cell._temp += sidesFactor * oldValues.get( getRightNeigh( cell._id ) )._temp;
            // System.out.println(sidesFactor*oldValues.get(getRightNeigh(cell.id)).temp);
            howManyNeigh += sidesFactor;
        }
        if( getBackNeigh( cell._id ) != NO_SUCH_NEIGH )
        {
            cell._temp += sidesFactor * oldValues.get( getBackNeigh( cell._id ) )._temp;
            howManyNeigh += sidesFactor;
        }
        if( getFrontNeigh( cell._id ) != NO_SUCH_NEIGH )
        {
            cell._temp += sidesFactor * oldValues.get( getFrontNeigh( cell._id ) )._temp;
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
        cell._temp /= howManyNeigh;
        // System.out.println("srednia: "+ cell.temp);
    }

    // oblicza na podstawie algo z pere³ek nowe wartoœci temp w siatce ca
    private void updateOldValues()
    {
        for( int i = 0; i < oldValues.size(); ++i )
        {
            oldValues.get( i )._temp = building.get( i )._temp;
        }
    }

    public void conductHeat( Cell cell )
    {
        double topConstantEnergyFactor = 0.02;
        double bottomConstantEnergyFactor = 0.02;
        double sidesConstantEnergyFactor = 0.02;
        List<Cell> neighbours = makeCellNeighborsList( cell );
        updateOldValues();
        for( int i = 0; i < neighbours.size(); ++i )
        {
            cell._heatCapacity = cell._specificHeat * cell._mass;
            Cell neigh = neighbours.get( i );
            neigh._heatCapacity = neigh._specificHeat * neigh._mass;
            float l_energyFlow = building.get( neigh._id )._temp - building.get( cell._id )._temp;
            if( l_energyFlow > 0.0 ) // s¹siad ma wiêksz¹ temp, sasiad
                                     // przekazuje cieplo
            {
                l_energyFlow *= neigh._heatCapacity;
                if( getTopNeigh( neigh._id ) == cell._id ) // przekazywanie e do
                                                           // gory
                {
                    l_energyFlow *= topConstantEnergyFactor;
                }
                else if( getBottomNeigh( neigh._id ) == cell._id ) // przekzywanie
                                                                   // E do dolu
                {
                    l_energyFlow *= bottomConstantEnergyFactor;
                }
                else
                // przekazywanie E na boki
                {
                    l_energyFlow *= sidesConstantEnergyFactor;
                }
            }
            else
            {
                l_energyFlow *= cell._heatCapacity;
                if( getTopNeigh( cell._id ) == neigh._id )
                {
                    l_energyFlow *= topConstantEnergyFactor;
                }
                else if( getBottomNeigh( cell._id ) == neigh._id )
                {
                    l_energyFlow *= bottomConstantEnergyFactor;
                }
                else
                {
                    l_energyFlow *= sidesConstantEnergyFactor;
                }
            }
            // double constantEnergyFactor=0.02;
            // l_energyFlow *= constantEnergyFactor;
            System.out.println( l_energyFlow );
            neigh._temp -= l_energyFlow / neigh._heatCapacity;
            cell._temp += l_energyFlow / cell._heatCapacity;
            if( ( l_energyFlow > 0 && neigh._temp < cell._temp )
                    || ( l_energyFlow <= 0 && neigh._temp > cell._temp ) )
            {
                float l_totalEnergy = cell._heatCapacity * building.get( cell._id )._temp
                        + neigh._heatCapacity * building.get( cell._id )._temp;
                l_totalEnergy = cell._heatCapacity * cell._temp + neigh._heatCapacity * neigh._temp;
                float l_avarageTemp = l_totalEnergy / ( cell._heatCapacity + neigh._heatCapacity );
                cell._temp = l_avarageTemp;
                neigh._temp = l_avarageTemp;
            }
        }
    }
}

class CellCoordinates
{
    int x;
    int y;
    int z;
}
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.io.BufferedWriter;
// import java.io.FileWriter;
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.Date;
// import java.util.Enumeration;
// import java.util.List;
// import java.util.Random;
//
// import javax.media.j3d.AmbientLight;
// import javax.media.j3d.Appearance;
// import javax.media.j3d.Background;
// import javax.media.j3d.Behavior;
// import javax.media.j3d.BoundingSphere;
// import javax.media.j3d.BranchGroup;
// import javax.media.j3d.ColoringAttributes;
// import javax.media.j3d.Transform3D;
// import javax.media.j3d.TransformGroup;
// import javax.media.j3d.TransparencyAttributes;
// import javax.media.j3d.WakeupCriterion;
// import javax.media.j3d.WakeupOnElapsedTime;
// import javax.swing.Timer;
// import javax.vecmath.Color3f;
// import javax.vecmath.Point3d;
// import javax.vecmath.Vector3f;
//
// import com.sun.j3d.utils.geometry.Box;
// import com.sun.j3d.utils.universe.SimpleUniverse;
//
// class Cell
// {
// public Cell( int id, float temp, String material, float specificHeat, float
// mass )
// {
// this._id = id;
// _temp = temp;
// _mass = mass;
// _specificHeat = specificHeat;
// _material = material;
// }
//
// float _temp;
// int _id;
// float _heatCapacity;
// float _mass;
// float _specificHeat;
// String _material;
// }
//
// public class Demo extends Behavior
// {
// // simulation parameters
// private final float brickImageLength = 0.05f;
// private final float brickLength = 50f;
// public int roomHeight = 200;
// public int roomX = 200;
// public int roomZ = 200;
// final float FIRE_TEMP = 300;
// // visualization colors
// Color3f blue = new Color3f( 0f, 0.9f, 0.9f );
// Color3f yellow = new Color3f( 1f, 1f, 0f );
// Color3f orange = new Color3f( 1f, 0.2f, 0f );
// Color3f red = new Color3f( 1f, 0f, 0f );
// float transparency = 0.9f;
// // 3d graphics & simulation needed variables
// BranchGroup gr;
// BoundingSphere bounds = new BoundingSphere( new Point3d( 0.0, 0.0, 0.0 ),
// 100.0 );
// SimpleUniverse univ;
// long SimTime;
// int DeltaT;
// WakeupCriterion yawn;
//
// @Override
// public void initialize()
// {
// DeltaT = 100; // milliseconds
// SimTime = 0;
// yawn = new WakeupOnElapsedTime( DeltaT );
// wakeupOn( yawn );
// }
//
// @Override
// public void processStimulus( Enumeration e )
// {
// SimTime += DeltaT;
// wakeupOn( yawn );
// // updateScene();
// }
//
// public Demo()
// {
// univ = new SimpleUniverse();
// gr = new BranchGroup();
// Appearance app = new Appearance();
// app.setTransparencyAttributes( new TransparencyAttributes(
// TransparencyAttributes.FASTEST,
// transparency ) );
// // gr.addChild(new Box((float)(0.1), (float)(0.1), (float)(0.1),app));
// AmbientLight lightA = new AmbientLight();
// lightA.setColor( new Color3f( 255, 0, 100 ) );
// gr.addChild( lightA );
// Background background = new Background();
// background.setColor( new Color3f( 0.1f, 0.2f, 0.1f ) );
// background.setApplicationBounds( bounds );
// gr.addChild( background );
// // System.out.println(roomHeight/br)
// int l_howManyBricksInX = (int)( roomX / brickLength );
// int l_howManyBricksInY = (int)( roomHeight / brickLength );
// int l_howManyBricksInZ = (int)( roomZ / brickLength );
// System.out.println( "how many bricks " + l_howManyBricksInX *
// l_howManyBricksInY
// * l_howManyBricksInZ );
// for( int i = 1; i <= roomHeight / brickLength; ++i )
// {
// for( int j = 1; j <= roomZ / brickLength; ++j )
// {
// for( int k = 1; k <= roomX / brickLength; ++k )
// {
// gr.addChild( addObject( ( k - 1 ) * brickImageLength, ( i - 1 )
// * brickImageLength, ( j - 1 ) * brickImageLength, blue ) );
// // building.add( new Cell( i * j + k + 2 ) );
// int cellId = ( ( i - 1 ) * ( l_howManyBricksInX * l_howManyBricksInZ ) )
// + ( j - 1 ) * l_howManyBricksInX + k - 1;
// // masa i ciep³o w³aœciwe drewna
// building.add( new Cell( cellId, 20.0f, "oxygen", 2400, 50 ) );
// oldValues.add( new Cell( cellId, 20.0f, "oxygen", 2400, 50 ) );
// }
// }
// }
// System.out.println( building.size() );
// univ.getViewingPlatform().setNominalViewingTransform();
// univ.addBranchGraph( gr );
// }
//
// private int getRandomPosition()
// {
// Random rn = new Random( new Date().getTime() );
// return rn.nextInt( gr.numChildren() - 3 );
// }
//
// public void updateScene()
// {
// // int firePosition=getRandomPosition();
// //
// setCellColor(red,(Box)(((TransformGroup)(gr.getChild(firePosition))).getChild(0)));
// for( int i = 0; i < building.size(); ++i )
// {
// // System.out.println(building.get(i).temp);
// if( building.get( i )._temp >= FIRE_TEMP )
// {
// setCellColor( building.get( i )._id );
// }
// else
// {
// setCellColor( blue, building.get( i )._id );
// }
// }
// }
//
// // adds 3d object to be rendered
// private TransformGroup addObject( float xLoc, float yLoc, float zLoc, Color3f
// cellColor )
// {
// TransformGroup tg = new TransformGroup();
// Transform3D transform = new Transform3D();
// Vector3f vector = new Vector3f( xLoc, yLoc, zLoc );
// transform.setTranslation( vector );
// tg.setTransform( transform );
// Appearance app = new Appearance();
// ColoringAttributes coloringAttributes = new ColoringAttributes( cellColor,
// ColoringAttributes.NICEST );
// app.setColoringAttributes( coloringAttributes );
// app.setTransparencyAttributes( new TransparencyAttributes(
// TransparencyAttributes.FASTEST,
// 0.9f ) );
// // tg.addChild(new Box((scale*50), scale*50, scale*50, app));
// tg.addChild( new Box( brickImageLength / 2, brickImageLength / 2,
// brickImageLength / 2,
// Box.ENABLE_APPEARANCE_MODIFY, app ) );
// tg.getChild( 0 ).setCapability( Box.ENABLE_APPEARANCE_MODIFY );
// return tg;
// }
//
// // sets color of the box with given boxid to red
// public void setCellColor( int boxId )
// {
// setCellColor( red, boxId );
// }
//
// private void setCellColor( Color3f cellColor, int boxId )
// {
// Box cell = (Box)( ( (TransformGroup)( gr.getChild( boxId + 2 ) ) ).getChild(
// 0 ) );
// Appearance app = new Appearance();
// ColoringAttributes coloringAttributes = new ColoringAttributes( cellColor,
// ColoringAttributes.NICEST );
// app.setCapability( Appearance.ALLOW_COLORING_ATTRIBUTES_READ );
// app.setCapability( Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE );
// coloringAttributes.setCapability( ColoringAttributes.ALLOW_COLOR_WRITE );
// coloringAttributes.setCapability( ColoringAttributes.ALLOW_COLOR_READ );
// app.setColoringAttributes( coloringAttributes );
// app.setTransparencyAttributes( new TransparencyAttributes(
// TransparencyAttributes.FASTEST,
// 0.9f ) );
// cell.setAppearance( app );
// }
//
// public int initFire()
// {
// // int startOfFire=getRandomPosition();
// // int startOfFire=159;
// int startOfFire = 0;
// for( int i = 0; i < 15; ++i )
// {
// building.get( i )._temp = 1500;
// oldValues.get( i )._temp = 1500;
// }
// // if(getRightNeigh(startOfFire)!=NO_SUCH_NEIGH)
// // {
// // building.get(getRightNeigh(startOfFire)).temp=1000;
// // oldValues.get(getRightNeigh(startOfFire)).temp=1000;
// // }
// // if(getTopNeigh(startOfFire)!=NO_SUCH_NEIGH)
// // {
// // building.get(getTopNeigh(startOfFire)).temp=1000;
// // oldValues.get(getTopNeigh(startOfFire)).temp=1000;
// // }
// return startOfFire;
// }
//
// private void showCellAndNeighbours( int startId )
// {
// System.out.println( "temperatura startu " + this.building.get( startId
// )._temp );
// if( this.getLeftNeigh( startId ) != this.NO_SUCH_NEIGH )
// {
// System.out.println( "left " + this.building.get( this.getLeftNeigh( startId )
// )._temp );
// }
// if( this.getRightNeigh( startId ) != this.NO_SUCH_NEIGH )
// {
// System.out
// .println( "right " + this.building.get( this.getRightNeigh( startId ) )._temp
// );
// }
// if( this.getBackNeigh( startId ) != this.NO_SUCH_NEIGH )
// {
// System.out.println( "back " + this.building.get( this.getBackNeigh( startId )
// )._temp );
// }
// if( this.getFrontNeigh( startId ) != this.NO_SUCH_NEIGH )
// {
// System.out
// .println( "front " + this.building.get( this.getFrontNeigh( startId ) )._temp
// );
// }
// if( this.getTopNeigh( startId ) != this.NO_SUCH_NEIGH )
// {
// System.out.println( "top" + this.building.get( this.getTopNeigh( startId )
// )._temp );
// }
// if( this.getBottomNeigh( startId ) != this.NO_SUCH_NEIGH )
// {
// System.out.println( "bottom "
// + this.building.get( this.getBottomNeigh( startId ) )._temp );
// }
// }
//
// public static void main( String[] args )
// {
// // Get the jvm heap size.
// long heapSize = Runtime.getRuntime().totalMemory();
// // Print the jvm heap size.
// System.out.println( "Heap Size = " + heapSize );
// // int sampleTime=100;
// int timeDelay = 500;
// Timer samplingTimer;
// try
// {
// samplingTimer = new Timer( timeDelay, new ActionListener()
// {
// Demo d = new Demo();
// final int FIRST_TIME_ACTION_PERFORMED = 0;
// int whichTimeActionPerformed = 0;
// int startId = 2;
// BufferedWriter writer = new BufferedWriter( new FileWriter( "plik.txt" ) );
//
// public void actionPerformed( ActionEvent e )
// {
// System.out.println( "building size: " + d.building.size() );
// if( whichTimeActionPerformed == FIRST_TIME_ACTION_PERFORMED )
// {
// try
// {
// startId = d.initFire();
// d.updateScene();
// Thread.sleep( 500 );
// }
// catch( InterruptedException exception )
// {
// }
// }
// else
// {
// for( int i = 0; i < d.building.size(); ++i )
// {
// d.conductHeat( d.building.get( i ) );
// }
// System.out.println();
// for( int i = 0; i < d.building.size(); ++i )
// {
// System.out.print( d.building.get( i )._temp );
// }
// System.out.println();
// d.updateScene();
// }
// ++whichTimeActionPerformed;
// }
// } );
// samplingTimer.start();
// }
// catch( IOException e )
// {
// }
// }
//
// private final int NO_SUCH_NEIGH = -1;
// List<Cell> building = new ArrayList<Cell>();
// List<Cell> oldValues = new ArrayList<Cell>();
//
// public int getLeftNeigh( int cellId )
// {
// int howManyBrickInX = (int)( roomX / brickLength );
// if( cellId % howManyBrickInX == 0 ) // no left neigh
// {
// return NO_SUCH_NEIGH;
// }
// else
// {
// return cellId - 1;
// }
// }
//
// public int getRightNeigh( int cellId )
// {
// int howManyBrickInX = (int)( roomX / brickLength );
// // System.out.println("int right neigh"+cellId%howManyBrickInX + " "+
// // (howManyBrickInX-1));
// if( cellId % howManyBrickInX == ( howManyBrickInX - 1 ) ) // no right
// // neigh
// {
// return NO_SUCH_NEIGH;
// }
// else
// {
// return cellId + 1;
// }
// }
//
// public int getTopNeigh( int cellId )
// {
// int howManyBrickInY = (int)( roomHeight / brickLength );
// int howManyBricksInX = (int)( roomX / brickLength );
// int howManyBricksInZ = (int)( roomZ / brickLength );
// if( cellId >= ( ( howManyBrickInY - 1 ) * howManyBricksInX * howManyBricksInZ
// ) ) // no
// // top
// // neigh
// {
// return NO_SUCH_NEIGH;
// }
// else
// {
// return cellId + ( howManyBricksInX * howManyBricksInZ );
// }
// }
//
// public int getBottomNeigh( int cellId )
// {
// int howManyBrickInY = (int)( roomHeight / brickLength );
// int howManyBricksInX = (int)( roomX / brickLength );
// int howManyBricksInZ = (int)( roomZ / brickLength );
// if( cellId < ( howManyBricksInX * howManyBricksInZ ) ) // no left neigh
// {
// return NO_SUCH_NEIGH;
// }
// else
// {
// return cellId - ( howManyBricksInX * howManyBricksInZ );
// }
// }
//
// public int getFrontNeigh( int cellId )
// {
// int howManyBricksInX = (int)( roomX / brickLength );
// int howManyBricksInZ = (int)( roomZ / brickLength );
// if( ( cellId % ( howManyBricksInX * howManyBricksInZ ) ) >= ( (
// howManyBricksInZ - 1 ) * howManyBricksInX ) )
// {
// return NO_SUCH_NEIGH;
// }
// else
// {
// return cellId + howManyBricksInX;
// }
// }
//
// public int getBackNeigh( int cellId )
// {
// int howManyBricksInX = (int)( roomX / brickLength );
// int howManyBricksInZ = (int)( roomZ / brickLength );
// if( ( cellId % ( howManyBricksInX * howManyBricksInZ ) ) < howManyBricksInX )
// {
// return NO_SUCH_NEIGH;
// }
// else
// {
// return cellId - howManyBricksInX;
// }
// }
//
// private void addToNeighbours( int neighId, List<Cell> neighbours )
// {
// if( neighId != NO_SUCH_NEIGH )
// {
// neighbours.add( building.get( neighId ) );
// }
// }
//
// private List<Cell> makeCellNeighborsList( Cell cell )
// {
// List<Cell> neighbours = new ArrayList<Cell>();
// int leftNeigh = getLeftNeigh( cell._id );
// int rightNeigh = getRightNeigh( cell._id );
// int frontNeigh = getFrontNeigh( cell._id );
// int backNeigh = getBackNeigh( cell._id );
// int topNeigh = getTopNeigh( cell._id );
// int bottomNeigh = getBottomNeigh( cell._id );
// addToNeighbours( leftNeigh, neighbours );
// addToNeighbours( rightNeigh, neighbours );
// addToNeighbours( bottomNeigh, neighbours );
// addToNeighbours( topNeigh, neighbours );
// addToNeighbours( frontNeigh, neighbours );
// addToNeighbours( backNeigh, neighbours );
// return neighbours;
// }
//
// public void conductHeatByAvarage( Cell cell )
// {
// // float topFactor=0.25f;
// // float bottomFactor=0.75f;
// // float sidesFactor=0.5f;
// int topFactor = 1;
// int bottomFactor = 3;
// int sidesFactor = 2;
// int howManyNeigh = 0;
// oldValues.get( cell._id )._temp = cell._temp;
// cell._temp = 0;
// if( getTopNeigh( cell._id ) != NO_SUCH_NEIGH )
// {
// cell._temp = topFactor * oldValues.get( getTopNeigh( cell._id ) )._temp;
// // System.out.println(cell.temp);
// ++howManyNeigh;
// }
// if( getBottomNeigh( cell._id ) != NO_SUCH_NEIGH )
// {
// cell._temp += bottomFactor * oldValues.get( getBottomNeigh( cell._id )
// )._temp;
// howManyNeigh += bottomFactor;
// }
// if( getLeftNeigh( cell._id ) != NO_SUCH_NEIGH )
// {
// cell._temp += sidesFactor * oldValues.get( getLeftNeigh( cell._id ) )._temp;
// howManyNeigh += sidesFactor;
// }
// if( getRightNeigh( cell._id ) != NO_SUCH_NEIGH )
// {
// cell._temp += sidesFactor * oldValues.get( getRightNeigh( cell._id ) )._temp;
// //
// System.out.println(sidesFactor*oldValues.get(getRightNeigh(cell.id)).temp);
// howManyNeigh += sidesFactor;
// }
// if( getBackNeigh( cell._id ) != NO_SUCH_NEIGH )
// {
// cell._temp += sidesFactor * oldValues.get( getBackNeigh( cell._id ) )._temp;
// howManyNeigh += sidesFactor;
// }
// if( getFrontNeigh( cell._id ) != NO_SUCH_NEIGH )
// {
// cell._temp += sidesFactor * oldValues.get( getFrontNeigh( cell._id ) )._temp;
// howManyNeigh += sidesFactor;
// }
// // System.out.println("new building");
// // for(int i=0; i<building.size(); ++i)
// // {
// // System.out.print(building.get(i).temp);
// // }
// // System.out.println("\n old values");
// // for(int i=0; i<oldValues.size(); ++i)
// // {
// // System.out.print(oldValues.get(i).temp);
// // }
// // System.out.println(cell.id +"     "+howManyNeigh);
// cell._temp /= howManyNeigh;
// // System.out.println("srednia: "+ cell.temp);
// }
//
// // oblicza na podstawie algo z pere³ek nowe wartoœci temp w siatce ca
// private void updateOldValues()
// {
// for( int i = 0; i < oldValues.size(); ++i )
// {
// oldValues.get( i )._temp = building.get( i )._temp;
// }
// }
//
// public void conductHeat( Cell cell )
// {
// System.out.println( cell._temp );
// double constantEnergyFactor = 0.2;
// List<Cell> neighbours = makeCellNeighborsList( cell );
// updateOldValues();
// for( int i = 0; i < neighbours.size(); ++i )
// {
// cell._heatCapacity = cell._specificHeat * cell._mass;
// Cell neigh = neighbours.get( i );
// neigh._heatCapacity = neigh._specificHeat * neigh._mass;
// float l_energyFlow = building.get( neigh._id )._temp - building.get( cell._id
// )._temp;
// if( l_energyFlow > 0.0 ) // s¹siad ma wiêksz¹ temp
// {
// l_energyFlow *= neigh._heatCapacity;
// }
// else
// {
// l_energyFlow *= cell._heatCapacity;
// }
// l_energyFlow *= constantEnergyFactor;
// // System.out.println( l_energyFlow );
// neigh._temp -= l_energyFlow / neigh._heatCapacity;
// cell._temp += l_energyFlow / cell._heatCapacity;
// if( ( l_energyFlow > 0 && neigh._temp < cell._temp )
// || ( l_energyFlow <= 0 && neigh._temp > cell._temp ) )
// {
// float l_totalEnergy = cell._heatCapacity * building.get( cell._id )._temp
// + neigh._heatCapacity * building.get( cell._id )._temp;
// l_totalEnergy = cell._heatCapacity * cell._temp + neigh._heatCapacity *
// neigh._temp;
// float l_avarageTemp = l_totalEnergy / ( cell._heatCapacity +
// neigh._heatCapacity );
// cell._temp = l_avarageTemp;
// neigh._temp = l_avarageTemp;
// }
// }
// }
// }
//
// class CellCoordinates
// {
// int x;
// int y;
// int z;
// }