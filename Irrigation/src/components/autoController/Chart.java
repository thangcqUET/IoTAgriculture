package components.autoController;

import java.time.LocalTime;
import java.util.ArrayList;

public class Chart {
    private ArrayList<Point> points;
    private Float upperThreshold;
    private Float lowerThreshold;
    private Boolean isOutOfRange;
    public Chart(){
        points = new ArrayList<Point>();
        upperThreshold = Float.MAX_VALUE;
        lowerThreshold = 0F;
        isOutOfRange=false;
    }
    public Chart(Chart chart){
        this.isOutOfRange=chart.isOutOfRange.booleanValue();
        this.lowerThreshold=chart.lowerThreshold.floatValue();
        this.upperThreshold=chart.upperThreshold.floatValue();
        points = new ArrayList<Point>();
        for(Point p:chart.points){
            Point np = new Point(p);
            this.points.add(np);
        }
    }
    public Chart(Float upperThreshold, Float lowerThreshold) {
        points = new ArrayList<Point>();
        this.upperThreshold = upperThreshold;
        this.lowerThreshold = lowerThreshold;
        isOutOfRange=false;
    }

    public Boolean getOutOfRange() {
        return isOutOfRange;
    }

    public boolean add(Point point){
        if(point.getValue()>upperThreshold||point.getValue()<lowerThreshold) isOutOfRange=true;
        return points.add(point);
    }
    public Point remove(int index){
        Point ret = points.remove(index);
        isOutOfRange=false;
        for(int i=0;i<points.size();i++){
            if(points.get(i).getValue()>upperThreshold||points.get(i).getValue()<lowerThreshold){
                isOutOfRange=true;
                break;
            }
        }
        return ret;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public static class Point {
        private Float value;
        private LocalTime time;

        public Point(Float value, LocalTime time) {
            this.value = value;
            this.time = time;
        }
        public Point(Point point){
            int hour = point.getTime().getHour();
            int min = point.getTime().getMinute();
            int sec = point.getTime().getSecond();
            int nano = point.getTime().getNano();
            this.time= LocalTime.of(hour,min,sec,nano);
            this.value=point.value;
        }
        public Float getValue() {
            return value;
        }

        public LocalTime getTime() {
            return time;
        }
    }
}
