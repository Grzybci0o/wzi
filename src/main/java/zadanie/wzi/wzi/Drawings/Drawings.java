package zadanie.wzi.wzi.Drawings;

import javafx.geometry.Point3D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Drawings {

    List<double[]> polygonPoints = new ArrayList<>();

    boolean polygonClosed;
    boolean polygonSelected;
    boolean voxelSelected;

    Point3D point3D = new Point3D(0, 0, 0);

    public void setupMouseEventsForDrawings(Canvas drawing1, Canvas drawing2, Canvas drawing3) {
        GraphicsContext gc1 = drawing1.getGraphicsContext2D();
        GraphicsContext gc2 = drawing2.getGraphicsContext2D();
        GraphicsContext gc3 = drawing3.getGraphicsContext2D();

        setupDrawingEvents(drawing2, drawing3, drawing1, gc1);

        setupDrawingEvents(drawing1, drawing3, drawing2, gc2);

        setupDrawingEvents(drawing1, drawing2, drawing3, gc3);
    }

    public void setupMouseEventForVoxelDrawing(Canvas drawing1, Canvas drawing2, Canvas drawing3, int sliceIndex, int function) {
        GraphicsContext gc = drawing1.getGraphicsContext2D();
        GraphicsContext gc2 = drawing2.getGraphicsContext2D();
        GraphicsContext gc3 = drawing3.getGraphicsContext2D();

        if (function == 1) {
            drawing1.setOnMousePressed(event -> {
                if (event.isPrimaryButtonDown() && voxelSelected) {
                    clearCanvasDrawing(drawing1);
                    clearCanvasDrawing(drawing2);
                    clearCanvasDrawing(drawing3);
                    double x = event.getX();
                    double y = event.getY();

                    point3D = new Point3D(x, y, sliceIndex);
                    System.out.println(point3D);

                    gc.setFill(Color.RED);
                    gc.fillOval(x - 2, y - 2, 4, 4);

                    gc2.setFill(Color.RED);
                    gc2.fillOval(250 - x - 2, 125 + sliceIndex - 7, 4, 4);


                    gc3.setFill(Color.RED);
                    gc3.fillOval(y - 2, y, 4, 4);
                }
            });
        } else if (function == 2) {
            drawing1.setOnMousePressed(event -> {
                if (event.isPrimaryButtonDown() && voxelSelected) {
                    clearCanvasDrawing(drawing1);
                    clearCanvasDrawing(drawing2);
                    clearCanvasDrawing(drawing3);
                    double x = event.getX();
                    double y = event.getY();

                    point3D = new Point3D(x, y, sliceIndex);
                    System.out.println(point3D);

                    gc.setFill(Color.RED);
                    gc.fillOval(x - 2, y - 2, 4, 4);

                    gc2.setFill(Color.RED);
                    gc2.fillOval(250 - x - 2, sliceIndex - 2, 4, 4);

                    gc3.setFill(Color.RED);
                    gc3.fillOval(y - 2, sliceIndex - 2, 4, 4);
                }
            });
        } else if (function == 3) {
            drawing1.setOnMousePressed(event -> {
                if (event.isPrimaryButtonDown() && voxelSelected) {
                    clearCanvasDrawing(drawing1);
                    clearCanvasDrawing(drawing2);
                    clearCanvasDrawing(drawing3);
                    double x = event.getX();
                    double y = event.getY();

                    point3D = new Point3D(x, y, sliceIndex);
                    System.out.println(point3D);

                    gc.setFill(Color.RED);
                    gc.fillOval(x - 2, y - 2, 4, 4);

                    gc2.setFill(Color.RED);
                    gc2.fillOval(x - 2, sliceIndex - 2, 4, 4);

                    gc3.setFill(Color.RED);
                    gc3.fillOval(y - 2, sliceIndex - 2, 4, 4);
                }
            });
        }

    }

    private void setupDrawingEvents(Canvas block1, Canvas block2, Canvas primary, GraphicsContext gc) {
        primary.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && polygonSelected) {
                double x = event.getX();
                double y = event.getY();
                addPointsToList(x, y);
                System.out.println(x);
                gc.setFill(Color.RED);
                gc.fillOval(x - 2, y - 2, 4, 4);

                if (polygonPoints.size() > 1) {
                    double[] previousPoint = polygonPoints.get(polygonPoints.size() - 2);
                    gc.setStroke(Color.RED);
                    gc.strokeLine(previousPoint[0], previousPoint[1], x, y);
                }

                block1.setOnMousePressed(null);
                block2.setOnMousePressed(null);

            } else if (event.isSecondaryButtonDown()) {
                if (polygonPoints.size() >= 2) {
                    double[] firstPoint = polygonPoints.get(0);
                    double[] lastPoint = polygonPoints.get(polygonPoints.size() - 1);
                    gc.setStroke(Color.RED);
                    gc.strokeLine(lastPoint[0], lastPoint[1], firstPoint[0], firstPoint[1]);
                    polygonClosed = true;
                }
            }
        });

        primary.setOnMouseReleased(event -> {
            block1.setOnMouseReleased(null);
            block2.setOnMouseReleased(null);
        });
    }

    private double calculateDistance(double firstPoint, double lastPoint, double firstPoint1, double lastPoint1) {
        return Math.sqrt(Math.pow(firstPoint - lastPoint, 2) + Math.pow(firstPoint1 - lastPoint1, 2));
    }

    public void clearCanvasDrawing(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        polygonPoints.clear();
        polygonClosed = false;
    }

    public void addPointsToList(double x, double y) {
        polygonPoints.add(new double[]{x, y});
    }

    public double calculatePolygonAreaInPixels() {
        if (polygonPoints.size() < 3) {
            return 0.0;
        }

        double area = 0.0;

        int numPoints = polygonPoints.size();
        for (int i = 0; i < numPoints - 1; i++) {
            double[] currentPoint = polygonPoints.get(i);
            double[] nextPoint = polygonPoints.get(i + 1);
            area += (currentPoint[0] * nextPoint[1]) - (nextPoint[0] * currentPoint[1]);
        }

        double[] lastPoint = polygonPoints.get(numPoints - 1);
        double[] firstPoint = polygonPoints.get(0);
        area += (lastPoint[0] * firstPoint[1]) - (firstPoint[0] * lastPoint[1]);

        area = 0.5 * Math.abs(area);

        return area;
    }

    public double calculatePerimeterInPixels() {
        double perimeter = 0.0;
        int numOfPoints = polygonPoints.size();

        if (numOfPoints < 2) {
            return perimeter;
        }

        for (int i = 0; i < numOfPoints - 1; i++) {
            double[] currentPoint = polygonPoints.get(i);
            double[] nextPoint = polygonPoints.get(i + 1);
            perimeter += calculateDistance(currentPoint[0], nextPoint[0], currentPoint[1], nextPoint[1]);
        }

        double[] lastPoint = polygonPoints.get(numOfPoints - 1);
        double[] firstPoint = polygonPoints.get(0);
        perimeter += calculateDistance(lastPoint[0], firstPoint[0], lastPoint[1], firstPoint[1]);

        perimeter = Math.round(perimeter * 10.0) / 10.0;

        return perimeter;
    }

    public double calculatePolygonAreaInVoxels() {
        return 0;
    }

    public double calculatePerimeterInVoxels() {
        return 0;
    }

    public List<double[]> getPolygonPointsList() {
        return polygonPoints;
    }

    public boolean isPolygonClosed() {
        return polygonClosed;
    }

    public void setPolygonSelected(boolean polygonSelected) {
        this.polygonSelected = polygonSelected;
    }

    public void setVoxelSelected(boolean voxelSelected) {
        this.voxelSelected = voxelSelected;
    }

    public Point3D getPoint3D() {
        return point3D;
    }
}
