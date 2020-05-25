package halfback.cmpm.view;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Color;

import com.mxgraph.view.mxGraph;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.util.mxCellRenderer;

import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.Itemset;
import halfback.cmpm.controller.ChronicleViewListener;
import halfback.cmpm.model.Chronicle;
import halfback.cmpm.model.tce.TimeConstraint;

/**
 * Visualization of a chronicle as a graph using JGraphX.
 * 
 * @author <a href="mailto:carlos.miranda_lopez@insa-rouen.fr">Carlos Miranda</a>
 */
public class ChronicleView extends JInternalFrame {

    /**
     * 
     */
    private static final long serialVersionUID = -2707712944901661772L;
    
    public static ChronicleViewListener cvl;
    
    private final Map<Itemset, Object> _itemset2vertex;

    private final mxGraph _graph;
    
    private final int _id;

    public ChronicleView(Chronicle chronicle, int frameId) {
        super("Chronicle #" + frameId, true, true, true, true);
        _id = frameId;
        setSize(400, 320);

        _itemset2vertex = new LinkedHashMap<Itemset, Object>(chronicle.size());

        _graph = new mxGraph();
        Object parent = _graph.getDefaultParent();
        mxGraphComponent graphComponent = new mxGraphComponent(_graph);

        _graph.getModel().beginUpdate();
        
        try {
            for (Itemset itemset : chronicle.getEpisode()) {
            	String id = itemset.toString();
            	String name = id.substring(0, itemset.toString().length() - 2);
                _itemset2vertex.put(itemset, _graph.insertVertex(parent, id, name, 0, 0, 50, 50, "shape=ellipse;fillColor=orange"));
            }

            for (Entry<Itemset, List<TimeConstraint>> entry : chronicle.getTrace().entrySet()) {
                for (TimeConstraint tc : entry.getValue()) {
                    _graph.insertEdge(parent, null, tc.intervalString(), _itemset2vertex.get(entry.getKey()), _itemset2vertex.get(tc.getEndEvent()));
                }
            }
        } finally {
            _graph.getModel().endUpdate();
        }

        new mxCircleLayout(_graph).execute(_graph.getDefaultParent());

        addInternalFrameListener(cvl);
        getContentPane().add(graphComponent);
        pack();
        setLocation(0, 0);
        
    }
 
    public void savePNG(String filepath) {
        BufferedImage image = mxCellRenderer.createBufferedImage(_graph, null, 1, Color.WHITE, true, null);
        try {
            ImageIO.write(image, "PNG", new File(filepath + "_jchronicle.png"));
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        }
    }
    
    public int getId() {
    	return _id;
    }
    
    public static void setInternalFrameListener(ChronicleViewListener ifl) {
    	cvl = ifl;
    }
}