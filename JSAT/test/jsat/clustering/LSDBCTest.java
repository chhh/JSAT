package jsat.clustering;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import jsat.SimpleDataSet;
import jsat.classifiers.DataPoint;
import jsat.distributions.Normal;
import jsat.utils.GridDataGenerator;
import jsat.utils.SystemInfo;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Edward Raff
 */
public class LSDBCTest
{
    
    public LSDBCTest()
    {
    }

    static private LSDBC algo;
    static private SimpleDataSet easyData10;
    static private ExecutorService ex;

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        algo = new LSDBC();
        GridDataGenerator gdg = new GridDataGenerator(new Normal(0, 0.10), new Random(12), 2, 5);
        easyData10 = gdg.generateData(40);
        ex = Executors.newFixedThreadPool(SystemInfo.LogicalCores);
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
        ex.shutdown();
    }
    
    @Before
    public void setUp()
    {
        
    }

    @Test
    public void testCluster_DataSet()
    {
        System.out.println("cluster(dataset)");
        Clusterer toUse = algo.clone();
        List<List<DataPoint>> clusters = toUse.cluster(easyData10);
        assertEquals(10, clusters.size());
        Set<Integer> seenBefore = new HashSet<Integer>();
        for(List<DataPoint> cluster :  clusters)
        {
            int thisClass = cluster.get(0).getCategoricalValue(0);
            assertFalse(seenBefore.contains(thisClass));
            for(DataPoint dp : cluster)
                assertEquals(thisClass, dp.getCategoricalValue(0));
        }
    }
    
    @Test
    public void testCluster_DataSet_ExecutorService()
    {
        System.out.println("cluster(dataset, ExecutorService)");
        Clusterer toUse = algo.clone();
        List<List<DataPoint>> clusters = toUse.cluster(easyData10, ex);
        assertEquals(10, clusters.size());
        Set<Integer> seenBefore = new HashSet<Integer>();
        for(List<DataPoint> cluster :  clusters)
        {
            int thisClass = cluster.get(0).getCategoricalValue(0);
            assertFalse(seenBefore.contains(thisClass));
            for(DataPoint dp : cluster)
                assertEquals(thisClass, dp.getCategoricalValue(0));
        }
    }
}
