public class Semester {
    private static int counter = 1;
    private final int id; 
    private int period;
    private Curriculum curriculum;

    public Semester(int period, Curriculum curriculum) {
        this.id = counter++;
        this.period = period;
        this.curriculum = curriculum;
    }


    public int getId() {
        return this.id;
    }

    public int getPeriod() {
        return this.period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public Curriculum getCurriculum() {
        return this.curriculum;
    }

    public void setCurriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
    }    
}
