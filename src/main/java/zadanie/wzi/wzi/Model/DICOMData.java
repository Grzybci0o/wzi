package zadanie.wzi.wzi.Model;

public class DICOMData {
    private int rows;
    private int columns;
    private float[] pixelSpacing;
    private float sliceThickness;
    private float[] imagePosition;
    private int bitsStored;
    private int bitsAllocated;
    private boolean signedData;
    private float rescaleSlope;
    private float rescaleIntercept;
    private float windowCenter;
    private float windowWidth;
    private float sliceLocation;
    private float distanceBetweenSlices;
    private int[][] pixelData;
    private boolean isSigned;
    private String endian;

    public String getEndian() {
        return endian;
    }

    public void setEndian(String endian) {
        this.endian = endian;
    }

    public boolean isSigned() {
        return isSigned;
    }

    public void setSigned(boolean signed) {
        isSigned = signed;
    }

    public float getDistanceBetweenSlices() {
        return distanceBetweenSlices;
    }

    public void setDistanceBetweenSlices(float distanceBetweenSlices) {
        this.distanceBetweenSlices = distanceBetweenSlices;
    }

    public float getSliceLocation() {
        return sliceLocation;
    }

    public void setSliceLocation(float sliceLocation) {
        this.sliceLocation = sliceLocation;
    }

    public int[][] getPixelData() {
        return pixelData;
    }

    public void setPixelData(int[][] pixelData) {
        this.pixelData = pixelData;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public float[] getPixelSpacing() {
        return pixelSpacing;
    }

    public void setPixelSpacing(float[] pixelSpacing) {
        this.pixelSpacing = pixelSpacing;
    }

    public float getSliceThickness() {
        return sliceThickness;
    }

    public void setSliceThickness(float sliceThickness) {
        this.sliceThickness = sliceThickness;
    }

    public float[] getImagePosition() {
        return imagePosition;
    }

    public void setImagePosition(float[] imagePosition) {
        this.imagePosition = imagePosition;
    }

    public int getBitsStored() {
        return bitsStored;
    }

    public void setBitsStored(int bitsStored) {
        this.bitsStored = bitsStored;
    }

    public int getBitsAllocated() {
        return bitsAllocated;
    }

    public void setBitsAllocated(int bitsAllocated) {
        this.bitsAllocated = bitsAllocated;
    }

    public boolean isSignedData() {
        return signedData;
    }

    public void setSignedData(boolean signedData) {
        this.signedData = signedData;
    }

    public float getRescaleSlope() {
        return rescaleSlope;
    }

    public void setRescaleSlope(float rescaleSlope) {
        this.rescaleSlope = rescaleSlope;
    }

    public float getRescaleIntercept() {
        return rescaleIntercept;
    }

    public void setRescaleIntercept(float rescaleIntercept) {
        this.rescaleIntercept = rescaleIntercept;
    }

    public float getWindowCenter() {
        return windowCenter;
    }

    public void setWindowCenter(float windowCenter) {
        this.windowCenter = windowCenter;
    }

    public float getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(float windowWidth) {
        this.windowWidth = windowWidth;
    }

}
