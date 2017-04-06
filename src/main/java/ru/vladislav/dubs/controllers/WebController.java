package ru.vladislav.dubs.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.IntStream;

@Controller
public class WebController {

    private static final int MAX_NUMBERS_COUNT = 10000;
    private static final int MIN_NUMBER = -1000;
    private static final int MAX_NUMBER = 1000;

    @RequestMapping(value = "/")
    public String index(Map<String, Object> model){
        model.put("MAX_NUMBERS_COUNT",MAX_NUMBERS_COUNT);
        model.put("MIN_NUMBER",MIN_NUMBER);
        model.put("MAX_NUMBER",MAX_NUMBER);
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/processNumbers")
    public int firstAlgorithm(Algorithm algorithm, @RequestParam(value="numbers[]") int[] numbers){
        if(numbers.length>MAX_NUMBERS_COUNT){
            throw new RuntimeException("Too many numbers");
        }
        if(IntStream.of(numbers).anyMatch(x->x>MAX_NUMBER || x<MIN_NUMBER)){
            throw new RuntimeException("At least one number >" + MAX_NUMBER + " or <" + MIN_NUMBER);
        }
        switch (algorithm){
            case FIRST:
                return IntStream.of(numbers).sorted().limit(2).sum();
            case SECOND:
                if(numbers.length<5) throw new RuntimeException("numbers.length must be greater than or equal to 5");
                Entry[] entries = IntStream.range(1, numbers.length-1)
                        .mapToObj(index->new Entry(index, numbers[index]))
                        .sorted(Comparator.comparingInt(o -> o.number))
                        .toArray(Entry[]::new);
                for(int i=0;i<entries.length;i++){
                    for(int j=0;j<entries.length;j++){
                        if(i==j) continue;
                        if(entries[i].index!=entries[j].index+1 && entries[i].index!=entries[j].index-1){
                            return entries[i].number +entries[j].number;
                        }
                    }
                }
                throw new RuntimeException("Error in the algorithm");
            default:
                throw new RuntimeException("NotImplemented");
        }
    }

    public enum Algorithm {FIRST,SECOND}

    private static class Entry{
        final int index;
        final int number;
        Entry(int index, int number){
            this.index = index;
            this.number = number;
        }
    }
}