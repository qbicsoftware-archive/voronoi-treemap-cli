package life.qbic.voronoi.algorithms;

import kn.uni.voronoitreemap.IO.WriteStatusObject;
import kn.uni.voronoitreemap.interfaces.data.TreeData;
import kn.uni.voronoitreemap.j2d.PolygonSimple;
import kn.uni.voronoitreemap.treemap.VoronoiTreemap;
import life.qbic.voronoi.io.parser.CSVToRowDataParser;
import life.qbic.voronoi.io.parser.PolygonDataParser;
import life.qbic.voronoi.io.writer.HTMLWriter;
import life.qbic.voronoi.model.PolygonData;
import life.qbic.voronoi.model.RowData;
import life.qbic.voronoi.util.NumberUtil;
import life.qbic.voronoi.util.RowDataComparator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreemapCreator {

    private static final int border = 6;
    private static double size = 800;

    private static final Logger LOG = LogManager.getLogger(TreemapCreator.class);

    /**
     * entry point for the treemap creation algorithm
     *
     * @param inFilePath
     * @param outputFilePath
     * @param isTemporaryHtml
     * @param columnNames
     * @throws IOException
     */
    public static String startTreemapCreation(String inFilePath, String outputFilePath, boolean isTemporaryHtml, List<String> columnNames) throws IOException {
        // create a convex root polygon
        PolygonSimple rootPolygon = new PolygonSimple();

        rootPolygon.add(border, border);
        rootPolygon.add(size + border, border);
        rootPolygon.add(size + border, size + border);
        rootPolygon.add(border, size + border);

        //fetch the CSV data and create a hierarchy
        List<RowData> csvRows = CSVToRowDataParser.parseCSV(inFilePath, columnNames);
        csvRows.sort(new RowDataComparator());
        TreeData data = createHierarchy(csvRows);
        LOG.info("finished creation of VoroCells");
        // data.setWeight("file036", 4);// increase cell size (leafs only)

        VoronoiTreemap treemap = new VoronoiTreemap();
        // VoronoiCore.setDebugMode(); //shows iteration process
        treemap.setRootPolygon(rootPolygon);
        treemap.setTreeData(data);
        treemap.setCancelOnMaxIteration(true);
        treemap.setNumberMaxIterations(1500);
        treemap.setCancelOnThreshold(true);
        treemap.setErrorAreaThreshold(0.01);
//		 treemap.setUniformWeights(true);
        treemap.setNumberThreads(1);

        // add result handler
//		treemap.setStatusObject(new PDFStatusObject("miniHierarchy", treemap));
//		treemap.setStatusObject(new PNGStatusObject("miniHierarchy", treemap));

        treemap.setStatusObject(new WriteStatusObject("VoroTreemap", treemap));
        treemap.computeLocked();
        List<PolygonData> polygonData = PolygonDataParser.readPolygonData( "VoroTreemap.txt");

        createColorEncoding(polygonData, csvRows);

        return HTMLWriter.writeToHtml(polygonData, isTemporaryHtml, size, border, outputFilePath);
    }

    /**
     * creates a hierarchy from a alphabetically sorted list of VoroCell objects
     * and returns the structure as TreeData object.
     *
     * @param cellData
     * @return TreeData object
     */
    private static TreeData createHierarchy(List<RowData> cellData) {
        LOG.info("Creating hierarchy of VoroCells");
        TreeData data = new TreeData();

        List<String> duplicateHelper = new ArrayList<>();

        int i = 0;
        while (true) {    // add level1 to root
            String p = cellData.get(i).getLevels().get(0);

            data.addLink(p, "root");
            data.setRoot("root");

            List<String> parentList = new ArrayList<>();
            parentList.add(p);

            i = addLevel(data, cellData, parentList, duplicateHelper, "", 0, i);    // add next level to root
            if (i >= cellData.size())
                return data;
        }
    }

    /**
     * add hierarchy levels recursively for dynamic amount of levels.
     *
     * @param data
     * @param cellData
     * @param prevParentList
     * @param duplicateHelper
     * @param parentTag
     * @param prevNum
     * @param i
     * @return
     */
    private static int addLevel(TreeData data, List<RowData> cellData, List<String> prevParentList, List<String> duplicateHelper, String parentTag, int prevNum, int i) {
        List<String> parentList = cellData.get(i).getLevels().subList(0, prevNum + 1);

        while (parentList.equals(prevParentList)) {
            String currName = cellData.get(i).getLevels().get(prevNum + 1);
            String prevName = cellData.get(i).getLevels().get(prevNum);

            String duplicateTag = "";
            int occurrences = 1;
            if (duplicateHelper.contains(currName)) {
                occurrences += Collections.frequency(duplicateHelper, currName);
                duplicateTag = "(" + occurrences + ")";
            }

            data.addLink(currName + duplicateTag, prevName + parentTag);

            // set weight based on occurences of this element in the data
            double weight = 1.0d / (occurrences);
            if (prevNum + 1 == cellData.get(i).getLevels().size() - 1) {
                data.setWeight(currName, weight);
                for (int j = 2; j <= occurrences; j++) {
                    data.setWeight(currName + "(" + j + ")", weight);
                }
            }

            duplicateHelper.add(currName);

            if (prevNum + 2 < cellData.get(i).getLevels().size()) {
                prevParentList.add(currName);
                i = addLevel(data, cellData, prevParentList, duplicateHelper, duplicateTag, prevNum + 1, i);    // add next level to this level
                prevParentList.remove(prevParentList.size() - 1);
            } else
                i++;

            if (i >= cellData.size())
                return cellData.size();

            parentList = cellData.get(i).getLevels().subList(0, prevNum + 1);
        }
        return i;
    }


    /**
     * set the color of each polygon to visualize the ratio of the represented protein
     *
     * @param polygonData
     */
    private static void createColorEncoding(List<PolygonData> polygonData, List<RowData> rowsData) {
        LOG.info("Creating color encoding");
        List<Double> ratios = new ArrayList<>();
        List<Double> ratiosLower = new ArrayList<>();
        List<Double> ratiosUpper = new ArrayList<>();

        double threshold;

        for (RowData row : rowsData) {
            ratios.add(row.getRatios().get(0));
        }

        double sum = 0.0d;
        for (double r : ratios) {
            sum += r;
        }
        threshold = sum / ratios.size();

        for (Double r : ratios) {
            if (r < threshold)
                ratiosLower.add(r);
            if (r > threshold)
                ratiosUpper.add(r);
        }

        for (PolygonData pDat : polygonData) {
            for (RowData row : rowsData) {
                if (!pDat.getName().equals(row.getLevels().get(row.getLevels().size() - 1)))
                    continue;

                pDat.setRatios(row.getRatios());

                double r = pDat.getRatios().get(0);

                if (r == 0)
                    continue;

                if (r < threshold) {
                    int normRatioLower = (int) NumberUtil.normalize(r, NumberUtil.getMin(ratiosLower), NumberUtil.getMax(ratiosLower), 30.0d, 100.0d);
                    Color colBlue = new Color(0, 100, 255 - normRatioLower);
                    pDat.setColor(colBlue);
                }

                if (r > threshold) {
                    int normRatioUpper = (int) NumberUtil.normalize(r, NumberUtil.getMin(ratiosUpper), NumberUtil.getMax(ratiosUpper), 30.0d, 100.0d);
                    Color colRed = new Color(255 - normRatioUpper, 100,0);
                    pDat.setColor(colRed);
                }
            }
        }

        LOG.info("Finished creating color encoding");
    }

}
