package life.qbic.voronoi.io.writer;

import kn.uni.voronoitreemap.interfaces.StatusObject;
import kn.uni.voronoitreemap.j2d.PolygonSimple;
import kn.uni.voronoitreemap.j2d.Site;
import kn.uni.voronoitreemap.treemap.VoroNode;
import kn.uni.voronoitreemap.treemap.VoronoiTreemap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * code adopted from https://github.com/ArlindNocaj/Voronoi-Treemap-Library/blob/master/src/kn/uni/voronoitreemap/IO/WriteStatusObject.java
 * No longer adding .txt as default file suffix
 * Some refactoring and logging improvements
 */
public class TreeWriteStatusObject implements StatusObject {

    private static final Logger LOG = LogManager.getLogger(TreeWriteStatusObject.class);

    BufferedWriter bufferedWriter;
    private VoronoiTreemap voronoiTreemap;

    public TreeWriteStatusObject(String fileName, VoronoiTreemap voronoiTreemap) {
        this.voronoiTreemap = voronoiTreemap;

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(fileName));
            String header = "nodeId;parentID;name;weight;hierarchyLevel;sitePosX;sitePosY;siteWeight;polygonPoints x1,y2,x2,y2\n";
            bufferedWriter.write(header);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeTreemap() {
        if (voronoiTreemap == null)
            return;

        int written = 0;
        for (VoroNode voroNode : voronoiTreemap.getIdToNode().values()) {
            if (voroNode.getParent() == null) continue;
            VoroNode parent = voroNode.getParent();
            Site site = voroNode.getSite();
            if (site == null) {
                LOG.error("Node site null: " + voroNode.getName() + "\t level: " + voroNode.getHeight() + "\t parent: " + parent.getName());
                continue;
            }

            written++;
            StringBuilder builder = new StringBuilder();
            builder.append(voroNode.getNodeID()).append(";")
                    .append(parent.getNodeID()).append(";")
                    .append(voroNode.name).append(";")
                    .append(voroNode.getWeight()).append(";")
                    .append(voroNode.getHeight()).append(";")
                    .append(site.x).append(";")
                    .append(site.y).append(";")
                    .append(site.getWeight())
                    .append(";");

            PolygonSimple polygon = voroNode.getPolygon();
            if (polygon != null) {
                double[] xPoints = polygon.getXPoints();
                double[] yPoints = polygon.getYPoints();

                for (int j = 0; j < polygon.length; j++) {
                    String first = (j == 0) ? "" : ",";
                    builder.append(first).append(xPoints[j]).append(",").append(yPoints[j]);
                }
            } else {
                builder.append("0,0");
            }

            try {
                bufferedWriter.write(builder.toString() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        LOG.info("Wrote: " + written + " elements");
    }

    @Override
    public void finished() {
        writeTreemap();

        try {
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    synchronized public void finishedNode(int Node, int layer, int[] children,
                                          PolygonSimple[] polygons) {

    }

}
