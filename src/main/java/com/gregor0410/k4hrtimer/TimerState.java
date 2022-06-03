package com.gregor0410.k4hrtimer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;

public class TimerState {

    public ArrayList<Run> runs = new ArrayList<>();

    public static TimerState getTimerState(Path path) throws IOException {
        BufferedReader reader = null;
        try {
            reader = Files.newBufferedReader(path);
            Gson gson = new Gson();
            TimerState state = gson.fromJson(reader,TimerState.class);
            reader.close();
            if(state != null) return state;
            return new TimerState();
        } catch (IOException e) {
            return new TimerState();
        }
    }

    public boolean runsWithin4Hours(){
        if(runs.size()==0) return true;
        Run firstRun = runs.get(0);
        Run lastRun = runs.get(runs.size()-1);
        Instant startTime = Instant.ofEpochSecond(firstRun.startTimeSeconds, firstRun.startTimeNanos);
        Instant endTime = Instant.ofEpochSecond(lastRun.completedTimeSeconds, lastRun.completedTimeNanos);
        return endTime.minus(Duration.ofHours(4)).isBefore(startTime);
    }

    public void addRun(Run run){
        runs.add(run);
        while(!runsWithin4Hours()){
            runs.remove(0);
        }
    }

    public void save(Path path) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Files.createDirectories(path.getParent());
        BufferedWriter writer = Files.newBufferedWriter(path);
        gson.toJson(this,writer);
        writer.close();
        File displayFile = path.getParent().resolve("k4hr.txt").toFile();
        displayFile.createNewFile();
        PrintWriter pw = new PrintWriter(displayFile);
        if(runs.size()>0) {
            Run firstRun = runs.get(0);
            Instant startTime = Instant.ofEpochSecond(firstRun.startTimeSeconds, firstRun.startTimeNanos);
            for (int i = 0; i < runs.size(); i++) {
                Run run = runs.get(i);
                Instant completedTime = Instant.ofEpochSecond(run.completedTimeSeconds, run.completedTimeNanos);
                Duration rta = Duration.ofMillis(completedTime.toEpochMilli() - startTime.toEpochMilli());
                Duration igt = Duration.ofMillis(run.igt);
                pw.println("Run #%d completed at %02d:%02d:%02d with igt %02d:%02d:%02d.%03d".formatted(
                        i + 1,
                        rta.toHoursPart(),
                        rta.toMinutesPart(),
                        rta.toSecondsPart(),
                        igt.toHoursPart(),
                        igt.toMinutesPart(),
                        igt.toSecondsPart(),
                        igt.toMillisPart()));
            }
        }
        pw.close();
    }


    public static class Run{
        public long startTimeSeconds;
        public long startTimeNanos;
        public long completedTimeSeconds;
        public long completedTimeNanos;
        public long igt;

        public Run(Instant startTime,Instant completedTime,long igt){
            startTimeSeconds = startTime.getEpochSecond();
            startTimeNanos = startTime.getNano();
            completedTimeSeconds = completedTime.getEpochSecond();
            completedTimeNanos = completedTime.getNano();
            this.igt = igt;
        }
    }
}
