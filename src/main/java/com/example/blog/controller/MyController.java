package com.example.blog.controller;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.blog.Event;
import com.fasterxml.jackson.core.sym.Name;

@Controller
public class MyController {
	
	String handle = "";
	String subs = "";
	String rank = "";
	HashSet<String> solved = new HashSet<String>();
	ArrayList<ArrayList<String>> ques1300 = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> ques1400 = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> ques1500 = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> ques1600 = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> ques1700 = new ArrayList<ArrayList<String>>();
	ArrayList<Integer> ran1300 = new ArrayList<Integer>();
	ArrayList<Integer> ran1400 = new ArrayList<Integer>();
	ArrayList<Integer> ran1500 = new ArrayList<Integer>();
	ArrayList<Integer> ran1600 = new ArrayList<Integer>();
	ArrayList<Integer> ran1700 = new ArrayList<Integer>();
	
	@GetMapping("/start")
	public String start() {
		return "pagelist";
	}
	
	@PostMapping("/submit-handle")
    public String submitHandle(@RequestParam String codeforcesHandle, Model model) {
        String data = "Codeforces Handle: " + codeforcesHandle;
        this.handle = codeforcesHandle;
        String dataset = "";
		try {
			dataset = fetch("https://codeforces.com/api/user.info?handles=" + this.handle);
			int index = dataset.indexOf("maxRank");
			rank = Character.toUpperCase(dataset.charAt(index+10)) + dataset.substring(index+11, dataset.indexOf("}", index)-1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        model.addAttribute("handle", "Welcome , " + rank + " " + handle);
        fill();
        return "pagelist";
    }
	
	@PostMapping("/submit-checkboxes")
	public String submitCheckboxes(@RequestParam(value = "selectedCheckboxes", required = false) String[] selectedCheckboxes, Model model) {
        if (selectedCheckboxes != null) {
            model.addAttribute("selectedCheckboxes", selectedCheckboxes);
        }
        String url  = "https://codeforces.com/api/problemset.problems?tags=";
        for(int i=0;i<selectedCheckboxes.length;i++) {
        	url += selectedCheckboxes[i] + ";";
        }
        String data = "";
        if(selectedCheckboxes.length > 0) {
        	try {
    			data = fetch(url);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        }
        model.addAttribute("handle", "Welcome , " + rank + " " + handle);
		int first = data.indexOf("contestId");
		ques1300 = new ArrayList<ArrayList<String>>();
		ques1400 = new ArrayList<ArrayList<String>>();
		ques1500 = new ArrayList<ArrayList<String>>();
		ques1600 = new ArrayList<ArrayList<String>>();
		ques1700 = new ArrayList<ArrayList<String>>();
		ran1300 = new ArrayList<Integer>();
		ran1400 = new ArrayList<Integer>();
		ran1500 = new ArrayList<Integer>();
		ran1600 = new ArrayList<Integer>();
		ran1700 = new ArrayList<Integer>();
		while(true) {
			if(first == -1) break;
			int end = data.indexOf("tags", first+1) - 1;
			if(end == -2) break;
			String con = data.substring(first, end);
			int p1 = con.indexOf(",");
			String id = con.substring(con.indexOf("contestId") + 11, p1);
			int p2 = con.indexOf(",", p1+1);
			String index = con.substring(con.indexOf("index")+8, p2-1);
			String rating = con.substring(con.indexOf("rating") + 8, con.lastIndexOf(","));
			int p3 = con.indexOf(",", p2+1);
			if(p3 == -1) break;
			String name = con.substring(con.indexOf("name")+7, p3-1);
			String link = "https://codeforces.com/problemset/problem/" + id + "/" + index;
			first = data.indexOf("contestId", end+1);
			if(solved.contains(name) || rating.length() > 4) {
				continue;
			}
			int rt = Integer.parseInt(rating);
			ArrayList<String> details = new ArrayList<String>();
			details.add(id);
			details.add(index);
			details.add(name);
			details.add(rating);
			details.add(link);
			if(rt == 1300) {
				ques1300.add(details);
			}
			if(rt == 1400) {
				ques1400.add(details);
			}
			if(rt == 1500) {
				ques1500.add(details);
			}
			if(rt == 1600) {
				ques1600.add(details);
			}
			if(rt == 1700) {
				ques1700.add(details);
			}
			if(rank.equals("Expert")) {
				if(rt < 1500 || rt >= 2100) continue;
			}
			if(rank.equals("Specialist")) {
				if(rt < 1300 || rt >= 1800) continue;
			}
			if(rank.equals("Pupil")) {
				if(rt < 1000 || rt >= 1500) continue;
			}
			if(rank.equals("Newbie")) {
				if(rt > 1300) continue;
			}
		}
		random();
        return "pagelist"; // Return the name of the display page
    }
	
	public void random() {
		int size = ques1400.size();
		List<Integer> possibleNumbers = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) possibleNumbers.add(i);
        Collections.shuffle(possibleNumbers);
        for (int i = 0; i < Math.min(4, possibleNumbers.size()); i++) { ran1400.add(possibleNumbers.get(i));}
        size = ques1300.size();
		possibleNumbers = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) possibleNumbers.add(i);
        Collections.shuffle(possibleNumbers);
        for (int i = 0; i < Math.min(4, possibleNumbers.size()); i++) { ran1300.add(possibleNumbers.get(i));}
        size = ques1500.size();
        possibleNumbers = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) possibleNumbers.add(i);
        Collections.shuffle(possibleNumbers);
        for (int i = 0; i < Math.min(4, possibleNumbers.size()); i++) { ran1500.add(possibleNumbers.get(i));}
        size = ques1600.size();
        possibleNumbers = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) possibleNumbers.add(i);
        Collections.shuffle(possibleNumbers);
        for (int i = 0; i < Math.min(4, possibleNumbers.size()); i++) { ran1600.add(possibleNumbers.get(i));}
        size = ques1700.size();
        possibleNumbers = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) possibleNumbers.add(i);
        Collections.shuffle(possibleNumbers);
        for (int i = 0; i < Math.min(4, possibleNumbers.size()); i++) { ran1700.add(possibleNumbers.get(i));}
	}
	
	@PostMapping("/submit-difficulty")
	public String handleDifficulty(@RequestParam("selectedDifficulty") String selectedDifficulty, Model model) {
		model.addAttribute("handle", "Welcome , " + rank + " " + handle);
		ArrayList<String> links = new ArrayList<String>();
        ArrayList<String> ID = new ArrayList<String>();
        ArrayList<String> Index = new ArrayList<String>();
        ArrayList<String> Name = new ArrayList<String>();
        ArrayList<String> rate = new ArrayList<String>();
        ArrayList<ArrayList<String>> details = new ArrayList<ArrayList<String>>();
        if(selectedDifficulty.equals("easy")) {
        	if(ran1300.size()>0)details.add(ques1300.get(ran1300.get(0)));
        	if(ran1300.size()>1)details.add(ques1300.get(ran1300.get(1)));
        	if(ran1400.size()>0)details.add(ques1400.get(ran1400.get(0)));
        	if(ran1400.size()>1)details.add(ques1400.get(ran1400.get(1)));
        }
        if(selectedDifficulty.equals("normal")) {
        	if(ran1400.size()>0)details.add(ques1400.get(ran1400.get(0)));
        	if(ran1500.size()>0)details.add(ques1500.get(ran1500.get(0)));
        	if(ran1500.size()>1)details.add(ques1500.get(ran1500.get(1)));
        	if(ran1600.size()>0)details.add(ques1600.get(ran1600.get(0)));
        }
        if(selectedDifficulty.equals("medium")) {
        	if(ran1500.size()>0)details.add(ques1500.get(ran1500.get(0)));
        	if(ran1500.size()>1)details.add(ques1500.get(ran1500.get(1)));
        	if(ran1600.size()>0)details.add(ques1600.get(ran1600.get(0)));
        	if(ran1600.size()>1)details.add(ques1600.get(ran1600.get(1)));
        }
        if(selectedDifficulty.equals("hard")) {
        	if(ran1500.size()>0)details.add(ques1500.get(ran1500.get(0)));
        	if(ran1600.size()>0)details.add(ques1600.get(ran1600.get(0)));
        	if(ran1600.size()>1)details.add(ques1600.get(ran1600.get(1)));
        	if(ran1700.size()>0)details.add(ques1700.get(ran1700.get(0)));
        }
        if(selectedDifficulty.equals("extreme")) {
        	if(ran1600.size()>0)details.add(ques1600.get(ran1600.get(0)));
        	if(ran1600.size()>1)details.add(ques1600.get(ran1600.get(1)));
        	if(ran1700.size()>0)details.add(ques1700.get(ran1700.get(0)));
        	if(ran1700.size()>1)details.add(ques1700.get(ran1700.get(1)));
        }
        for(int i=0;i<Math.min(4, details.size());i++) {
        	ArrayList<String> get = details.get(i);
        	ID.add(get.get(0));
        	Index.add(get.get(1));
        	Name.add(get.get(2));
        	rate.add(get.get(3));
        	links.add(get.get(4));
        }
        model.addAttribute("num", ID);
        model.addAttribute("index", Index);
        model.addAttribute("name", Name);
        model.addAttribute("links", links);
        model.addAttribute("rating", rate);
		return "pagelist";
	}
	
	public void fill() {
		try {
			subs = fetch("https://codeforces.com/api/user.status?handle=" + handle);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int start = 0;
		while(true) {
			int s = subs.indexOf("name", start);
			if(s == -1) break;
			int e = subs.indexOf(",", s);
			if(e == -1) break;
			String name = subs.substring(s+7, e-1);
			int check = subs.indexOf("verdict", s);
			if(check == -1) break;
			String verdict = subs.substring(check+10, check+12);
			if(verdict.equals("OK")) solved.add(name);
			start = check+12;
		}
	}
    
    private static final String API_KEY = "0ab56a03f7f200b16f5c42357e7d267a045f6463";
    private static final String API_SECRET = "0e3284ca90e2ddb6630cfdd496a63b5779d5bd89";

    public String fetch(String URL) throws Exception {
        URL url = new URL(URL + "&key=" + API_KEY + "&secret=" + API_SECRET);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            String response="";
            while ((inputLine = in.readLine()) != null) {
            	response += inputLine + "\n"; 
            }
            in.close();
            return response;
        } else {
            throw new Exception("API request failed with response code: " + responseCode);
        }
    }
    

    @GetMapping("/fetch")
    public String fetchDataFromAPI(Model model) {
    	String response = "";
		try {
			response = fetch("https://codeforces.com/api/contest.list?gym=false");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String res = "";
		int first = response.indexOf('{', 1);
		ArrayList<String> data = new ArrayList<String>();
		while(true) {
			int end = response.indexOf('}', first);
			String con = response.substring(first+1, end);
			String add = con.substring(0, con.indexOf(',', 13));
			data.add(add);
			first = response.indexOf('{', end);
			if(first == -1) break;
		}
        ArrayList<Event> eventData = new ArrayList<Event>();
        for(String s : data) {
        	String id = s.substring(0, s.indexOf(','));
        	String name = s.substring(s.indexOf(',')+1,s.length());
        	id = id.substring(5, id.length());
        	name = name.substring(8, name.length()-1);
        	eventData.add(new Event(id, name));
        }
        model.addAttribute("eventData", eventData);
        return "index";
    }
    
    @GetMapping("/questions/{eventId}")
    public String viewQuestions(@PathVariable String eventId, Model model) {
    	model.addAttribute("eventId", eventId);
        return "questions";
    }
}


/*
 * Specialist
 * Level easy   -  1300 1300 1400 1400
 * Level normal -  1400 1500 1500 1600
 * Level medium -  1500 1500 1600 1600
 * Level hard   -  1500 1600 1600 1700
 * Level extreme - 1600 1600 1700 1700
 */