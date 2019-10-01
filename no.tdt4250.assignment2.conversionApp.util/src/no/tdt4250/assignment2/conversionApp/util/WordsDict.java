package no.tdt4250.assignment2.conversionApp.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.SortedSet;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;

import no.tdt4250.assignment2.conversionApp.api.Dict;
import no.tdt4250.assignment2.conversionApp.api.DictSearchResult;

@Component(
		configurationPid = WordsDict.FACTORY_PID,
		configurationPolicy = ConfigurationPolicy.REQUIRE
		)
public class WordsDict implements Dict {

	public static final String FACTORY_PID = "no.tdt4250.assignment2.util.WordsDict";
	
	public static final String DICT_WORDS_PROP = "dictWords";
	public static final String DICT_RESOURCE_PROP = "dictResource";
	public static final String DICT_NAME_PROP = "dictName";
	
	private String name;
	private Words words;
	
	@Override
	public String getDictName() {
		return name;
	}
	
	public Words getWords() {
		return words;
	}

	protected void setDictName(String name) {
		this.name = name;
	}
	
	public @interface WordsDictConfig {
		String dictName();
		String dictResource() default "";
		String[] dictWords() default {};
	}

	@Activate
	public void activate(BundleContext bc, WordsDictConfig config) {
		update(bc, config);
	}

	@Modified
	public void modify(BundleContext bc, WordsDictConfig config) {
		update(bc, config);		
	}

	protected void update(BundleContext bc, WordsDictConfig config) {
		setDictName(config.dictName());
		String dictUrl = config.dictResource();
		if (dictUrl.length() > 0) {
			URL url = null;
			try {
				url = new URL(dictUrl);
			} catch (MalformedURLException e) {
				// try bundle resource format: <bundle-id>#<resource-path>
				int pos = dictUrl.indexOf('#');
				String bundleId = dictUrl.substring(0, pos);
				String resourcePath = dictUrl.substring(pos + 1);
				for (Bundle bundle : bc.getBundles()) {
					if (bundle.getSymbolicName().equals(bundleId)) {
						url = bundle.getResource(resourcePath);
					}
				}
			}
			try {
				System.out.println("Loading words from " + url);
				words = new ResourceWords(url.openStream());
			} catch (IOException e) {
				System.err.println(e);
			}
		}
		if (config.dictWords().length > 0) {
			String[] ss = config.dictWords();
			if (words == null) {
				words = new SortedSetWords();
			}
			if (words instanceof MutableWords) {
				for (int i = 0; i < ss.length; i++) {
					((MutableWords) words).addWord(ss[i].trim());
				}
			}
		}
	}

	protected void setWords(Words words) {
		this.words = words;
	}
	
	protected void setWords(InputStream input) throws IOException {
		words = new ResourceWords(input);
	}

	protected void setWords(URL url) throws IOException {
		setWords(url.openStream());
	}
	
	protected String getSuccessMessageStringFormat() {
		return "Yes, %s was found!";
	}

	protected String getFailureMessageStringFormat() {
		return "No, %s was not found!";
	}
	
	public DictSearchResult search(String searchKey) {
		if(searchKey.length() > 2) {
			String searchKeyP = searchKey.substring(0, 2);
			char from = searchKey.charAt(0);
			char to = searchKey.charAt(1);
			String vals = searchKey.substring(2);
			if(Character.isLetter(searchKey.charAt(2))) {
				return new DictSearchResult(true, String.format(getFailureMessageStringFormat(), searchKey) + "\n" + "You might have too many letters! Only use 2 letters." + "\n" + "F.eks: from 20 Celsius to Fahrenheit is q=cf20." + showSupportedFormulas(), null);
			}
			if (words != null && words.hasWord(searchKeyP)) {
				
				String candidate = ((SortedSetWords) getWords()).getWord(searchKeyP);
				double response = parseCalculation(candidate, vals);
				
				return new DictSearchResult(true, String.format(getSuccessMessageStringFormat(), searchKeyP) + "\n" + vals + from + "="  +  response + to, null);
			} else {
				return new DictSearchResult(true, String.format(getFailureMessageStringFormat(), searchKeyP) + "\n" + "ERROR" + showSupportedFormulas(), null);
			}
		}else {
			return new DictSearchResult(true, String.format(getFailureMessageStringFormat(), searchKey), null);
		}
	}
	
	protected String showSupportedFormulas() {
		ArrayList<String> arr = ((SortedSetWords)words).getWords();
		String returnString = "\n Supported Formulas: \n";
		for(String s : arr) {
			returnString += s;
			returnString += "\n";
		}
		return returnString;
		
	}
	
	protected Double parseCalculation(String key, String value) {
		int equalSign = key.indexOf('=');
		String equation = key.substring(equalSign + 2);
		equation = equation.replace("x", value);
		
		return Math.round(eval(equation) * 100.0) / 100.0;
		
	}
	
	public static double eval(String str) {
	    return new Object() {
	        int pos = -1, ch;

	        void nextChar() {
	            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
	        }

	        boolean eat(int charToEat) {
	            while (ch == ' ') nextChar();
	            if (ch == charToEat) {
	                nextChar();
	                return true;
	            }
	            return false;
	        }

	        double parse() {
	            nextChar();
	            double x = parseExpression();
	            if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
	            return x;
	        }

	        // Grammar:
	        // expression = term | expression `+` term | expression `-` term
	        // term = factor | term `*` factor | term `/` factor
	        // factor = `+` factor | `-` factor | `(` expression `)`
	        //        | number | functionName factor | factor `^` factor

	        double parseExpression() {
	            double x = parseTerm();
	            for (;;) {
	                if      (eat('+')) x += parseTerm(); // addition
	                else if (eat('-')) x -= parseTerm(); // subtraction
	                else return x;
	            }
	        }

	        double parseTerm() {
	            double x = parseFactor();
	            for (;;) {
	                if      (eat('*')) x *= parseFactor(); // multiplication
	                else if (eat('/')) x /= parseFactor(); // division
	                else return x;
	            }
	        }

	        double parseFactor() {
	            if (eat('+')) return parseFactor(); // unary plus
	            if (eat('-')) return -parseFactor(); // unary minus

	            double x;
	            int startPos = this.pos;
	            if (eat('(')) { // parentheses
	                x = parseExpression();
	                eat(')');
	            } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
	                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
	                x = Double.parseDouble(str.substring(startPos, this.pos));
	            } else if (ch >= 'a' && ch <= 'z') { // functions
	                while (ch >= 'a' && ch <= 'z') nextChar();
	                String func = str.substring(startPos, this.pos);
	                x = parseFactor();
	                if (func.equals("sqrt")) x = Math.sqrt(x);
	                else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
	                else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
	                else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
	                else throw new RuntimeException("Unknown function: " + func);
	            } else {
	                throw new RuntimeException("Unexpected: " + (char)ch);
	            }

	            if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

	            return x;
	        }
	    }.parse();
	}
}
