import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import java.security.KeyStore.Builder;
import java.util.*;
import com.sun.j3d.utils.universe.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.ConvolveOp;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.Timer;


import javax.vecmath.*;


class Cell
{
	public Cell(int id)
	{
		this.id=id;
	}
	float temp=20.0f;
	int id;
	float heatCapacity;
	Material material=new Material();
	double DONT_TOUCH_PART_OF_RO_FOR_OXYGEN=36.85; //czesæ wzoru na geœtosc obliczone dla powietrza nie ruszaæ
	float cellLength=0.25f;
	float mass=0.02f;	 
}
class Material
{
	String name="oxygen";
	float specificHeat=916;
}

public class Demo extends Behavior{
	
	private float brickImageLength=0.1f;
	private float brickLength=50;
	public int roomHeight=600;
	public int roomX=400;
	public int roomZ=200;
	Color3f blue = new Color3f(0f,0.9f,0.9f);
	Color3f yellow= new Color3f(1f,1f,0f);
	Color3f orange=new Color3f(1f,0.2f, 0f);
	Color3f red = new Color3f(1f,0f,0f);
	final float WOOD_FIRE_TEMP=100;
	BranchGroup gr;
	BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
	        100.0);
	SimpleUniverse univ;

	long SimTime;
    int DeltaT;
    WakeupCriterion yawn;
	public void initialize()
	{
		DeltaT = 25;    //      milliseconds
        SimTime = 0;
        yawn = new WakeupOnElapsedTime(DeltaT);
        wakeupOn(yawn);
	}
	public void processStimulus(Enumeration e)
	{
		SimTime += DeltaT;
        wakeupOn(yawn);
        updateScene();
	}
	
	
	public Demo(){
		univ = new SimpleUniverse();
		gr = new BranchGroup();
		Appearance app=new Appearance();
		app.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.9f));
//		gr.addChild(new Box((float)(0.1), (float)(0.1), (float)(0.1),app));
		AmbientLight lightA = new AmbientLight();
		lightA.setColor(new Color3f(255,0,100));
		gr.addChild(lightA);
		Background background = new Background();
	    background.setColor(new Color3f(0.1f, 0.2f, 0.1f));
	    background.setApplicationBounds(bounds);
	    gr.addChild(background);
//	    System.out.println(roomHeight/br)
	    int l_howManyBricksInX=(int)(roomX/brickLength);
	    int l_howManyBricksInY=(int)(roomHeight /brickLength);
	    int l_howManyBricksInZ=(int)(roomZ/brickLength);
	    
		for(int i=1; i<=roomHeight/brickLength; ++i)
		{
			for(int j=1; j<=roomZ/brickLength; ++j)
			{
				for(int k=1; k<=roomX/brickLength;++k)
				{
					gr.addChild(addObject((k-1)*brickImageLength,
							(i-1)*brickImageLength, (j-1)*brickImageLength,blue));
//					building.add(new Cell(i*j+k+2));
					building.add(new Cell(((i-1)*(l_howManyBricksInX*l_howManyBricksInZ))+
							(j-1)*l_howManyBricksInX+k-1));
				}
			}
		}
		
		System.out.println(building.size());
//		System.out.println("buillding ids: ");
//		for(int i=0; i<building.size();++i)
//		{
//			System.out.println(building.get(i).id +  i);
//		}
//		System.out.println("The end of building ids");

		univ.getViewingPlatform().setNominalViewingTransform();
		univ.addBranchGraph(gr);
	}
	
	private int getRandomPosition()
	{
		Random rn=new Random(new Date().getTime());
		return rn.nextInt(gr.numChildren()-3);
	}
	public void updateScene()
	{
//		int firePosition=getRandomPosition();
//		setCellColor(red,(Box)(((TransformGroup)(gr.getChild(firePosition))).getChild(0)));
		for(int i=0; i<building.size(); ++i)
		{
//			System.out.println(building.get(i).temp);
			if(building.get(i).temp>=WOOD_FIRE_TEMP)
			{
				setCellColor(building.get(i).id);
			}
		}
	}
	
	private TransformGroup addObject(float xLoc, float yLoc, float zLoc, Color3f cellColor)
	{
		TransformGroup tg = new TransformGroup();
		Transform3D transform = new Transform3D();
		Vector3f vector = new Vector3f( xLoc, yLoc, zLoc);
		transform.setTranslation(vector);
		tg.setTransform(transform);
		Appearance app=new Appearance();
		ColoringAttributes coloringAttributes = new ColoringAttributes(cellColor,
				ColoringAttributes.NICEST);
		app.setColoringAttributes(coloringAttributes);
		app.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.9f));
//		tg.addChild(new Box((scale*50), scale*50, scale*50, app));
		tg.addChild(new Box(brickImageLength/2,brickImageLength/2,brickImageLength/2,Box.ENABLE_APPEARANCE_MODIFY,app));
		tg.getChild(0).setCapability(Box.ENABLE_APPEARANCE_MODIFY);
		return tg;
		
	}
	public void setCellColor( int boxId)
	{
		setCellColor(red,(Box)(((TransformGroup)(gr.getChild(boxId+2))).getChild(0)));
	}
	private void setCellColor(Color3f cellColor,Box cell)
	{
		Appearance app=new Appearance();
		ColoringAttributes coloringAttributes = new ColoringAttributes(cellColor,
				ColoringAttributes.NICEST);
		app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
        app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        coloringAttributes.setCapability(ColoringAttributes.ALLOW_COLOR_WRITE);
        coloringAttributes.setCapability(ColoringAttributes.ALLOW_COLOR_READ);
		app.setColoringAttributes(coloringAttributes);
		app.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.9f));
		cell.setAppearance(app);
	}
	public int initFire()
	{
		int startOfFire=getRandomPosition();
//		int startOfFire=159;
		building.get(startOfFire).temp=500;
		return startOfFire;
		
	}
	public static void main(String[] args) {
		
//		int sampleTime=100;
		int timeDelay=40;
		Timer samplingTimer;
		try
		{
		samplingTimer = new Timer(timeDelay, 
				new ActionListener()
				{
					Demo d=new Demo();
					final int FIRST_TIME_ACTION_PERFORMED=0;
					int whichTimeActionPerformed=0;
					int startId=2;
					BufferedWriter writer=new BufferedWriter(new FileWriter("plik.txt"));
					//odpowiada za zegar symulacji i uruchamianie conductHeat w równych odstêpach czasu.
					//w pierwszym kroku po¿ar jest inicjalizowany  - losowane Ÿród³o
					// w ka¿dym nastepnym obliczane sa nowe temperatury i updateowana scena.
					// update sceny polega na tym, ¿e je¿eli temp wy¿sza od Flashpoint dla drewna (zakladamy, ¿e paliwem jest drewno i w powetrzu s¹ tylko opary drewna)
					//to kolor komórki ma siê zmieniaæ na czerwony
					//wszystkie komorki ca przechowywane w jednej wielkiej liscie building a wszystkie im odpowidnie graficzne odpowiedniki (boxy) s¹ dziecmi branchGroup
					public void actionPerformed(ActionEvent e)
					{
						System.out.println("building size: "+d.building.size());
						if(whichTimeActionPerformed==FIRST_TIME_ACTION_PERFORMED)
						{
							System.out.println("init");
							startId=d.initFire();
							System.out.println(startId);
							
//							for(int i=0; i<d.building.size(); ++i)
//							{
//								System.out.println(d.building.get(i).temp);
//							}
						}
						else
						{							
							for(int i=0; i<d.building.size(); ++i)
							{
								d.conductHeat(d.building.get(i));
							}
							d.updateScene();
							System.out.println("temperatura startu "+d.building.get(startId).temp);
							if(d.getLeftNeigh(startId)!=d.NO_SUCH_NEIGH)
							{
							System.out.println("left "+d.building.get(d.getLeftNeigh(startId)).temp);
							}
							if(d.getRightNeigh(startId)!=d.NO_SUCH_NEIGH)
							{
							System.out.println("right "+d.building.get(d.getRightNeigh(startId)).temp);
							}
							if(d.getBackNeigh(startId)!=d.NO_SUCH_NEIGH)
							{
							System.out.println("back "+d.building.get(d.getBackNeigh(startId)).temp);
							}
							if(d.getFrontNeigh(startId)!=d.NO_SUCH_NEIGH)
							{
							System.out.println("front "+d.building.get(d.getFrontNeigh(startId)).temp);
							}
							if(d.getTopNeigh(startId)!=d.NO_SUCH_NEIGH)
							{
							System.out.println("top"+d.building.get(d.getTopNeigh(startId)).temp);
							}
							if(d.getBottomNeigh(startId)!=d.NO_SUCH_NEIGH)
							{
							System.out.println("bottom "+d.building.get(d.getBottomNeigh(startId)).temp);
							}
						}						
						++whichTimeActionPerformed;
					}
				});
				samplingTimer.start();
		}
		catch(IOException e)
		{
			
		}
	}	
	
	private int NO_SUCH_NEIGH=-1;
	List<Cell> building=new ArrayList<Cell>();
	public int getLeftNeigh(int cellId)
	{
		int howManyBrickInX=(int)(roomX/brickLength);
		if(cellId%howManyBrickInX==0) //no left neigh
		{
			return NO_SUCH_NEIGH;
		}
		else
		{
			return cellId-1;
		}
	}
	public int getRightNeigh(int cellId)
	{
		int howManyBrickInX=(int)(roomX/brickLength);
		System.out.println("int right neigh"+cellId%howManyBrickInX + " "+ (howManyBrickInX-1));
		if(cellId%howManyBrickInX==(howManyBrickInX-1)) //no right neigh
		{
			return NO_SUCH_NEIGH;
		}
		else
		{
			return cellId+1;
		}
	}
	public int getTopNeigh(int cellId)
	{
		int howManyBrickInY=(int)(roomHeight/brickLength);
		int howManyBricksInX=(int)(roomX/brickLength);
		int howManyBricksInZ=(int)(roomZ/brickLength);
		if(cellId>=((howManyBrickInY-1)*howManyBricksInX*howManyBricksInZ)) //no top neigh
		{
			return NO_SUCH_NEIGH;
		}
		else
		{
			return cellId+(howManyBricksInX*howManyBricksInZ);
		}
	}
	public int getBottomNeigh(int cellId)
	{
		int howManyBrickInY=(int)(roomHeight/brickLength);
		int howManyBricksInX=(int)(roomX/brickLength);
		int howManyBricksInZ=(int)(roomZ/brickLength);
		if(cellId<(howManyBricksInX*howManyBricksInZ)) //no left neigh
		{
			return NO_SUCH_NEIGH;
		}
		else
		{
			return cellId-(howManyBricksInX*howManyBricksInZ);
		}
	}
	public int getFrontNeigh(int cellId)
	{
		int howManyBricksInX=(int)(roomX/brickLength);
		int howManyBricksInZ=(int)(roomZ/brickLength);
		if((cellId%(howManyBricksInX*howManyBricksInZ))>=((howManyBricksInZ-1)*howManyBricksInX))
		{
			return NO_SUCH_NEIGH;
		}
		else
		{
			return cellId+howManyBricksInX;
		}
	}
	public int getBackNeigh(int cellId)
	{
		int howManyBricksInX=(int)(roomX/brickLength);
		int howManyBricksInZ=(int)(roomZ/brickLength);
		if((cellId%(howManyBricksInX*howManyBricksInZ))<howManyBricksInX)
		{
			return NO_SUCH_NEIGH;
		}
		else
		{
			return cellId-howManyBricksInX;
		}
	}
	private void addToNeighbours(int neighId, List<Cell>neighbours)
	{
		if(neighId!=NO_SUCH_NEIGH)
		{
			neighbours.add(building.get(neighId));
		}
	}
	//oblicza na podstawie algo z pere³ek nowe wartoœci temp w siatce ca

	public void conductHeat(Cell cell)
	{
		int TopConstantEnergyFlowFactor=15;
		int BottomConstantEnergyFlowFactor=5;
		int SidesConstantEnergyFlowFactor=10;		
		List<Cell> neighbours=new ArrayList<Cell>();
		int leftNeigh=getLeftNeigh(cell.id);
		int rightNeigh=getRightNeigh(cell.id);
		int frontNeigh=getFrontNeigh(cell.id);
		int backNeigh=getBackNeigh(cell.id);
		int topNeigh=getTopNeigh(cell.id);
		int bottomNeigh=getBottomNeigh(cell.id);
		addToNeighbours(leftNeigh, neighbours);
		addToNeighbours(rightNeigh, neighbours);
		addToNeighbours(bottomNeigh, neighbours);
		addToNeighbours(topNeigh, neighbours);
		addToNeighbours(frontNeigh, neighbours);
		addToNeighbours(backNeigh, neighbours);
		for(int i=0; i<neighbours.size();++i)
		{
//			cell.calculateMass(); //mass depends on temperature; need to be calculated each time
			cell.heatCapacity=cell.material.specificHeat*cell.mass;
			Cell neigh=neighbours.get(i);
			neigh.heatCapacity=neigh.material.specificHeat*neigh.mass;
			float l_energyFlow=neigh.temp-cell.temp;
			if(l_energyFlow>0.0)
			{
				l_energyFlow*=neigh.heatCapacity;
			}
			else
			{
				l_energyFlow*=cell.heatCapacity;
			}
			if(getTopNeigh(cell.id)==neigh.id) //top neighbour
			{
				l_energyFlow*=TopConstantEnergyFlowFactor;
			}
			else if(getBottomNeigh(cell.id)==neigh.id)
			{
				l_energyFlow*=BottomConstantEnergyFlowFactor;
			}
			else
			{
				l_energyFlow*=SidesConstantEnergyFlowFactor;
			}
			neigh.temp-=l_energyFlow/neigh.heatCapacity;
			cell.temp+=l_energyFlow/cell.heatCapacity;
			
			if((l_energyFlow>0 && neigh.temp<cell.temp) ||
					(l_energyFlow<=0 && neigh.temp>cell.temp))
			{
				float l_totalEnergy=cell.heatCapacity*cell.temp+neigh.heatCapacity*neigh.temp;
				float l_avarageTemp=l_totalEnergy/(cell.heatCapacity+neigh.heatCapacity);
				cell.temp=l_avarageTemp;
				neigh.temp=l_avarageTemp;				
			}
		}
	}
//	private CellCoordinates cellIdToString(int cellId)
//	{
//		CellCoordinates cell=new CellCoordinates();
//		int howManyInX=(int)(roomX/brickLength);
//		int howManyInY=(int)(roomHeight/brickLength);
//		int howManyInZ=(int)(rooZ/brickLength);
//		cell.x=(cellId-2)%howManyInX;
//		cell.y=
//		return 
//	}
}

class CellCoordinates
{
	int x;
	int y;
	int z;
}