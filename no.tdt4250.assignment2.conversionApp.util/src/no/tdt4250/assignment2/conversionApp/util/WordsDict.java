package no.tdt4250.assignment2.conversionApp.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

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
			if (words != null && words.hasWord(searchKeyP)) {
				double parsed_word = getParse(searchKey);
				return new DictSearchResult(true, String.format(getSuccessMessageStringFormat(), searchKeyP) + "\n" + vals + from + "=" + parsed_word + to, null);
			} else {
				return new DictSearchResult(true, String.format(getFailureMessageStringFormat(), searchKeyP), null);
			}
		}else {
			return new DictSearchResult(true, String.format(getFailureMessageStringFormat(), searchKey), null);
		}
	}
	
	protected double getParse(String searchKey) {
		char from = searchKey.charAt(0);
		char to = searchKey.charAt(1);
		String value = searchKey.substring(2);
		
		double val = Double.valueOf(value);
		if (from == 'k') {
			val = val / 0.45359237;
		}else {
			val = val * 0.45359237;
		}
		return val;
	}
}
