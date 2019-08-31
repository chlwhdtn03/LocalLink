package beat;

public class Delay {
    private long start;
    private long keepUp = -2000;
    private double fps = 0;
    private int ms;
    //private double tick = 0;

    private double plusms = 0;
    private double plusmsSum = 0;

    private int interval;

    /**
     * this class calculate time automatically
     * 시간을 자동으로 계산해주는 클래스입니다.
     * @param fps fps, 프레임
     */
    public Delay(double fps) {
        this(fps, System.currentTimeMillis());
    }

    /**
     * added start parameter to synchronize time
     * 시간을 동기화하기 위한 start 매개변수가 추가됨.
     * @param fps fps, 프레임
     * @param start start time, 시작 시간
     */

    public Delay(double fps, long start) {
        this(fps,start,false);
    }

    public Delay(double fps, long start, boolean sync) {
        this.start = start;
        this.fps = fps;

        double time = 1000D/fps;
        int ms = (int) time;
        int ns = (int) ((time - ms)*1000000);
        this.ms = ms;

        double val = ns*fps;
        interval = (int) (1000000-(val-(int)val/1000000*1000000));

        //System.out.println(ms + ", " + ns + ", " + interval);

        if(interval>=1000000) {
            interval-=1000000;
        }
        if(sync)
            plusms = interval/fps;
    }

    public void setSyncDelay(boolean istrue) {
        if(istrue) {
            plusms = interval/fps;
        } else {
            plusms = 0;
            plusmsSum = 0;
        }
    }

    /**
     * 일정 주기마다 정지하는 MS를 얻습니다.
     * @return ms time
     */
    public int getMS() {
        return ms;
    }

    private int getUpdateMS() {
        //tick++;
        plusmsSum += plusms;
        if(plusmsSum>=1 || plusmsSum<=-1) {
            int delta = (int)plusmsSum;
            plusmsSum-=delta;
            return delta;
        }
        return 0;
    }

    /**
     * time when keepUp is triggered
     * 얼마나 느려졌을떄 keepUp을 실행할지 시간을 정합니다.
     * @param keepUpTime keepUp time
     */
    public void setKeepUpTime(long keepUpTime) {
        keepUp = -Math.abs(keepUpTime);
    }

    /**
     * calculate one tick time automatically
     * 시간에 대해 자동으로 계산합니다. 이 메서드가 총괄적으로 한 틱을 계산해 낼 것입니다.
     * @return keepUp is triggered, keepUp이 실행될 때
     * @throws InterruptedException because it is about sleep, sleep 관련 함수이므로 InterruptedException이 붙게 됨
     */
    public boolean autoCompute() throws InterruptedException {
        int ms = getRemainMS(start);
        int msplus = getUpdateMS();

        start+=getMS() + msplus;

        /* keep up calculation */
        long cal = 0;
        if(ms<=keepUp) {
            while(getRemainMS(start + cal)<=0)
                cal+=getMS() + getUpdateMS();
        }

        start+=cal;
        /* sleep */
        Delay.sleep(ms + msplus);


        return cal!=0;
    }

    /**
     * get fps
     * 프레임을 얻습니다.
     * @return fps
     */
    public double getFPS() {
        return fps;
    }

    /**
     * 일정 시간을 멈춥니다.
     * @param ms ms
     * @param ns ns
     * @throws InterruptedException because it is about sleep, sleep 관련 함수이므로 InterruptedException이 붙게 됨
     */
    public static void sleep(int ms, int ns) throws InterruptedException {
        if (ms > 0) {
            if (ns > 0) {
                Thread.sleep(ms, ns);
            } else {
                Thread.sleep(ms);
            }
        } else {
            if (ns > 0) {
                Thread.sleep(0, ns);
            }
        }
    }

    /**
     * 일정 시간을 멈춥니다.
     * @param ms ms
     * @throws InterruptedException because it is about sleep, sleep 관련 함수이므로 InterruptedException이 붙게 됨
     */
    public static void sleep(int ms) throws InterruptedException {
        if(ms>0)
            Thread.sleep(ms);
    }

    /**
     * get time really waiting
     * 실질적으로 기다려야하는 시간을 얻습니다.
     * @param start start time
     * @return time
     */
    public int getRemainMS(long start) {
        //System.out.println("remain: " + start);
        return (int) (getMS() - (System.currentTimeMillis() - start));
    }

    /**
     * 현재 시간을 설정합니다.
     * @param time 시간
     */
    public void setTime(long time) {
        this.start = time;
    }

    /**
     * 현재 시간을 얻습니다.
     * @return time
     */
    public long getTime() {
        return start;
    }
}